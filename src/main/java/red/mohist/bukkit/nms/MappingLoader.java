package red.mohist.bukkit.nms;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MavenShade;
import red.mohist.Mohist;
import red.mohist.bukkit.nms.cache.ClassMapping;
import red.mohist.bukkit.nms.proxy.ProxyClass;
import red.mohist.bukkit.nms.proxy.ProxyClassLoader;
import red.mohist.bukkit.nms.proxy.ProxyField;
import red.mohist.bukkit.nms.proxy.ProxyMethod;
import red.mohist.bukkit.nms.proxy.ProxyMethodHandles_Lookup;
import red.mohist.bukkit.nms.proxy.ProxyYamlConfiguration;
import red.mohist.bukkit.nms.remappers.MohistJarRemapper;
import red.mohist.bukkit.nms.remappers.RemapUtils;

public class MappingLoader {
    public static JarMapping jarMapping;
    public static MohistJarRemapper remapper;
    private static final String org_bukkit_craftbukkit = new String(new char[] {'o','r','g','/','b','u','k','k','i','t','/','c','r','a','f','t','b','u','k','k','i','t'});

    public static JarMapping loadMapping() {
        JarMapping jarMapping = new JarMapping();
        try {
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/it/unimi/dsi/fastutil", "it/unimi/dsi/fastutil");
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/jline", "jline");
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/joptsimple", "joptsimple");
            jarMapping.methods.put("org/bukkit/Bukkit/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "_INVALID_getOnlinePlayers");
            jarMapping.methods.put("org/bukkit/Server/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "_INVALID_getOnlinePlayers");
            jarMapping.methods.put(org_bukkit_craftbukkit + "/v1_12_R1/CraftServer/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "_INVALID_getOnlinePlayers");

            Map<String, String> relocations = Maps.newHashMap();
            relocations.put(ClassUtils.NMS_PREFIX1, ClassUtils.NMS_PREFIX2);
            jarMapping.loadMappings(ClassUtils.getSrgBufferedReader(), new MavenShade(relocations),null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jarMapping;
    }

    public static void loadMappings() throws IOException {
        String line;
        BufferedReader reader = ClassUtils.getSrgBufferedReader();
        while ((line = reader.readLine()) != null) {
            int commentIndex = line.indexOf('#');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }
            if (line.isEmpty() || !line.startsWith("MD: ")) {
                continue;
            }
            String[] sp = line.split("\\s+");
            String firDesc = sp[2];
            String secDesc = sp[4];
            ClassMapping.map_MD.put(firDesc, secDesc);
        }
    }

    static {
        ClassMapping.VirtualMethodToStatic.put("java/lang/Class;getField", ProxyClass.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/Class;getDeclaredField", ProxyClass.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/Class;getMethod", ProxyClass.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/Class;getDeclaredMethod", ProxyClass.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/Class;getSimpleName", ProxyClass.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/Class;getDeclaredMethods", ProxyClass.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/reflect/Field;getName", ProxyField.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/reflect/Method;getName", ProxyMethod.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/ClassLoader;loadClass", ProxyClassLoader.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findVirtual", ProxyMethodHandles_Lookup.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findStatic", ProxyMethodHandles_Lookup.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findSpecial", ProxyMethodHandles_Lookup.class);
        ClassMapping.VirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;unreflect", ProxyMethodHandles_Lookup.class);
        ClassMapping.VirtualMethodToStatic.put("org/bukkit/configuration/file/YamlConfiguration;loadConfiguration", ProxyYamlConfiguration.class);
    }

    public static void initializeMAP() {
        jarMapping = RemapUtils.loadJarMapping(null , false);
        remapper = RemapUtils.loadRemapper(null , false);

        jarMapping.classes.forEach((k, v) -> ClassMapping.classDeMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> ClassMapping.methodDeMapping.put(v, k));
        jarMapping.fields.forEach((k, v) -> ClassMapping.fieldDeMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> ClassMapping.methodFastMapping.put(k.split("\\s+")[0], k));

        try {
            MappingLoader.loadMappings();
        } catch (IOException e) {
            Mohist.LOGGER.error("NMS mapping table initialization failed", e);
        }
    }
}
