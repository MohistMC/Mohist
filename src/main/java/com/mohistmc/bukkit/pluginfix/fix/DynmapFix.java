package com.mohistmc.bukkit.pluginfix.fix;

import com.mohistmc.bukkit.nms.utils.RemapUtils;
import java.util.ListIterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import static org.objectweb.asm.Opcodes.ARETURN;

public class DynmapFix {

    public static byte[] replaceBukkitVersionHelperSpigot116_4(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("getNMSPackage")) {
                InsnList insnList = new InsnList();
                insnList.add(new LdcInsnNode(RemapUtils.nmspackage));
                insnList.add(new InsnNode(ARETURN));
                method.instructions = insnList;
                method.tryCatchBlocks.clear(); // fix java.lang.ClassFormatError: Illegal exception table range in class file
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public static byte[] replaceBukkitVersionHelperGeneric(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            for (AbstractInsnNode next : method.instructions) {
                if (next instanceof LdcInsnNode) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) next;
                    if (ldcInsnNode.cst instanceof String) {
                        String str = (String) ldcInsnNode.cst;
                        if ("[Lnet.minecraft.server.BiomeBase;".equals(str)) {
                            ldcInsnNode.cst = "[Lnet.minecraft.world.biome.Biome;";
                        }
                    }
                }
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
