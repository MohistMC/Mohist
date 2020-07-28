package red.mohist.reampper.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 *
 * @author pyz
 * @date 2019/7/2 8:16 PM
 */
public class ASMUtils {

    private static final Map<Integer, String> opcodeMap = new HashMap<>();
    private static final Map<Integer, String> typeMap = new HashMap<>();
    private static final Map<Integer, BiConsumer<String, AbstractInsnNode>> printerMap = new HashMap<>();

    static {
        for (Field field : Opcodes.class.getDeclaredFields()) {
            if (field.getType() == int.class && Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    opcodeMap.put((Integer) field.get(null), field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Field field : AbstractInsnNode.class.getDeclaredFields()) {
            if (field.getType() == int.class && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    typeMap.put((Integer) field.get(null), field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        printerMap.put(AbstractInsnNode.FIELD_INSN, (prefix, item) -> {
            FieldInsnNode node = (FieldInsnNode) item;
            System.out.println(prefix + " " + node.owner + " " + node.name + " " + node.desc);
        });
        printerMap.put(AbstractInsnNode.VAR_INSN, (prefix, item) -> {
            VarInsnNode node = (VarInsnNode) item;
            System.out.println(prefix + " " + node.var);
        });
        printerMap.put(AbstractInsnNode.TYPE_INSN, (prefix, item) -> {
            TypeInsnNode node = (TypeInsnNode) item;
            System.out.println(prefix + " " + node.desc);
        });

        printerMap.put(AbstractInsnNode.METHOD_INSN, (prefix, item) -> {
            MethodInsnNode node = (MethodInsnNode) item;
            System.out.println(prefix + " " + node.owner + " " + node.name + " " + node.desc);
        });
        printerMap.put(AbstractInsnNode.INVOKE_DYNAMIC_INSN, (prefix, item) -> {
            InvokeDynamicInsnNode node = (InvokeDynamicInsnNode) item;
            System.out.println(prefix + " " + node.name + " " + node.desc);
            if (node.bsm != null) {
                print(prefix, node.bsm);
            }
            if (node.bsmArgs != null) {
                for (Object bsmArg : node.bsmArgs) {
                    if (bsmArg instanceof Type) {
                        print(prefix, (Type) bsmArg);
                    } else if (bsmArg instanceof Handle) {
                        print(prefix, (Handle) bsmArg);
                    } else if (bsmArg instanceof String) {
                        System.out.println(prefix + " String " + bsmArg);
                    } else {
                        System.out.println(prefix + " " + bsmArg.getClass().getSimpleName() + " " + bsmArg);
                    }
                }
            }
        });
    }

    public static String toDescriptorV1(String className) {
        if (className.startsWith("[")) {
            return className.replace('.', '/');
        }
        switch (className) {
            case "byte":
                return "B";
            case "short":
                return "S";
            case "int":
                return "I";
            case "long":
                return "J";
            case "float":
                return "F";
            case "double":
                return "D";
            case "boolean":
                return "Z";
            case "char":
                return "C";
            case "void":
                return "V";
            default:
                return "L" + className.replace('.', '/') + ";";
        }
    }

    public static String toDescriptorV2(String internalName) {
        if (internalName.startsWith("[")) {
            return internalName;
        }
        if (internalName.length() == 1) {
            return internalName;
        }
        return "L" + internalName.replace('.', '/') + ";";
    }

    public static String toClassName(String internalName) {
        return internalName.replace('/', '.');
    }

    public static Type toType(Class<?> clazz) {
        return Type.getType(clazz);
    }

    public static String toDescriptor(Class<?> clazz) {
        return Type.getDescriptor(clazz);
    }

    public static String toInternalName(Class<?> clazz) {
        return Type.getInternalName(clazz);
    }

    public static String toInternalName(String className) {
        return className.replace('.', '/');
    }

    public static String toArgumentDescriptor(Class<?>... classes) {
        StringJoiner sj = new StringJoiner("", "(", ")");
        for (Class<?> aClass : classes) {
            sj.add(Type.getDescriptor(aClass));
        }
        return sj.toString();
    }

    public static String toArgumentDescriptor(String methodDescriptor) {
        return methodDescriptor.substring(0, methodDescriptor.lastIndexOf(')'));
    }

    public static String toMethodDescriptor(Class<?> returnType, Class<?>... classes) {
        StringJoiner sj = new StringJoiner("", "(", ")");
        for (Class<?> aClass : classes) {
            sj.add(toDescriptor(aClass));
        }
        return sj.toString() + toDescriptor(returnType);
    }

    public static void dump(Path dir, byte[] bs) throws IOException {
        ClassReader classReader = new ClassReader(bs);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        File file = dir.resolve(classNode.name + ".class").toFile();
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        Files.write(file.toPath(), bs);
    }

    public static String getOpcodeName(int opcode) {
        return opcodeMap.get(opcode);
    }

    public static String getTypeName(int type) {
        return opcodeMap.get(type);
    }

    public static String getInternalName(Type type) {
        if (type.getSort() <= 8) {
            return type.getDescriptor();
        }
        return type.getInternalName();
    }

    private static void print(String prefix, Handle o) {
        System.out.println(prefix + " Handle " + o.getOwner() + " " + o.getName() + " " + o.getDesc());
    }

    private static void print(String prefix, Type o) {
        System.out.println(prefix + " Type " + o.getDescriptor());
    }

    private static void print(String prefix, AbstractInsnNode insn) {
        prefix = prefix + " " + insn.getClass().getSimpleName() + " " + getTypeName(insn.getType()) + " " + getOpcodeName(insn.getOpcode());
        BiConsumer<String, AbstractInsnNode> printer = printerMap.get(insn.getType());
        if (printer != null) {
            printer.accept(prefix, insn);
        } else {
            System.out.println(prefix);
        }
    }
}
