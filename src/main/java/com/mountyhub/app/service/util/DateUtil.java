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
        return (hours < 10 ? "0" + hours : hours) + "h " + (mins < 10 ? "0" + mins : mins) + "m " + (secs < 10 ? "0" + secs : secs) + "s";
    }
}
