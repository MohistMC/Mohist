package cc.uraniummc.api;

import cc.uraniummc.Uranium;
import java.io.File;

public abstract class UraniumUtil {
    private static UraniumUtil instance;
    static public void setInstance(UraniumUtil instance){
        if(UraniumUtil.instance!=null){
            throw new UnsupportedOperationException("Already set instance");
        }
        UraniumUtil.instance=instance;
    }
    public static void restart() {
        Uranium.restart();
    }

    public static int lookupForgeRevision() {
        return Uranium.lookupForgeRevision();
    }


    public static String getCurrentVersion() {
        return Uranium.getCurrentVersion();
    }


    public static File getServerLocation() {
        return Uranium.getServerLocation();
    }


    public static File getServerHome() {
        return Uranium.getServerHome();
    }

    public static String getGroup() {
        return Uranium.getGroup();
    }

    public static String getBranch() {
        return Uranium.getBranch();
    }

    public static String getChannel() {
        return Uranium.getChannel();
    }

    public static boolean isLegacy() {
        return Uranium.isLegacy();
    }

    public static boolean isOfficial() {
        return Uranium.isOfficial();
    }
}
