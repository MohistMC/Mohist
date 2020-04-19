package red.mohist.bukkit.nms.remappers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.md_5.specialsource.JarRemapper;
import org.objectweb.asm.Type;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.remappers.RemapperProcessor;

public class RemapUtils {

    private static Map<String, Boolean> classNeedRemap = new ConcurrentHashMap<>();

    // Classes
    public static String reverseMapExternal(Class<?> name) {
        return reverseMap(name).replace('$', '.').replace('/', '.');
    }

    public static String reverseMap(Class<?> name) {
        return reverseMap(Type.getInternalName(name));
    }

    public static String reverseMap(String check) {
        return RemapperProcessor.classDeMapping.getOrDefault(check, check);
    }

    // Methods
    public static String mapMethod(Class<?> inst, String name, Class<?>... parameterTypes) {
        String result = mapMethodInternal(inst, name, parameterTypes);
        if (result != null) {
            return result;
        }
        return name;
    }

    /**
     * Recursive method for finding a method from superclasses/interfaces
     */
    public static String mapMethodInternal(Class<?> inst, String name, Class<?>... parameterTypes) {
        String match = reverseMap(inst) + "/" + name;

        Collection<String> colls = RemapperProcessor.methodFastMapping.get(match);
        for (String value : colls) {
            String[] str = value.split("\\s+");
            int i = 0;
            for (Type type : Type.getArgumentTypes(str[1])) {
                String typename = (type.getSort() == Type.ARRAY ? type.getInternalName() : type.getClassName());
                if (i >= parameterTypes.length || !typename.equals(reverseMapExternal(parameterTypes[i]))) {
                    i=-1;
                    break;
                }
                i++;
            }

            if (i >= parameterTypes.length)
                return RemapperProcessor.jarMapping.methods.get(value);
        }

        // Search superclass
        Class<?> superClass = inst.getSuperclass();
        if (superClass != null) {
            String superMethodName = mapMethodInternal(superClass, name, parameterTypes);
            if (superMethodName != null) return superMethodName;
        }

        // Search interfaces
        for (Class<?> interfaceClass : inst.getInterfaces()) {
            String superMethodName = mapMethodInternal(interfaceClass, name, parameterTypes);
            if (superMethodName != null) return superMethodName;
        }

        return null;
    }

    public static String mapFieldName(Class<?> inst, String name) {
        String key = reverseMap(inst) + "/" + name;
        String mapped = RemapperProcessor.jarMapping.fields.get(key);
        if (mapped == null) {
            Class<?> superClass = inst.getSuperclass();
            if (superClass != null) {
                mapped = mapFieldName(superClass, name);
            }
        }
        return mapped != null ? mapped : name;
    }

    public static String mapClass(String className) {
        String remapped = JarRemapper.mapTypeName(className, RemapperProcessor.jarMapping.packages, RemapperProcessor.jarMapping.classes, className);
        if (remapped.equals(className) && className.startsWith(ClassUtils.NMS_PREFIX) && !className.contains(ClassUtils.NMS_VERSION)) {
            String[] cn = className.split("/");
            return JarRemapper.mapTypeName(ClassUtils.NMS_PREFIX3 + cn[cn.length - 1], RemapperProcessor.jarMapping.packages, RemapperProcessor.jarMapping.classes, className);
        }
        return remapped;
    }

    public static String demapFieldName(Field field) {
        String name = field.getName();
        String match = reverseMap(field.getDeclaringClass()) + "/";

        Collection<String> colls = RemapperProcessor.fieldDeMapping.get(name);

        for (String value : colls) {
            if (value.startsWith(match)) {
                String[] matched = value.split("\\/");
                String rtr =  matched[matched.length - 1];
                return rtr;
            }
        }

        return name;
    }

    public static String demapMethodName(Method method) {
        String name = method.getName();
        String match = reverseMap(method.getDeclaringClass()) + "/";

        Collection<String> colls = RemapperProcessor.methodDeMapping.get(name);

        for (String value : colls) {
            if (value.startsWith(match)) {
                String[] matched = value.split("\\s+")[0].split("\\/");
                String rtr =  matched[matched.length - 1];
                return rtr;
            }
        }

        return name;
    }

    public static boolean isClassNeedRemap(Class<?> clazz, boolean checkSuperClass) {
        final String className = clazz.getName();
        Boolean cache = classNeedRemap.get(className);
        if (cache != null) return cache;

        while (clazz != null && clazz.getClassLoader() != null) {
            if (ClassUtils.isNMClass(clazz)) {
                classNeedRemap.put(className, true);
                return true;
            }
            if (checkSuperClass) {
                for (Class<?> interfaceClass : clazz.getInterfaces()) {
                    if (isClassNeedRemap(interfaceClass, true))
                        return true;
                }
                clazz = clazz.getSuperclass();
            } else {
                return false;
            }
        }
        classNeedRemap.put(className, false);
        return false;
    }
}
