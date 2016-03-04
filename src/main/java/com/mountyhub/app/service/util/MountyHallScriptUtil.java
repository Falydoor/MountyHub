package com.mountyhub.app.service.util;

import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.repository.ScriptCallRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Theo on 2/27/16.
 */
public final class MountyHallScriptUtil {

    public static String BASE_URL = "http://sp.mountyhall.com/";

    public static String getUrl(ScriptName name, Long number, String password) {
        return BASE_URL + name + ".php?Numero=" + number + "&Motdepasse=" + password;
    }

    public static int getScriptLimitByDay(ScriptType type) {
        switch (type) {
            case APPEL:
                // 4
                return 1;
            case DYNAMIQUE:
                // 24
                return 5;
            case MESSAGE:
                // 12
                return 2;
            case STATIQUE:
                // 10
                return 2;
            default:
                return 0;
        }
    }

    public static ZonedDateTime parseDate(String date) {
        // 2016-02-03 04:54:13
        date = date.replace(" ", "T") + "+01:00[Europe/Paris]";
        return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static ScriptCall createScriptCall(ScriptName name, ScriptType type) {
        ScriptCall scriptCall = new ScriptCall();
        scriptCall.setDateCalled(ZonedDateTime.now());
        scriptCall.setName(name);
        scriptCall.setType(type);
        scriptCall.setSuccessful(false);
        return scriptCall;
    }

    public static String[] getScriptCallResponse(ScriptCall scriptCall, ScriptCallRepository scriptCallRepository) throws MountyHallScriptException, IOException {
        String url = MountyHallScriptUtil.getUrl(scriptCall.getName(), scriptCall.getTroll().getNumber(), scriptCall.getTroll().getRestrictedPassword());
        scriptCall.setUrl(url);

        checkScriptCallSizeByDay(scriptCall, scriptCallRepository);

        // Get reponse from the script call and encode it in UTF-8
        String response = StringUtils.toEncodedString(IOUtils.toString(new URL(url)).getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
        scriptCall.setBody(response);
        String[] lines = StringUtils.split(response, "\n");

        // Save without troll
        scriptCall.setTroll(null);
        scriptCallRepository.save(scriptCall);

        // Bad number/password
        if (StringUtils.startsWith(response, "Erreur")) {
            throw new MountyHallScriptException("Le numéro de troll ou le mot de passe restreint sont faux !");
        }

        return lines;
    }

    public static void checkScriptCallSizeByDay(ScriptCall scriptCall, ScriptCallRepository scriptCallRepository) throws MountyHallScriptException {
        ZonedDateTime date = scriptCall.getDateCalled().minusDays(1);
        Long count = scriptCallRepository.countByTrollNumberAndTypeAndSuccessfulTrueAndDateCalledAfter(scriptCall.getTroll().getNumber(), scriptCall.getType(), date);
        if (count >= MountyHallScriptUtil.getScriptLimitByDay(scriptCall.getType())) {
            throw new MountyHallScriptException("Limite d'appel par jour des scripts de type " + scriptCall.getType() + " atteinte !");
        }
    }

}
