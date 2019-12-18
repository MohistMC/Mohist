package red.mohist.common.remap.proxy;

import red.mohist.common.remap.ASMUtils;
import red.mohist.common.remap.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/1 8:09 PM
 */
public class ProxyClassLoader {
    public static Class<?> loadClass(final ClassLoader inst, String className) throws ClassNotFoundException {
        className = ASMUtils.toClassName(RemapUtils.map(ASMUtils.toInternalName(className)));
        return inst.loadClass(className);
    }
}
