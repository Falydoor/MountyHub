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
import com.mountyhub.app.service.util.MountyHallScriptUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service class for managing trolls.
 */
@Service
@Transactional(noRollbackForClassName = {"DateTimeParseException"})
public class TrollService {

    private final Logger log = LoggerFactory.getLogger(TrollService.class);

    @Inject
    private UserService userService;

    @Inject
    private TrollRepository trollRepository;

    @Inject
    private ScriptCallRepository scriptCallRepository;

    public void addTroll(Long number, String restrictedPassword) throws IOException, MountyHubException, MountyHallScriptException {
        User currentUser = userService.getUserWithAuthorities();

        // A troll is already linked to the user
        if (currentUser.getTroll() != null) {
            throw new MountyHubException("Votre utilisateur a déjà un troll lié !");
        }

        Optional<Troll> trollResult = trollRepository.findOneByNumber(number);
        if (trollResult.isPresent()) {
            throw new MountyHubException("Le numero de troll " + number + " est déjà associé à l'utilisteur " + trollResult.get().getUser().getLogin() + " !");
        }

        // Caract call
        ScriptCall caractCall = new ScriptCall();
        caractCall.setDateCalled(ZonedDateTime.now());
        caractCall.setName(ScriptName.SP_Caract);
        caractCall.setType(ScriptType.DYNAMIQUE);
        caractCall.setSuccessful(false);

        // Position call
        ScriptCall positionCall = new ScriptCall();
        positionCall.setDateCalled(ZonedDateTime.now());
        positionCall.setName(ScriptName.SP_Profil3);
        positionCall.setType(ScriptType.DYNAMIQUE);
        positionCall.setSuccessful(false);

        // Profil call
        ScriptCall profilCall = new ScriptCall();
        profilCall.setDateCalled(ZonedDateTime.now());
        profilCall.setName(ScriptName.SP_ProfilPublic2);
        profilCall.setType(ScriptType.STATIQUE);
        profilCall.setSuccessful(false);

        try {
            Troll troll = new Troll();
            troll.setNumber(number);
            troll.setRestrictedPassword(restrictedPassword);

            setTrollCharacteristicFromMHScript(troll, caractCall);
            setTrollPositionFromMHScript(troll, positionCall);
            setTrollProfilFromMHScript(troll, profilCall);

            // Save the troll
            currentUser.setTroll(troll);
            trollRepository.save(troll);
            caractCall.setTroll(troll);
            positionCall.setTroll(troll);
            profilCall.setTroll(troll);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MountyHallScriptException("Erreur lors du parsage du script MountyHall !");
        } finally {
            scriptCallRepository.save(caractCall);
            if (caractCall.getSuccessful()) {
                scriptCallRepository.save(positionCall);
                if (positionCall.getSuccessful()) {
                    scriptCallRepository.save(profilCall);
                }
            }
        }
    }

    public void checkScriptCallSizeByDay(Long number, ScriptType type) throws MountyHallScriptException {
        ZonedDateTime date = ZonedDateTime.now().minusDays(1);
        Long count = scriptCallRepository.countByTrollNumberAndTypeAndSuccessfulTrueAndDateCalledAfter(number, type, date);
        if (count >= MountyHallScriptUtil.getScriptLimitByDay(type)) {
            throw new MountyHallScriptException("Limite d'appel par jour des scripts de type " + type + " atteinte !");
        }
    }

    public void setTrollProfilFromMHScript(Troll troll, ScriptCall scriptCall) throws MountyHallScriptException, IOException {
        String[] lines = getScriptCallResponse(troll, scriptCall);

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        scriptCall.setSuccessful(true);

        String[] values = StringUtils.split(lines[0], ";");

        // numéro; Nom; Race; Niveau; Date d'inscription ; E-mail ; Blason ; Intangible ; Nb mouches ; Nb kills ; Nb morts; Numéro de Guilde; Nniveau de Rang; PNJ ?
        troll.setRace(TrollRace.valueOf(values[2]));
        troll.setLevel(Integer.valueOf(values[3]));
        troll.setBirthDate(MountyHallScriptUtil.parseDate(values[4]));
        troll.setKill(Integer.valueOf(values[9]));
        troll.setDeath(Integer.valueOf(values[10]));
    }

    public void setTrollPositionFromMHScript(Troll troll, ScriptCall scriptCall) throws MountyHallScriptException, IOException {
        String[] lines = getScriptCallResponse(troll, scriptCall);

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        scriptCall.setSuccessful(true);

        String[] values = StringUtils.split(lines[0], ";");

        // ID, Nom; Position X ; Position Y ; Position N ; PA Restant; DLA ; Fatigue ; Camouflage; Invisible ; Intangible ; PX; PX perso; PI; Gigots de Gob'
        troll.setName(values[1]);
        troll.setX(Integer.valueOf(values[2]));
        troll.setY(Integer.valueOf(values[3]));
        troll.setZ(Integer.valueOf(values[4]));
    }

    public void setTrollCharacteristicFromMHScript(Troll troll, ScriptCall scriptCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MountyHallScriptException, IOException {
        String[] lines = getScriptCallResponse(troll, scriptCall);

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

            // Attaque; Esquive; Dégats; Régénération; PVMax; PVActuels; Portée deVue; RM; MM; Armure; Duree du Tour; Poids; Concentration
            String[] methods = new String[]{"Attack", "Dodge", "Damage", "Regeneration", "HitPoint", "CurrentHitPoint", "View", "Rm", "Mm", "Armor", "Turn", "Weight", "Focus"};
            Method method;

            String prefix;
            switch (values[0]) {
                case "BMM":
                    prefix = "M";
                    break;
                case "BMP":
                    prefix = "P";
                    break;
                default:
                    prefix = "";
            }

            // Set characteristics of the troll
            for (int i = 0; i < methods.length; ++i) {
                // CurrentHitPoint, Turn and Focus are skipped for P/M
                if (StringUtils.isNotEmpty(prefix) && (i == 5 || i == 10 || i == 12)) {
                    continue;
                }
                method = troll.getClass().getMethod("set" + methods[i] + prefix, Integer.class);
                method.invoke(troll, Integer.parseInt(values[i + 1]));
            }
        }
    }

    private String[] getScriptCallResponse(Troll troll, ScriptCall scriptCall) throws MountyHallScriptException, IOException {
        String url = MountyHallScriptUtil.getUrl(scriptCall.getName(), troll.getNumber(), troll.getRestrictedPassword());
        scriptCall.setUrl(url);

        checkScriptCallSizeByDay(troll.getNumber(), scriptCall.getType());

        // Get the characteristic of the troll
        String response = IOUtils.toString(new URL(url));
        scriptCall.setBody(StringUtils.abbreviate(response, 255));
        String[] lines = StringUtils.split(response, "\n");

        // Bad number/password
        if (StringUtils.startsWith(response, "Erreur")) {
            throw new MountyHallScriptException("Le numéro de troll ou le mot de passe restreint sont faux !");
        }

        return lines;
    }

}
