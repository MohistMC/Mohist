package org.spigotmc;

public class SneakyThrow {

    public static void sneaky(Throwable t) {
        throw SneakyThrow.superSneaky(t);
    }

    private static <T extends Throwable> T superSneaky(Throwable t) throws T {
        throw (T) t;
    }
}
