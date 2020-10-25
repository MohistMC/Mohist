package com.mohistmc.bukkit.nms.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import com.mohistmc.bukkit.nms.remappers.ReflectMethodRemapper;
import com.mohistmc.bukkit.nms.utils.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/1 7:45 PM
 */
public class ProxyMethodHandles_Lookup {

    public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> clazz, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (clazz.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethodName(clazz, name, type);
        } else {
            Class<?> aClass = ReflectMethodRemapper.getVirtualMethod().get((clazz.getName().replace(".", "/") + ";" + name));
            if (aClass != null) {
                Class<?>[] parameterArray = type.parameterArray();
                Class<?>[] newParameterArray = new Class<?>[parameterArray.length + 1];
                newParameterArray[0] = clazz;
                System.arraycopy(parameterArray, 0 , newParameterArray, 1, parameterArray.length);
                MethodType newType = MethodType.methodType(type.returnType(), newParameterArray);
                return lookup.findStatic(aClass, name, newType);
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

    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m) throws IllegalAccessException {
        Class<?> aClass = ReflectMethodRemapper.getVirtualMethod().get((m.getDeclaringClass().getName().replace(".", "/") + ";" + m.getName()));
        if (aClass != null) {
            try {
                Class<?>[] parameterTypes = m.getParameterTypes();
                Class<?>[] newParameterTypes = new Class<?>[parameterTypes.length + 1];
                newParameterTypes[0] = m.getDeclaringClass();
                System.arraycopy(parameterTypes, 0 , newParameterTypes, 1, parameterTypes.length);
                return lookup.unreflect(aClass.getMethod(m.getName(), newParameterTypes));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return lookup.unreflect(m);
    }

    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader classLoader) throws IllegalArgumentException, TypeNotPresentException {
        return MethodType.fromMethodDescriptorString(RemapUtils.remapMethodDesc(descriptor), classLoader);
    }

}
