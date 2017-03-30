package com.mountyhub.app.service.util;

import com.google.common.collect.ImmutableMap;
import com.mountyhub.app.domain.Competence;
import com.mountyhub.app.domain.Spell;
import com.mountyhub.app.domain.Troll;
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
        for (String wholeEffect : StringUtils.splitByWholeSeparator(description, " | ")) {
            String[] values = StringUtils.splitByWholeSeparator(wholeEffect, " : ");
            String methodName = methodsByNameGlobal.get(values[0]);
            if (methodName != null) {
                String[] effects = StringUtils.splitPreserveAllTokens(values[1], "\\");

                effects[0] = StringUtils.replace(effects[0], getSufixByCharacteristic(values[0]), "");

                methodName = "set" + methodName;
                if (!"setProtection".equals(methodName)) {
                    Method method = object.getClass().getMethod(methodName, Integer.class);
                    method.invoke(object, Integer.parseInt(effects[0]));
                    if (effects.length == 2 && effectCanBeMagic(wholeEffect)) {
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

    public static Boolean effectCanBeMagic(String effect) {
        return StringUtils.startsWithAny(effect, "ATT", "ESQ", "DEG", "Armure");
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
                        effects.add(formatValueWithName(value, valueM, entry.getKey()));
                    }
                    return;
                }
                Method methodM = globalEffectDTO.getClass().getMethod(methodName + "M");
                valueM = (Long) methodM.invoke(globalEffectDTO);
                if (value != 0 || valueM != 0) {
                    effects.add(formatValueWithName(value, valueM, entry.getKey()));
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("Unable to retrieve value", e);
            }
        });
        return StringUtils.join(effects, " | ");
    }

    private static String formatValueWithName(Long value, Long valueM, String name) {
        return name + " : " + formatValue(value) + (valueM != 0 ? "/" + formatValue(valueM) : "") + getSufixByCharacteristic(name);
    }

    private static String formatValue(Long v) {
        return formatValue(v.intValue());
    }

    private static String formatValue(Integer v) {
        DecimalFormat decimalFormat = new DecimalFormat("+#;-#");
        return decimalFormat.format(v);
    }

    public static String flyTypeToEffect(FlyType type) {
        switch (type) {
            case CROBATE:
                return "ATT : +1";
            case LUNETTES:
                return "Vue : +1";
            case MIEL:
                return "REG : +1";
            case NABOLISANTS:
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

    public static Integer applyDecumul(Integer value, Integer time) {
        Double percent = 0.10D;
        switch (time) {
            case 1:
                percent = 1D;
                break;
            case 2:
                percent = 0.67D;
                break;
            case 3:
                percent = 0.40D;
                break;
            case 4:
                percent = 0.25D;
                break;
            case 5:
                percent = 0.15D;
                break;
            default:
                break;
        }
        return Double.valueOf(Math.floor(value * percent)).intValue();
    }

    public static String getCompetenceTooltip(Competence competence) {
        switch (competence.getCompetenceMH().getNumber().intValue()) {
            default:
                return "Tooltip à faire";
        }
    }

    public static String getSpellTooltip(Spell spell) {
        Troll troll = spell.getTroll();
        StringBuilder tooltip = new StringBuilder();
        switch (spell.getSpellMH().getNumber().intValue()) {
            case 1:
                Integer range = rangeProjo(troll.getView(), 4, 1);
                tooltip.append("Portée : ").append(range);
                tooltip.append("\n");
                tooltip.append(formatAttack(troll.getView(), troll.getAttackM()));
                for (int i = 0; i <= range; ++i) {
                    tooltip.append("\n");
                    tooltip.append("Dégats à ").append(i).append(" case").append(i > 0 ? "s" : "").append(" : ");
                    tooltip.append(formatDamageWithCritical(troll.getView() / 2 + range - i, troll.getDamageM()));
                }
                return tooltip.toString();
            case 2:
                tooltip.append("Non-résisté : -").append(Math.floor(troll.getDodge() * 1.5)).append("D d'esquive");
                tooltip.append("\n");
                tooltip.append("Résisté : -").append(Math.floor(troll.getDodge() * 0.33)).append("D d'esquive");
            case 3:
                tooltip.append(formatAttack(Double.valueOf(troll.getDamage() * 0.66).intValue(), troll.getAttackM()));
                tooltip.append("\n");
                tooltip.append("Dégats : ").append(formatDamageWithCritical(troll.getDamage(), troll.getDamageM()));
                return tooltip.toString();
            case 4:
                tooltip.append("Dégats : ").append(formatDamage(troll.getDamage(), troll.getDamageM()));
                tooltip.append("\n");
                tooltip.append("Malus de régénération : -").append(troll.getDamage());
                return tooltip.toString();
            default:
                return "Tooltip à faire";
        }
    }

    private static String formatAttack(Integer attack, Integer bonus) {
        return "Attaque : " + attack + "D6" + formatValue(bonus) + " (" + (attack * 3.5 + bonus) + ")";
    }

    private static String formatDamageWithCritical(Integer damage, Integer bonus) {
        return formatDamage(damage, bonus) + " / " + formatDamage(Double.valueOf(Math.floor(damage * 1.5)).intValue(), bonus);
    }

    private static String formatDamage(Integer damage, Integer bonus) {
        return (damage + bonus) + "-" + (damage * 3 + bonus) + " (" + (damage * 2 + bonus) + ")";
    }

    private static Integer rangeProjo(Integer view, Integer viewMax, Integer range) {
        return view <= viewMax ? range : rangeProjo(view, viewMax + range + 4, range + 1);
    }
}
