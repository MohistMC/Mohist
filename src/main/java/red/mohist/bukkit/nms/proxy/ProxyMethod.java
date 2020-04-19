package red.mohist.bukkit.nms.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import red.mohist.bukkit.nms.remappers.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/6 10:09 AM
 */
public class ProxyMethod {

    private final static ConcurrentHashMap<Method, String> methodGetNameCache = new ConcurrentHashMap<>();

    public static String getName(Method method) {
        if (!RemapUtils.isClassNeedRemap(method.getDeclaringClass(), true)) return method.getName();
        String cache = methodGetNameCache.get(method);
        if (cache != null) return cache;
        String retn = RemapUtils.demapMethodName(method);
        methodGetNameCache.put(method, retn);
        return retn;
    }
}
