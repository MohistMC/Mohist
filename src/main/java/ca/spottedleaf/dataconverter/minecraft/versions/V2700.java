package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import com.google.common.collect.ImmutableMap;

public final class V2700 {

    protected static final int VERSION = MCVersions.V21W10A + 1;

    public static void register() {
        ConverterAbstractBlockRename.registerAndFixJigsaw(VERSION, ImmutableMap.of(
                "minecraft:cave_vines_head", "minecraft:cave_vines",
                "minecraft:cave_vines_body", "minecraft:cave_vines_plant"
        )::get);
    }
}
