package red.mohist.bukkit.nms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author Mgazul
 * @date 2020/4/15 22:32
 */
public class ClassUtils {

    public static final String NM_PREFIX = "net.minecraft.";
    public static final String NMS_PREFIX = "net/minecraft/server/";
    public static final String NMS_PREFIX1 = "net.minecraft.server";
    public static final String NMS_PREFIX2 = "net.minecraft.server.v1_12_R1";
    public static final String NMS_PREFIX3 = "net/minecraft/server/v1_12_R1/";
    public static final String NMS_VERSION = "v1_12_R1";

    public static boolean isNeedRemap(String className) {
        return className.replace("/", ".").startsWith(NMS_PREFIX2);
    }

    public static boolean isNMClass(Class<?> refc){
        return refc.getName().startsWith(NM_PREFIX);
    }

    public static boolean isNMClass(String className){
        return className.startsWith(NM_PREFIX);
    }

    public static String getInternalName(String className){
        return className.replace('.', '/');
    }

    public static String toClassName(String internalName) {
        return internalName.replace('/', '.');
    }

    public static boolean isNMSClass(String className){
        return className.startsWith(NMS_PREFIX2);
    }

    public static BufferedReader getSrgBufferedReader(){
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/nms.srg"))));
    }
}
