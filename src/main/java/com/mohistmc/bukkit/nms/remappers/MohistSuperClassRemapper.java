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

package com.mohistmc.bukkit.nms.remappers;

import com.google.common.collect.Maps;
import com.mohistmc.bukkit.nms.proxy.DelegateClassLoder;
import com.mohistmc.bukkit.nms.proxy.DelegateURLClassLoder;
import com.mohistmc.bukkit.nms.utils.ASMUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Map;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
public class MohistSuperClassRemapper {
    public static Map<String, Class<?>> defineClass = Maps.newHashMap();

    public static void init(ClassNode node) {

        boolean remapSpClass  = false;
        switch (node.superName) {
            case ASMUtils.urlclassLoaderdesc:
                node.superName = Type.getInternalName(DelegateURLClassLoder.class);
                remapSpClass = true;
                break;
            case ASMUtils.classLoaderdesc:
                defineClass.put(node.name + ";defineClass", DelegateClassLoder.class);
                node.superName = Type.getInternalName(DelegateClassLoder.class);
                remapSpClass = true;
                break;
        }
        // https://github.com/Maxqia/ReflectionRemapper/blob/a75046eb0a864ad1f20b8f723ed467db614fff98/src/main/java/com/maxqia/ReflectionRemapper/Transformer.java#L68
        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW) { // remap new URLClassLoader
                    TypeInsnNode insn = (TypeInsnNode) next;
                    switch (insn.desc) {
                        case ASMUtils.urlclassLoaderdesc:
                            insn.desc = Type.getInternalName(DelegateURLClassLoder.class);
                            remapSpClass = true;
                            break;
                        case ASMUtils.classLoaderdesc:
                            insn.desc = Type.getInternalName(DelegateClassLoder.class);
                            remapSpClass = true;
                            break;
                    }
                }

                if (next instanceof MethodInsnNode) {
                    MethodInsnNode ins = (MethodInsnNode) next;
                    switch (ins.getOpcode()) {
                        case Opcodes.INVOKEVIRTUAL:
                            remapVirtual(next);
                            break;
                        case Opcodes.INVOKESPECIAL:
                            if (remapSpClass && ins.name.equals("<init>")) {
                                switch (ins.owner) {
                                    case ASMUtils.urlclassLoaderdesc:
                                        ins.owner = Type.getInternalName(DelegateURLClassLoder.class);
                                        break;
                                    case ASMUtils.classLoaderdesc:
                                        ins.owner = Type.getInternalName(DelegateClassLoder.class);
                                        break;
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    // https://github.com/Maxqia/ReflectionRemapper/blob/a75046eb0a864ad1f20b8f723ed467db614fff98/src/main/java/com/maxqia/ReflectionRemapper/Transformer.java#L95
    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        Class<?> proxyClass = ReflectMethodRemapper.getVirtualMethod().get((method.owner + ";" + method.name));
        if (proxyClass != null) {
            Type returnType = Type.getReturnType(method.desc);
            ArrayList<Type> args = new ArrayList<>();
            args.add(Type.getObjectType(method.owner));
            args.addAll(Arrays.asList(Type.getArgumentTypes(method.desc)));

            method.setOpcode(Opcodes.INVOKESTATIC);
            method.owner = Type.getInternalName(proxyClass);
            method.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
        } else {
            proxyClass = defineClass.get((method.owner + ";" + method.name));
            if (proxyClass != null) {
                method.name += "Mohist";
                method.owner = Type.getInternalName(proxyClass);
            }
        }
    }
}
