package com.mountyhub.app.service.util;

import com.mountyhub.app.domain.*;
import com.mountyhub.app.domain.enumeration.*;
import com.mountyhub.app.exception.MountyHallScriptException;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            case SP_Profil2:
            case SP_Bonusmalus:
                type = ScriptType.DYNAMIQUE;
                break;
            case SP_Equipement:
            case SP_ProfilPublic2:
            case SP_Mouche:
            case SP_Aptitudes2:
                type = ScriptType.STATIQUE;
                break;
            default:
                throw new MountyHallScriptException("Script non-implémenté !");
        }
        ScriptCall scriptCall = new ScriptCall();
        scriptCall.setDateCalled(ZonedDateTime.now());
        scriptCall.setName(name);
        scriptCall.setType(type);
        scriptCall.setSuccessfullyCalled(false);
        scriptCall.setSuccessfullyParsed(false);
        return scriptCall;
    }

    public static void parseGear(Gear gear, String[] values) throws UnsupportedEncodingException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // ID, équipé ? ; type ; identifié ? ; nom ; magie ; description ; poids
        gear.setNumber(Long.parseLong(values[0]));
        gear.setWore(Integer.valueOf(values[1]) > 0);
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
        if (gear.getWore()) {
            MountyHallUtil.setCharacteristicsFromDescription(gear, gear.getDescription());
        }
    }

    public static void parseState(Troll troll, String[] values) {
        // ID, Position X ; Position Y ; Position N ; PV Actuels; PV Max ; PA Restant; DLA ; Nb Dés d'Attaque ; Nb Dés d'Esquive ; Nb Dés de Dégat ;
        // Nb Dés de Régénération ; Vue ; Armure ; MM ; RM ; attaques subies ; fatigue ; camouflage ? ; invisible ? ; intangible ? ;
        // Nb parades programmées ; Nb contre-attaques programmées ; durée du tour ; bonus de durée (hors équipement) ; Armure naturelle ;
        // Nombre de dés d'armure en moins
        troll.setX(Integer.valueOf(values[1]));
        troll.setY(Integer.valueOf(values[2]));
        troll.setZ(Integer.valueOf(values[3]));
        troll.setCurrentHitPoint(Integer.valueOf(values[4]));
        troll.setPa(Integer.valueOf(values[6]));
        troll.setDla(DateUtil.parseDateFromMHScript(values[7]));
        troll.setStrain(Integer.valueOf(values[17]));
        troll.setHidden("1".equals(values[18]));
        troll.setInvisible("1".equals(values[19]));
        troll.setIntangible("1".equals(values[20]));
    }

    public static void parseFly(Fly fly, String[] values) {
        // ID ; nom ; type ; âge ; présence
        fly.setNumber(Long.parseLong(values[0]));
        fly.setName(values[1]);
        fly.setType(FlyType.valueOf(StringUtils.stripAccents(values[2]).toUpperCase()));
        fly.setOld(Integer.valueOf(values[3]));
        fly.setHere("LA".equals(values[4]));
    }

    public static void parseBonusMalus(BonusMalus bonusMalus, String[] values) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Nom ; Type ; Effet ; Durée (en tours)
        bonusMalus.setName(values[0]);
        bonusMalus.setEffect(values[2]);
        bonusMalus.setDuration(Integer.valueOf(values[3]));
    }

    public static void parseProfil(Troll troll, String[] values) {
        // numéro; Nom; Race; Niveau; Date d'inscription ; E-mail ; Blason ; Intangible ; Nb mouches ; Nb kills ; Nb morts; Numéro de Guilde; Nniveau de Rang; PNJ ?
        troll.setName(values[1]);
        troll.setRace(TrollRace.valueOf(values[2]));
        troll.setLevel(Integer.valueOf(values[3]));
        troll.setBirthDate(DateUtil.parseDateFromMHScript(values[4]));
        troll.setKill(Integer.valueOf(values[9]));
        troll.setDeath(Integer.valueOf(values[10]));
    }

    public static void parseCharacteristic(Troll troll, String[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
}
