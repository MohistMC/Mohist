package com.mohistmc.bukkit.nms_v2;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mohistmc.bukkit.nms_v2.utils.Unsafe;
import net.md_5.specialsource.InheritanceMap;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * MagmaRemapper
 *
 * @author Mainly by IzzelAliz and modified Mgazul
 * &#064;originalClassName ArclightRemapper
 * &#064;classFrom <a href="https://github.com/IzzelAliz/Arclight/blob/1.19/arclight-common/src/main/java/io/izzel/arclight/common/mod/util/remapper/ArclightRemapper.java">Click here to get to github</a>
 *
 * These classes are modified by MohistMC to support the Mohist software.
 */
@SuppressWarnings("unchecked")
public class MohistRemapper {

    public static final MohistRemapper INSTANCE;
    public static final File DUMP;

    static {
        try {
            INSTANCE = new MohistRemapper();
            DUMP = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final JarMapping toNmsMapping;
    private final JarMapping toBukkitMapping;
    public final InheritanceMap inheritanceMap;
    private final List<PluginTransformer> transformerList = new ArrayList<>();
    private final JarRemapper toBukkitRemapper;
    private final JarRemapper toNmsRemapper;

    public MohistRemapper() throws Exception {
        this.toNmsMapping = new JarMapping();
        this.toBukkitMapping = new JarMapping();
        this.inheritanceMap = new InheritanceMap();
        this.toNmsMapping.loadMappings(
            new BufferedReader(new InputStreamReader(MohistRemapper.class.getClassLoader().getResourceAsStream("mappings/nms.srg"))),
            null, null, false
        );
        // TODO workaround for https://github.com/md-5/SpecialSource/pull/81
        //  remove on update
        var content = new String(MohistRemapper.class.getClassLoader().getResourceAsStream("mappings/nms.srg").readAllBytes(), StandardCharsets.UTF_8);
        var i = content.indexOf("net/minecraft/server/level/ChunkMap net/minecraft/server/level/ChunkTracker");
        var nextSection = content.substring(i).lines().skip(1).dropWhile(it -> it.startsWith("\t")).findFirst().orElseThrow();
        var nextIndex = content.indexOf(nextSection);
        this.toBukkitMapping.loadMappings(
            new BufferedReader(new StringReader(content.substring(0, i) + content.substring(nextIndex))),
            null, null, true
        );
        this.toBukkitMapping.loadMappings(
            new BufferedReader(new StringReader(content.substring(i, nextIndex))),
            null, null, true
        );
        BiMap<String, String> inverseClassMap = HashBiMap.create(toNmsMapping.classes).inverse();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(MohistRemapper.class.getClassLoader().getResourceAsStream("mappings/inheritanceMap.txt")))) {
            inheritanceMap.load(reader, inverseClassMap);
        }
        JointProvider inheritanceProvider = new JointProvider();
        inheritanceProvider.add(inheritanceMap);
        inheritanceProvider.add(new ClassLoaderProvider(ClassLoader.getSystemClassLoader()));
        this.toNmsMapping.setFallbackInheritanceProvider(inheritanceProvider);
        this.toBukkitMapping.setFallbackInheritanceProvider(inheritanceProvider);
        this.transformerList.add(MohistInterfaceInvokerGen.INSTANCE);
        this.transformerList.add(MohistRedirectAdapter.INSTANCE);
        this.transformerList.add(ClassLoaderAdapter.INSTANCE);
        //this.transformerList.add((node, remapper) -> SwitchTableFixer.handleClass(node));
        toBukkitMapping.setFallbackInheritanceProvider(GlobalClassRepo.inheritanceProvider());
        this.toBukkitRemapper = new LenientJarRemapper(toBukkitMapping);
        this.toNmsRemapper = new LenientJarRemapper(toNmsMapping);
        RemapSourceHandler.register();
    }

    public static ClassLoaderRemapper createClassLoaderRemapper(ClassLoader classLoader) {
        return new ClassLoaderRemapper(INSTANCE.copyOf(INSTANCE.toNmsMapping), INSTANCE.copyOf(INSTANCE.toBukkitMapping), classLoader);
    }

    public static JarRemapper getResourceMapper() {
        return INSTANCE.toBukkitRemapper;
    }

    public static JarRemapper getNmsMapper() {
        return INSTANCE.toNmsRemapper;
    }

    public List<PluginTransformer> getTransformerList() {
        return transformerList;
    }

    private static long pkgOffset, clOffset, mdOffset, fdOffset, mapOffset;

    static {
        try {
            pkgOffset = Unsafe.objectFieldOffset(JarMapping.class.getField("packages"));
            clOffset = Unsafe.objectFieldOffset(JarMapping.class.getField("classes"));
            mdOffset = Unsafe.objectFieldOffset(JarMapping.class.getField("methods"));
            fdOffset = Unsafe.objectFieldOffset(JarMapping.class.getField("fields"));
            mapOffset = Unsafe.objectFieldOffset(JarMapping.class.getDeclaredField("inheritanceMap"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private JarMapping copyOf(JarMapping mapping) {
        JarMapping jarMapping = new JarMapping();
        Unsafe.putObject(jarMapping, pkgOffset, Unsafe.getObject(mapping, pkgOffset));
        Unsafe.putObject(jarMapping, clOffset, Unsafe.getObject(mapping, clOffset));
        Unsafe.putObject(jarMapping, mdOffset, Unsafe.getObject(mapping, mdOffset));
        Unsafe.putObject(jarMapping, fdOffset, Unsafe.getObject(mapping, fdOffset));
        Unsafe.putObject(jarMapping, mapOffset, Unsafe.getObject(mapping, mapOffset));
        return jarMapping;
    }
}
