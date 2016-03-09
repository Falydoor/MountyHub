package com.mountyhub.app.service.util;

import com.mountyhub.app.domain.Gear;
import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.domain.enumeration.GearType;
import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;
import com.mountyhub.app.exception.MountyHallScriptException;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;

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
                return 12;
            case MESSAGE:
                // 12
                return 6;
            case STATIQUE:
                // 10
                return 5;
            default:
                return 0;
        }
    }

    public static ScriptCall createScriptCall(ScriptName name) throws MountyHallScriptException {
        ScriptType type;
        switch (name) {
            case SP_Caract:
            case SP_Profil3:
                type = ScriptType.DYNAMIQUE;
                break;
            case SP_Equipement:
            case SP_ProfilPublic2:
                type = ScriptType.STATIQUE;
                break;
            default:
                throw new MountyHallScriptException("Script non-implémenté !");
        }
        ScriptCall scriptCall = new ScriptCall();
        scriptCall.setDateCalled(ZonedDateTime.now());
        scriptCall.setName(name);
        scriptCall.setType(type);
        scriptCall.setSuccessful(false);
        return scriptCall;
    }

    public static void parseGear(Gear gear, String[] values) throws UnsupportedEncodingException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // ID, équipé ? ; type ; identifié ? ; nom ; magie ; description ; poids
        gear.setNumber(Long.parseLong(values[0]));
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
        gear.setWeight(gear.getIdentified() ? Float.valueOf(values[7]) : 0);
        if (gear.getWeared()) {
            MountyHallUtil.setCharacteristicsFromDescription(gear, gear.getDescription());
        }
    }

    public static void parseState(Troll troll, String[] values) {
        // ID, Nom; Position X ; Position Y ; Position N ; PA Restant; DLA ; Fatigue ; Camouflage; Invisible ; Intangible ; PX; PX perso; PI; Gigots de Gob'
        troll.setName(values[1]);
        troll.setX(Integer.valueOf(values[2]));
        troll.setY(Integer.valueOf(values[3]));
        troll.setZ(Integer.valueOf(values[4]));
        troll.setPa(Integer.valueOf(values[5]));
        troll.setDla(DateUtil.parseDateFromMHScript(values[6]));
        troll.setStrain(Integer.valueOf(values[7]));
        troll.setHidden("1".equals(values[8]));
        troll.setInvisible("1".equals(values[9]));
        troll.setIntangible("1".equals(values[10]));
    }

}
