package com.mountyhub.app.service.util;

import com.mountyhub.app.domain.UserOption;
import com.mountyhub.app.domain.enumeration.UserOptionName;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

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

    public static String formatZonedDate(ZonedDateTime date, ZoneId zoneId) {
        return date.withZoneSameInstant(zoneId).format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss"));
    }

    public static String formatSinceDate(ZonedDateTime date) {
        Duration duration = Duration.between(date.toLocalDateTime(), ZonedDateTime.now().toLocalDateTime());
        long days = duration.toDays();
        long hours = duration.toHours();
        long mins = duration.toMinutes() % 60;
        long secs = duration.getSeconds() % 60;
        return secs == 0 ? "0s" : (days > 0 ? days + "j " : "") + (hours > 0 ? hours + "h " : "") + (mins > 0 ? mins + "m " : "") + (secs > 0 ? secs + "s" : "");
    }

    public static ZonedDateTime parseDateFromMHScript(String date) {
        // 2016-02-03 04:54:13
        date = date.replace(" ", "T") + "+01:00[Europe/Paris]";
        return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static ZoneId getZoneIdFromUserOption(Set<UserOption> options) {
        Optional<UserOption> zone = options.stream().filter(userOption -> userOption.getName() == UserOptionName.ZONEID).findFirst();
        return zone.isPresent() ? ZoneId.of(zone.get().getValue()) : ZoneId.of("GMT+01:00");
    }
}
