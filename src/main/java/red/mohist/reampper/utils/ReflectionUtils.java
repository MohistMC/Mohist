package red.mohist.reampper.utils;

public class ReflectionUtils {
    private static final SecurityManager securityManager = new SecurityManager();

    public static Class<?> getCallerClass(int skip) {
        return securityManager.getCallerClass(skip);
    }

    public static ClassLoader getCallerClassLoader() {
        return ReflectionUtils.getCallerClass(3).getClassLoader(); // added one due to it being the caller of the caller;
    }

    static class SecurityManager extends java.lang.SecurityManager {
        public Class<?> getCallerClass(int skip) {
            return getClassContext()[skip + 1];
        }
    }
}

