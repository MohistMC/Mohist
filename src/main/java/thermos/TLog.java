package thermos;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class TLog {
    private static final TLog DEFAULT_LOGGER = new TLog("Thermos");

    public static TLog get() {
        return DEFAULT_LOGGER;
    }

    public static TLog get(String tag) {
        return new TLog("Thermos: " + tag);
    }
    
    private final String mTag;

    public TLog(String tag) {
        mTag = tag;
    }

    public void log(Level level, Throwable throwable, String message,
            Object... args) {
        Throwable t = null;
        if (throwable != null) {
            t = new Throwable();
            t.initCause(throwable);
            t.fillInStackTrace();
        }
        FMLLog.log(mTag, level, t, String.format(message, args));
    }

    public void warning(String message, Object... args) {
        log(Level.WARN, null, message, args);
    }

    public void warning(Throwable throwable, String message,
            Object... args) {
        log(Level.WARN, throwable, message, args);
    }

    public void info(String message, Object... args) {
        log(Level.INFO, null, message, args);
    }

    public void info(Throwable throwable, String message,
            Object... args) {
        log(Level.INFO, throwable, message, args);
    }
}
