package com.mohistmc.util;

import java.util.concurrent.TimeUnit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/20 10:00:34
 */
public class TimeUtils {

    public static String getFriendlyName(TimeUnit unit) {
        return switch (unit) {
            case NANOSECONDS -> "ns";
            case MILLISECONDS -> "ms";
            case MICROSECONDS -> "micros";
            case SECONDS -> "s";
            case MINUTES -> "m";
            case DAYS -> "d";
            case HOURS -> "h";
        };
    }
}
