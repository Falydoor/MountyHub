package com.mountyhub.app.service;

import com.mountyhub.app.domain.Gear;
import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.domain.User;
import com.mountyhub.app.domain.enumeration.GearType;
import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;
import com.mountyhub.app.domain.enumeration.TrollRace;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.repository.GearRepository;
import com.mountyhub.app.repository.ScriptCallRepository;
import com.mountyhub.app.repository.TrollRepository;
import com.mountyhub.app.repository.UserRepository;
import com.mountyhub.app.service.util.DateUtil;
import com.mountyhub.app.service.util.MountyHallScriptUtil;
import com.mountyhub.app.service.util.MountyHallUtil;
import com.mountyhub.app.web.rest.dto.GearDTO;
import com.mountyhub.app.web.rest.dto.ProfilDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing trolls.
 */
@Service
@Transactional
public class TrollService {

    private final Logger log = LoggerFactory.getLogger(TrollService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private TrollRepository trollRepository;

    @Inject
    private ScriptCallRepository scriptCallRepository;

    @Inject
    private GearRepository gearRepository;

    @Inject
    private ScriptCallService scriptCallService;

    public Troll createUpdateTroll(Troll troll) throws IOException, MountyHubException, MountyHallScriptException {
        // Check if the troll isn't already linked to a user for a troll creation
        User currentUser = null;
        if (troll.getUser() == null) {
            currentUser = userService.getUserWithAuthorities();

            // A troll is already linked to the current user
            if (currentUser.getTroll() != null) {
                throw new MountyHubException("Votre utilisateur a déjà un troll lié !");
            }

            // Troll already present with a user linked
            Optional<Troll> trollResult = trollRepository.findOneByNumber(troll.getNumber());
            if (trollResult.isPresent() && trollResult.get().getUser() != null) {
                throw new MountyHubException("Le numero de troll " + troll.getNumber() + " est déjà associé à l'utilisteur " + trollResult.get().getUser().getLogin() + " !");
            }
        }

        // Caract, position and profil call
        ScriptCall caractCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Caract, ScriptType.DYNAMIQUE);
        ScriptCall positionCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Profil3, ScriptType.DYNAMIQUE);
        ScriptCall profilCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_ProfilPublic2, ScriptType.STATIQUE);
        ScriptCall gearCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Equipement, ScriptType.STATIQUE);

        try {
            // Set characteristic, position and profil
            scriptCallService.callScript(caractCall, troll);
            setTrollCharacteristicFromMHScript(caractCall, troll);
            scriptCallService.callScript(positionCall, troll);
            setTrollPositionFromMHScript(positionCall, troll);
            scriptCallService.callScript(profilCall, troll);
            setTrollProfilFromMHScript(profilCall, troll);

            // Save the troll
            trollRepository.save(troll);
            log.debug("Created Information for Troll: {}", troll);

            // Save gear
            scriptCallService.callScript(gearCall, troll);
            gearCall.setTroll(troll);
            setTrollGearFromMHScript(gearCall);

            // Save script calls with troll number
            caractCall.setTroll(troll);
            scriptCallRepository.save(caractCall);
            positionCall.setTroll(troll);
            scriptCallRepository.save(positionCall);
            profilCall.setTroll(troll);
            scriptCallRepository.save(profilCall);

            // Link troll to current user
            if (currentUser != null) {
                currentUser.setTroll(troll);
                userRepository.save(currentUser);
            }

            return troll;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MountyHallScriptException("Erreur lors du parsage du script MountyHall !", e);
        }
    }

    public void setTrollGearFromMHScript(ScriptCall scriptCall) throws IOException, MountyHallScriptException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        scriptCall.setSuccessful(true);
        scriptCallRepository.save(scriptCall);

        // Remove old gear
        gearRepository.deleteByTrollNumber(scriptCall.getTroll().getNumber());

        for (String line : lines) {
            line = StringUtils.replace(line, "\\'", "'");
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ";");

            // ID, équipé ? ; type ; identifié ? ; nom ; magie ; description ; poids
            Gear gear = new Gear();
            gear.setNumber(Long.valueOf(values[0]));
            gear.setWeared(Integer.valueOf(values[1]) > 0);
            gear.setType(GearType.fromString(values[2]));
            gear.setIdentified("1".equals(values[3]));
            String name = values[4];
            // Renamed gear
            if (name.contains("Ã")) {
                name = StringUtils.toEncodedString(name.getBytes("Windows-1252"), Charset.forName("UTF-8"));
            }
            gear.setName(name);
            gear.setTemplate(values[5]);
            gear.setDescription(values[6]);
            gear.setWeight(Float.valueOf(values[7]));
            gear.setArmor(0);
            gear.setArmorM(0);
            gear.setAttack(0);
            gear.setAttackM(0);
            gear.setDamage(0);
            gear.setDamageM(0);
            gear.setDodge(0);
            gear.setDodgeM(0);
            gear.setHitPoint(0);
            gear.setMm(0);
            gear.setRm(0);
            gear.setRegeneration(0);
            gear.setView(0);
            gear.setTurn(0);
            gear.setProtection("");
            if (gear.getWeared()) {
                MountyHallUtil.setCharacteristicsFromDescription(gear, gear.getDescription());
            }
            gear.setTroll(scriptCall.getTroll());
            gearRepository.save(gear);
        }
    }

    public void setTrollProfilFromMHScript(ScriptCall scriptCall, Troll troll) throws MountyHallScriptException, IOException {
        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        scriptCall.setSuccessful(true);
        scriptCallRepository.save(scriptCall);
        String[] values = StringUtils.split(lines[0], ";");

        // numéro; Nom; Race; Niveau; Date d'inscription ; E-mail ; Blason ; Intangible ; Nb mouches ; Nb kills ; Nb morts; Numéro de Guilde; Nniveau de Rang; PNJ ?
        troll.setRace(TrollRace.valueOf(values[2]));
        troll.setLevel(Integer.valueOf(values[3]));
        troll.setBirthDate(MountyHallScriptUtil.parseDate(values[4]));
        troll.setKill(Integer.valueOf(values[9]));
        troll.setDeath(Integer.valueOf(values[10]));
    }

    public void setTrollPositionFromMHScript(ScriptCall scriptCall, Troll troll) throws MountyHallScriptException, IOException {
        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        scriptCall.setSuccessful(true);
        scriptCallRepository.save(scriptCall);
        String[] values = StringUtils.split(lines[0], ";");

        // ID, Nom; Position X ; Position Y ; Position N ; PA Restant; DLA ; Fatigue ; Camouflage; Invisible ; Intangible ; PX; PX perso; PI; Gigots de Gob'
        troll.setName(values[1]);
        troll.setX(Integer.valueOf(values[2]));
        troll.setY(Integer.valueOf(values[3]));
        troll.setZ(Integer.valueOf(values[4]));
    }

    public void setTrollCharacteristicFromMHScript(ScriptCall scriptCall, Troll troll) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MountyHallScriptException, IOException {
        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 3) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        for (String line : lines) {
            String[] values = StringUtils.split(line, ";");

            // Bad values size
            if (values.length != 14) {
                throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
            }

            scriptCall.setSuccessful(true);
            scriptCallRepository.save(scriptCall);

            // Attaque; Esquive; Dégats; Régénération; PVMax; PVActuels; Portée deVue; RM; MM; Armure; Duree du Tour; Poids; Concentration
            Method method;

            String sufix;
            switch (values[0]) {
                case "BMM":
                    sufix = "M";
                    break;
                case "BMP":
                    sufix = "P";
                    break;
                default:
                    sufix = "";
            }

            // Set characteristics of the troll
            for (int i = 0; i < MountyHallUtil.methodsByName.length; ++i) {
                // CurrentHitPoint, Turn and Focus are skipped for P/M
                if (StringUtils.isNotEmpty(sufix) && (i == 5 || i == 10 || i == 12)) {
                    continue;
                }
                String methodName = "set" + MountyHallUtil.methodsByName[i] + sufix;
                if (!"Turn".equals(MountyHallUtil.methodsByName[i]) && !"Weight".equals(MountyHallUtil.methodsByName[i])) {
                    method = troll.getClass().getMethod(methodName, Integer.class);
                    method.invoke(troll, Integer.parseInt(values[i + 1]));
                } else {
                    method = troll.getClass().getMethod(methodName, Float.class);
                    method.invoke(troll, Float.parseFloat(values[i + 1]));
                }
            }
        }
    }

    /**
     * Update every troll's informations one time per day at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyTrollsUpdate() {
        log.debug("START UPDATING TROLLS");
        trollRepository.findAll().stream().forEach(troll -> {
            try {
                createUpdateTroll(troll);
            } catch (IOException | MountyHubException | MountyHallScriptException e) {
                log.debug("FAIL TO UPDATE TROLL " + troll.getNumber() + " : " + e.getMessage());
            }
        });
        log.debug("END UPDATING TROLLS");
    }

    @Transactional
    public ProfilDTO getPrivateProfil() throws MountyHubException {
        User user = userService.getUserWithAuthorities();
        ProfilDTO profil = new ProfilDTO();

        if (user.getTroll() == null) {
            return profil;
        }

        BeanUtils.copyProperties(user.getTroll(), profil);

        // Additionnal fields
        profil.setPercentHitPoint(100 * profil.getCurrentHitPoint() / (profil.getHitPoint() + profil.getHitPointP() + profil.getHitPointM()));

        // Gears
        List<GearDTO> gearDTOs = user.getTroll().getGears().stream().map(gear -> {
            GearDTO dto = new GearDTO();
            BeanUtils.copyProperties(gear, dto);
            return dto;
        }).collect(Collectors.toList());
        profil.setGears(gearDTOs);

        // Turn, weight and turn total fields
        Duration turn = Duration.ofMinutes(profil.getTurn().longValue());
        Float seconds = (profil.getTurn() - turn.toMinutes()) * 60;
        turn = turn.plusSeconds(seconds.longValue());
        profil.setTurnFormatted(DateUtil.formatDuration(turn));
        Duration weight = Duration.ofMinutes(profil.getWeightP().longValue() + profil.getWeightM().longValue());
        seconds = (profil.getWeightP() + profil.getWeightM() - weight.toMinutes()) * 60;
        weight = weight.plusSeconds(seconds.longValue());
        profil.setWeightFormatted(DateUtil.formatDuration(weight));
        turn = turn.plus(weight);
        profil.setTurnTotalFormatted(DateUtil.formatDuration(turn));

        // Scripts call per day
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);
        Map<ScriptType, Long> groupedScripts = user.getTroll().getScriptCalls().stream()
            .filter(script -> script.getDateCalled().isAfter(yesterday))
            .collect(Collectors.groupingBy(ScriptCall::getType, Collectors.counting()));

        profil.setScriptAppelByDay(groupedScripts.getOrDefault(ScriptType.APPEL, 0L));
        profil.setScriptDynamiqueByDay(groupedScripts.getOrDefault(ScriptType.DYNAMIQUE, 0L));
        profil.setScriptMessageByDay(groupedScripts.getOrDefault(ScriptType.MESSAGE, 0L));
        profil.setScriptStatiqueByDay(groupedScripts.getOrDefault(ScriptType.STATIQUE, 0L));

        return profil;
    }

    public void deleteTroll() throws MountyHubException {
        User currentUser = userService.getUserWithAuthorities();

        // No troll linked to user
        if (currentUser.getTroll() == null) {
            throw new MountyHubException("Votre utilisateur n'a pas de troll lié !");
        }

        // Set troll deleted and remove it from the user
        currentUser.getTroll().setDeleted(true);
        trollRepository.save(currentUser.getTroll());
        currentUser.setTroll(null);
        userRepository.save(currentUser);
    }
}
