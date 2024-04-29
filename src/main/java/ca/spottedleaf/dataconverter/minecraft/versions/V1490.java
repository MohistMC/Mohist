package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import com.google.common.collect.ImmutableMap;

public final class V1490 {

    protected static final int VERSION = MCVersions.V18W20A + 1;

    public static void register() {
        ConverterAbstractBlockRename.register(VERSION, ImmutableMap.of(
                "minecraft:melon_block", "minecraft:melon"
        )::get);
        ConverterAbstractItemRename.register(VERSION, ImmutableMap.of(
                "minecraft:melon_block", "minecraft:melon",
                "minecraft:melon", "minecraft:melon_slice",
                "minecraft:speckled_melon", "minecraft:glistering_melon_slice"
        )::get);
    }

    private V1490() {}

}
