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
@Transactional
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

        // ScriptCall creation
        ScriptCall scriptCall = new ScriptCall();
        scriptCall.setDateCalled(ZonedDateTime.now());
        scriptCall.setName(ScriptName.SP_Caract);
        scriptCall.setType(ScriptType.DYNAMIQUE);
        scriptCall.setSuccessful(false);

        checkScriptCallSizeByDay(number, ScriptType.DYNAMIQUE);

        String url = MountyHallScriptUtil.getUrl(ScriptName.SP_Caract, number, restrictedPassword);
//        String url = "https://gist.githubusercontent.com/Falydoor/b53eaca2eb09778a943b/raw/432af4f161f78941b101a9bba9b5c640f103dd27/gistfile1.txt";
        scriptCall.setUrl(url);

        try {
            // Get the characteristic of the troll
            String response = IOUtils.toString(new URL(url));
            scriptCall.setBody(StringUtils.abbreviate(response, 255));
            String[] lines = StringUtils.split(response, "\n");

            // Bad number/password
            if (lines.length != 3) {
                throw new MountyHallScriptException("Le numéro de troll ou le mot de passe restreint sont faux !");
            }

            // Create the troll
            Troll troll = new Troll();
            troll.setNumber(number);

            for (String line : lines) {
                String[] values = StringUtils.split(line, ";");

                // Bad number of values
                if (values.length != 14) {
                    throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
                }

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

            // Save the troll
            troll.setX(0);
            troll.setY(0);
            troll.setZ(0);
            troll.setRace(TrollRace.TOMAWAK);
            troll.setBirthDate(ZonedDateTime.now());
            troll.setName("Test");
            currentUser.setTroll(troll);
            trollRepository.save(troll);
            scriptCall.setTroll(troll);
            scriptCall.setSuccessful(true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MountyHallScriptException("Erreur lors du parsage du script MountyHall !");
        } finally {
            scriptCallRepository.save(scriptCall);
        }
    }

    public void checkScriptCallSizeByDay(Long number, ScriptType type) throws MountyHallScriptException {
        ZonedDateTime date = ZonedDateTime.now().minusDays(1);
        Long count = scriptCallRepository.countByTrollNumberAndTypeAndSuccessfulTrueAndDateCalledAfter(number, type, date);
        if (count >= MountyHallScriptUtil.getScriptLimitByDay(type)) {
            throw new MountyHallScriptException("Limite d'appel des scripts de type " + type + " atteinte !");
        }
    }

}
