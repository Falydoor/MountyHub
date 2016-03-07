package com.mountyhub.app.service.util;

import java.time.Duration;

/**
 * Created by Theo on 3/2/16.
 */
public class DateUtil {
    public static String formatDuration(Duration duration) {
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
}
