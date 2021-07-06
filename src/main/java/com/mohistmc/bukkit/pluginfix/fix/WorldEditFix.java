package com.mohistmc.bukkit.pluginfix.fix;

import org.bukkit.Material;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.objectweb.asm.Opcodes.*;

public class WorldEditFix {

    public static byte[] replaceGetKeyByGetKeyForgeAndGetAsString(byte[] basicClass) {
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
                    if (methodInsnNode.owner.equals("org/bukkit/Material") && methodInsnNode.name.equals("getKey") && methodInsnNode.desc.equals("()Lorg/bukkit/NamespacedKey;")) {
                        methodInsnNode.name = "getKeyForge";
                    }
                    if (methodInsnNode.owner.equals("org/bukkit/block/data/BlockData") && methodInsnNode.name.equals("getAsString") && methodInsnNode.desc.equals("()Ljava/lang/String;")) {
                        methodInsnNode.name = "getAsStringFix";
                    }
                }
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public static byte[] replaceAdaptBlockType(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            // Fix method 'public static Material adapt(BlockType blockType)'
            if (methodNode.name.equals("adapt") && methodNode.desc.equals("(Lcom/sk89q/worldedit/world/block/BlockType;)Lorg/bukkit/Material;")) {
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "com/sk89q/worldedit/world/block/BlockType", "getId", "()Ljava/lang/String;"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(WorldEditFix.class), "adaptFix", "(Ljava/lang/String;)Lorg/bukkit/Material;"));
                toInject.add(new InsnNode(ARETURN));
                methodNode.instructions = toInject;
            }
            // Fix method 'public static Material adapt(ItemType itemType)'
            if (methodNode.name.equals("adapt") && methodNode.desc.equals("(Lcom/sk89q/worldedit/world/item/ItemType;)Lorg/bukkit/Material;")) {
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "com/sk89q/worldedit/world/item/ItemType", "getId", "()Ljava/lang/String;"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(WorldEditFix.class), "adaptFix", "(Ljava/lang/String;)Lorg/bukkit/Material;"));
                toInject.add(new InsnNode(ARETURN));
                methodNode.instructions = toInject;
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    // Method inject in worldedit plugin
    public static Material adaptFix(String type) {
        checkNotNull(type);
        if (!type.startsWith("minecraft:")) {
            return Material.getMaterial(type.replace(":", "_").toUpperCase(Locale.ROOT));
        }
        return Material.getMaterial(type.substring(10).toUpperCase(Locale.ROOT));
    }
}
