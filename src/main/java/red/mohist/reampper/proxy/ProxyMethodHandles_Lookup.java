package red.mohist.reampper.proxy;

import red.mohist.reampper.utils.RemapUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
/**
 *
 * @author pyz
 * @date 2019/7/1 7:45 PM
 */
public class ProxyMethodHandles_Lookup {
    public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> clazz, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (clazz.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethodName(clazz, name, type);
        } else if (clazz == Class.class) {
            switch (name) {
                case "getField":
                case "getDeclaredField":
                case "getMethod":
                case "getDeclaredMethod":
                    type = MethodType.methodType(type.returnType(), new Class[]{Class.class, String.class});
                    clazz = ProxyClass.class;
                    break;
                default:
            }
        } else if (clazz == ClassLoader.class) {
            if (name.equals("loadClass")) {
                type = MethodType.methodType(type.returnType(), new Class[]{ClassLoader.class, String.class});
                clazz = ProxyClassLoader.class;
            }
        }
        return lookup.findVirtual(clazz, name, type);
    }

    public static MethodHandle findStatic(MethodHandles.Lookup lookup, Class<?> clazz, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (clazz.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethodName(clazz, name, type);
        } else if (clazz == Class.class && name.equals("forName")) {
            clazz = ProxyClass.class;
        }
        return lookup.findStatic(clazz, name, type);
    }

    public static MethodHandle findSpecial(MethodHandles.Lookup lookup, Class<?> clazz, String name, MethodType type, Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
        if (clazz.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethodName(clazz, name, type);
        }
        return lookup.findSpecial(clazz, name, type, specialCaller);
    }

    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m) throws IllegalAccessException, NoSuchMethodException {
        if (m.getDeclaringClass() == Class.class) {
            String name = m.getName();
            switch (name) {
                case "forName":
                    return lookup.unreflect(ProxyClass.class.getMethod(name, new Class[]{String.class}));
                case "getField":
                case "getDeclaredField": {
                    return lookup.unreflect(ProxyClass.class.getMethod(name, new Class[]{Class.class, String.class}));
                }
                case "getMethod":
                case "getDeclaredMethod":
                    return lookup.unreflect(ProxyClass.class.getMethod(name, new Class[]{Class.class, String.class, Class[].class}));
            }
        } else if (m.getDeclaringClass() == ClassLoader.class && m.getName().equals("loadClass")) {
            return lookup.unreflect(ClassLoader.class.getMethod(m.getName(), new Class[]{ClassLoader.class, String.class}));
        }
        return lookup.unreflect(m);
    }

}
