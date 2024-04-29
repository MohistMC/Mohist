package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.recipe.ConverterAbstractRecipeRename;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V1502 {

    protected static final int VERSION = MCVersions.V1_13_PRE2;

    private static final Map<String, String> RECIPES_UPDATES = ImmutableMap.<String, String>builder()
            .put("minecraft:acacia_wooden_slab", "minecraft:acacia_slab")
            .put("minecraft:birch_wooden_slab", "minecraft:birch_slab")
            .put("minecraft:black_stained_hardened_clay", "minecraft:black_terracotta")
            .put("minecraft:blue_stained_hardened_clay", "minecraft:blue_terracotta")
            .put("minecraft:boat", "minecraft:oak_boat")
            .put("minecraft:bone_meal_from_block", "minecraft:bone_meal_from_bone_block")
            .put("minecraft:bone_meal_from_bone", "minecraft:bone_meal")
            .put("minecraft:brick_block", "minecraft:bricks")
            .put("minecraft:brown_stained_hardened_clay", "minecraft:brown_terracotta")
            .put("minecraft:chiseled_stonebrick", "minecraft:chiseled_stone_bricks")
            .put("minecraft:cyan_stained_hardened_clay", "minecraft:cyan_terracotta")
            .put("minecraft:dark_oak_wooden_slab", "minecraft:dark_oak_slab")
            .put("minecraft:end_bricks", "minecraft:end_stone_bricks")
            .put("minecraft:fence_gate", "minecraft:oak_fence_gate")
            .put("minecraft:fence", "minecraft:oak_fence")
            .put("minecraft:golden_rail", "minecraft:powered_rail")
            .put("minecraft:gold_ingot_from_block", "minecraft:gold_ingot_from_gold_block")
            .put("minecraft:gray_stained_hardened_clay", "minecraft:gray_terracotta")
            .put("minecraft:green_stained_hardened_clay", "minecraft:green_terracotta")
            .put("minecraft:iron_ingot_from_block", "minecraft:iron_ingot_from_iron_block")
            .put("minecraft:jungle_wooden_slab", "minecraft:jungle_slab")
            .put("minecraft:light_blue_stained_hardened_clay", "minecraft:light_blue_terracotta")
            .put("minecraft:light_gray_stained_hardened_clay", "minecraft:light_gray_terracotta")
            .put("minecraft:lime_stained_hardened_clay", "minecraft:lime_terracotta")
            .put("minecraft:lit_pumpkin", "minecraft:jack_o_lantern")
            .put("minecraft:magenta_stained_hardened_clay", "minecraft:magenta_terracotta")
            .put("minecraft:magma", "minecraft:magma_block")
            .put("minecraft:melon_block", "minecraft:melon")
            .put("minecraft:mossy_stonebrick", "minecraft:mossy_stone_bricks")
            .put("minecraft:noteblock", "minecraft:note_block")
            .put("minecraft:oak_wooden_slab", "minecraft:oak_slab")
            .put("minecraft:orange_stained_hardened_clay", "minecraft:orange_terracotta")
            .put("minecraft:pillar_quartz_block", "minecraft:quartz_pillar")
            .put("minecraft:pink_stained_hardened_clay", "minecraft:pink_terracotta")
            .put("minecraft:purple_shulker_box", "minecraft:shulker_box")
            .put("minecraft:purple_stained_hardened_clay", "minecraft:purple_terracotta")
            .put("minecraft:red_nether_brick", "minecraft:red_nether_bricks")
            .put("minecraft:red_stained_hardened_clay", "minecraft:red_terracotta")
            .put("minecraft:slime", "minecraft:slime_block")
            .put("minecraft:smooth_red_sandstone", "minecraft:cut_red_sandstone")
            .put("minecraft:smooth_sandstone", "minecraft:cut_sandstone")
            .put("minecraft:snow_layer", "minecraft:snow")
            .put("minecraft:snow", "minecraft:snow_block")
            .put("minecraft:speckled_melon", "minecraft:glistering_melon_slice")
            .put("minecraft:spruce_wooden_slab", "minecraft:spruce_slab")
            .put("minecraft:stonebrick", "minecraft:stone_bricks")
            .put("minecraft:stone_stairs", "minecraft:cobblestone_stairs")
            .put("minecraft:string_to_wool", "minecraft:white_wool_from_string")
            .put("minecraft:trapdoor", "minecraft:oak_trapdoor")
            .put("minecraft:white_stained_hardened_clay", "minecraft:white_terracotta")
            .put("minecraft:wooden_button", "minecraft:oak_button")
            .put("minecraft:wooden_door", "minecraft:oak_door")
            .put("minecraft:wooden_pressure_plate", "minecraft:oak_pressure_plate")
            .put("minecraft:yellow_stained_hardened_clay", "minecraft:yellow_terracotta")
            .build();

    private V1502() {}

    public static void register() {
        ConverterAbstractRecipeRename.register(VERSION, RECIPES_UPDATES::get);
    }
}
