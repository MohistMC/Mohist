package com.mohistmc.bukkit.pluginfix.fix;

import com.mohistmc.configuration.MohistConfig;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.objectweb.asm.Opcodes.*;

public class ActivePaperOptimization {

    public static List<String> plugin_work_with_paper_optimization = new ArrayList<>();

    static {
        if (MohistConfig.instance.paperoptimization.getValue().equals("only_works")) {
            //plugin_work_with_paper_optimization.add("plugin class");
        }
    }

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

    public static byte[] activeIfItWorkWithPaperOptimization(byte[] basicClass) {
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
                    if (methodInsnNode.owner.endsWith("PaperLib") && methodInsnNode.name.equals("isPaper") && methodInsnNode.desc.equals("()Z")) {
                        methodInsnNode.owner = Type.getInternalName(ActivePaperOptimization.class);
                        methodInsnNode.name = "returnTrue";
                    }
                }
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

    public static boolean returnTrue() {
        return true;
    }

}
