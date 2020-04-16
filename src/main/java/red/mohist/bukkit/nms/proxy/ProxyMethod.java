package red.mohist.bukkit.nms.proxy;

import java.lang.reflect.Method;
import java.util.Collection;
import org.objectweb.asm.Type;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.remappers.RemapUtils;
import red.mohist.bukkit.nms.cache.ClassMapping;

/**
 *
 * @author pyz
 * @date 2019/7/6 10:09 AM
 */
public class ProxyMethod {

    public static String getName(Method method) {
        if (!ClassUtils.isClassNeedRemap(method.getDeclaringClass(), true)) return method.getName();
        String name = method.getName();
        String match = RemapUtils.reverseMap(Type.getInternalName(method.getDeclaringClass())) + "/";

        Collection<String> colls = ClassMapping.methodDeMapping.get(name);

        for (String value : colls) {
            if (value.startsWith(match)) {
                String[] matched = value.split("\\s+")[0].split("\\/");
                return matched[matched.length - 1];
            }
        }

        return name;
    }
}
