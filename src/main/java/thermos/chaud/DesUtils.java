package thermos.chaud;

public class DesUtils {

    public static boolean isModded(String pkg)
    {
    	return !(pkg.startsWith("net.minecraft") || pkg.startsWith("org.bukkit") || pkg.startsWith("cpw.mods.fml") || pkg.startsWith("org.spigotmc") || pkg.startsWith("thermos"));
    }
}
