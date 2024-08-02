package ca.spottedleaf.dataconverter.minecraft.converters.helpers;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class HelperItemNameV102 {

    // This class is responsible for mapping the id -> string update in itemstacks and potions

    private static final Int2ObjectOpenHashMap<String> ITEM_NAMES = new Int2ObjectOpenHashMap<String>() {
        @Override
        public String put(final int k, final String o) {
            final String ret =  super.put(k, o);

            if (ret != null) {
                throw new IllegalStateException("Mapping already exists for " + k + ": prev: " + ret + ", new: " + o);
            }

            return ret;
        }
    };

    static {
        ITEM_NAMES.put(0, "minecraft:air");
        ITEM_NAMES.put(1, "minecraft:stone");
        ITEM_NAMES.put(2, "minecraft:grass");
        ITEM_NAMES.put(3, "minecraft:dirt");
        ITEM_NAMES.put(4, "minecraft:cobblestone");
        ITEM_NAMES.put(5, "minecraft:planks");
        ITEM_NAMES.put(6, "minecraft:sapling");
        ITEM_NAMES.put(7, "minecraft:bedrock");
        ITEM_NAMES.put(8, "minecraft:flowing_water");
        ITEM_NAMES.put(9, "minecraft:water");
        ITEM_NAMES.put(10, "minecraft:flowing_lava");
        ITEM_NAMES.put(11, "minecraft:lava");
        ITEM_NAMES.put(12, "minecraft:sand");
        ITEM_NAMES.put(13, "minecraft:gravel");
        ITEM_NAMES.put(14, "minecraft:gold_ore");
        ITEM_NAMES.put(15, "minecraft:iron_ore");
        ITEM_NAMES.put(16, "minecraft:coal_ore");
        ITEM_NAMES.put(17, "minecraft:log");
        ITEM_NAMES.put(18, "minecraft:leaves");
        ITEM_NAMES.put(19, "minecraft:sponge");
        ITEM_NAMES.put(20, "minecraft:glass");
        ITEM_NAMES.put(21, "minecraft:lapis_ore");
        ITEM_NAMES.put(22, "minecraft:lapis_block");
        ITEM_NAMES.put(23, "minecraft:dispenser");
        ITEM_NAMES.put(24, "minecraft:sandstone");
        ITEM_NAMES.put(25, "minecraft:noteblock");
        ITEM_NAMES.put(27, "minecraft:golden_rail");
        ITEM_NAMES.put(28, "minecraft:detector_rail");
        ITEM_NAMES.put(29, "minecraft:sticky_piston");
        ITEM_NAMES.put(30, "minecraft:web");
        ITEM_NAMES.put(31, "minecraft:tallgrass");
        ITEM_NAMES.put(32, "minecraft:deadbush");
        ITEM_NAMES.put(33, "minecraft:piston");
        ITEM_NAMES.put(35, "minecraft:wool");
        ITEM_NAMES.put(37, "minecraft:yellow_flower");
        ITEM_NAMES.put(38, "minecraft:red_flower");
        ITEM_NAMES.put(39, "minecraft:brown_mushroom");
        ITEM_NAMES.put(40, "minecraft:red_mushroom");
        ITEM_NAMES.put(41, "minecraft:gold_block");
        ITEM_NAMES.put(42, "minecraft:iron_block");
        ITEM_NAMES.put(43, "minecraft:double_stone_slab");
        ITEM_NAMES.put(44, "minecraft:stone_slab");
        ITEM_NAMES.put(45, "minecraft:brick_block");
        ITEM_NAMES.put(46, "minecraft:tnt");
        ITEM_NAMES.put(47, "minecraft:bookshelf");
        ITEM_NAMES.put(48, "minecraft:mossy_cobblestone");
        ITEM_NAMES.put(49, "minecraft:obsidian");
        ITEM_NAMES.put(50, "minecraft:torch");
        ITEM_NAMES.put(51, "minecraft:fire");
        ITEM_NAMES.put(52, "minecraft:mob_spawner");
        ITEM_NAMES.put(53, "minecraft:oak_stairs");
        ITEM_NAMES.put(54, "minecraft:chest");
        ITEM_NAMES.put(56, "minecraft:diamond_ore");
        ITEM_NAMES.put(57, "minecraft:diamond_block");
        ITEM_NAMES.put(58, "minecraft:crafting_table");
        ITEM_NAMES.put(60, "minecraft:farmland");
        ITEM_NAMES.put(61, "minecraft:furnace");
        ITEM_NAMES.put(62, "minecraft:lit_furnace");
        ITEM_NAMES.put(65, "minecraft:ladder");
        ITEM_NAMES.put(66, "minecraft:rail");
        ITEM_NAMES.put(67, "minecraft:stone_stairs");
        ITEM_NAMES.put(69, "minecraft:lever");
        ITEM_NAMES.put(70, "minecraft:stone_pressure_plate");
        ITEM_NAMES.put(72, "minecraft:wooden_pressure_plate");
        ITEM_NAMES.put(73, "minecraft:redstone_ore");
        ITEM_NAMES.put(76, "minecraft:redstone_torch");
        ITEM_NAMES.put(77, "minecraft:stone_button");
        ITEM_NAMES.put(78, "minecraft:snow_layer");
        ITEM_NAMES.put(79, "minecraft:ice");
        ITEM_NAMES.put(80, "minecraft:snow");
        ITEM_NAMES.put(81, "minecraft:cactus");
        ITEM_NAMES.put(82, "minecraft:clay");
        ITEM_NAMES.put(84, "minecraft:jukebox");
        ITEM_NAMES.put(85, "minecraft:fence");
        ITEM_NAMES.put(86, "minecraft:pumpkin");
        ITEM_NAMES.put(87, "minecraft:netherrack");
        ITEM_NAMES.put(88, "minecraft:soul_sand");
        ITEM_NAMES.put(89, "minecraft:glowstone");
        ITEM_NAMES.put(90, "minecraft:portal");
        ITEM_NAMES.put(91, "minecraft:lit_pumpkin");
        ITEM_NAMES.put(95, "minecraft:stained_glass");
        ITEM_NAMES.put(96, "minecraft:trapdoor");
        ITEM_NAMES.put(97, "minecraft:monster_egg");
        ITEM_NAMES.put(98, "minecraft:stonebrick");
        ITEM_NAMES.put(99, "minecraft:brown_mushroom_block");
        ITEM_NAMES.put(100, "minecraft:red_mushroom_block");
        ITEM_NAMES.put(101, "minecraft:iron_bars");
        ITEM_NAMES.put(102, "minecraft:glass_pane");
        ITEM_NAMES.put(103, "minecraft:melon_block");
        ITEM_NAMES.put(106, "minecraft:vine");
        ITEM_NAMES.put(107, "minecraft:fence_gate");
        ITEM_NAMES.put(108, "minecraft:brick_stairs");
        ITEM_NAMES.put(109, "minecraft:stone_brick_stairs");
        ITEM_NAMES.put(110, "minecraft:mycelium");
        ITEM_NAMES.put(111, "minecraft:waterlily");
        ITEM_NAMES.put(112, "minecraft:nether_brick");
        ITEM_NAMES.put(113, "minecraft:nether_brick_fence");
        ITEM_NAMES.put(114, "minecraft:nether_brick_stairs");
        ITEM_NAMES.put(116, "minecraft:enchanting_table");
        ITEM_NAMES.put(119, "minecraft:end_portal");
        ITEM_NAMES.put(120, "minecraft:end_portal_frame");
        ITEM_NAMES.put(121, "minecraft:end_stone");
        ITEM_NAMES.put(122, "minecraft:dragon_egg");
        ITEM_NAMES.put(123, "minecraft:redstone_lamp");
        ITEM_NAMES.put(125, "minecraft:double_wooden_slab");
        ITEM_NAMES.put(126, "minecraft:wooden_slab");
        ITEM_NAMES.put(127, "minecraft:cocoa");
        ITEM_NAMES.put(128, "minecraft:sandstone_stairs");
        ITEM_NAMES.put(129, "minecraft:emerald_ore");
        ITEM_NAMES.put(130, "minecraft:ender_chest");
        ITEM_NAMES.put(131, "minecraft:tripwire_hook");
        ITEM_NAMES.put(133, "minecraft:emerald_block");
        ITEM_NAMES.put(134, "minecraft:spruce_stairs");
        ITEM_NAMES.put(135, "minecraft:birch_stairs");
        ITEM_NAMES.put(136, "minecraft:jungle_stairs");
        ITEM_NAMES.put(137, "minecraft:command_block");
        ITEM_NAMES.put(138, "minecraft:beacon");
        ITEM_NAMES.put(139, "minecraft:cobblestone_wall");
        ITEM_NAMES.put(141, "minecraft:carrots");
        ITEM_NAMES.put(142, "minecraft:potatoes");
        ITEM_NAMES.put(143, "minecraft:wooden_button");
        ITEM_NAMES.put(145, "minecraft:anvil");
        ITEM_NAMES.put(146, "minecraft:trapped_chest");
        ITEM_NAMES.put(147, "minecraft:light_weighted_pressure_plate");
        ITEM_NAMES.put(148, "minecraft:heavy_weighted_pressure_plate");
        ITEM_NAMES.put(151, "minecraft:daylight_detector");
        ITEM_NAMES.put(152, "minecraft:redstone_block");
        ITEM_NAMES.put(153, "minecraft:quartz_ore");
        ITEM_NAMES.put(154, "minecraft:hopper");
        ITEM_NAMES.put(155, "minecraft:quartz_block");
        ITEM_NAMES.put(156, "minecraft:quartz_stairs");
        ITEM_NAMES.put(157, "minecraft:activator_rail");
        ITEM_NAMES.put(158, "minecraft:dropper");
        ITEM_NAMES.put(159, "minecraft:stained_hardened_clay");
        ITEM_NAMES.put(160, "minecraft:stained_glass_pane");
        ITEM_NAMES.put(161, "minecraft:leaves2");
        ITEM_NAMES.put(162, "minecraft:log2");
        ITEM_NAMES.put(163, "minecraft:acacia_stairs");
        ITEM_NAMES.put(164, "minecraft:dark_oak_stairs");
        ITEM_NAMES.put(170, "minecraft:hay_block");
        ITEM_NAMES.put(171, "minecraft:carpet");
        ITEM_NAMES.put(172, "minecraft:hardened_clay");
        ITEM_NAMES.put(173, "minecraft:coal_block");
        ITEM_NAMES.put(174, "minecraft:packed_ice");
        ITEM_NAMES.put(175, "minecraft:double_plant");
        ITEM_NAMES.put(256, "minecraft:iron_shovel");
        ITEM_NAMES.put(257, "minecraft:iron_pickaxe");
        ITEM_NAMES.put(258, "minecraft:iron_axe");
        ITEM_NAMES.put(259, "minecraft:flint_and_steel");
        ITEM_NAMES.put(260, "minecraft:apple");
        ITEM_NAMES.put(261, "minecraft:bow");
        ITEM_NAMES.put(262, "minecraft:arrow");
        ITEM_NAMES.put(263, "minecraft:coal");
        ITEM_NAMES.put(264, "minecraft:diamond");
        ITEM_NAMES.put(265, "minecraft:iron_ingot");
        ITEM_NAMES.put(266, "minecraft:gold_ingot");
        ITEM_NAMES.put(267, "minecraft:iron_sword");
        ITEM_NAMES.put(268, "minecraft:wooden_sword");
        ITEM_NAMES.put(269, "minecraft:wooden_shovel");
        ITEM_NAMES.put(270, "minecraft:wooden_pickaxe");
        ITEM_NAMES.put(271, "minecraft:wooden_axe");
        ITEM_NAMES.put(272, "minecraft:stone_sword");
        ITEM_NAMES.put(273, "minecraft:stone_shovel");
        ITEM_NAMES.put(274, "minecraft:stone_pickaxe");
        ITEM_NAMES.put(275, "minecraft:stone_axe");
        ITEM_NAMES.put(276, "minecraft:diamond_sword");
        ITEM_NAMES.put(277, "minecraft:diamond_shovel");
        ITEM_NAMES.put(278, "minecraft:diamond_pickaxe");
        ITEM_NAMES.put(279, "minecraft:diamond_axe");
        ITEM_NAMES.put(280, "minecraft:stick");
        ITEM_NAMES.put(281, "minecraft:bowl");
        ITEM_NAMES.put(282, "minecraft:mushroom_stew");
        ITEM_NAMES.put(283, "minecraft:golden_sword");
        ITEM_NAMES.put(284, "minecraft:golden_shovel");
        ITEM_NAMES.put(285, "minecraft:golden_pickaxe");
        ITEM_NAMES.put(286, "minecraft:golden_axe");
        ITEM_NAMES.put(287, "minecraft:string");
        ITEM_NAMES.put(288, "minecraft:feather");
        ITEM_NAMES.put(289, "minecraft:gunpowder");
        ITEM_NAMES.put(290, "minecraft:wooden_hoe");
        ITEM_NAMES.put(291, "minecraft:stone_hoe");
        ITEM_NAMES.put(292, "minecraft:iron_hoe");
        ITEM_NAMES.put(293, "minecraft:diamond_hoe");
        ITEM_NAMES.put(294, "minecraft:golden_hoe");
        ITEM_NAMES.put(295, "minecraft:wheat_seeds");
        ITEM_NAMES.put(296, "minecraft:wheat");
        ITEM_NAMES.put(297, "minecraft:bread");
        ITEM_NAMES.put(298, "minecraft:leather_helmet");
        ITEM_NAMES.put(299, "minecraft:leather_chestplate");
        ITEM_NAMES.put(300, "minecraft:leather_leggings");
        ITEM_NAMES.put(301, "minecraft:leather_boots");
        ITEM_NAMES.put(302, "minecraft:chainmail_helmet");
        ITEM_NAMES.put(303, "minecraft:chainmail_chestplate");
        ITEM_NAMES.put(304, "minecraft:chainmail_leggings");
        ITEM_NAMES.put(305, "minecraft:chainmail_boots");
        ITEM_NAMES.put(306, "minecraft:iron_helmet");
        ITEM_NAMES.put(307, "minecraft:iron_chestplate");
        ITEM_NAMES.put(308, "minecraft:iron_leggings");
        ITEM_NAMES.put(309, "minecraft:iron_boots");
        ITEM_NAMES.put(310, "minecraft:diamond_helmet");
        ITEM_NAMES.put(311, "minecraft:diamond_chestplate");
        ITEM_NAMES.put(312, "minecraft:diamond_leggings");
        ITEM_NAMES.put(313, "minecraft:diamond_boots");
        ITEM_NAMES.put(314, "minecraft:golden_helmet");
        ITEM_NAMES.put(315, "minecraft:golden_chestplate");
        ITEM_NAMES.put(316, "minecraft:golden_leggings");
        ITEM_NAMES.put(317, "minecraft:golden_boots");
        ITEM_NAMES.put(318, "minecraft:flint");
        ITEM_NAMES.put(319, "minecraft:porkchop");
        ITEM_NAMES.put(320, "minecraft:cooked_porkchop");
        ITEM_NAMES.put(321, "minecraft:painting");
        ITEM_NAMES.put(322, "minecraft:golden_apple");
        ITEM_NAMES.put(323, "minecraft:sign");
        ITEM_NAMES.put(324, "minecraft:wooden_door");
        ITEM_NAMES.put(325, "minecraft:bucket");
        ITEM_NAMES.put(326, "minecraft:water_bucket");
        ITEM_NAMES.put(327, "minecraft:lava_bucket");
        ITEM_NAMES.put(328, "minecraft:minecart");
        ITEM_NAMES.put(329, "minecraft:saddle");
        ITEM_NAMES.put(330, "minecraft:iron_door");
        ITEM_NAMES.put(331, "minecraft:redstone");
        ITEM_NAMES.put(332, "minecraft:snowball");
        ITEM_NAMES.put(333, "minecraft:boat");
        ITEM_NAMES.put(334, "minecraft:leather");
        ITEM_NAMES.put(335, "minecraft:milk_bucket");
        ITEM_NAMES.put(336, "minecraft:brick");
        ITEM_NAMES.put(337, "minecraft:clay_ball");
        ITEM_NAMES.put(338, "minecraft:reeds");
        ITEM_NAMES.put(339, "minecraft:paper");
        ITEM_NAMES.put(340, "minecraft:book");
        ITEM_NAMES.put(341, "minecraft:slime_ball");
        ITEM_NAMES.put(342, "minecraft:chest_minecart");
        ITEM_NAMES.put(343, "minecraft:furnace_minecart");
        ITEM_NAMES.put(344, "minecraft:egg");
        ITEM_NAMES.put(345, "minecraft:compass");
        ITEM_NAMES.put(346, "minecraft:fishing_rod");
        ITEM_NAMES.put(347, "minecraft:clock");
        ITEM_NAMES.put(348, "minecraft:glowstone_dust");
        ITEM_NAMES.put(349, "minecraft:fish");
        ITEM_NAMES.put(350, "minecraft:cooked_fish"); // Fix typo, the game never recognized cooked_fished
        ITEM_NAMES.put(351, "minecraft:dye");
        ITEM_NAMES.put(352, "minecraft:bone");
        ITEM_NAMES.put(353, "minecraft:sugar");
        ITEM_NAMES.put(354, "minecraft:cake");
        ITEM_NAMES.put(355, "minecraft:bed");
        ITEM_NAMES.put(356, "minecraft:repeater");
        ITEM_NAMES.put(357, "minecraft:cookie");
        ITEM_NAMES.put(358, "minecraft:filled_map");
        ITEM_NAMES.put(359, "minecraft:shears");
        ITEM_NAMES.put(360, "minecraft:melon");
        ITEM_NAMES.put(361, "minecraft:pumpkin_seeds");
        ITEM_NAMES.put(362, "minecraft:melon_seeds");
        ITEM_NAMES.put(363, "minecraft:beef");
        ITEM_NAMES.put(364, "minecraft:cooked_beef");
        ITEM_NAMES.put(365, "minecraft:chicken");
        ITEM_NAMES.put(366, "minecraft:cooked_chicken");
        ITEM_NAMES.put(367, "minecraft:rotten_flesh");
        ITEM_NAMES.put(368, "minecraft:ender_pearl");
        ITEM_NAMES.put(369, "minecraft:blaze_rod");
        ITEM_NAMES.put(370, "minecraft:ghast_tear");
        ITEM_NAMES.put(371, "minecraft:gold_nugget");
        ITEM_NAMES.put(372, "minecraft:nether_wart");
        ITEM_NAMES.put(373, "minecraft:potion");
        ITEM_NAMES.put(374, "minecraft:glass_bottle");
        ITEM_NAMES.put(375, "minecraft:spider_eye");
        ITEM_NAMES.put(376, "minecraft:fermented_spider_eye");
        ITEM_NAMES.put(377, "minecraft:blaze_powder");
        ITEM_NAMES.put(378, "minecraft:magma_cream");
        ITEM_NAMES.put(379, "minecraft:brewing_stand");
        ITEM_NAMES.put(380, "minecraft:cauldron");
        ITEM_NAMES.put(381, "minecraft:ender_eye");
        ITEM_NAMES.put(382, "minecraft:speckled_melon");
        ITEM_NAMES.put(383, "minecraft:spawn_egg");
        ITEM_NAMES.put(384, "minecraft:experience_bottle");
        ITEM_NAMES.put(385, "minecraft:fire_charge");
        ITEM_NAMES.put(386, "minecraft:writable_book");
        ITEM_NAMES.put(387, "minecraft:written_book");
        ITEM_NAMES.put(388, "minecraft:emerald");
        ITEM_NAMES.put(389, "minecraft:item_frame");
        ITEM_NAMES.put(390, "minecraft:flower_pot");
        ITEM_NAMES.put(391, "minecraft:carrot");
        ITEM_NAMES.put(392, "minecraft:potato");
        ITEM_NAMES.put(393, "minecraft:baked_potato");
        ITEM_NAMES.put(394, "minecraft:poisonous_potato");
        ITEM_NAMES.put(395, "minecraft:map");
        ITEM_NAMES.put(396, "minecraft:golden_carrot");
        ITEM_NAMES.put(397, "minecraft:skull");
        ITEM_NAMES.put(398, "minecraft:carrot_on_a_stick");
        ITEM_NAMES.put(399, "minecraft:nether_star");
        ITEM_NAMES.put(400, "minecraft:pumpkin_pie");
        ITEM_NAMES.put(401, "minecraft:fireworks");
        ITEM_NAMES.put(402, "minecraft:firework_charge");
        ITEM_NAMES.put(403, "minecraft:enchanted_book");
        ITEM_NAMES.put(404, "minecraft:comparator");
        ITEM_NAMES.put(405, "minecraft:netherbrick");
        ITEM_NAMES.put(406, "minecraft:quartz");
        ITEM_NAMES.put(407, "minecraft:tnt_minecart");
        ITEM_NAMES.put(408, "minecraft:hopper_minecart");
        ITEM_NAMES.put(417, "minecraft:iron_horse_armor");
        ITEM_NAMES.put(418, "minecraft:golden_horse_armor");
        ITEM_NAMES.put(419, "minecraft:diamond_horse_armor");
        ITEM_NAMES.put(420, "minecraft:lead");
        ITEM_NAMES.put(421, "minecraft:name_tag");
        ITEM_NAMES.put(422, "minecraft:command_block_minecart");
        ITEM_NAMES.put(2256, "minecraft:record_13");
        ITEM_NAMES.put(2257, "minecraft:record_cat");
        ITEM_NAMES.put(2258, "minecraft:record_blocks");
        ITEM_NAMES.put(2259, "minecraft:record_chirp");
        ITEM_NAMES.put(2260, "minecraft:record_far");
        ITEM_NAMES.put(2261, "minecraft:record_mall");
        ITEM_NAMES.put(2262, "minecraft:record_mellohi");
        ITEM_NAMES.put(2263, "minecraft:record_stal");
        ITEM_NAMES.put(2264, "minecraft:record_strad");
        ITEM_NAMES.put(2265, "minecraft:record_ward");
        ITEM_NAMES.put(2266, "minecraft:record_11");
        ITEM_NAMES.put(2267, "minecraft:record_wait");
        // https://github.com/starlis/empirecraft/commit/2da59d1901407fc0c135ef0458c0fe9b016570b3
        // It's likely that this is a result of old CB/Spigot behavior still writing ids into items as ints.
        // These ids do not appear to be used by regular MC anyways, so I do not see the harm of porting it here.
        // Extras can be added if needed
        String[] extra = new String[4_000];
        // EMC start
        extra[409] = "minecraft:prismarine_shard";
        extra[410] = "minecraft:prismarine_crystals";
        extra[411] = "minecraft:rabbit";
        extra[412] = "minecraft:cooked_rabbit";
        extra[413] = "minecraft:rabbit_stew";
        extra[414] = "minecraft:rabbit_foot";
        extra[415] = "minecraft:rabbit_hide";
        extra[416] = "minecraft:armor_stand";
        extra[423] = "minecraft:mutton";
        extra[424] = "minecraft:cooked_mutton";
        extra[425] = "minecraft:banner";
        extra[426] = "minecraft:end_crystal";
        extra[427] = "minecraft:spruce_door";
        extra[428] = "minecraft:birch_door";
        extra[429] = "minecraft:jungle_door";
        extra[430] = "minecraft:acacia_door";
        extra[431] = "minecraft:dark_oak_door";
        extra[432] = "minecraft:chorus_fruit";
        extra[433] = "minecraft:chorus_fruit_popped";
        extra[434] = "minecraft:beetroot";
        extra[435] = "minecraft:beetroot_seeds";
        extra[436] = "minecraft:beetroot_soup";
        extra[437] = "minecraft:dragon_breath";
        extra[438] = "minecraft:splash_potion";
        extra[439] = "minecraft:spectral_arrow";
        extra[440] = "minecraft:tipped_arrow";
        extra[441] = "minecraft:lingering_potion";
        extra[442] = "minecraft:shield";
        extra[443] = "minecraft:elytra";
        extra[444] = "minecraft:spruce_boat";
        extra[445] = "minecraft:birch_boat";
        extra[446] = "minecraft:jungle_boat";
        extra[447] = "minecraft:acacia_boat";
        extra[448] = "minecraft:dark_oak_boat";
        extra[449] = "minecraft:totem_of_undying";
        extra[450] = "minecraft:shulker_shell";
        extra[452] = "minecraft:iron_nugget";
        extra[453] = "minecraft:knowledge_book";
        // EMC end

        // dump extra into map
        for (int i = 0; i < extra.length; ++i) {
            if (extra[i] != null) {
                ITEM_NAMES.put(i, extra[i]);
            }
        }

        // Add block ids into conversion as well
        // Very old versions of the game handled them, but it seems 1.8.8 did not parse them at all, so no conversion
        // was written.
        // block ids are only skipped (set to AIR) if there is no 1-1 replacement item.
        ITEM_NAMES.put(26, "minecraft:bed"); // bed block
        ITEM_NAMES.put(34, ITEM_NAMES.get(0)); // skip (piston head block)
        ITEM_NAMES.put(55, "minecraft:redstone"); // redstone wire block
        ITEM_NAMES.put(59, ITEM_NAMES.get(0)); // skip (wheat crop block)
        ITEM_NAMES.put(63, "minecraft:sign"); // standing sign
        ITEM_NAMES.put(64, "minecraft:wooden_door"); // wooden door block
        ITEM_NAMES.put(68, "minecraft:sign"); // wall sign
        ITEM_NAMES.put(71, "minecraft:iron_door"); // iron door block
        ITEM_NAMES.put(74, "minecraft:redstone_ore"); // lit redstone ore block
        ITEM_NAMES.put(75, "minecraft:redstone_torch"); // unlit redstone torch
        ITEM_NAMES.put(83, "minecraft:reeds"); // sugar cane block
        ITEM_NAMES.put(92, "minecraft:cake"); // cake block
        ITEM_NAMES.put(93, "minecraft:repeater"); // unpowered repeater block
        ITEM_NAMES.put(94, "minecraft:repeater"); // powered repeater block
        ITEM_NAMES.put(104, ITEM_NAMES.get(0)); // skip (pumpkin stem)
        ITEM_NAMES.put(105, ITEM_NAMES.get(0)); // skip (melon stem)
        ITEM_NAMES.put(115, "minecraft:nether_wart"); // nether wart block
        ITEM_NAMES.put(117, "minecraft:brewing_stand"); // brewing stand block
        ITEM_NAMES.put(118, "minecraft:cauldron"); // cauldron block
        ITEM_NAMES.put(124, "minecraft:redstone_lamp"); // lit redstone lamp block
        ITEM_NAMES.put(132, ITEM_NAMES.get(0)); // skip (tripwire wire block)
        ITEM_NAMES.put(140, "minecraft:flower_pot"); // flower pot block
        ITEM_NAMES.put(144, "minecraft:skull"); // skull block
        ITEM_NAMES.put(149, "minecraft:comparator"); // unpowered comparator block
        ITEM_NAMES.put(150, "minecraft:comparator"); // powered comparator block
        // there are technically more, but at some point even older versions pre id -> name conversion didn't even load them.
        // (all I know is 1.7.10 does not load them)
        // and so given even the vanilla game wouldn't load them, there's no conversion path for them - they were never valid.
    }

    private static final String[] POTION_NAMES = new String[128];
    static {
        POTION_NAMES[0] = "minecraft:water";
        POTION_NAMES[1] = "minecraft:regeneration";
        POTION_NAMES[2] = "minecraft:swiftness";
        POTION_NAMES[3] = "minecraft:fire_resistance";
        POTION_NAMES[4] = "minecraft:poison";
        POTION_NAMES[5] = "minecraft:healing";
        POTION_NAMES[6] = "minecraft:night_vision";
        POTION_NAMES[7] = null;
        POTION_NAMES[8] = "minecraft:weakness";
        POTION_NAMES[9] = "minecraft:strength";
        POTION_NAMES[10] = "minecraft:slowness";
        POTION_NAMES[11] = "minecraft:leaping";
        POTION_NAMES[12] = "minecraft:harming";
        POTION_NAMES[13] = "minecraft:water_breathing";
        POTION_NAMES[14] = "minecraft:invisibility";
        POTION_NAMES[15] = null;
        POTION_NAMES[16] = "minecraft:awkward";
        POTION_NAMES[17] = "minecraft:regeneration";
        POTION_NAMES[18] = "minecraft:swiftness";
        POTION_NAMES[19] = "minecraft:fire_resistance";
        POTION_NAMES[20] = "minecraft:poison";
        POTION_NAMES[21] = "minecraft:healing";
        POTION_NAMES[22] = "minecraft:night_vision";
        POTION_NAMES[23] = null;
        POTION_NAMES[24] = "minecraft:weakness";
        POTION_NAMES[25] = "minecraft:strength";
        POTION_NAMES[26] = "minecraft:slowness";
        POTION_NAMES[27] = "minecraft:leaping";
        POTION_NAMES[28] = "minecraft:harming";
        POTION_NAMES[29] = "minecraft:water_breathing";
        POTION_NAMES[30] = "minecraft:invisibility";
        POTION_NAMES[31] = null;
        POTION_NAMES[32] = "minecraft:thick";
        POTION_NAMES[33] = "minecraft:strong_regeneration";
        POTION_NAMES[34] = "minecraft:strong_swiftness";
        POTION_NAMES[35] = "minecraft:fire_resistance";
        POTION_NAMES[36] = "minecraft:strong_poison";
        POTION_NAMES[37] = "minecraft:strong_healing";
        POTION_NAMES[38] = "minecraft:night_vision";
        POTION_NAMES[39] = null;
        POTION_NAMES[40] = "minecraft:weakness";
        POTION_NAMES[41] = "minecraft:strong_strength";
        POTION_NAMES[42] = "minecraft:slowness";
        POTION_NAMES[43] = "minecraft:strong_leaping";
        POTION_NAMES[44] = "minecraft:strong_harming";
        POTION_NAMES[45] = "minecraft:water_breathing";
        POTION_NAMES[46] = "minecraft:invisibility";
        POTION_NAMES[47] = null;
        POTION_NAMES[48] = null;
        POTION_NAMES[49] = "minecraft:strong_regeneration";
        POTION_NAMES[50] = "minecraft:strong_swiftness";
        POTION_NAMES[51] = "minecraft:fire_resistance";
        POTION_NAMES[52] = "minecraft:strong_poison";
        POTION_NAMES[53] = "minecraft:strong_healing";
        POTION_NAMES[54] = "minecraft:night_vision";
        POTION_NAMES[55] = null;
        POTION_NAMES[56] = "minecraft:weakness";
        POTION_NAMES[57] = "minecraft:strong_strength";
        POTION_NAMES[58] = "minecraft:slowness";
        POTION_NAMES[59] = "minecraft:strong_leaping";
        POTION_NAMES[60] = "minecraft:strong_harming";
        POTION_NAMES[61] = "minecraft:water_breathing";
        POTION_NAMES[62] = "minecraft:invisibility";
        POTION_NAMES[63] = null;
        POTION_NAMES[64] = "minecraft:mundane";
        POTION_NAMES[65] = "minecraft:long_regeneration";
        POTION_NAMES[66] = "minecraft:long_swiftness";
        POTION_NAMES[67] = "minecraft:long_fire_resistance";
        POTION_NAMES[68] = "minecraft:long_poison";
        POTION_NAMES[69] = "minecraft:healing";
        POTION_NAMES[70] = "minecraft:long_night_vision";
        POTION_NAMES[71] = null;
        POTION_NAMES[72] = "minecraft:long_weakness";
        POTION_NAMES[73] = "minecraft:long_strength";
        POTION_NAMES[74] = "minecraft:long_slowness";
        POTION_NAMES[75] = "minecraft:long_leaping";
        POTION_NAMES[76] = "minecraft:harming";
        POTION_NAMES[77] = "minecraft:long_water_breathing";
        POTION_NAMES[78] = "minecraft:long_invisibility";
        POTION_NAMES[79] = null;
        POTION_NAMES[80] = "minecraft:awkward";
        POTION_NAMES[81] = "minecraft:long_regeneration";
        POTION_NAMES[82] = "minecraft:long_swiftness";
        POTION_NAMES[83] = "minecraft:long_fire_resistance";
        POTION_NAMES[84] = "minecraft:long_poison";
        POTION_NAMES[85] = "minecraft:healing";
        POTION_NAMES[86] = "minecraft:long_night_vision";
        POTION_NAMES[87] = null;
        POTION_NAMES[88] = "minecraft:long_weakness";
        POTION_NAMES[89] = "minecraft:long_strength";
        POTION_NAMES[90] = "minecraft:long_slowness";
        POTION_NAMES[91] = "minecraft:long_leaping";
        POTION_NAMES[92] = "minecraft:harming";
        POTION_NAMES[93] = "minecraft:long_water_breathing";
        POTION_NAMES[94] = "minecraft:long_invisibility";
        POTION_NAMES[95] = null;
        POTION_NAMES[96] = "minecraft:thick";
        POTION_NAMES[97] = "minecraft:regeneration";
        POTION_NAMES[98] = "minecraft:swiftness";
        POTION_NAMES[99] = "minecraft:long_fire_resistance";
        POTION_NAMES[100] = "minecraft:poison";
        POTION_NAMES[101] = "minecraft:strong_healing";
        POTION_NAMES[102] = "minecraft:long_night_vision";
        POTION_NAMES[103] = null;
        POTION_NAMES[104] = "minecraft:long_weakness";
        POTION_NAMES[105] = "minecraft:strength";
        POTION_NAMES[106] = "minecraft:long_slowness";
        POTION_NAMES[107] = "minecraft:leaping";
        POTION_NAMES[108] = "minecraft:strong_harming";
        POTION_NAMES[109] = "minecraft:long_water_breathing";
        POTION_NAMES[110] = "minecraft:long_invisibility";
        POTION_NAMES[111] = null;
        POTION_NAMES[112] = null;
        POTION_NAMES[113] = "minecraft:regeneration";
        POTION_NAMES[114] = "minecraft:swiftness";
        POTION_NAMES[115] = "minecraft:long_fire_resistance";
        POTION_NAMES[116] = "minecraft:poison";
        POTION_NAMES[117] = "minecraft:strong_healing";
        POTION_NAMES[118] = "minecraft:long_night_vision";
        POTION_NAMES[119] = null;
        POTION_NAMES[120] = "minecraft:long_weakness";
        POTION_NAMES[121] = "minecraft:strength";
        POTION_NAMES[122] = "minecraft:long_slowness";
        POTION_NAMES[123] = "minecraft:leaping";
        POTION_NAMES[124] = "minecraft:strong_harming";
        POTION_NAMES[125] = "minecraft:long_water_breathing";
        POTION_NAMES[126] = "minecraft:long_invisibility";
        POTION_NAMES[127] = null;
    }

    // ret is nullable, you are supposed to log when it does not exist, NOT HIDE IT!
    public static String getNameFromId(final int id) {
        return ITEM_NAMES.get(id);
    }

    public static String getPotionNameFromId(final short id) {
        return POTION_NAMES[id & 127];
    }
}
