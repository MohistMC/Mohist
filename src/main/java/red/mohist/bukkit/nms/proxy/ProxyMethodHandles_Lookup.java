package red.mohist.bukkit.nms.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import red.mohist.bukkit.nms.MappingLoader;
import red.mohist.bukkit.nms.remappers.RemapperProcessor;
import red.mohist.bukkit.nms.remappers.RemapUtils;

public class ProxyMethodHandles_Lookup {
    public static HashMap<String, String> map = new HashMap<>();

    static {
        try {
            ProxyMethodHandles_Lookup.loadMappings(new BufferedReader(new InputStreamReader(Objects.requireNonNull(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/nms.srg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MethodHandle findSpecial(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        }
        return lookup.findSpecial(refc, name, type, specialCaller);
    }

    public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType oldType) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, oldType.parameterArray());
        } else {
            Class<?> remappedClass = RemapperProcessor.VirtualMethodToStaticMapping.get((refc.getName().replace(".", "/") + ";" + name));
            if (remappedClass != null) {
                Class<?>[] newParArr = new Class<?>[oldType.parameterArray().length + 1];
                newParArr[0] = refc;
                System.arraycopy(oldType.parameterArray(), 0 , newParArr, 1, oldType.parameterArray().length);

                MethodType newType = MethodType.methodType(oldType.returnType(), newParArr);
                MethodHandle handle = lookup.findStatic(remappedClass, name, newType);

                return handle;
            }
        }
        return lookup.findVirtual(refc, name, oldType);
    }

    public static MethodHandle findStatic(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        } else {
            Class<?> remappedClass = RemapperProcessor.StaticMethodMapping.get((refc.getName().replace(".", "/") + ";" + name));
            if (remappedClass != null) {
                refc = remappedClass;
            }
        }
        return lookup.findStatic(refc, name, type);
    }

    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m) throws IllegalAccessException {
        Class<?> remappedClass = RemapperProcessor.VirtualMethodToStaticMapping.get((m.getDeclaringClass().getName().replace(".", "/") + ";" + m.getName()));
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


    public static void loadMappings(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            int commentIndex = line.indexOf('#');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }
            if (line.isEmpty() || !line.startsWith("MD: ")) {
                continue;
            }
            String[] sp = line.split("\\s+");
            String firDesc = sp[2];
            String secDesc = sp[4];
            map.put(firDesc, secDesc);
        }
    }

}
