package com.mohistmc.bukkit.pluginfix;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/27 11:29:47
 */
public class MultiverseCore {

    public static void fix(ClassNode node) {
        for (MethodNode method : node.methods) {
            if (method.name.equals("doLoad") && method.desc.equals("(Ljava/lang/String;ZLorg/bukkit/WorldType;)Z")) {
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MultiverseCore.class), "isLoad", "(Ljava/lang/String;)Z"));
                toInject.add(new VarInsnNode(Opcodes.ISTORE, 2));
                method.instructions.insert(toInject);
            }
        }
    }

    public static boolean isLoad(String name) {
        return ((CraftServer) Bukkit.getServer()).getWorldsByName().contains(name);
    }
}

