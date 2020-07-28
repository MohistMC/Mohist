package red.mohist.reampper.remappers;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;
import red.mohist.reampper.model.MethodRedirectRule;
import red.mohist.reampper.proxy.*;
import red.mohist.reampper.proxy.asm.ProxyClassWriter;
import red.mohist.reampper.utils.ASMUtils;

/**
 *
 * @author pyz
 * @date 2019/7/2 8:51 PM
 */
public class ReflectMethodRemapper extends MethodRemapper {
    private static Map<String, Map<String, Map<String, MethodRedirectRule>>> methodRedirectMapping = new HashMap<>();

    static {
        registerMethodRemapper("java/lang/Class", "forName", Class.class, new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "forName", Class.class, new Class[]{String.class, Boolean.TYPE, ClassLoader.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getField", Field.class, new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getDeclaredField", Field.class, new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getMethod", Method.class, new Class[]{String.class, Class[].class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getDeclaredMethod", Method.class, new Class[]{String.class, Class[].class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getName", String.class, new Class[]{}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getSimpleName", String.class, new Class[]{}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getDeclaredMethods", Method.class, new Class[]{}, ProxyClass.class);
        registerMethodRemapper("java/lang/reflect/Method", "getName", String.class, new Class[]{}, ProxyMethod.class);
        registerMethodRemapper("java/lang/reflect/Field", "getName", String.class, new Class[]{}, ProxyField.class);
        registerMethodRemapper("java/lang/invoke/MethodType", "fromMethodDescriptorString", MethodType.class, new Class[]{String.class, ClassLoader.class}, ProxyMethodType.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "unreflect", MethodHandle.class, new Class[]{String.class, ClassLoader.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "findSpecial", MethodHandle.class, new Class[]{Class.class, String.class, MethodType.class, Class.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "findStatic", MethodHandle.class, new Class[]{Class.class, String.class, MethodType.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "findVirtual", MethodHandle.class, new Class[]{Class.class, String.class, MethodType.class}, ProxyMethodHandles_Lookup.class);

        registerMethodRemapper("java/lang/ClassLoader", "loadClass", Class.class, new Class[]{String.class}, ProxyClassLoader.class);
        registerMethodRemapper("java/net/URLClassLoader", "loadClass", Class.class, new Class[]{String.class}, ProxyClassLoader.class);
        registerMethodRemapper("java/net/URLClassLoader", "<init>", void.class, new Class[]{URL[].class, ClassLoader.class, URLStreamHandlerFactory.class}, DelegateURLClassLoder.class);
        registerMethodRemapper("java/net/URLClassLoader", "<init>", void.class, new Class[]{URL[].class, ClassLoader.class}, DelegateURLClassLoder.class);
        registerMethodRemapper("java/net/URLClassLoader", "<init>", void.class, new Class[]{URL[].class}, DelegateURLClassLoder.class);

        registerMethodRemapper("org/bukkit/configuration/file/YamlConfiguration", "loadConfiguration", YamlConfiguration.class, new Class[]{InputStream.class}, ProxyYamlConfiguration.class);
    }

    public ReflectMethodRemapper(MethodVisitor mv, Remapper remapper) {
        super(mv, remapper);
    }

    public ReflectMethodRemapper(int api, MethodVisitor mv, Remapper remapper) {
        super(api, mv, remapper);
    }

    private static void registerMethodRemapper(String owner, String name, Class<?> returnType, Class<?>[] args, Class<?> remapOwner) {
        Map<String, Map<String, MethodRedirectRule>> byName = methodRedirectMapping.computeIfAbsent(owner, k -> new HashMap<>());
        Map<String, MethodRedirectRule> byDesc = byName.computeIfAbsent(name, k -> new HashMap<>());
        String methodDescriptor = ASMUtils.toMethodDescriptor(returnType, args);
        byDesc.put(methodDescriptor, new MethodRedirectRule(owner, name, methodDescriptor, remapOwner.getName().replace('.', '/')));
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (opcode == Opcodes.NEW && "java/net/URLClassLoader".equals(type)) {
            type = DelegateURLClassLoder.desc;
        }
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (Opcodes.INVOKEVIRTUAL == opcode) {
            redirectVirtual(opcode, owner, name, desc, itf);
        } else if (Opcodes.INVOKESTATIC == opcode) {
            redirectStatic(opcode, owner, name, desc, itf);
        } else if (Opcodes.INVOKESPECIAL == opcode) {
            redirectSpecial(opcode, owner, name, desc, itf);
        } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    private MethodRedirectRule findRule(int opcode, String owner, String name, String desc, boolean itf) {
        Map<String, Map<String, MethodRedirectRule>> byOwner = methodRedirectMapping.get(owner);
        if (byOwner == null) {
            return null;
        }
        Map<String, MethodRedirectRule> byName = byOwner.get(name);
        if (byName == null) {
            return null;
        }
        MethodRedirectRule rule = byName.get(desc);
        return rule;
    }

    private void redirectSpecial(int opcode, String owner, String name, String desc, boolean itf) {
        MethodRedirectRule rule = findRule(opcode, owner, name, desc, itf);
        if (rule != null) {
            owner = rule.getRemapOwner();
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    private void redirectVirtual(int opcode, String owner, String name, String desc, boolean itf) {
        if (desc.equals("()[B")) {
            if (name.equals("toByteArray")) {
                if (owner.equals("com/comphenix/net/sf/cglib/asm/$ClassWriter")) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, ProxyClassWriter.class.getName().replace('.', '/'), "remapClass", "([B)[B", false);
                    return;
                }
            }
        }
        MethodRedirectRule rule = findRule(opcode, owner, name, desc, itf);
        if (rule != null) {
            opcode = Opcodes.INVOKESTATIC;
            Type r = Type.getReturnType(desc);
            Type[] args = Type.getArgumentTypes(desc);
            Type[] newArgs = new Type[args.length + 1];
            if ("red/mohist/remapper/proxy/ProxyClassLoader".equals(rule.getRemapOwner()) && "loadClass".equals(name)) {
                newArgs[0] = Type.getObjectType("java/lang/ClassLoader");
            } else {
                newArgs[0] = Type.getObjectType(owner);
            }
            owner = rule.getRemapOwner();
            System.arraycopy(args, 0, newArgs, 1, args.length);
            desc = Type.getMethodDescriptor(r, newArgs);
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    private void redirectStatic(int opcode, String owner, String name, String desc, boolean itf) {
        MethodRedirectRule rule = findRule(opcode, owner, name, desc, itf);
        if (rule != null) {
            owner = rule.getRemapOwner();
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}