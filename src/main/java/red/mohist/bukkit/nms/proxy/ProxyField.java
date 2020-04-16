package red.mohist.bukkit.nms.proxy;

import java.lang.reflect.Field;
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
public class ProxyField {
    public static String getName(Field field) {
        if (!ClassUtils.isClassNeedRemap(field.getDeclaringClass(), false)) return field.getName();
        String name = field.getName();
        String match = RemapUtils.reverseMap(Type.getInternalName(field.getDeclaringClass())) + "/";

        Collection<String> colls = ClassMapping.fieldDeMapping.get(name);

        for (String value : colls) {
            if (value.startsWith(match)) {
                String[] matched = value.split("\\/");
                return matched[matched.length - 1];
            }
        }

        return name;
    }
}
