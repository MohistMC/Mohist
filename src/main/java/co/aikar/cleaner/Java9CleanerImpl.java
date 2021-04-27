package co.aikar.cleaner;

public class Java9CleanerImpl {
    private static final Cleaner.CleanerAPI cleaner = Cleaner.create();

    public Java9CleanerImpl() {
    }

    public static void clean(Object object, Runnable run) {
        cleaner.register(object, run);
    }
}
