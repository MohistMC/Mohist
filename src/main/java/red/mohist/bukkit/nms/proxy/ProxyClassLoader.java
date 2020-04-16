package red.mohist.bukkit.nms.proxy;

import net.md_5.specialsource.JarRemapper;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.MappingLoader;

/**
 *
 * @author pyz
 * @date 2019/7/1 8:09 PM
 */
public class ProxyClassLoader {

    public static Class<?> loadClass(ClassLoader inst, String className) throws ClassNotFoundException {
        if (ClassUtils.isNMClass(className)) {
            String InternalName = ClassUtils.getInternalName(className);
            String remapped = JarRemapper.mapTypeName(InternalName, MappingLoader.jarMapping.packages, MappingLoader.jarMapping.classes, InternalName);
            if (remapped.equals(InternalName) && InternalName.startsWith(ClassUtils.NMS_PREFIX) && !InternalName.contains(ClassUtils.NMS_VERSION)) {
                String[] splitStr = InternalName.split("/");
                className = ClassUtils.toClassName(JarRemapper.mapTypeName(ClassUtils.NMS_PREFIX + ClassUtils.NMS_VERSION + "/" + splitStr[splitStr.length - 1], MappingLoader.jarMapping.packages, MappingLoader.jarMapping.classes, InternalName));
            } else {
                className = ClassUtils.toClassName(remapped);
            }
        }
        return inst.loadClass(className);
    }
}
