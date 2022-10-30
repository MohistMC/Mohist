package com.mohistmc.bukkit.pluginfix;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;


import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;

public class PluginFixManager {

    public static byte[] injectPluginFix(String className, byte[] clazz) {
        if (className.endsWith("PaperLib")) {
            return PluginFixManager.removePaper(clazz);
        }
        if (className.equals("com.earth2me.essentials.utils.VersionUtil")) {
            return helloWorld(clazz, "net.minecraftforge.common.MinecraftForge", "hello.World");
        }
        return clazz;
    }

    public static byte[] removePaper(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("isPaper") && methodNode.desc.equals("()Z")) {
                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(PluginFixManager.class), "isPaper", "()Z"));
                toInject.add(new InsnNode(IRETURN));
                methodNode.instructions = toInject;
            }
        }
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public static boolean isPaper() {
        return false;
    }

    public static byte[] helloWorld(byte[] basicClass, String a, String b) {
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
                        if (a.equals(str)) {
                            ldcInsnNode.cst = b;
                        }
                    }
                }
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
