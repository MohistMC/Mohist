package com.mohistmc.util;

import net.minecraft.util.datafix.codec.DatapackCodec;

public class DatapackCodecUtils {

    private static transient DatapackCodec datapackCodec;

    public static void putDatapackConfig(DatapackCodec codec) {
        datapackCodec = codec;
    }

    public static DatapackCodec getDatapackCodec() {
        try {
            return datapackCodec;
        } finally {
            datapackCodec = null;
        }
    }
}
