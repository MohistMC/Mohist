package net.minecraft.launchwrapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogWrapper {
    public static LogWrapper log = new LogWrapper();
    private Logger myLog;

    private static boolean configured;

    private static void configureLogging() {
        log.myLog = LogManager.getLogger("LaunchWrapper");
        configured = true;
    }

    public static void retarget(Logger to) {
        log.myLog = to;
    }
    public static void log(String logChannel, Level level, String format, Object... data) {
        makeLog(logChannel);
        LogManager.getLogger(logChannel).log(level, String.format(format, data));
    }

    public static void log(Level level, String format, Object... data) {
        if (!configured) {
            configureLogging();
        }
        log.myLog.log(level, String.format(format, data));
    }

    public static void log(String logChannel, Level level, Throwable ex, String format, Object... data) {
        makeLog(logChannel);
        LogManager.getLogger(logChannel).log(level, String.format(format, data), ex);
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        if (!configured) {
            configureLogging();
        }
        log.myLog.log(level, String.format(format, data), ex);
    }

    public static void severe(String format, Object... data) {
        log(Level.ERROR, format, data);
    }

    public static void warning(String format, Object... data) {
        log(Level.WARN, format, data);
    }

    public static void info(String format, Object... data) {
        log(Level.INFO, format, data);
    }

    public static void fine(String format, Object... data) {
        log(Level.DEBUG, format, data);
    }

    public static void finer(String format, Object... data) {
        log(Level.TRACE, format, data);
    }

    public static void finest(String format, Object... data) {
        log(Level.TRACE, format, data);
    }

    public static void makeLog(String logChannel) {
        LogManager.getLogger(logChannel);
    }
}
