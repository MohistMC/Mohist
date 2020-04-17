package red.mohist.bukkit.nms.remappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import red.mohist.bukkit.nms.cache.ClassMapping;
import red.mohist.bukkit.nms.proxy.ProxyClass;
import red.mohist.bukkit.nms.proxy.ProxyCustomClassLoder;
import red.mohist.bukkit.nms.proxy.ProxyCustomURLClassLoder;
import red.mohist.bukkit.nms.proxy.ProxyMethodHandles_Lookup;

public class RemapperProcessor {  // This is kinda like RemapperProcessor from SpecialSource

    /**
     * Convert code from using Class.X methods to our remapped versions
     */
    public static byte[] transform(byte[] code) {
        ClassReader reader = new ClassReader(code); // Turn from bytes into visitor
        ClassNode node = new ClassNode();
        reader.accept(node, 0); // Visit using ClassNode

        boolean remapCL = false;
        switch (node.superName) {
            case "java/net/URLClassLoader":
                node.superName = Type.getInternalName(ProxyCustomURLClassLoder.class);
                remapCL = true;
                break;
            case "java/lang/ClassLoader":
                node.superName = Type.getInternalName(ProxyCustomClassLoder.class);
                ClassMapping.VirtualMethod.put(node.name + ";defineClass", ProxyCustomClassLoder.class);
                remapCL = true;
                break;
        }

        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW) { // remap new URLClassLoader
                    TypeInsnNode insn = (TypeInsnNode) next;
                    switch (insn.desc) {
                        case "java/net/URLClassLoader":
                            insn.desc = Type.getInternalName(ProxyCustomURLClassLoder.class);
                            remapCL = true;
                            break;
                        case "java/lang/ClassLoader":
                            insn.desc = Type.getInternalName(ProxyCustomClassLoder.class);
                            remapCL = true;
                            break;
                    }
                }

                if (next instanceof MethodInsnNode) {
                    MethodInsnNode insn = (MethodInsnNode) next;
                    if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        Class<?> remappedClass = ClassMapping.VirtualMethodToStatic.get((insn.owner + ";" + insn.name));
                        if (remappedClass != null) {
                            Type returnType = Type.getReturnType(insn.desc);
                            ArrayList<Type> args = new ArrayList<>();
                            args.add(Type.getObjectType(insn.owner));
                            args.addAll(Arrays.asList(Type.getArgumentTypes(insn.desc)));

                            insn.setOpcode(Opcodes.INVOKESTATIC);
                            insn.owner = Type.getInternalName(remappedClass);
                            insn.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
                        } else {
                            remappedClass = ClassMapping.VirtualMethod.get((insn.owner + ";" + insn.name));
                            if (remappedClass != null) {
                                insn.name += "Mohist";
                                insn.owner = Type.getInternalName(remappedClass);
                            }
                        }
                    } else if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
                        switch (insn.owner + ";" + insn.name) {
                            case "java/lang/Class;forName":
                                insn.owner = Type.getInternalName(ProxyClass.class);
                                break;
                            case "java/lang/invoke/MethodType;fromMethodDescriptorString":
                                insn.owner = Type.getInternalName(ProxyMethodHandles_Lookup.class);
                                break;
                        }
                    } else if (insn.getOpcode() == Opcodes.INVOKESPECIAL) {
                        if (remapCL) {
                            String s = insn.owner + ";" + insn.name;
                            switch (s) {
                                case "java/net/URLClassLoader;<init>":
                                    insn.owner = Type.getInternalName(ProxyCustomURLClassLoder.class);
                                    break;
                                case "java/lang/ClassLoader;<init>":
                                    insn.owner = Type.getInternalName(ProxyCustomClassLoder.class);
                                    break;
                            }
                        }
                    }

                    if (insn.owner.equals("javax/script/ScriptEngineManager") && insn.desc.equals("()V") && insn.name.equals("<init>")) {
                        insn.desc = "(Ljava/lang/ClassLoader;)V";
                        method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;", false));
                        method.maxStack++;
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0/* ClassWriter.COMPUTE_FRAMES */);
        node.accept(writer); // Convert back into bytes
        return writer.toByteArray();
    }
}
