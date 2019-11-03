package red.mohist.common.remap.proxy.asm;

import java.io.IOException;
import red.mohist.common.remap.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/15 8:52 PM
 */
public class ProxyClassWriter {

    public static byte[] remapClass(byte[] code) {
        try {
            return RemapUtils.remapFindClass(null, null, code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
