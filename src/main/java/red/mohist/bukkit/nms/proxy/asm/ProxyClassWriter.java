package red.mohist.bukkit.nms.proxy.asm;

import red.mohist.bukkit.nms.utils.RemapUtils;

/**
 * @author pyz
 * @date 2019/7/15 8:52 PM
 */
public class ProxyClassWriter {

    public static byte[] remapClass(byte[] code) {
        return RemapUtils.remapFindClass(code);
    }
}
