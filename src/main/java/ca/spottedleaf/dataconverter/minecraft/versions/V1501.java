package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.advancements.ConverterAbstractAdvancementsRename;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V1501 {

    protected static final int VERSION = MCVersions.V1_13_PRE1;

    private static final Map<String, String> RENAMES = ImmutableMap.<String, String>builder()
            .put("minecraft:recipes/brewing/speckled_melon", "minecraft:recipes/brewing/glistering_melon_slice")
            .put("minecraft:recipes/building_blocks/black_stained_hardened_clay", "minecraft:recipes/building_blocks/black_terracotta")
            .put("minecraft:recipes/building_blocks/blue_stained_hardened_clay", "minecraft:recipes/building_blocks/blue_terracotta")
            .put("minecraft:recipes/building_blocks/brown_stained_hardened_clay", "minecraft:recipes/building_blocks/brown_terracotta")
            .put("minecraft:recipes/building_blocks/cyan_stained_hardened_clay", "minecraft:recipes/building_blocks/cyan_terracotta")
            .put("minecraft:recipes/building_blocks/gray_stained_hardened_clay", "minecraft:recipes/building_blocks/gray_terracotta")
            .put("minecraft:recipes/building_blocks/green_stained_hardened_clay", "minecraft:recipes/building_blocks/green_terracotta")
            .put("minecraft:recipes/building_blocks/light_blue_stained_hardened_clay", "minecraft:recipes/building_blocks/light_blue_terracotta")
            .put("minecraft:recipes/building_blocks/light_gray_stained_hardened_clay", "minecraft:recipes/building_blocks/light_gray_terracotta")
            .put("minecraft:recipes/building_blocks/lime_stained_hardened_clay", "minecraft:recipes/building_blocks/lime_terracotta")
            .put("minecraft:recipes/building_blocks/magenta_stained_hardened_clay", "minecraft:recipes/building_blocks/magenta_terracotta")
            .put("minecraft:recipes/building_blocks/orange_stained_hardened_clay", "minecraft:recipes/building_blocks/orange_terracotta")
            .put("minecraft:recipes/building_blocks/pink_stained_hardened_clay", "minecraft:recipes/building_blocks/pink_terracotta")
            .put("minecraft:recipes/building_blocks/purple_stained_hardened_clay", "minecraft:recipes/building_blocks/purple_terracotta")
            .put("minecraft:recipes/building_blocks/red_stained_hardened_clay", "minecraft:recipes/building_blocks/red_terracotta")
            .put("minecraft:recipes/building_blocks/white_stained_hardened_clay", "minecraft:recipes/building_blocks/white_terracotta")
            .put("minecraft:recipes/building_blocks/yellow_stained_hardened_clay", "minecraft:recipes/building_blocks/yellow_terracotta")
            .put("minecraft:recipes/building_blocks/acacia_wooden_slab", "minecraft:recipes/building_blocks/acacia_slab")
            .put("minecraft:recipes/building_blocks/birch_wooden_slab", "minecraft:recipes/building_blocks/birch_slab")
            .put("minecraft:recipes/building_blocks/dark_oak_wooden_slab", "minecraft:recipes/building_blocks/dark_oak_slab")
            .put("minecraft:recipes/building_blocks/jungle_wooden_slab", "minecraft:recipes/building_blocks/jungle_slab")
            .put("minecraft:recipes/building_blocks/oak_wooden_slab", "minecraft:recipes/building_blocks/oak_slab")
            .put("minecraft:recipes/building_blocks/spruce_wooden_slab", "minecraft:recipes/building_blocks/spruce_slab")
            .put("minecraft:recipes/building_blocks/brick_block", "minecraft:recipes/building_blocks/bricks")
            .put("minecraft:recipes/building_blocks/chiseled_stonebrick", "minecraft:recipes/building_blocks/chiseled_stone_bricks")
            .put("minecraft:recipes/building_blocks/end_bricks", "minecraft:recipes/building_blocks/end_stone_bricks")
            .put("minecraft:recipes/building_blocks/lit_pumpkin", "minecraft:recipes/building_blocks/jack_o_lantern")
            .put("minecraft:recipes/building_blocks/magma", "minecraft:recipes/building_blocks/magma_block")
            .put("minecraft:recipes/building_blocks/melon_block", "minecraft:recipes/building_blocks/melon")
            .put("minecraft:recipes/building_blocks/mossy_stonebrick", "minecraft:recipes/building_blocks/mossy_stone_bricks")
            .put("minecraft:recipes/building_blocks/nether_brick", "minecraft:recipes/building_blocks/nether_bricks")
            .put("minecraft:recipes/building_blocks/pillar_quartz_block", "minecraft:recipes/building_blocks/quartz_pillar")
            .put("minecraft:recipes/building_blocks/red_nether_brick", "minecraft:recipes/building_blocks/red_nether_bricks")
            .put("minecraft:recipes/building_blocks/snow", "minecraft:recipes/building_blocks/snow_block")
            .put("minecraft:recipes/building_blocks/smooth_red_sandstone", "minecraft:recipes/building_blocks/cut_red_sandstone")
            .put("minecraft:recipes/building_blocks/smooth_sandstone", "minecraft:recipes/building_blocks/cut_sandstone")
            .put("minecraft:recipes/building_blocks/stonebrick", "minecraft:recipes/building_blocks/stone_bricks")
            .put("minecraft:recipes/building_blocks/stone_stairs", "minecraft:recipes/building_blocks/cobblestone_stairs")
            .put("minecraft:recipes/building_blocks/string_to_wool", "minecraft:recipes/building_blocks/white_wool_from_string")
            .put("minecraft:recipes/decorations/fence", "minecraft:recipes/decorations/oak_fence")
            .put("minecraft:recipes/decorations/purple_shulker_box", "minecraft:recipes/decorations/shulker_box")
            .put("minecraft:recipes/decorations/slime", "minecraft:recipes/decorations/slime_block")
            .put("minecraft:recipes/decorations/snow_layer", "minecraft:recipes/decorations/snow")
            .put("minecraft:recipes/misc/bone_meal_from_block", "minecraft:recipes/misc/bone_meal_from_bone_block")
            .put("minecraft:recipes/misc/bone_meal_from_bone", "minecraft:recipes/misc/bone_meal")
            .put("minecraft:recipes/misc/gold_ingot_from_block", "minecraft:recipes/misc/gold_ingot_from_gold_block")
            .put("minecraft:recipes/misc/iron_ingot_from_block", "minecraft:recipes/misc/iron_ingot_from_iron_block")
            .put("minecraft:recipes/redstone/fence_gate", "minecraft:recipes/redstone/oak_fence_gate")
            .put("minecraft:recipes/redstone/noteblock", "minecraft:recipes/redstone/note_block")
            .put("minecraft:recipes/redstone/trapdoor", "minecraft:recipes/redstone/oak_trapdoor")
            .put("minecraft:recipes/redstone/wooden_button", "minecraft:recipes/redstone/oak_button")
            .put("minecraft:recipes/redstone/wooden_door", "minecraft:recipes/redstone/oak_door")
            .put("minecraft:recipes/redstone/wooden_pressure_plate", "minecraft:recipes/redstone/oak_pressure_plate")
            .put("minecraft:recipes/transportation/boat", "minecraft:recipes/transportation/oak_boat")
            .put("minecraft:recipes/transportation/golden_rail", "minecraft:recipes/transportation/powered_rail")
            .build();

    private V1501() {}

    public static void register() {
        ConverterAbstractAdvancementsRename.register(VERSION, RENAMES::get);
    }
}
