package com.mohistmc.bukkit.nms.remappers;

import com.google.common.collect.Maps;
import com.mohistmc.bukkit.nms.proxy.DelegateClassLoder;
import com.mohistmc.bukkit.nms.proxy.DelegateURLClassLoder;
import com.mohistmc.bukkit.nms.utils.ASMUtils;
import com.mohistmc.bukkit.pluginfix.JavaScriptRemaper;
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

    public static Map<String, Class<?>> superClassMap = Maps.newHashMap();
    public static Map<String, Class<?>> defineClass = Maps.newHashMap();

    static {
        superClassMap.put("java/net/URLClassLoader", DelegateURLClassLoder.class);
        superClassMap.put(ASMUtils.classLoaderdesc, DelegateClassLoder.class);
    }

    public static void init(ClassNode node) {

        boolean isDefineClass = false;
        Class<?> superClass = MohistSuperClassRemapper.superClassMap.get(node.superName);
        if (superClass != null) {
            if (superClass == DelegateClassLoder.class) defineClass.put(node.name + ";defineClass", DelegateClassLoder.class);
            node.superName = superClass.getName().replace('.', '/');
            isDefineClass = true;
        }
        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW) { // remap new URLClassLoader
                    TypeInsnNode insn = (TypeInsnNode) next;
                    Class<?> remappedClass = superClassMap.get(insn.desc);
                    if (remappedClass != null) {
                        insn.desc = Type.getInternalName(remappedClass);
                        isDefineClass = true;
                    }
                }

                if (next instanceof MethodInsnNode) {
                    MethodInsnNode insn = (MethodInsnNode) next;
                    switch (insn.getOpcode()) {
                        case Opcodes.INVOKEVIRTUAL:
                            Class<?> VirtualMethodClass = ReflectMethodRemapper.getVirtualMethod().get((insn.owner + ";" + insn.name));
                            if (VirtualMethodClass != null) {
                                Type returnType = Type.getReturnType(insn.desc);
                                ArrayList<Type> args = new ArrayList<>();
                                args.add(Type.getObjectType(insn.owner));
                                args.addAll(Arrays.asList(Type.getArgumentTypes(insn.desc)));

                                insn.setOpcode(Opcodes.INVOKESTATIC);
                                insn.owner = Type.getInternalName(VirtualMethodClass);
                                insn.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
                            } else {
                                VirtualMethodClass = defineClass.get((insn.owner + ";" + insn.name));
                                if (VirtualMethodClass != null) {
                                    insn.name += "Mohist";
                                    insn.owner = Type.getInternalName(VirtualMethodClass);
                                }
                            }
                            break;
                        case Opcodes.INVOKESPECIAL:
                            if (isDefineClass) {
                                Class<?> superClassClass = superClassMap.get(insn.owner);
                                if (superClassClass != null && insn.name.equals("<init>")) {
                                    insn.owner = Type.getInternalName(superClassClass);
                                }
                            }
                            JavaScriptRemaper.init(insn, method);
                            break;
                    }
                }
            }
        }
    }
}
