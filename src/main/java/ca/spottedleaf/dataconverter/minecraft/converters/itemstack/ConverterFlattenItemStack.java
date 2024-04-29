package ca.spottedleaf.dataconverter.minecraft.converters.itemstack;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ConverterFlattenItemStack extends DataConverter<MapType<String>, MapType<String>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    // Map of "id.damage" -> "flattened id"
    private static final Map<String, String> FLATTEN_MAP = new HashMap<>();
    static {
        FLATTEN_MAP.put("minecraft:stone.0", "minecraft:stone");
        FLATTEN_MAP.put("minecraft:stone.1", "minecraft:granite");
        FLATTEN_MAP.put("minecraft:stone.2", "minecraft:polished_granite");
        FLATTEN_MAP.put("minecraft:stone.3", "minecraft:diorite");
        FLATTEN_MAP.put("minecraft:stone.4", "minecraft:polished_diorite");
        FLATTEN_MAP.put("minecraft:stone.5", "minecraft:andesite");
        FLATTEN_MAP.put("minecraft:stone.6", "minecraft:polished_andesite");
        FLATTEN_MAP.put("minecraft:dirt.0", "minecraft:dirt");
        FLATTEN_MAP.put("minecraft:dirt.1", "minecraft:coarse_dirt");
        FLATTEN_MAP.put("minecraft:dirt.2", "minecraft:podzol");
        FLATTEN_MAP.put("minecraft:leaves.0", "minecraft:oak_leaves");
        FLATTEN_MAP.put("minecraft:leaves.1", "minecraft:spruce_leaves");
        FLATTEN_MAP.put("minecraft:leaves.2", "minecraft:birch_leaves");
        FLATTEN_MAP.put("minecraft:leaves.3", "minecraft:jungle_leaves");
        FLATTEN_MAP.put("minecraft:leaves2.0", "minecraft:acacia_leaves");
        FLATTEN_MAP.put("minecraft:leaves2.1", "minecraft:dark_oak_leaves");
        FLATTEN_MAP.put("minecraft:log.0", "minecraft:oak_log");
        FLATTEN_MAP.put("minecraft:log.1", "minecraft:spruce_log");
        FLATTEN_MAP.put("minecraft:log.2", "minecraft:birch_log");
        FLATTEN_MAP.put("minecraft:log.3", "minecraft:jungle_log");
        FLATTEN_MAP.put("minecraft:log2.0", "minecraft:acacia_log");
        FLATTEN_MAP.put("minecraft:log2.1", "minecraft:dark_oak_log");
        FLATTEN_MAP.put("minecraft:sapling.0", "minecraft:oak_sapling");
        FLATTEN_MAP.put("minecraft:sapling.1", "minecraft:spruce_sapling");
        FLATTEN_MAP.put("minecraft:sapling.2", "minecraft:birch_sapling");
        FLATTEN_MAP.put("minecraft:sapling.3", "minecraft:jungle_sapling");
        FLATTEN_MAP.put("minecraft:sapling.4", "minecraft:acacia_sapling");
        FLATTEN_MAP.put("minecraft:sapling.5", "minecraft:dark_oak_sapling");
        FLATTEN_MAP.put("minecraft:planks.0", "minecraft:oak_planks");
        FLATTEN_MAP.put("minecraft:planks.1", "minecraft:spruce_planks");
        FLATTEN_MAP.put("minecraft:planks.2", "minecraft:birch_planks");
        FLATTEN_MAP.put("minecraft:planks.3", "minecraft:jungle_planks");
        FLATTEN_MAP.put("minecraft:planks.4", "minecraft:acacia_planks");
        FLATTEN_MAP.put("minecraft:planks.5", "minecraft:dark_oak_planks");
        FLATTEN_MAP.put("minecraft:sand.0", "minecraft:sand");
        FLATTEN_MAP.put("minecraft:sand.1", "minecraft:red_sand");
        FLATTEN_MAP.put("minecraft:quartz_block.0", "minecraft:quartz_block");
        FLATTEN_MAP.put("minecraft:quartz_block.1", "minecraft:chiseled_quartz_block");
        FLATTEN_MAP.put("minecraft:quartz_block.2", "minecraft:quartz_pillar");
        FLATTEN_MAP.put("minecraft:anvil.0", "minecraft:anvil");
        FLATTEN_MAP.put("minecraft:anvil.1", "minecraft:chipped_anvil");
        FLATTEN_MAP.put("minecraft:anvil.2", "minecraft:damaged_anvil");
        FLATTEN_MAP.put("minecraft:wool.0", "minecraft:white_wool");
        FLATTEN_MAP.put("minecraft:wool.1", "minecraft:orange_wool");
        FLATTEN_MAP.put("minecraft:wool.2", "minecraft:magenta_wool");
        FLATTEN_MAP.put("minecraft:wool.3", "minecraft:light_blue_wool");
        FLATTEN_MAP.put("minecraft:wool.4", "minecraft:yellow_wool");
        FLATTEN_MAP.put("minecraft:wool.5", "minecraft:lime_wool");
        FLATTEN_MAP.put("minecraft:wool.6", "minecraft:pink_wool");
        FLATTEN_MAP.put("minecraft:wool.7", "minecraft:gray_wool");
        FLATTEN_MAP.put("minecraft:wool.8", "minecraft:light_gray_wool");
        FLATTEN_MAP.put("minecraft:wool.9", "minecraft:cyan_wool");
        FLATTEN_MAP.put("minecraft:wool.10", "minecraft:purple_wool");
        FLATTEN_MAP.put("minecraft:wool.11", "minecraft:blue_wool");
        FLATTEN_MAP.put("minecraft:wool.12", "minecraft:brown_wool");
        FLATTEN_MAP.put("minecraft:wool.13", "minecraft:green_wool");
        FLATTEN_MAP.put("minecraft:wool.14", "minecraft:red_wool");
        FLATTEN_MAP.put("minecraft:wool.15", "minecraft:black_wool");
        FLATTEN_MAP.put("minecraft:carpet.0", "minecraft:white_carpet");
        FLATTEN_MAP.put("minecraft:carpet.1", "minecraft:orange_carpet");
        FLATTEN_MAP.put("minecraft:carpet.2", "minecraft:magenta_carpet");
        FLATTEN_MAP.put("minecraft:carpet.3", "minecraft:light_blue_carpet");
        FLATTEN_MAP.put("minecraft:carpet.4", "minecraft:yellow_carpet");
        FLATTEN_MAP.put("minecraft:carpet.5", "minecraft:lime_carpet");
        FLATTEN_MAP.put("minecraft:carpet.6", "minecraft:pink_carpet");
        FLATTEN_MAP.put("minecraft:carpet.7", "minecraft:gray_carpet");
        FLATTEN_MAP.put("minecraft:carpet.8", "minecraft:light_gray_carpet");
        FLATTEN_MAP.put("minecraft:carpet.9", "minecraft:cyan_carpet");
        FLATTEN_MAP.put("minecraft:carpet.10", "minecraft:purple_carpet");
        FLATTEN_MAP.put("minecraft:carpet.11", "minecraft:blue_carpet");
        FLATTEN_MAP.put("minecraft:carpet.12", "minecraft:brown_carpet");
        FLATTEN_MAP.put("minecraft:carpet.13", "minecraft:green_carpet");
        FLATTEN_MAP.put("minecraft:carpet.14", "minecraft:red_carpet");
        FLATTEN_MAP.put("minecraft:carpet.15", "minecraft:black_carpet");
        FLATTEN_MAP.put("minecraft:hardened_clay.0", "minecraft:terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.0", "minecraft:white_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.1", "minecraft:orange_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.2", "minecraft:magenta_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.3", "minecraft:light_blue_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.4", "minecraft:yellow_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.5", "minecraft:lime_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.6", "minecraft:pink_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.7", "minecraft:gray_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.8", "minecraft:light_gray_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.9", "minecraft:cyan_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.10", "minecraft:purple_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.11", "minecraft:blue_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.12", "minecraft:brown_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.13", "minecraft:green_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.14", "minecraft:red_terracotta");
        FLATTEN_MAP.put("minecraft:stained_hardened_clay.15", "minecraft:black_terracotta");
        FLATTEN_MAP.put("minecraft:silver_glazed_terracotta.0", "minecraft:light_gray_glazed_terracotta");
        FLATTEN_MAP.put("minecraft:stained_glass.0", "minecraft:white_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.1", "minecraft:orange_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.2", "minecraft:magenta_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.3", "minecraft:light_blue_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.4", "minecraft:yellow_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.5", "minecraft:lime_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.6", "minecraft:pink_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.7", "minecraft:gray_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.8", "minecraft:light_gray_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.9", "minecraft:cyan_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.10", "minecraft:purple_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.11", "minecraft:blue_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.12", "minecraft:brown_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.13", "minecraft:green_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.14", "minecraft:red_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass.15", "minecraft:black_stained_glass");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.0", "minecraft:white_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.1", "minecraft:orange_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.2", "minecraft:magenta_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.3", "minecraft:light_blue_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.4", "minecraft:yellow_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.5", "minecraft:lime_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.6", "minecraft:pink_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.7", "minecraft:gray_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.8", "minecraft:light_gray_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.9", "minecraft:cyan_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.10", "minecraft:purple_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.11", "minecraft:blue_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.12", "minecraft:brown_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.13", "minecraft:green_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.14", "minecraft:red_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:stained_glass_pane.15", "minecraft:black_stained_glass_pane");
        FLATTEN_MAP.put("minecraft:prismarine.0", "minecraft:prismarine");
        FLATTEN_MAP.put("minecraft:prismarine.1", "minecraft:prismarine_bricks");
        FLATTEN_MAP.put("minecraft:prismarine.2", "minecraft:dark_prismarine");
        FLATTEN_MAP.put("minecraft:concrete.0", "minecraft:white_concrete");
        FLATTEN_MAP.put("minecraft:concrete.1", "minecraft:orange_concrete");
        FLATTEN_MAP.put("minecraft:concrete.2", "minecraft:magenta_concrete");
        FLATTEN_MAP.put("minecraft:concrete.3", "minecraft:light_blue_concrete");
        FLATTEN_MAP.put("minecraft:concrete.4", "minecraft:yellow_concrete");
        FLATTEN_MAP.put("minecraft:concrete.5", "minecraft:lime_concrete");
        FLATTEN_MAP.put("minecraft:concrete.6", "minecraft:pink_concrete");
        FLATTEN_MAP.put("minecraft:concrete.7", "minecraft:gray_concrete");
        FLATTEN_MAP.put("minecraft:concrete.8", "minecraft:light_gray_concrete");
        FLATTEN_MAP.put("minecraft:concrete.9", "minecraft:cyan_concrete");
        FLATTEN_MAP.put("minecraft:concrete.10", "minecraft:purple_concrete");
        FLATTEN_MAP.put("minecraft:concrete.11", "minecraft:blue_concrete");
        FLATTEN_MAP.put("minecraft:concrete.12", "minecraft:brown_concrete");
        FLATTEN_MAP.put("minecraft:concrete.13", "minecraft:green_concrete");
        FLATTEN_MAP.put("minecraft:concrete.14", "minecraft:red_concrete");
        FLATTEN_MAP.put("minecraft:concrete.15", "minecraft:black_concrete");
        FLATTEN_MAP.put("minecraft:concrete_powder.0", "minecraft:white_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.1", "minecraft:orange_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.2", "minecraft:magenta_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.3", "minecraft:light_blue_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.4", "minecraft:yellow_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.5", "minecraft:lime_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.6", "minecraft:pink_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.7", "minecraft:gray_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.8", "minecraft:light_gray_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.9", "minecraft:cyan_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.10", "minecraft:purple_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.11", "minecraft:blue_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.12", "minecraft:brown_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.13", "minecraft:green_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.14", "minecraft:red_concrete_powder");
        FLATTEN_MAP.put("minecraft:concrete_powder.15", "minecraft:black_concrete_powder");
        FLATTEN_MAP.put("minecraft:cobblestone_wall.0", "minecraft:cobblestone_wall");
        FLATTEN_MAP.put("minecraft:cobblestone_wall.1", "minecraft:mossy_cobblestone_wall");
        FLATTEN_MAP.put("minecraft:sandstone.0", "minecraft:sandstone");
        FLATTEN_MAP.put("minecraft:sandstone.1", "minecraft:chiseled_sandstone");
        FLATTEN_MAP.put("minecraft:sandstone.2", "minecraft:cut_sandstone");
        FLATTEN_MAP.put("minecraft:red_sandstone.0", "minecraft:red_sandstone");
        FLATTEN_MAP.put("minecraft:red_sandstone.1", "minecraft:chiseled_red_sandstone");
        FLATTEN_MAP.put("minecraft:red_sandstone.2", "minecraft:cut_red_sandstone");
        FLATTEN_MAP.put("minecraft:stonebrick.0", "minecraft:stone_bricks");
        FLATTEN_MAP.put("minecraft:stonebrick.1", "minecraft:mossy_stone_bricks");
        FLATTEN_MAP.put("minecraft:stonebrick.2", "minecraft:cracked_stone_bricks");
        FLATTEN_MAP.put("minecraft:stonebrick.3", "minecraft:chiseled_stone_bricks");
        FLATTEN_MAP.put("minecraft:monster_egg.0", "minecraft:infested_stone");
        FLATTEN_MAP.put("minecraft:monster_egg.1", "minecraft:infested_cobblestone");
        FLATTEN_MAP.put("minecraft:monster_egg.2", "minecraft:infested_stone_bricks");
        FLATTEN_MAP.put("minecraft:monster_egg.3", "minecraft:infested_mossy_stone_bricks");
        FLATTEN_MAP.put("minecraft:monster_egg.4", "minecraft:infested_cracked_stone_bricks");
        FLATTEN_MAP.put("minecraft:monster_egg.5", "minecraft:infested_chiseled_stone_bricks");
        FLATTEN_MAP.put("minecraft:yellow_flower.0", "minecraft:dandelion");
        FLATTEN_MAP.put("minecraft:red_flower.0", "minecraft:poppy");
        FLATTEN_MAP.put("minecraft:red_flower.1", "minecraft:blue_orchid");
        FLATTEN_MAP.put("minecraft:red_flower.2", "minecraft:allium");
        FLATTEN_MAP.put("minecraft:red_flower.3", "minecraft:azure_bluet");
        FLATTEN_MAP.put("minecraft:red_flower.4", "minecraft:red_tulip");
        FLATTEN_MAP.put("minecraft:red_flower.5", "minecraft:orange_tulip");
        FLATTEN_MAP.put("minecraft:red_flower.6", "minecraft:white_tulip");
        FLATTEN_MAP.put("minecraft:red_flower.7", "minecraft:pink_tulip");
        FLATTEN_MAP.put("minecraft:red_flower.8", "minecraft:oxeye_daisy");
        FLATTEN_MAP.put("minecraft:double_plant.0", "minecraft:sunflower");
        FLATTEN_MAP.put("minecraft:double_plant.1", "minecraft:lilac");
        FLATTEN_MAP.put("minecraft:double_plant.2", "minecraft:tall_grass");
        FLATTEN_MAP.put("minecraft:double_plant.3", "minecraft:large_fern");
        FLATTEN_MAP.put("minecraft:double_plant.4", "minecraft:rose_bush");
        FLATTEN_MAP.put("minecraft:double_plant.5", "minecraft:peony");
        FLATTEN_MAP.put("minecraft:deadbush.0", "minecraft:dead_bush");
        FLATTEN_MAP.put("minecraft:tallgrass.0", "minecraft:dead_bush");
        FLATTEN_MAP.put("minecraft:tallgrass.1", "minecraft:grass");
        FLATTEN_MAP.put("minecraft:tallgrass.2", "minecraft:fern");
        FLATTEN_MAP.put("minecraft:sponge.0", "minecraft:sponge");
        FLATTEN_MAP.put("minecraft:sponge.1", "minecraft:wet_sponge");
        FLATTEN_MAP.put("minecraft:purpur_slab.0", "minecraft:purpur_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.0", "minecraft:stone_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.1", "minecraft:sandstone_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.2", "minecraft:petrified_oak_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.3", "minecraft:cobblestone_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.4", "minecraft:brick_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.5", "minecraft:stone_brick_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.6", "minecraft:nether_brick_slab");
        FLATTEN_MAP.put("minecraft:stone_slab.7", "minecraft:quartz_slab");
        FLATTEN_MAP.put("minecraft:stone_slab2.0", "minecraft:red_sandstone_slab");
        FLATTEN_MAP.put("minecraft:wooden_slab.0", "minecraft:oak_slab");
        FLATTEN_MAP.put("minecraft:wooden_slab.1", "minecraft:spruce_slab");
        FLATTEN_MAP.put("minecraft:wooden_slab.2", "minecraft:birch_slab");
        FLATTEN_MAP.put("minecraft:wooden_slab.3", "minecraft:jungle_slab");
        FLATTEN_MAP.put("minecraft:wooden_slab.4", "minecraft:acacia_slab");
        FLATTEN_MAP.put("minecraft:wooden_slab.5", "minecraft:dark_oak_slab");
        FLATTEN_MAP.put("minecraft:coal.0", "minecraft:coal");
        FLATTEN_MAP.put("minecraft:coal.1", "minecraft:charcoal");
        FLATTEN_MAP.put("minecraft:fish.0", "minecraft:cod");
        FLATTEN_MAP.put("minecraft:fish.1", "minecraft:salmon");
        FLATTEN_MAP.put("minecraft:fish.2", "minecraft:clownfish");
        FLATTEN_MAP.put("minecraft:fish.3", "minecraft:pufferfish");
        FLATTEN_MAP.put("minecraft:cooked_fish.0", "minecraft:cooked_cod");
        FLATTEN_MAP.put("minecraft:cooked_fish.1", "minecraft:cooked_salmon");
        FLATTEN_MAP.put("minecraft:skull.0", "minecraft:skeleton_skull");
        FLATTEN_MAP.put("minecraft:skull.1", "minecraft:wither_skeleton_skull");
        FLATTEN_MAP.put("minecraft:skull.2", "minecraft:zombie_head");
        FLATTEN_MAP.put("minecraft:skull.3", "minecraft:player_head");
        FLATTEN_MAP.put("minecraft:skull.4", "minecraft:creeper_head");
        FLATTEN_MAP.put("minecraft:skull.5", "minecraft:dragon_head");
        FLATTEN_MAP.put("minecraft:golden_apple.0", "minecraft:golden_apple");
        FLATTEN_MAP.put("minecraft:golden_apple.1", "minecraft:enchanted_golden_apple");
        FLATTEN_MAP.put("minecraft:fireworks.0", "minecraft:firework_rocket");
        FLATTEN_MAP.put("minecraft:firework_charge.0", "minecraft:firework_star");
        FLATTEN_MAP.put("minecraft:dye.0", "minecraft:ink_sac");
        FLATTEN_MAP.put("minecraft:dye.1", "minecraft:rose_red");
        FLATTEN_MAP.put("minecraft:dye.2", "minecraft:cactus_green");
        FLATTEN_MAP.put("minecraft:dye.3", "minecraft:cocoa_beans");
        FLATTEN_MAP.put("minecraft:dye.4", "minecraft:lapis_lazuli");
        FLATTEN_MAP.put("minecraft:dye.5", "minecraft:purple_dye");
        FLATTEN_MAP.put("minecraft:dye.6", "minecraft:cyan_dye");
        FLATTEN_MAP.put("minecraft:dye.7", "minecraft:light_gray_dye");
        FLATTEN_MAP.put("minecraft:dye.8", "minecraft:gray_dye");
        FLATTEN_MAP.put("minecraft:dye.9", "minecraft:pink_dye");
        FLATTEN_MAP.put("minecraft:dye.10", "minecraft:lime_dye");
        FLATTEN_MAP.put("minecraft:dye.11", "minecraft:dandelion_yellow");
        FLATTEN_MAP.put("minecraft:dye.12", "minecraft:light_blue_dye");
        FLATTEN_MAP.put("minecraft:dye.13", "minecraft:magenta_dye");
        FLATTEN_MAP.put("minecraft:dye.14", "minecraft:orange_dye");
        FLATTEN_MAP.put("minecraft:dye.15", "minecraft:bone_meal");
        FLATTEN_MAP.put("minecraft:silver_shulker_box.0", "minecraft:light_gray_shulker_box");
        FLATTEN_MAP.put("minecraft:fence.0", "minecraft:oak_fence");
        FLATTEN_MAP.put("minecraft:fence_gate.0", "minecraft:oak_fence_gate");
        FLATTEN_MAP.put("minecraft:wooden_door.0", "minecraft:oak_door");
        FLATTEN_MAP.put("minecraft:boat.0", "minecraft:oak_boat");
        FLATTEN_MAP.put("minecraft:lit_pumpkin.0", "minecraft:jack_o_lantern");
        FLATTEN_MAP.put("minecraft:pumpkin.0", "minecraft:carved_pumpkin");
        FLATTEN_MAP.put("minecraft:trapdoor.0", "minecraft:oak_trapdoor");
        FLATTEN_MAP.put("minecraft:nether_brick.0", "minecraft:nether_bricks");
        FLATTEN_MAP.put("minecraft:red_nether_brick.0", "minecraft:red_nether_bricks");
        FLATTEN_MAP.put("minecraft:netherbrick.0", "minecraft:nether_brick");
        FLATTEN_MAP.put("minecraft:wooden_button.0", "minecraft:oak_button");
        FLATTEN_MAP.put("minecraft:wooden_pressure_plate.0", "minecraft:oak_pressure_plate");
        FLATTEN_MAP.put("minecraft:noteblock.0", "minecraft:note_block");
        FLATTEN_MAP.put("minecraft:bed.0", "minecraft:white_bed");
        FLATTEN_MAP.put("minecraft:bed.1", "minecraft:orange_bed");
        FLATTEN_MAP.put("minecraft:bed.2", "minecraft:magenta_bed");
        FLATTEN_MAP.put("minecraft:bed.3", "minecraft:light_blue_bed");
        FLATTEN_MAP.put("minecraft:bed.4", "minecraft:yellow_bed");
        FLATTEN_MAP.put("minecraft:bed.5", "minecraft:lime_bed");
        FLATTEN_MAP.put("minecraft:bed.6", "minecraft:pink_bed");
        FLATTEN_MAP.put("minecraft:bed.7", "minecraft:gray_bed");
        FLATTEN_MAP.put("minecraft:bed.8", "minecraft:light_gray_bed");
        FLATTEN_MAP.put("minecraft:bed.9", "minecraft:cyan_bed");
        FLATTEN_MAP.put("minecraft:bed.10", "minecraft:purple_bed");
        FLATTEN_MAP.put("minecraft:bed.11", "minecraft:blue_bed");
        FLATTEN_MAP.put("minecraft:bed.12", "minecraft:brown_bed");
        FLATTEN_MAP.put("minecraft:bed.13", "minecraft:green_bed");
        FLATTEN_MAP.put("minecraft:bed.14", "minecraft:red_bed");
        FLATTEN_MAP.put("minecraft:bed.15", "minecraft:black_bed");
        FLATTEN_MAP.put("minecraft:banner.15", "minecraft:white_banner");
        FLATTEN_MAP.put("minecraft:banner.14", "minecraft:orange_banner");
        FLATTEN_MAP.put("minecraft:banner.13", "minecraft:magenta_banner");
        FLATTEN_MAP.put("minecraft:banner.12", "minecraft:light_blue_banner");
        FLATTEN_MAP.put("minecraft:banner.11", "minecraft:yellow_banner");
        FLATTEN_MAP.put("minecraft:banner.10", "minecraft:lime_banner");
        FLATTEN_MAP.put("minecraft:banner.9", "minecraft:pink_banner");
        FLATTEN_MAP.put("minecraft:banner.8", "minecraft:gray_banner");
        FLATTEN_MAP.put("minecraft:banner.7", "minecraft:light_gray_banner");
        FLATTEN_MAP.put("minecraft:banner.6", "minecraft:cyan_banner");
        FLATTEN_MAP.put("minecraft:banner.5", "minecraft:purple_banner");
        FLATTEN_MAP.put("minecraft:banner.4", "minecraft:blue_banner");
        FLATTEN_MAP.put("minecraft:banner.3", "minecraft:brown_banner");
        FLATTEN_MAP.put("minecraft:banner.2", "minecraft:green_banner");
        FLATTEN_MAP.put("minecraft:banner.1", "minecraft:red_banner");
        FLATTEN_MAP.put("minecraft:banner.0", "minecraft:black_banner");
        FLATTEN_MAP.put("minecraft:grass.0", "minecraft:grass_block");
        FLATTEN_MAP.put("minecraft:brick_block.0", "minecraft:bricks");
        FLATTEN_MAP.put("minecraft:end_bricks.0", "minecraft:end_stone_bricks");
        FLATTEN_MAP.put("minecraft:golden_rail.0", "minecraft:powered_rail");
        FLATTEN_MAP.put("minecraft:magma.0", "minecraft:magma_block");
        FLATTEN_MAP.put("minecraft:quartz_ore.0", "minecraft:nether_quartz_ore");
        FLATTEN_MAP.put("minecraft:reeds.0", "minecraft:sugar_cane");
        FLATTEN_MAP.put("minecraft:slime.0", "minecraft:slime_block");
        FLATTEN_MAP.put("minecraft:stone_stairs.0", "minecraft:cobblestone_stairs");
        FLATTEN_MAP.put("minecraft:waterlily.0", "minecraft:lily_pad");
        FLATTEN_MAP.put("minecraft:web.0", "minecraft:cobweb");
        FLATTEN_MAP.put("minecraft:snow.0", "minecraft:snow_block");
        FLATTEN_MAP.put("minecraft:snow_layer.0", "minecraft:snow");
        FLATTEN_MAP.put("minecraft:record_11.0", "minecraft:music_disc_11");
        FLATTEN_MAP.put("minecraft:record_13.0", "minecraft:music_disc_13");
        FLATTEN_MAP.put("minecraft:record_blocks.0", "minecraft:music_disc_blocks");
        FLATTEN_MAP.put("minecraft:record_cat.0", "minecraft:music_disc_cat");
        FLATTEN_MAP.put("minecraft:record_chirp.0", "minecraft:music_disc_chirp");
        FLATTEN_MAP.put("minecraft:record_far.0", "minecraft:music_disc_far");
        FLATTEN_MAP.put("minecraft:record_mall.0", "minecraft:music_disc_mall");
        FLATTEN_MAP.put("minecraft:record_mellohi.0", "minecraft:music_disc_mellohi");
        FLATTEN_MAP.put("minecraft:record_stal.0", "minecraft:music_disc_stal");
        FLATTEN_MAP.put("minecraft:record_strad.0", "minecraft:music_disc_strad");
        FLATTEN_MAP.put("minecraft:record_wait.0", "minecraft:music_disc_wait");
        FLATTEN_MAP.put("minecraft:record_ward.0", "minecraft:music_disc_ward");
    }

    // maps out ids requiring flattening
    private static final Set<String> IDS_REQUIRING_FLATTENING = new HashSet<>();
    static {
        for (final String key : FLATTEN_MAP.keySet()) {
            IDS_REQUIRING_FLATTENING.add(key.substring(0, key.indexOf('.')));
        }
    }

    // Damage tag is moved from the ItemStack base tag to the ItemStack tag, and we only want to migrate that
    // for items that actually require it for damage purposes (Remember, old damage was used to differentiate item types)
    // It should be noted that this ID set should not be included in the flattening map, because damage for these items
    // is actual damage and not a subtype specifier
    private static final Set<String> ITEMS_WITH_DAMAGE = new HashSet<>(Arrays.asList(
            "minecraft:bow",
            "minecraft:carrot_on_a_stick",
            "minecraft:chainmail_boots",
            "minecraft:chainmail_chestplate",
            "minecraft:chainmail_helmet",
            "minecraft:chainmail_leggings",
            "minecraft:diamond_axe",
            "minecraft:diamond_boots",
            "minecraft:diamond_chestplate",
            "minecraft:diamond_helmet",
            "minecraft:diamond_hoe",
            "minecraft:diamond_leggings",
            "minecraft:diamond_pickaxe",
            "minecraft:diamond_shovel",
            "minecraft:diamond_sword",
            "minecraft:elytra",
            "minecraft:fishing_rod",
            "minecraft:flint_and_steel",
            "minecraft:golden_axe",
            "minecraft:golden_boots",
            "minecraft:golden_chestplate",
            "minecraft:golden_helmet",
            "minecraft:golden_hoe",
            "minecraft:golden_leggings",
            "minecraft:golden_pickaxe",
            "minecraft:golden_shovel",
            "minecraft:golden_sword",
            "minecraft:iron_axe",
            "minecraft:iron_boots",
            "minecraft:iron_chestplate",
            "minecraft:iron_helmet",
            "minecraft:iron_hoe",
            "minecraft:iron_leggings",
            "minecraft:iron_pickaxe",
            "minecraft:iron_shovel",
            "minecraft:iron_sword",
            "minecraft:leather_boots",
            "minecraft:leather_chestplate",
            "minecraft:leather_helmet",
            "minecraft:leather_leggings",
            "minecraft:shears",
            "minecraft:shield",
            "minecraft:stone_axe",
            "minecraft:stone_hoe",
            "minecraft:stone_pickaxe",
            "minecraft:stone_shovel",
            "minecraft:stone_sword",
            "minecraft:wooden_axe",
            "minecraft:wooden_hoe",
            "minecraft:wooden_pickaxe",
            "minecraft:wooden_shovel",
            "minecraft:wooden_sword"
    ));

    public ConverterFlattenItemStack() {
        super(MCVersions.V17W47A, 4);
    }

    public static String flattenItem(final String oldName, final int data) {
        if (IDS_REQUIRING_FLATTENING.contains(oldName)) {
            final String flattened = FLATTEN_MAP.get(oldName + '.' + data);
            return flattened == null ? FLATTEN_MAP.get(oldName.concat(".0")) : flattened;
        } else {
            return null;
        }
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        final String id = data.getString("id");

        if (id == null) {
            return null;
        }

        final int damage = data.getInt("Damage");
        data.remove("Damage");

        if (IDS_REQUIRING_FLATTENING.contains(id)) {
            String remap = FLATTEN_MAP.get(id + '.' + damage);
            if (remap == null) {
                remap = FLATTEN_MAP.get(id.concat(".0"));
                // this shouldn't be null
            }
            if (remap != null) {
                data.setString("id", remap);
            } else {
                LOGGER.warn("Item '" + id + "' requires flattening but found no mapping for it! (ConverterFlattenItemStack)");
            }
        }

        if (damage != 0 && ITEMS_WITH_DAMAGE.contains(id)) {
            // migrate damage
            MapType<String> tag = data.getMap("tag");
            if (tag == null) {
                tag = Types.NBT.createEmptyMap();
                data.setMap("tag", tag);
            }
            tag.setInt("Damage", damage);
        }

        return null;
    }
}
