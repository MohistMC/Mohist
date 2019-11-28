package cc.uraniummc;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class ULog {
    private static final ULog DEFAULT_LOGGER = new ULog("Uranium");

    public static ULog get() {
        return DEFAULT_LOGGER;
    }

    public static ULog get(String tag) {
        return new ULog("Uranium: " + tag);
    }
    
    private final String mTag;

    public ULog(String tag) {
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
