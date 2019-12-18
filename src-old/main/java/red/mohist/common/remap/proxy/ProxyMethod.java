package red.mohist.common.remap.proxy;

import java.lang.reflect.Method;
import red.mohist.common.remap.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/6 10:09 AM
 */
public class ProxyMethod {
    public static String getName(Method method) {
        return RemapUtils.inverseMapMethodName(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
    }
}
