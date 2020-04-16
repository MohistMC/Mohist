package red.mohist.bukkit.nms.remappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import org.objectweb.asm.Type;
import red.mohist.bukkit.nms.MappingLoader;
import red.mohist.bukkit.nms.cache.ClassMapping;
import red.mohist.bukkit.nms.remappers.MohistInheritanceProvider;
import red.mohist.bukkit.nms.remappers.MohistJarRemapper;

public class RemapUtils {

    // Classes
    public static String reverseMapExternal(Class<?> name) {
        return reverseMap(Type.getInternalName(name)).replace('$', '.').replace('/', '.');
    }

    public static String reverseMap(String check) {
        return ClassMapping.classDeMapping.getOrDefault(check, check);
    }

    public static String remappedClass(JarMapping jarMapping, String className){
        return jarMapping.classes.get(className.replaceAll("\\.", "\\/"));
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
        String match = reverseMap(Type.getInternalName(inst)) + "/" + name;

        Collection<String> colls = ClassMapping.methodFastMapping.get(match);
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
                return MappingLoader.jarMapping.methods.get(value);
        }

        // Search superclass
        ArrayList<Class<?>> parents = new ArrayList<>();
        parents.add(inst.getSuperclass());
        for (Class<?> superClass : parents) {
            if (superClass == null) continue;
            return mapMethodInternal(superClass, name, parameterTypes);
        }

        // Search interfaces
        ArrayList<Class<?>> interfaces = new ArrayList<>();
        interfaces.addAll(Arrays.asList(inst.getInterfaces()));
        for (Class<?> interfaceClass : interfaces) {
            if (interfaceClass == null) continue;
            return mapMethodInternal(interfaceClass, name, parameterTypes);
        }

        return null;
    }

    public static String mapFieldName(Class<?> inst, String name) {
        String key = reverseMap(Type.getInternalName(inst)) + "/" + name;
        String mapped = MappingLoader.jarMapping.fields.get(key);
        if (mapped == null) {
            Class<?> superClass = inst.getSuperclass();
            if (superClass != null) {
                mapped = mapFieldName(superClass, name);
            }
        }
        return mapped != null ? mapped : name;
    }

    public static JarMapping loadJarMapping(ClassLoader classLoader, boolean isClassLoader){
        JarMapping jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new MohistInheritanceProvider());
        if (isClassLoader) provider.add(new ClassLoaderProvider(classLoader));
        jarMapping.setFallbackInheritanceProvider(provider);
        return jarMapping;
    }

    public static MohistJarRemapper loadRemapper(ClassLoader classLoader, boolean isClassLoader){
        return new MohistJarRemapper(loadJarMapping(classLoader, isClassLoader));
    }
}
