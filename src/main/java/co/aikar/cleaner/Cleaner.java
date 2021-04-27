package co.aikar.cleaner;

public class Cleaner {
    private static final Cleaner.CleanerAPI api = create();

    public Cleaner() {
    }

    static Cleaner.CleanerAPI create() {
        try {
            Class.forName("java.lang.ref.Cleaner");
            return Java9CleanerImpl::clean;
        } catch (ClassNotFoundException var3) {
            try {
                Class.forName("sun.misc.Cleaner");
                return Java8CleanerImpl::clean;
            } catch (ClassNotFoundException var2) {
                throw new RuntimeException("No cleaner method supported");
            }
        }
    }

    public static void register(Object obj, Runnable run) {
        api.register(obj, run);
    }

    public interface CleanerAPI {
        void register(Object var1, Runnable var2);
    }
}
