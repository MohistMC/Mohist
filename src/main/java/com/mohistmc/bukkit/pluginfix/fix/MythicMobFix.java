package com.mohistmc.bukkit.pluginfix.fix;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;

public class MythicMobFix {

    public static byte[] patchVolatileWorldHandler(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            ListIterator<AbstractInsnNode> nodeListIterator = methodNode.instructions.iterator();
            while (nodeListIterator.hasNext()) {
                AbstractInsnNode nextNode = nodeListIterator.next();
                if (nextNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) nextNode;
                    if (methodInsnNode.owner.equals("net/minecraft/world/chunk/Chunk") && methodInsnNode.name.equals("getEntitySlices") && methodInsnNode.desc.equals("()[Ljava/util/List;")) {
                        methodInsnNode.name = "func_177429_s";
                        methodInsnNode.desc = "()[Lnet/minecraft/util/ClassInheritanceMultiMap;";
                    }
                    if (methodInsnNode.owner.equals("java/util/List") && (methodInsnNode.name.equals("size") || methodInsnNode.name.equals("iterator") || methodInsnNode.name.equals("add") || methodInsnNode.name.equals("remove"))) {
                        methodInsnNode.owner = "java/util/Collection";
                    }
                }
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

}
