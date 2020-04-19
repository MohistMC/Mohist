package red.mohist.bukkit.nms.proxy;

import java.lang.invoke.MethodType;

/**
 *
 * @author pyz
 * @date 2019/7/1 7:38 PM
 */
public class ProxyMethodType {

    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader loader) {
        String remapDesc = ProxyMethodHandles_Lookup.map.getOrDefault(descriptor, descriptor);
        return MethodType.fromMethodDescriptorString(remapDesc, loader);
    }
}
