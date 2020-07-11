package com.empireminecraft;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MCLog {
    private static final Logger LOGGER = Logger.getLogger("NMS");
    private static final Pattern NEWLINE = Pattern.compile("\n");

    private MCLog() {
    }


    public static void log(String message) {
        info(message);
    }


    public static void info(String message) {
        for (String s : NEWLINE.split(message)) {
            LOGGER.info(s);
        }
    }

    public static void warn(String message) {
        for (String s : NEWLINE.split(message)) {
            LOGGER.warning(s);
        }
    }

    public static void severe(String message) {
        for (String s : NEWLINE.split(message)) {
            LOGGER.severe(s);
        }
    }

    public static void exception(String msg) {
        exception(new Throwable(msg));
    }

    public static void exception(Throwable e) {
        exception(e.getMessage(), e);
    }

    public static void exception(String msg, Throwable e) {
        if (msg != null) {
            severe(msg);
        }
        severe(ExceptionUtils.getFullStackTrace(e));
    }


}