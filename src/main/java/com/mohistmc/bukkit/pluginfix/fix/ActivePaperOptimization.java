package com.mohistmc.bukkit.pluginfix.fix;

import com.mohistmc.configuration.MohistConfig;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ARETURN;

public class ActivePaperOptimization {

    public static byte[] activePaperOptimization(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("isPaper") && methodNode.desc.equals("()Z")) {
                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ActivePaperOptimization.class), "isPaper", "()Z"));
                toInject.add(new InsnNode(IRETURN));
                methodNode.instructions = toInject;
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public static boolean isPaper() {
        String config_value = MohistConfig.instance.paperoptimization.getValue();
        if (config_value.equals("true"))
            return true;
        return false;
    }

}
