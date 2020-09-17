package com.mohistmc.bukkit.nms.proxy.asm;

import com.mohistmc.bukkit.nms.utils.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/15 8:52 PM
 */
public class ProxyClassWriter {

    public static byte[] remapClass(byte[] code) {
        return RemapUtils.remapFindClass(code);
    }
}
