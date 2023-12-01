/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.bukkit.pluginfix;

import java.util.function.Consumer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class PluginFixManager {

    public static byte[] injectPluginFix(String className, byte[] clazz) {
        if (className.endsWith("PaperLib")) {
            return patch(clazz, PluginFixManager::removePaper);
        }
        if (className.equals("com.onarandombox.MultiverseCore.utils.WorldManager")) {
            return patch(clazz, MultiverseCore::fix);
        }
        if (className.equals("org.dynmap.bukkit.helper.v116_4.BukkitVersionHelperSpigot116_4")) {
            return DynmapFix.replaceBukkitVersionHelperSpigot116_4(clazz);
        }
        if (className.equals("com.sk89q.worldedit.bukkit.adapter.impl.Spigot_v1_16_R3")) {
            return WorldEdit.patchSpigot_v1_16_R3(clazz);
        }

        Consumer<ClassNode> patcher;
        switch (className) {
            case "com.sk89q.worldedit.bukkit.adapter.Refraction":
                patcher = WorldEdit::handlePickName;
                break;
            case "com.earth2me.essentials.utils.VersionUtil":
                patcher = node -> helloWorld(node, 110, 109);
                break;
            case "org.dynmap.bukkit.helper.BukkitVersionHelperGeneric":
                patcher = node -> helloWorld(node, "[Lnet.minecraft.server.BiomeBase;", "[Lnet.minecraft.world.biome.Biome;");
                break;
            case "net.Zrips.CMILib.Reflections":
                patcher = node -> helloWorld(node, "bR", "f_36096_");
                break;
            default:
                patcher = null;
                break;
        }
        return patcher == null ? clazz : patch(clazz, patcher);
    }

    private static byte[] patch(byte[] basicClass, Consumer<ClassNode> handler) {
        ClassNode node = new ClassNode();
        new ClassReader(basicClass).accept(node, 0);
        handler.accept(node);
        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static void removePaper(ClassNode node) {
        for (MethodNode methodNode : node.methods) {
            if (methodNode.name.equals("isPaper") && methodNode.desc.equals("()Z")) {
                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(PluginFixManager.class), "isPaper", "()Z"));
                toInject.add(new InsnNode(Opcodes.IRETURN));
                methodNode.instructions = toInject;
            }
        }
    }

    public static boolean isPaper() {
        return false;
    }

    private static void helloWorld(ClassNode node, String a, String b) {
        node.methods.forEach(method -> {
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
        });
    }

    private static void helloWorld(ClassNode node, int a, int b) {
        node.methods.forEach(method -> {
            for (AbstractInsnNode next : method.instructions) {
                if (next instanceof IntInsnNode) {
                    IntInsnNode ldcInsnNode = (IntInsnNode) next;
                    if (ldcInsnNode.operand == a) {
                        ldcInsnNode.operand = b;
                    }
                }
            }
        });
    }

}
