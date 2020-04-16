package red.mohist.bukkit.nms.proxy;

import java.lang.invoke.MethodType;
import red.mohist.bukkit.nms.cache.ClassMapping;

/**
 *
 * @author pyz
 * @date 2019/7/1 7:38 PM
 */
public class ProxyMethodType {
    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader loader) {
        String remapDesc = ClassMapping.map_MD.getOrDefault(descriptor, descriptor);
        return MethodType.fromMethodDescriptorString(remapDesc, loader);
    }
}
