package red.mohist.bukkit.nms.remappers;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.JointProvider;
import org.objectweb.asm.tree.TypeInsnNode;
import red.mohist.bukkit.nms.MappingLoader;
import red.mohist.bukkit.nms.proxy.ProxyClass;
import red.mohist.bukkit.nms.proxy.ProxyClassLoader;
import red.mohist.bukkit.nms.proxy.ProxyCustomClassLoder;
import red.mohist.bukkit.nms.proxy.ProxyCustomURLClassLoder;
import red.mohist.bukkit.nms.proxy.ProxyField;
import red.mohist.bukkit.nms.proxy.ProxyMethod;
import red.mohist.bukkit.nms.proxy.ProxyMethodHandles_Lookup;
import red.mohist.bukkit.nms.proxy.ProxyMethodType;
import red.mohist.bukkit.nms.proxy.ProxyYamlConfiguration;

public class RemapperProcessor {
    public static Map<String, Class> StaticMethodMapping = Maps.newHashMap();
    protected static Map<String, Class> VirtualMethodMapping = Maps.newHashMap();
    public static Map<String, Class> VirtualMethodToStaticMapping = Maps.newHashMap();
    protected static Map<String, Class> SuperClassMapping = Maps.newHashMap();

    public static JarMapping jarMapping;
    public static MohistJarRemapper remapper;

    public static final HashMap<String, String> classDeMapping = Maps.newHashMap();
    public static final Multimap<String, String> methodDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> fieldDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> methodFastMapping = ArrayListMultimap.create();

    static {
        StaticMethodMapping.put("java/lang/Class;forName", ProxyClass.class);
        StaticMethodMapping.put("java/lang/invoke/MethodType;fromMethodDescriptorString", ProxyMethodType.class);

        VirtualMethodToStaticMapping.put("java/lang/Class;getField", ProxyClass.class);
        VirtualMethodToStaticMapping.put("java/lang/Class;getDeclaredField", ProxyClass.class);
        VirtualMethodToStaticMapping.put("java/lang/Class;getMethod", ProxyClass.class);
        VirtualMethodToStaticMapping.put("java/lang/Class;getDeclaredMethod", ProxyClass.class);
        VirtualMethodToStaticMapping.put("java/lang/Class;getSimpleName", ProxyClass.class);
        VirtualMethodToStaticMapping.put("java/lang/Class;getDeclaredMethods", ProxyClass.class);
        VirtualMethodToStaticMapping.put("java/lang/reflect/Field;getName", ProxyField.class);
        VirtualMethodToStaticMapping.put("java/lang/reflect/Method;getName", ProxyMethod.class);
        VirtualMethodToStaticMapping.put("java/lang/ClassLoader;loadClass", ProxyClassLoader.class);
        VirtualMethodToStaticMapping.put("java/lang/invoke/MethodHandles$Lookup;findVirtual", ProxyMethodHandles_Lookup.class);
        VirtualMethodToStaticMapping.put("java/lang/invoke/MethodHandles$Lookup;findStatic", ProxyMethodHandles_Lookup.class);
        VirtualMethodToStaticMapping.put("java/lang/invoke/MethodHandles$Lookup;findSpecial", ProxyMethodHandles_Lookup.class);
        VirtualMethodToStaticMapping.put("java/lang/invoke/MethodHandles$Lookup;unreflect", ProxyMethodHandles_Lookup.class);
        VirtualMethodToStaticMapping.put("org/bukkit/configuration/file/YamlConfiguration;loadConfiguration", ProxyYamlConfiguration.class);

        SuperClassMapping.put("java/net/URLClassLoader", ProxyCustomURLClassLoder.class);
        SuperClassMapping.put("java/lang/ClassLoader", ProxyCustomClassLoder.class);
    }

    public static void init() {
        jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new MohistInheritanceProvider());
        jarMapping.setFallbackInheritanceProvider(provider);
        remapper = new MohistJarRemapper(jarMapping);

        jarMapping.classes.forEach((k, v) -> classDeMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> methodDeMapping.put(v, k));
        jarMapping.fields.forEach((k, v) -> fieldDeMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> methodFastMapping.put(k.split("\\s+")[0], k));

        try {
            Class.forName("red.mohist.bukkit.nms.proxy.ProxyMethodHandles_Lookup");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert code from using Class.X methods to our remapped versions
     */
    public static byte[] transform(byte[] code) {
        ClassReader reader = new ClassReader(code); // Turn from bytes into visitor
        ClassNode node = new ClassNode();
        reader.accept(node, 0); // Visit using ClassNode

        boolean remapCL = false;
        Class<?> remappedSuperClass = SuperClassMapping.get(node.superName);
        if (remappedSuperClass != null) {
            if (remappedSuperClass == ProxyCustomClassLoder.class) VirtualMethodMapping.put(node.name + ";defineClass", ProxyCustomClassLoder.class);
            node.superName = Type.getInternalName(remappedSuperClass);
            remapCL = true;
        }

        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW) { // remap new URLClassLoader
                    TypeInsnNode insn = (TypeInsnNode) next;
                    Class<?> remappedClass = SuperClassMapping.get(insn.desc);
                    if (remappedClass != null) {
                        insn.desc = Type.getInternalName(remappedClass);
                        remapCL = true;
                    }
                }

                if (next instanceof MethodInsnNode) {
                    MethodInsnNode insn = (MethodInsnNode) next;
                    int opcode = insn.getOpcode();
                    if (opcode == Opcodes.INVOKEVIRTUAL) {
                        remapVirtual(insn);
                    } else if (opcode == Opcodes.INVOKESTATIC) {
                        remapStatic(insn);
                    } else if (opcode == Opcodes.INVOKESPECIAL) {
                        if (remapCL) remapSuperClass(insn);
                    }
                    String sm = (insn.owner+ insn.desc + ";" + insn.name);
                    if (sm.equals("javax/script/ScriptEngineManager()V;<init>")) {
                        insn.desc = "(Ljava/lang/ClassLoader;)V";
                        method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;"));
                        method.maxStack++;
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0/* ClassWriter.COMPUTE_FRAMES */);
        node.accept(writer); // Convert back into bytes
        return writer.toByteArray();
    }

    public static void remapStatic(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        Class<?> remappedClass = StaticMethodMapping.get((method.owner + ";" + method.name));
        if (remappedClass != null) {
            method.owner = Type.getInternalName(remappedClass);
        }
    }

    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        Class<?> remappedClass = VirtualMethodToStaticMapping.get((method.owner + ";" + method.name));
        if (remappedClass != null) {
            Type returnType = Type.getReturnType(method.desc);
            ArrayList<Type> args = new ArrayList<>();
            args.add(Type.getObjectType(method.owner));
            args.addAll(Arrays.asList(Type.getArgumentTypes(method.desc)));

            method.setOpcode(Opcodes.INVOKESTATIC);
            method.owner = Type.getInternalName(remappedClass);
            method.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
        } else {
            remappedClass = VirtualMethodMapping.get((method.owner + ";" + method.name));
            if (remappedClass != null) {
                method.name += "Mohist";
                method.owner = Type.getInternalName(remappedClass);
            }
        }
    }

    private static void remapSuperClass(MethodInsnNode method) {
        Class<?> remappedClass = SuperClassMapping.get(method.owner);
        if (remappedClass != null && method.name.equals("<init>")) {
            method.owner = Type.getInternalName(remappedClass);
        }
    }
}
