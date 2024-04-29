package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V2508 {

    protected static final int VERSION = MCVersions.V20W08A + 1;

    private V2508() {}

    public static void register() {
        final Map<String, String> remap = ImmutableMap.of(
                "minecraft:warped_fungi", "minecraft:warped_fungus",
                "minecraft:crimson_fungi", "minecraft:crimson_fungus"
        );

        ConverterAbstractBlockRename.register(VERSION, remap::get);
        ConverterAbstractItemRename.register(VERSION, remap::get);
    }
}
