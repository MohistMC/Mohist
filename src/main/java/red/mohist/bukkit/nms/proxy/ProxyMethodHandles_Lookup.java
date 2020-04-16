package red.mohist.bukkit.nms.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import org.objectweb.asm.Type;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.remappers.RemapUtils;
import red.mohist.bukkit.nms.cache.ClassMapping;

/**
 *
 * @author pyz
 * @date 2019/7/1 7:45 PM
 */
public class ProxyMethodHandles_Lookup {

    public static MethodHandle findSpecial(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
        if (ClassUtils.isNMClass(refc)) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        }
        return lookup.findSpecial(refc, name, type, specialCaller);
    }

    public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType oldType) throws NoSuchMethodException, IllegalAccessException {
        if (ClassUtils.isNMClass(refc)) {
            name = RemapUtils.mapMethod(refc, name, oldType.parameterArray());
        } else {
            Class<?> remappedClass = ClassMapping.VirtualMethodToStatic.get((Type.getInternalName(refc) + ";" + name));
            if (remappedClass != null) {
                Class<?>[] newParArr = new Class<?>[oldType.parameterArray().length + 1];
                newParArr[0] = refc;
                System.arraycopy(oldType.parameterArray(), 0 , newParArr, 1, oldType.parameterArray().length);

                MethodType newType = MethodType.methodType(oldType.returnType(), newParArr);

                return lookup.findStatic(remappedClass, name, newType);
            }
        }
        return lookup.findVirtual(refc, name, oldType);
    }

    public static MethodHandle findStatic(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (ClassUtils.isNMClass(refc)) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        } else {
            String s = Type.getInternalName(refc) + ";" + name;
            switch (s) {
                case "java/lang/Class;forName":
                    refc = ProxyClass.class;
                    break;
                case "java/lang/invoke/MethodType;fromMethodDescriptorString":
                    refc = ProxyMethodHandles_Lookup.class;
                    break;
            }
        }
        return lookup.findStatic(refc, name, type);
    }

    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m) throws IllegalAccessException {
        Class<?> remappedClass = ClassMapping.VirtualMethodToStatic.get((m.getDeclaringClass().getName().replace(".", "/") + ";" + m.getName()));
        if (remappedClass != null) {
            try {
                return lookup.unreflect(getClassReflectionMethod(lookup, remappedClass, m));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return lookup.unreflect(m);
    }

    private static Method getClassReflectionMethod(MethodHandles.Lookup lookup, Class<?> remappedClass, Method originalMethod) throws NoSuchMethodException {
        Class<?>[] oldParArr = originalMethod.getParameterTypes();
        Class<?>[] newParArr = new Class<?>[oldParArr.length + 1];
        newParArr[0] = originalMethod.getDeclaringClass();
        System.arraycopy(oldParArr, 0 , newParArr, 1, oldParArr.length);

        return remappedClass.getMethod(originalMethod.getName(), newParArr);
    }

}
