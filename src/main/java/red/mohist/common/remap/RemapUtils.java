package red.mohist.common.remap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodType;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.specialsource.transformer.MavenShade;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.plugin.PluginDescriptionFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import red.mohist.Mohist;
import red.mohist.common.remap.model.ClassMapping;
import red.mohist.common.remap.remappers.ClassRemapperSupplier;
import red.mohist.common.remap.remappers.MohistInheritanceMap;
import red.mohist.common.remap.remappers.MohistInheritanceProvider;
import red.mohist.common.remap.remappers.MohistJarMapping;
import red.mohist.common.remap.remappers.MohistJarRemapper;
import red.mohist.common.remap.remappers.NMSVersionRemapper;
import red.mohist.common.remap.remappers.ReflectRemapper;
import red.mohist.configuration.MohistConfig;
import red.mohist.util.JarTool;
import sun.reflect.Reflection;

/**
 *
 * @author pyz
 * @date 2019/6/30 11:50 PM
 */
public class RemapUtils {

    public static final String nmsPrefix = "net.minecraft.server.";
    public static final String mohistPrefix = "red.mohist.";
    public static final MohistJarMapping jarMapping;
    private static final List<Remapper> remappers = new ArrayList<>();

    static {
        jarMapping = new MohistJarMapping();
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/", "it/unimi/dsi/fastutil/");
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/jline/", "jline/");
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/joptsimple/", "joptsimple/");
        jarMapping.registerMethodMapping("org/bukkit/Bukkit", "getOnlinePlayers", "()[Lorg/bukkit/entity/Player;", "org/bukkit/Bukkit", "_INVALID_getOnlinePlayers", "()[Lorg/bukkit/entity/Player;");
        jarMapping.registerMethodMapping("org/bukkit/Server", "getOnlinePlayers", "()[Lorg/bukkit/entity/Player;", "org/bukkit/Server", "_INVALID_getOnlinePlayers", "()[Lorg/bukkit/entity/Player;");
        jarMapping.registerMethodMapping("org/bukkit/craftbukkit/" + Mohist.getNativeVersion() + "/CraftServer", "getOnlinePlayers", "()[Lorg/bukkit/entity/Player;", "org/bukkit/craftbukkit/" + Mohist.getNativeVersion() + "/CraftServer", "_INVALID_getOnlinePlayers", "()[Lorg/bukkit/entity/Player;");
        jarMapping.setInheritanceMap(new MohistInheritanceMap());
        jarMapping.setFallbackInheritanceProvider(new MohistInheritanceProvider());

        Map<String, String> relocations = new HashMap<String, String>();
        relocations.put("net.minecraft.server", "net.minecraft.server." + Mohist.getNativeVersion());
        try {
            String f = JarTool.getJarDir();
            String f1 = f
                    .replace("file:\\", "") // win
                    .replace("file:/", "") // linux
                    .replace("\\red\\mohist\\util", "") // win
                    .replace("/red/mohist/util", ""); // linux
            String jarname = f1.substring(f1.lastIndexOf("\\")+1,f1.lastIndexOf("."));
            String jarname1 = f1.substring(f1.lastIndexOf("/")+1,f1.lastIndexOf("."));
            String path = f1
                    .replace("\\" + jarname + ".jar!", "")
                    .replace("/" + jarname1 + ".jar!", "");
            String fName;
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                fName = path + "/libraries/red/mohist/mappings/nms.srg";
            } else {
                fName = "/" + path + "/libraries/red/mohist/mappings/nms.srg";
            }
            File nms = new File(fName);
            if (!nms.exists()) {
                Mohist.LOGGER.error("Unable to find remapping dependencies, please re-download the libraries file!");
                FMLCommonHandler.instance().exitJava(1, true);
            }
            FileInputStream fos = new FileInputStream(nms);
            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(fos)),
                    new MavenShade(relocations),
                    null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MohistConfig.instance.multiVersionRemap.getValue()) {
            remappers.add(new NMSVersionRemapper());
        }
        MohistJarRemapper jarRemapper = new MohistJarRemapper(jarMapping);
        if (MohistConfig.instance.nmsRemap.getValue()) {
            remappers.add(jarRemapper);
        }
        if (MohistConfig.instance.reflectRemap.getValue()) {
            remappers.add(new ReflectRemapper());
        }
        jarMapping.initFastMethodMapping(jarRemapper);
    }

    private static final Object remapLock = new Object();

    public static byte[] remapFindClass(PluginDescriptionFile description, String name, byte[] bs) throws IOException {
        synchronized (remapLock) {
            if (MohistConfig.instance.printRemapPluginClass.getValue()) {
                System.out.println("========= before remap ========= ");
                ASMUtils.printClass(bs);
            }
            ClassNode classNode = new ClassNode();
            new ClassReader(bs).accept(classNode, ClassReader.EXPAND_FRAMES);
            for (Remapper remapper : remappers) {
                if (description != null && remapper instanceof NMSVersionRemapper && !MohistConfig.instance.multiVersionRemapPlugins.contains(description.getName())) {
                    continue;
                }
                try {
                    RemapContext.push(new RemapContext().setClassNode(classNode).setDescription(description));
                    ClassNode container = new ClassNode();
                    ClassRemapper classRemapper;
                    if (remapper instanceof ClassRemapperSupplier) {
                        classRemapper = ((ClassRemapperSupplier) remapper).getClassRemapper(container);
                    } else {
                        classRemapper = new ClassRemapper(container, remapper);
                    }
                    classNode.accept(classRemapper);
                    classNode = container;
                    if (MohistConfig.instance.printRemapPluginClass.getValue()) {
                        System.out.println("========= after " + remapper.getClass().getSimpleName() + " remap ========= ");
                        ASMUtils.printClass(classNode);
                    }
                } finally {
                    RemapContext.pop();
                }
            }
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            bs = writer.toByteArray();
            if (MohistConfig.instance.dumpRemapPluginClass.getValue()) {
                ASMUtils.dump(Paths.get(System.getProperty("user.dir"), "dumpRemapPluginClass"), bs);
            }
            return bs;
        }
    }

    public static String map(String typeName) {
        typeName = mapPackage(typeName);
        return jarMapping.classes.getOrDefault(typeName, typeName);
    }

    public static String reverseMap(String typeName) {
        ClassMapping mapping = jarMapping.byNMSInternalName.get(typeName);
        return mapping == null ? typeName : mapping.getNmsSrcName();
    }

    public static String reverseMap(Class clazz) {
        ClassMapping mapping = jarMapping.byMCPName.get(clazz.getName());
        return mapping == null ? ASMUtils.toInternalName(clazz) : mapping.getNmsSrcName();
    }

    public static String mapPackage(String typeName) {
        for (Map.Entry<String, String> entry : jarMapping.packages.entrySet()) {
            String prefix = entry.getKey();
            if (typeName.startsWith(prefix)) {
                return entry.getValue() + typeName.substring(prefix.length());
            }
        }
        return typeName;
    }

    public static String remapMethodDesc(String methodDescriptor) {
        Type rt = Type.getReturnType(methodDescriptor);
        Type[] ts = Type.getArgumentTypes(methodDescriptor);
        rt = Type.getType(ASMUtils.toDescriptorV2(map(ASMUtils.getInternalName(rt))));
        for (int i = 0; i < ts.length; i++) {
            ts[i] = Type.getType(ASMUtils.toDescriptorV2(map(ASMUtils.getInternalName(ts[i]))));
        }
        return Type.getMethodType(rt, ts).getDescriptor();
    }

    public static String mapMethodName(Class clazz, String name, MethodType methodType) {
        return mapMethodName(clazz, name, methodType.parameterArray());
    }

    public static String mapMethodName(Class type, String name, Class<?>... parameterTypes) {
        return jarMapping.fastMapMethodName(type, name, parameterTypes);
    }

    public static String inverseMapMethodName(Class type, String name, Class<?>... parameterTypes) {
        return jarMapping.fastReverseMapMethodName(type, name, parameterTypes);
    }

    public static String mapFieldName(Class type, String fieldName) {
        return jarMapping.fastMapFieldName(type, fieldName);
    }

    public static String inverseMapFieldName(Class type, String fieldName) {
        return jarMapping.fastReverseMapFieldName(type, fieldName);
    }

    public static ClassLoader getCallerClassLoder() {
        return Reflection.getCallerClass(3).getClassLoader();
    }

    public static String inverseMapName(Class clazz) {
        ClassMapping mapping = jarMapping.byMCPName.get(clazz.getName());
        return mapping == null ? clazz.getName() : mapping.getNmsName();
    }

    public static String inverseMapSimpleName(Class clazz) {
        ClassMapping mapping = jarMapping.byMCPName.get(clazz.getName());
        return mapping == null ? clazz.getSimpleName() : mapping.getNmsSimpleName();
    }
}
