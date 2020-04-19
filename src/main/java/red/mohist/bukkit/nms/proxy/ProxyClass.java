package red.mohist.bukkit.nms.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.remappers.RemapUtils;
import red.mohist.bukkit.nms.remappers.ReflectionUtils;
import red.mohist.bukkit.nms.remappers.RemapperProcessor;

/**
 * @author pyz
 * @date 2019/7/1 12:24 AM
 */
public class ProxyClass {

    private final static ConcurrentHashMap<Class<?>, String> simpleNameGetNameCache = new ConcurrentHashMap<>();

    // Class.forName
    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, ReflectionUtils.getCallerClassloader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (className.startsWith(ClassUtils.NMS_PREFIX2))
            className = RemapperProcessor.jarMapping.classes.getOrDefault(className.replace('.', '/'), className).replace('/', '.');
        return Class.forName(className, initialize, classLoader);
    }

    // Get Fields
    public static Field getField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true))
            name = RemapUtils.mapFieldName(inst, name);
        return inst.getField(name);
    }

    public static Field getDeclaredField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, false))
            name = RemapperProcessor.remapper.mapFieldName(RemapUtils.reverseMap(inst), name, null);
        return inst.getDeclaredField(name);
    }

    // Get Methods
    public static Method getMethod(Class<?> inst, String name, Class<?>...parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true))
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        try {
            return inst.getMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }

    }

    public static Method getDeclaredMethod(Class<?> inst, String name, Class<?>...parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true))
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        try {
            return inst.getDeclaredMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

    // getSimpleName
    public static String getSimpleName(Class<?> inst) {
        if (!RemapUtils.isClassNeedRemap(inst, false)) return inst.getSimpleName();
        String cache = simpleNameGetNameCache.get(inst);
        if (cache != null) return cache;
        String[] name = RemapUtils.reverseMapExternal(inst).split("\\.");
        String retn = name[name.length - 1];
        simpleNameGetNameCache.put(inst, retn);
        return retn;
    }

    // getDeclaredMethods
    public static Method[] getDeclaredMethods(Class<?> inst) {
        try {
            return inst.getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            return new Method[]{};
        }
    }
}
