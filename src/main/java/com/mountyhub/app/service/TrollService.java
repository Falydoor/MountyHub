package com.mountyhub.app.service;

import com.mountyhub.app.domain.*;
import com.mountyhub.app.domain.enumeration.*;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.repository.*;
import com.mountyhub.app.service.util.DateUtil;
import com.mountyhub.app.service.util.MountyHallScriptUtil;
import com.mountyhub.app.service.util.MountyHallUtil;
import com.mountyhub.app.web.rest.dto.FlyDTO;
import com.mountyhub.app.web.rest.dto.GearDTO;
import com.mountyhub.app.web.rest.dto.GlobalEffectDTO;
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
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @Inject
    private FlyRepository flyRepository;

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
            Optional<User> userResult = userRepository.findOneByTrollNumber(troll.getNumber());
            if (userResult.isPresent()) {
                throw new MountyHubException("Le numero de troll " + troll.getNumber() + " est déjà associé à l'utilisateur " + userResult.get().getLogin() + " !");
            }

            // Troll previously deleted
            Optional<Troll> trollResult = trollRepository.findOneByNumberAndDeletedTrue(troll.getNumber());
            if (trollResult.isPresent()) {
                troll = trollResult.get();
            }
        }

        try {
            // Set characteristic, state and profil
            ScriptCall caractCall = setTrollCharacteristicFromMHScript(troll);
            ScriptCall stateCall = setTrollStateFromMHScript(troll);
            ScriptCall profilCall = setTrollProfilFromMHScript(troll);

            // Save the troll
            trollRepository.save(troll);
            log.debug("Created Information for Troll: {}", troll);

            // Save gear
            setTrollGearFromMHScript(troll);

            // Save fly
            setTrollFlyFromMHScript(troll);

            // Save script calls with troll number
            caractCall.setTroll(troll);
            scriptCallRepository.save(caractCall);
            stateCall.setTroll(troll);
            scriptCallRepository.save(stateCall);
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

    public void setTrollFlyFromMHScript(Troll troll) throws MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Mouche);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        scriptCall.setSuccessful(true);
        scriptCallRepository.save(scriptCall);

        List<Long> exist = new ArrayList<>();

        for (String line : lines) {
            line = StringUtils.replace(line, "\\'", "'");
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ";");

            Optional<Fly> flyResult = flyRepository.findByNumber(Long.valueOf(values[0]));
            Fly fly = flyResult.isPresent() ? flyResult.get() : new Fly();
            MountyHallScriptUtil.parseFly(fly, values);
            fly.setTroll(troll);
            flyRepository.save(fly);
            exist.add(fly.getNumber());
        }

        // Delete fly that doesn't exist
        troll.getFlys().stream()
            .filter(fly -> !exist.contains(fly.getNumber()))
            .forEach(fly -> flyRepository.deleteByNumber(fly.getNumber()));
    }

    public void setTrollGearFromMHScript(Troll troll) throws IOException, MountyHallScriptException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Equipement);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        scriptCall.setSuccessful(true);
        scriptCallRepository.save(scriptCall);

        List<Long> weared = new ArrayList<>();

        for (String line : lines) {
            line = StringUtils.replace(line, "\\'", "'");
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ";");

            Optional<Gear> gearResult = gearRepository.findByNumber(Long.valueOf(values[0]));
            Gear gear = gearResult.isPresent() ? gearResult.get() : new Gear();
            MountyHallScriptUtil.parseGear(gear, values);
            gear.setTroll(troll);
            gearRepository.save(gear);
            weared.add(gear.getNumber());
        }

        // Delete gear that aren't weared
        troll.getGears().stream()
            .filter(gear -> !weared.contains(gear.getNumber()))
            .forEach(gear -> gearRepository.deleteByNumber(gear.getNumber()));
    }

    public ScriptCall setTrollProfilFromMHScript(Troll troll) throws MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_ProfilPublic2);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        scriptCall.setSuccessful(true);
        scriptCallRepository.save(scriptCall);
        String[] values = StringUtils.split(lines[0], ";");

        // numéro; Nom; Race; Niveau; Date d'inscription ; E-mail ; Blason ; Intangible ; Nb mouches ; Nb kills ; Nb morts; Numéro de Guilde; Nniveau de Rang; PNJ ?
        troll.setName(values[1]);
        troll.setRace(TrollRace.valueOf(values[2]));
        troll.setLevel(Integer.valueOf(values[3]));
        troll.setBirthDate(DateUtil.parseDateFromMHScript(values[4]));
        troll.setKill(Integer.valueOf(values[9]));
        troll.setDeath(Integer.valueOf(values[10]));

        return scriptCall;
    }

    public ScriptCall setTrollStateFromMHScript(Troll troll) throws MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Profil2);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        scriptCall.setSuccessful(true);
        scriptCallRepository.save(scriptCall);
        String[] values = StringUtils.split(lines[0], ";");
        MountyHallScriptUtil.parseState(troll, values);

        return scriptCall;
    }

    public ScriptCall setTrollCharacteristicFromMHScript(Troll troll) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Caract);
        scriptCallService.callScript(scriptCall, troll);

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
                // CurrentHitPoint is skipped
                if (i == 5) {
                    continue;
                }
                // Focus is skipped for BMP/BMM
                if (StringUtils.isNotEmpty(sufix) && i == 12) {
                    continue;
                }
                // Weight is skipped for BMM
                if ("BMM".equals(values[0]) && i == 11) {
                    continue;
                }
                // Turn is skipped for BMP
                if ("BMP".equals(values[0]) && i == 10) {
                    continue;
                }
                String methodName = "set" + MountyHallUtil.methodsByName[i] + sufix;
                // Turn replace weight for BMM
                if ("BMM".equals(values[0]) && i == 10) {
                    methodName = "setWeight" + sufix;
                }
                if (!"Turn".equals(MountyHallUtil.methodsByName[i]) && !"Weight".equals(MountyHallUtil.methodsByName[i])) {
                    method = troll.getClass().getMethod(methodName, Integer.class);
                    method.invoke(troll, Integer.parseInt(values[i + 1]));
                } else {
                    method = troll.getClass().getMethod(methodName, Float.class);
                    method.invoke(troll, Float.parseFloat(values[i + 1]));
                }
            }
        }

        return scriptCall;
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
                log.debug("FAIL TO UPDATE TROLL " + troll.getId() + " : " + e.getMessage());
            }
        });
        log.debug("END UPDATING TROLLS");
    }

    public ProfilDTO getPrivateProfil() throws MountyHubException {
        User user = userService.getUserWithAuthorities();
        ProfilDTO profil = new ProfilDTO();

        if (user.getTroll() == null) {
            return profil;
        }

        Troll troll = trollRepository.findOne(user.getTroll().getId());
        BeanUtils.copyProperties(troll, profil);

        // Additionnal fields
        profil.setTotalHitPoint(profil.getHitPoint() + profil.getHitPointP() + profil.getHitPointM());
        profil.setPercentHitPoint(100 * profil.getCurrentHitPoint() / profil.getTotalHitPoint());
        ZoneId zoneId = DateUtil.getZoneIdFromUserOption(user.getUserOptions());
        profil.setBirthDateFormatted(DateUtil.formatZonedDate(troll.getBirthDate(), zoneId));
        profil.setDla(DateUtil.formatZonedDate(troll.getDla(), zoneId));
        profil.setUserOptions(user.getUserOptions().stream().collect(Collectors.toMap(userOption -> userOption.getName().toString(), userOption1 -> userOption1)));
        UserOption userOption = new UserOption();
        userOption.setName(UserOptionName.ZONEID);
        userOption.setUser(user);
        userOption.setValue("GMT+01:00");
        profil.getUserOptions().putIfAbsent(UserOptionName.ZONEID.toString(), userOption);

        // Gears
        List<GearDTO> gears = troll.getGears().stream().map(gear -> {
            GearDTO dto = new GearDTO();
            BeanUtils.copyProperties(gear, dto);
            return dto;
        }).collect(Collectors.toList());
        profil.setGears(gears);
        profil.setGearEffect(MountyHallUtil.formatGlobalEffect(gearRepository.getWearedGlobalEffect(troll.getId())));

        // Flies
        List<FlyDTO> flies = troll.getFlys().stream().sorted((f1, f2) -> f1.getType().compareTo(f2.getType())).map(fly -> {
            FlyDTO dto = new FlyDTO();
            BeanUtils.copyProperties(fly, dto);
            dto.setEffect(MountyHallUtil.flyTypeToEffect(fly.getType()));
            return dto;
        }).collect(Collectors.toList());
        profil.setFlies(flies);
        Map<FlyType, Long> fliesByType = troll.getFlys().stream().filter(Fly::getHere)
            .collect(Collectors.groupingBy(Fly::getType, Collectors.counting()));
        profil.setFlyEffect(MountyHallUtil.formatGlobalEffect(new GlobalEffectDTO(fliesByType)));

        // Turn, bonus/malus, weight, wounds and total turn
        Duration turn = DateUtil.getDurationFromFloatMinutes(profil.getTurn());
        profil.setTurnFormatted(DateUtil.formatDuration(turn));
        Duration bonusMalus = DateUtil.getDurationFromFloatMinutes(profil.getWeightM());
        profil.setBonusMalusTimeFormatted(DateUtil.formatDuration(bonusMalus));
        Duration weight = DateUtil.getDurationFromFloatMinutes(profil.getWeightP());
        profil.setWeightTimeFormatted(DateUtil.formatDuration(weight));
        Duration wounds = DateUtil.getDurationFromFloatMinutes((float) Math.round(250F / profil.getTotalHitPoint() * (profil.getTotalHitPoint() - profil.getCurrentHitPoint())));
        profil.setWoundsTimeFormatted(DateUtil.formatDuration(wounds));
        Duration totalTurn = turn.plus(weight).plus(bonusMalus).plus(wounds);
        totalTurn = totalTurn.compareTo(turn) < 0 ? turn : totalTurn;
        profil.setTurnTotalFormatted(DateUtil.formatDuration(totalTurn));
        profil.setNextDla(DateUtil.formatZonedDate(troll.getDla().plus(totalTurn), zoneId));

        // Scripts call per day
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);
        Map<ScriptType, Long> scriptCallByDay = troll.getScriptCalls().stream()
            .filter(script -> script.getSuccessful() && script.getDateCalled().isAfter(yesterday))
            .collect(Collectors.groupingBy(ScriptCall::getType, Collectors.counting()));
        profil.setScriptCallByDay(scriptCallByDay);

        // Last scripts call by type
        Map<ScriptName, String> scriptCallLastCall = troll.getScriptCalls().stream()
            .filter(ScriptCall::getSuccessful)
            .sorted((sc1, sc2) -> sc2.getDateCalled().compareTo(sc1.getDateCalled()))
            .collect(Collectors.groupingBy(ScriptCall::getName))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> DateUtil.formatSinceDate(entry.getValue().get(0).getDateCalled())));
        profil.setScriptCallLastCall(scriptCallLastCall);

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

    public void refreshTroll(String refreshType) throws IOException, MountyHallScriptException, MountyHubException {
        Troll troll = userService.getUserWithAuthorities().getTroll();

        if (troll == null) {
            throw new MountyHubException("Vous n'avez pas de Troll !");
        }

        try {
            switch (refreshType) {
                case "state":
                    setTrollStateFromMHScript(troll);
                    break;
                case "fly":
                    setTrollFlyFromMHScript(troll);
                    setTrollCharacteristicFromMHScript(troll);
                    break;
                case "gear":
                    setTrollGearFromMHScript(troll);
                    setTrollCharacteristicFromMHScript(troll);
                    break;
                case "characteristic":
                    setTrollCharacteristicFromMHScript(troll);
                    break;
                default:
                    throw new MountyHubException("Type de refresh non-implémenté !");
            }
            trollRepository.save(troll);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MountyHallScriptException("Erreur lors du parsage du script MountyHall !", e);
        }

    }
}
