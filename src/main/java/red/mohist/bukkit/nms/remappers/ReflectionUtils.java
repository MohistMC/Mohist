package red.mohist.bukkit.nms.remappers;

/**
 * Based on Apache's ReflectionUtil
 * @author Maxqia
 */
public class ReflectionUtils {

    private static final SecurityManager sm = new SecurityManager();

    public static ClassLoader getCallerClassloader() {
        return sm.getCallerClass(3).getClassLoader(); // added one due to it being the caller of the caller;
    }

    static class SecurityManager extends java.lang.SecurityManager {
        public Class<?> getCallerClass(int skip) {
            return getClassContext()[skip + 1];
        }
    }
}
