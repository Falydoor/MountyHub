package com.mountyhub.app.service.util;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Theo on 3/2/16.
 */
public class DateUtil {
    public static String formatDuration(Duration duration) {
        if (duration.isZero()) {
            return null;
        }
        long hours = duration.toHours();
        long mins = duration.toMinutes() % 60;
        long secs = duration.getSeconds() % 60;
        return String.format("%02dh %02dm %02ds", hours, mins, secs);
    }

    public static Duration getDurationFromFloatMinutes(Float minutes) {
        Duration turn = Duration.ofMinutes(minutes.longValue());
        Float seconds = (minutes - turn.toMinutes()) * 60;
        return turn.plusSeconds(seconds.longValue());
    }

    public static String formatZonedDate(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/YYYY"));
    }

    public static String formatPassedDate(ZonedDateTime date) {
        Duration duration = Duration.between(date.toLocalDateTime(), ZonedDateTime.now().toLocalDateTime());
        long days = duration.toDays();
        long mins = duration.toMinutes() % 60;
        long secs = duration.getSeconds() % 60;
        return (days > 0 ? days + "j " : "") + (mins > 0 ? mins + "m " : "") + secs + "s";
    }

    public static ZonedDateTime parseDateFromMHScript(String date) {
        // 2016-02-03 04:54:13
        date = date.replace(" ", "T") + "+01:00[Europe/Paris]";
        return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
