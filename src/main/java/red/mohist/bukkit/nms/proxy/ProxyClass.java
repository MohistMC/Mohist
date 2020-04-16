package red.mohist.bukkit.nms.proxy;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.objectweb.asm.Type;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.MappingLoader;
import red.mohist.bukkit.nms.remappers.ReflectionUtils;
import red.mohist.bukkit.nms.remappers.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/1 12:24 AM
 */
public class ProxyClass {

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, ReflectionUtils.getCallerClassloader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (ClassUtils.isNMSClass(className))
            className = ClassUtils.toClassName(MappingLoader.jarMapping.classes.getOrDefault(ClassUtils.getInternalName(className), className));
        return Class.forName(className, initialize, classLoader);
    }

    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (ClassUtils.isClassNeedRemap(clazz, true))
            name = RemapUtils.mapMethod(clazz, name, parameterTypes);
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (ClassUtils.isClassNeedRemap(clazz, true))
            name = RemapUtils.mapMethod(clazz, name, parameterTypes);
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

    public static Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
        if (ClassUtils.isClassNeedRemap(clazz, false))
            name = MappingLoader.remapper.mapFieldName(RemapUtils.reverseMap(Type.getInternalName(clazz)), name, null);
        return clazz.getDeclaredField(name);
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
        if (ClassUtils.isClassNeedRemap(clazz, true))
            name = RemapUtils.mapFieldName(clazz, name);
        return clazz.getField(name);
    }

    public static String getSimpleName(Class<?> clazz) {
        if (!ClassUtils.isClassNeedRemap(clazz, false)) return clazz.getSimpleName();
        String[] name = RemapUtils.reverseMapExternal(clazz).split("\\.");
        return name[name.length - 1];
    }

    public static Method[] getDeclaredMethods(Class<?> inst) {
        try {
            return inst.getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            return new Method[]{};
        }
    }
}
