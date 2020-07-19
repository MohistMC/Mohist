package red.mohist.bukkit.nms.proxy;

import red.mohist.bukkit.nms.utils.RemapUtils;

import java.lang.reflect.Method;

/**
 * @author pyz
 * @date 2019/7/6 10:09 AM
 */
public class ProxyMethod {
    public static String getName(Method method) {
        return RemapUtils.inverseMapMethodName(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
    }
}
