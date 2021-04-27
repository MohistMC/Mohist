package co.aikar.cleaner;

public class Java8CleanerImpl {
    public Java8CleanerImpl() {
    }

    public static void clean(Object object, Runnable run) {
        Cleaner.register(object, run);
    }
}
