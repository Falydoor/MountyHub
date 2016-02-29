package com.mountyhub.app.service;

import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.domain.User;
import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;
import com.mountyhub.app.domain.enumeration.TrollRace;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.repository.ScriptCallRepository;
import com.mountyhub.app.repository.TrollRepository;
import com.mountyhub.app.repository.UserRepository;
import com.mountyhub.app.service.util.MountyHallScriptUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Service class for managing trolls.
 */
@Service
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

    public Troll createUpdateTroll(Troll troll) throws IOException, MountyHubException, MountyHallScriptException {
        // Check if the troll isn't already linked to a user for a troll creation
        User currentUser = null;
        if (troll.getUser() == null) {
            currentUser = userService.getUserWithAuthorities();

            // A troll is already linked to the current user
            if (currentUser.getTroll() != null) {
                throw new MountyHubException("Votre utilisateur a déjà un troll lié !");
            }

            // Troll already present
            Optional<Troll> trollResult = trollRepository.findOneByNumber(troll.getNumber());
            if (trollResult.isPresent()) {
                throw new MountyHubException("Le numero de troll " + troll.getNumber() + " est déjà associé à l'utilisteur " + trollResult.get().getUser().getLogin() + " !");
            }
        }

        // Caract, position and profil call
        ScriptCall caractCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Caract, ScriptType.DYNAMIQUE);
        ScriptCall positionCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Profil3, ScriptType.DYNAMIQUE);
        ScriptCall profilCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_ProfilPublic2, ScriptType.STATIQUE);
        caractCall.setTroll(troll);
        positionCall.setTroll(troll);
        profilCall.setTroll(troll);

        try {
            setTrollCharacteristicFromMHScript(troll, caractCall);
            setTrollPositionFromMHScript(troll, positionCall);
            setTrollProfilFromMHScript(troll, profilCall);

            // Save the troll
            trollRepository.save(troll);
            log.debug("Created Information for Troll: {}", troll);

            // Save script calls
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
            throw new MountyHallScriptException("Erreur lors du parsage du script MountyHall !");
        }
    }

    public void setTrollProfilFromMHScript(Troll troll, ScriptCall scriptCall) throws MountyHallScriptException, IOException {
        String[] lines = MountyHallScriptUtil.getScriptCallResponse(scriptCall, scriptCallRepository);

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

    public void setTrollPositionFromMHScript(Troll troll, ScriptCall scriptCall) throws MountyHallScriptException, IOException {
        String[] lines = MountyHallScriptUtil.getScriptCallResponse(scriptCall, scriptCallRepository);

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

    public void setTrollCharacteristicFromMHScript(Troll troll, ScriptCall scriptCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MountyHallScriptException, IOException {
        String[] lines = MountyHallScriptUtil.getScriptCallResponse(scriptCall, scriptCallRepository);

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
            String[] methods = new String[]{"Attack", "Dodge", "Damage", "Regeneration", "HitPoint", "CurrentHitPoint", "View", "Rm", "Mm", "Armor", "Turn", "Weight", "Focus"};
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
            for (int i = 0; i < methods.length; ++i) {
                // CurrentHitPoint, Turn and Focus are skipped for P/M
                if (StringUtils.isNotEmpty(sufix) && (i == 5 || i == 10 || i == 12)) {
                    continue;
                }
                method = troll.getClass().getMethod("set" + methods[i] + sufix, Integer.class);
                method.invoke(troll, Integer.parseInt(values[i + 1]));
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

}
