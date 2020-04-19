package red.mohist.bukkit.nms.proxy;

import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.remappers.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/1 8:09 PM
 */
public class ProxyClassLoader {

    public static Class<?> loadClass(ClassLoader inst, String className) throws ClassNotFoundException {
        if (ClassUtils.isNMClass(className))
            className = RemapUtils.mapClass(ClassUtils.getInternalName(className)).replace('/', '.');
        return inst.loadClass(className);
    }
}
