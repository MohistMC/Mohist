package com.mohistmc.mohist.libraries;

import com.mohistmc.tools.MD5Util;
import java.io.File;

/**
 * @author Mgazul by MohistMC
 * @date 2023/12/7 9:36:14
 */
public record Libraries(String path, String md5, long size, boolean installer) {

    public static Libraries from(String line) {
        String[] parts = line.split("\\|");
        return new Libraries(parts[0], parts[1], Long.parseLong(parts[2]), Boolean.parseBoolean(parts[3]));
    }

    public static Libraries from(File file) {
        return new Libraries(file.getAbsolutePath(), MD5Util.get(file), file.length(), false);
    }
}
