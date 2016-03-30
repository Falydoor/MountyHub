package com.mountyhub.app.domain.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * The GearType enumeration.
 */
public enum GearType {
    ANNEAU, BOTTES, ARME_1_MAIN, ARME_2_MAINS, BOUCLIER, ARMURE, TALISMAN, CASQUE, COMPOSANT, CHAMPIGNON, BIDOUILLE, POTION, PARCHEMIN, RUNE, INCONNU;

    public static GearType fromString(String s) {
        try {
            return GearType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            switch (s) {
                case "Arme (1 main)":
                    return ARME_1_MAIN;
                case "Arme (2 mains)":
                    return ARME_2_MAINS;
                default:
                    return INCONNU;
            }
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case ARME_1_MAIN:
                return "Arme (1 main)";
            case ARME_2_MAINS:
                return "Arme (2 mains)";
            default:
                return StringUtils.capitalize(this.name());
        }
    }
}
