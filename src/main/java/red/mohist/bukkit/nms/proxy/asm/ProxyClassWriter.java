package red.mohist.bukkit.nms.proxy.asm;

import java.io.IOException;
import red.mohist.bukkit.nms.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/15 8:52 PM
 */
public class ProxyClassWriter {

    public static byte[] remapClass(byte[] code) {
        try {
            return RemapUtils.remapFindClass(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
