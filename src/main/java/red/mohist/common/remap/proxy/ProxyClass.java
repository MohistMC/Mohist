package red.mohist.common.remap.proxy;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import red.mohist.common.remap.ASMUtils;
import red.mohist.common.remap.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/1 12:24 AM
 */
public class ProxyClass {

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, RemapUtils.getCallerClassLoder());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(ASMUtils.toClassName(RemapUtils.map(className.replace('.', '/'))), initialize, loader);
    }

    public static Method getDeclaredMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getDeclaredMethod, but class is null.methodname=" + name + ",parameters=" + Arrays.toString(parameterTypes));
        }
        return clazz.getDeclaredMethod(RemapUtils.mapMethodName(clazz, name, parameterTypes), parameterTypes);
    }

    public static Method getMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getMethod, but class is null.methodname=" + name + ",parameters=" + Arrays.toString(parameterTypes));
        }
        return clazz.getMethod(RemapUtils.mapMethodName(clazz, name, parameterTypes), parameterTypes);
    }

    public static Field getDeclaredField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getDeclaredField, but class is null.name=" + name);
        }
        return clazz.getDeclaredField(RemapUtils.mapFieldName(clazz, name));
    }

    public static Field getField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getField, but class is null.name=" + name);
        }
        return clazz.getField(RemapUtils.mapFieldName(clazz, name));
    }

    public static String getName(Class clazz) {
        Objects.requireNonNull(clazz);
        return RemapUtils.inverseMapName(clazz);
    }

    public static String getSimpleName(Class clazz) {
        Objects.requireNonNull(clazz);
        return RemapUtils.inverseMapSimpleName(clazz);
    }
}
