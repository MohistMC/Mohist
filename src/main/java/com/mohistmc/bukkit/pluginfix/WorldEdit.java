package com.mohistmc.bukkit.pluginfix;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/11 4:24:52
 */
public class WorldEdit {

    public static void handlePickName(ClassNode node) {
        for (MethodNode method : node.methods) {
            if (method.name.equals("pickName")) {
                method.instructions.clear();
                method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                method.instructions.add(new InsnNode(Opcodes.ARETURN));
            }
        }
    }

    public static void handleWatchdog(ClassNode node) {
        if (node.interfaces.size() == 1 && node.interfaces.get(0).equals("com/sk89q/worldedit/extension/platform/Watchdog") && node.name.contains("SpigotWatchdog")) {
            for (MethodNode method : node.methods) {
                if (method.name.equals("<init>")) {
                    method.instructions.clear();
                    method.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/lang/ClassNotFoundException"));
                    method.instructions.add(new InsnNode(Opcodes.DUP));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/ClassNotFoundException", "<init>", "()V", false));
                    method.instructions.add(new InsnNode(Opcodes.ATHROW));
                    method.tryCatchBlocks.clear();
                    method.localVariables.clear();
                }
            }
        }
    }
}
