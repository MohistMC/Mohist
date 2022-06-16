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

import com.mohistmc.bukkit.pluginfix.fix.DynmapFix;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;


import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;

public class PluginFixManager {

    public static byte[] injectPluginFix(String className, byte[] clazz) {
        if (className.endsWith("PaperLib")) {
            return PluginFixManager.removePaper(clazz);
        }
        if (className.equals("org.dynmap.bukkit.helper.v116_4.BukkitVersionHelperSpigot116_4")) {
            return DynmapFix.replaceBukkitVersionHelperSpigot116_4(clazz);
        }
        if (className.equals("org.dynmap.bukkit.helper.BukkitVersionHelperGeneric")) {
            return DynmapFix.replaceBukkitVersionHelperGeneric(clazz);
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

}
