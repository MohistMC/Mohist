package com.mohistmc.bukkit.pluginfix;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/11 4:24:52
 * FAWE: <a href="https://github.com/IntellectualSites/download/raw/gh-pages/artifacts/Fawe/FastAsyncWorldEdit-Bukkit-2.2.1-SNAPSHOT-193.jar">...</a>
 */
public class WorldEdit {

    public static byte[] patchSpigot_v1_16_R3(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for1:
        for (MethodNode methodNode : node.methods) {
            if (methodNode.name.equals("getProperties") && methodNode.desc.equals("(Lcom/sk89q/worldedit/world/block/BlockType;)Ljava/util/Map;")) {
                for (AbstractInsnNode insnNode : methodNode.instructions) {
                    if (insnNode instanceof InsnNode) {
                        if (insnNode.getOpcode() == Opcodes.ATHROW) {
                            InsnList insnList = new InsnList();
                            insnList.add(new InsnNode(Opcodes.POP));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/util/Collections", "emptyMap", "()Ljava/util/Map;"));
                            insnList.add(new InsnNode(Opcodes.ARETURN));
                            methodNode.instructions.insertBefore(insnNode, insnList);
                            methodNode.instructions.remove(insnNode);
                            break for1;
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static void handlePickName(ClassNode node) {
        for (MethodNode method : node.methods) {
            if (method.name.equals("pickName")) {
                method.instructions.clear();
                method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                method.instructions.add(new InsnNode(Opcodes.ARETURN));
                return;
            }
        }
    }
}
