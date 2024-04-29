package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import com.google.common.collect.ImmutableMap;

public final class V2696 {

    protected static final int VERSION = MCVersions.V21W07A + 1;

    protected static final ImmutableMap<String, String> RENAMES = ImmutableMap.<String, String>builder()
            .put("minecraft:grimstone", "minecraft:deepslate")
            .put("minecraft:grimstone_slab", "minecraft:cobbled_deepslate_slab")
            .put("minecraft:grimstone_stairs", "minecraft:cobbled_deepslate_stairs")
            .put("minecraft:grimstone_wall", "minecraft:cobbled_deepslate_wall")
            .put("minecraft:polished_grimstone", "minecraft:polished_deepslate")
            .put("minecraft:polished_grimstone_slab", "minecraft:polished_deepslate_slab")
            .put("minecraft:polished_grimstone_stairs", "minecraft:polished_deepslate_stairs")
            .put("minecraft:polished_grimstone_wall", "minecraft:polished_deepslate_wall")
            .put("minecraft:grimstone_tiles", "minecraft:deepslate_tiles")
            .put("minecraft:grimstone_tile_slab", "minecraft:deepslate_tile_slab")
            .put("minecraft:grimstone_tile_stairs", "minecraft:deepslate_tile_stairs")
            .put("minecraft:grimstone_tile_wall", "minecraft:deepslate_tile_wall")
            .put("minecraft:grimstone_bricks", "minecraft:deepslate_bricks")
            .put("minecraft:grimstone_brick_slab", "minecraft:deepslate_brick_slab")
            .put("minecraft:grimstone_brick_stairs", "minecraft:deepslate_brick_stairs")
            .put("minecraft:grimstone_brick_wall", "minecraft:deepslate_brick_wall")
            .put("minecraft:chiseled_grimstone", "minecraft:chiseled_deepslate")
            .build();

    public static void register() {
        ConverterAbstractItemRename.register(VERSION, RENAMES::get);
        ConverterAbstractBlockRename.registerAndFixJigsaw(VERSION, RENAMES::get);
    }
}
