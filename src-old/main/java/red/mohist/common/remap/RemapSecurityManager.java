package red.mohist.common.remap;

public class RemapSecurityManager extends SecurityManager {

    Class<?> getCallerClass(final int skip) {
        return (Class<?>) this.getClassContext()[skip + 1];
    }
}
