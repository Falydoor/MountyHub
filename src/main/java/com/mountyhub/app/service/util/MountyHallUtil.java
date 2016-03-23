package com.mountyhub.app.service.util;

import com.google.common.collect.ImmutableMap;
import com.mountyhub.app.domain.enumeration.FlyType;
import com.mountyhub.app.web.rest.dto.GlobalEffectDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Theo on 3/4/16.
 */
public final class MountyHallUtil {

    private static final Logger log = LoggerFactory.getLogger(MountyHallUtil.class);

    public static ImmutableMap<String, String> methodsByNameEffect;

    public static ImmutableMap<String, String> methodsByNameGlobal;

    public static String[] methodsByName;

    public static List<String> nameNoMagic = Arrays.asList("REG", "PV", "Vue", "TOUR", "RM", "MM");

    static {
        methodsByNameEffect = ImmutableMap.<String, String>builder().put("ATT", "Attack")
            .put("ESQ", "Dodge")
            .put("DEG", "Damage")
            .put("REG", "Regeneration")
            .put("PV", "HitPoint")
            .put("Vue", "View")
            .put("TOUR", "Turn")
            .put("Armure", "Armor")
            .put("RM", "Rm")
            .put("MM", "Mm").build();
        methodsByNameGlobal = ImmutableMap.<String, String>builder().putAll(methodsByNameEffect)
            .put("Weight", "Weight")
            .put("Focus", "Focus")
            .put("CurrentHitPoint", "CurrentHitPoint")
            .put("Protection", "Protection").build();
        methodsByName = new String[]{"Attack", "Dodge", "Damage", "Regeneration", "HitPoint", "CurrentHitPoint", "View", "Rm", "Mm", "Armor", "Turn", "Weight", "Focus"};
    }

    public static void setCharacteristicsFromDescription(Object object, String description) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // ATT : +1\+1 | ESQ : -1 | DEG : +1\+2 | Vue : -1 | TOUR : -30 min | Armure : +3 | RM : +76 %
        for (String part : StringUtils.splitByWholeSeparator(description, " | ")) {
            String[] values = StringUtils.splitByWholeSeparator(part, " : ");
            String methodName = methodsByNameGlobal.get(values[0]);
            if (methodName != null) {
                String[] effects = StringUtils.splitPreserveAllTokens(values[1], "\\");

                effects[0] = StringUtils.replace(effects[0], getSufixByCharacteristic(values[0]), "");

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

    public static String formatGlobalEffect(GlobalEffectDTO globalEffectDTO) {
        List<String> effects = new ArrayList<>();
        methodsByNameEffect.entrySet().stream().forEach(entry -> {
            Long value = 0L;
            Long valueM = 0L;
            try {
                String methodName = "get" + entry.getValue();
                Method method = globalEffectDTO.getClass().getMethod(methodName);
                value = (Long) method.invoke(globalEffectDTO);
                if (nameNoMagic.contains(entry.getKey())) {
                    if (value != 0) {
                        effects.add(formatValue(value, valueM, entry.getKey()));
                    }
                    return;
                }
                Method methodM = globalEffectDTO.getClass().getMethod(methodName + "M");
                valueM = (Long) methodM.invoke(globalEffectDTO);
                if (value != 0 || valueM != 0) {
                    effects.add(formatValue(value, valueM, entry.getKey()));
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("Unable to retrieve value", e);
            }
        });
        return StringUtils.join(effects, " | ");
    }

    private static String formatValue(Long value, Long valueM, String name) {
        DecimalFormat decimalFormat = new DecimalFormat("+#;-#");
        return name + " : " + decimalFormat.format(value) + (valueM != 0 ? "/" + decimalFormat.format(valueM) : "") + getSufixByCharacteristic(name);
    }

    public static String flyTypeToEffect(FlyType type) {
        switch (type) {
            case CROBATE:
                return "ATT : +1";
            case LUNETTES:
                return "Vue : +1";
            case MIEL:
                return "REG : +1";
            case NABOLISANT:
                return "DEG : +1";
            case RIVATANT:
                return "TOUR : -20 min";
            case TELAITE:
                return "PV : +5";
            case VERTIE:
                return "ESQ : +1";
            case XIDANT:
                return "Armure : +1";
            default:
                return "";
        }
    }

    private static String getSufixByCharacteristic(String name) {
        if ("TOUR".equals(name)) {
            return " min";
        }
        if ("RM".equals(name) || "MM".equals(name)) {
            return " %";
        }
        return "";
    }
}
