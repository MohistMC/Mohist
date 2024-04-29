package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V1487 {

    protected static final int VERSION = MCVersions.V18W19B + 2;

    public static void register() {
        final Map<String, String> remap = ImmutableMap.of(
                "minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab",
                "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"
        );

        ConverterAbstractItemRename.register(VERSION, remap::get);
        ConverterAbstractBlockRename.register(VERSION, remap::get);
    }

    private V1487() {}

}
