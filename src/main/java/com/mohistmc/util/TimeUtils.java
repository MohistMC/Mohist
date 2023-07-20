package com.mohistmc.util;

import java.util.concurrent.TimeUnit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/20 10:00:34
 */
public class TimeUtils {

    public static String getFriendlyName(TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
                return "ns";
            case MILLISECONDS:
                return "ms";
            case MICROSECONDS:
                return "micros";
            case SECONDS:
                return "s";
            case MINUTES:
                return "m";
            case DAYS:
                return "d";
            case HOURS:
                return "h";
            default:
                throw new AssertionError();
        }
    }
}
