package com.mountyhub.app.service.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Theo on 3/4/16.
 */
public final class MountyHallUtil {

    private static final Logger log = LoggerFactory.getLogger(MountyHallUtil.class);

    public static Map<String, String> methodsByNameGlobal;

    public static String[] methodsByName;

    static {
        methodsByNameGlobal = new HashMap<>();
        methodsByNameGlobal.put("ATT", "Attack");
        methodsByNameGlobal.put("ESQ", "Dodge");
        methodsByNameGlobal.put("DEG", "Damage");
        methodsByNameGlobal.put("REG", "Regeneration");
        methodsByNameGlobal.put("PV", "HitPoint");
        methodsByNameGlobal.put("CurrentHitPoint", "CurrentHitPoint");
        methodsByNameGlobal.put("Vue", "View");
        methodsByNameGlobal.put("RM", "Rm");
        methodsByNameGlobal.put("MM", "Mm");
        methodsByNameGlobal.put("Armure", "Armor");
        methodsByNameGlobal.put("TOUR", "Turn");
        methodsByNameGlobal.put("Weight", "Weight");
        methodsByNameGlobal.put("Focus", "Focus");
        methodsByNameGlobal.put("Protection", "Protection");
        methodsByName = new String[]{"Attack", "Dodge", "Damage", "Regeneration", "HitPoint", "CurrentHitPoint", "View", "Rm", "Mm", "Armor", "Turn", "Weight", "Focus"};
    }

    public static void setCharacteristicsFromDescription(Object object, String description) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // ATT : +1\+1 | ESQ : -1 | DEG : +1\+2 | Vue : -1 | TOUR : -30 min | Armure : +3 | RM : +76 %
        for (String part : StringUtils.splitByWholeSeparator(description, " | ")) {
            String[] values = StringUtils.splitByWholeSeparator(part, " : ");
            String methodName = methodsByNameGlobal.get(values[0]);
            if (methodName != null) {
                String[] effects = StringUtils.split(values[1], "\\");

                switch (values[0]) {
                    case "TOUR":
                        effects[0] = StringUtils.replace(effects[0], " min", "");
                        break;
                    case "MM":
                    case "RM":
                        effects[0] = StringUtils.replace(effects[0], " %", "");
                        break;
                    default:
                }

                methodName = "set" + methodName;
                if (!"setProtection".equals(methodName)) {
                    Method method = object.getClass().getMethod(methodName, Integer.class);
                    method.invoke(object, Integer.parseInt(effects[0]));
                    if (effects.length == 2) {
                        method = object.getClass().getMethod(methodName + "M", Integer.class);
                        method.invoke(object, Integer.parseInt(effects[1]));
                    }
                } else {
                    Method method = object.getClass().getMethod(methodName, String.class);
                    method.invoke(object, effects[0]);
                }
            } else {
                log.debug("Unable to find " + values[0] + " in methodsByNameGlobal");
            }
        }
    }
}
