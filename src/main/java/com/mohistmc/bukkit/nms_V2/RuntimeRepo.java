package com.mohistmc.bukkit.nms_v2;

import net.md_5.specialsource.repo.ClassRepo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RuntimeRepo
 *
 * @author Mainly by IzzelAliz and modified Mgazul
 * &#064;originalClassName RuntimeRepo
 * &#064;classFrom <a href="https://github.com/IzzelAliz/Arclight/blob/1.19/arclight-common/src/main/java/io/izzel/arclight/common/mod/util/remapper/RuntimeRepo.java">Click here to get to github</a>
 *
 * These classes are modified by MohistMC to support the Mohist software.
 */
public class RuntimeRepo implements ClassRepo {

    private final Map<String, ClassNode> map = new ConcurrentHashMap<>();

    @Override
    public ClassNode findClass(String internalName) {
        return map.get(internalName);
    }

    public void put(byte[] bytes) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, ClassReader.SKIP_CODE);
        this.map.put(reader.getClassName(), node);
    }
}
