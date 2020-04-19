package red.mohist.bukkit.nms;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MavenShade;

public class MappingLoader {
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
}
