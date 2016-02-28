package com.mountyhub.app.service.util;

import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;

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

}
