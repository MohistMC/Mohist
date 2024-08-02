package ca.spottedleaf.dataconverter.minecraft.converters.entity;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.HelperBlockFlatteningV1450;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

import java.util.HashMap;
import java.util.Map;

public final class ConverterFlattenEntity extends DataConverter<MapType<String>, MapType<String>> {

    private static final Map<String, Integer> BLOCK_NAME_TO_ID = new HashMap<>();
    static {
        BLOCK_NAME_TO_ID.put("minecraft:air", 0);
        BLOCK_NAME_TO_ID.put("minecraft:stone", 1);
        BLOCK_NAME_TO_ID.put("minecraft:grass", 2);
        BLOCK_NAME_TO_ID.put("minecraft:dirt", 3);
        BLOCK_NAME_TO_ID.put("minecraft:cobblestone", 4);
        BLOCK_NAME_TO_ID.put("minecraft:planks", 5);
        BLOCK_NAME_TO_ID.put("minecraft:sapling", 6);
        BLOCK_NAME_TO_ID.put("minecraft:bedrock", 7);
        BLOCK_NAME_TO_ID.put("minecraft:flowing_water", 8);
        BLOCK_NAME_TO_ID.put("minecraft:water", 9);
        BLOCK_NAME_TO_ID.put("minecraft:flowing_lava", 10);
        BLOCK_NAME_TO_ID.put("minecraft:lava", 11);
        BLOCK_NAME_TO_ID.put("minecraft:sand", 12);
        BLOCK_NAME_TO_ID.put("minecraft:gravel", 13);
        BLOCK_NAME_TO_ID.put("minecraft:gold_ore", 14);
        BLOCK_NAME_TO_ID.put("minecraft:iron_ore", 15);
        BLOCK_NAME_TO_ID.put("minecraft:coal_ore", 16);
        BLOCK_NAME_TO_ID.put("minecraft:log", 17);
        BLOCK_NAME_TO_ID.put("minecraft:leaves", 18);
        BLOCK_NAME_TO_ID.put("minecraft:sponge", 19);
        BLOCK_NAME_TO_ID.put("minecraft:glass", 20);
        BLOCK_NAME_TO_ID.put("minecraft:lapis_ore", 21);
        BLOCK_NAME_TO_ID.put("minecraft:lapis_block", 22);
        BLOCK_NAME_TO_ID.put("minecraft:dispenser", 23);
        BLOCK_NAME_TO_ID.put("minecraft:sandstone", 24);
        BLOCK_NAME_TO_ID.put("minecraft:noteblock", 25);
        BLOCK_NAME_TO_ID.put("minecraft:bed", 26);
        BLOCK_NAME_TO_ID.put("minecraft:golden_rail", 27);
        BLOCK_NAME_TO_ID.put("minecraft:detector_rail", 28);
        BLOCK_NAME_TO_ID.put("minecraft:sticky_piston", 29);
        BLOCK_NAME_TO_ID.put("minecraft:web", 30);
        BLOCK_NAME_TO_ID.put("minecraft:tallgrass", 31);
        BLOCK_NAME_TO_ID.put("minecraft:deadbush", 32);
        BLOCK_NAME_TO_ID.put("minecraft:piston", 33);
        BLOCK_NAME_TO_ID.put("minecraft:piston_head", 34);
        BLOCK_NAME_TO_ID.put("minecraft:wool", 35);
        BLOCK_NAME_TO_ID.put("minecraft:piston_extension", 36);
        BLOCK_NAME_TO_ID.put("minecraft:yellow_flower", 37);
        BLOCK_NAME_TO_ID.put("minecraft:red_flower", 38);
        BLOCK_NAME_TO_ID.put("minecraft:brown_mushroom", 39);
        BLOCK_NAME_TO_ID.put("minecraft:red_mushroom", 40);
        BLOCK_NAME_TO_ID.put("minecraft:gold_block", 41);
        BLOCK_NAME_TO_ID.put("minecraft:iron_block", 42);
        BLOCK_NAME_TO_ID.put("minecraft:double_stone_slab", 43);
        BLOCK_NAME_TO_ID.put("minecraft:stone_slab", 44);
        BLOCK_NAME_TO_ID.put("minecraft:brick_block", 45);
        BLOCK_NAME_TO_ID.put("minecraft:tnt", 46);
        BLOCK_NAME_TO_ID.put("minecraft:bookshelf", 47);
        BLOCK_NAME_TO_ID.put("minecraft:mossy_cobblestone", 48);
        BLOCK_NAME_TO_ID.put("minecraft:obsidian", 49);
        BLOCK_NAME_TO_ID.put("minecraft:torch", 50);
        BLOCK_NAME_TO_ID.put("minecraft:fire", 51);
        BLOCK_NAME_TO_ID.put("minecraft:mob_spawner", 52);
        BLOCK_NAME_TO_ID.put("minecraft:oak_stairs", 53);
        BLOCK_NAME_TO_ID.put("minecraft:chest", 54);
        BLOCK_NAME_TO_ID.put("minecraft:redstone_wire", 55);
        BLOCK_NAME_TO_ID.put("minecraft:diamond_ore", 56);
        BLOCK_NAME_TO_ID.put("minecraft:diamond_block", 57);
        BLOCK_NAME_TO_ID.put("minecraft:crafting_table", 58);
        BLOCK_NAME_TO_ID.put("minecraft:wheat", 59);
        BLOCK_NAME_TO_ID.put("minecraft:farmland", 60);
        BLOCK_NAME_TO_ID.put("minecraft:furnace", 61);
        BLOCK_NAME_TO_ID.put("minecraft:lit_furnace", 62);
        BLOCK_NAME_TO_ID.put("minecraft:standing_sign", 63);
        BLOCK_NAME_TO_ID.put("minecraft:wooden_door", 64);
        BLOCK_NAME_TO_ID.put("minecraft:ladder", 65);
        BLOCK_NAME_TO_ID.put("minecraft:rail", 66);
        BLOCK_NAME_TO_ID.put("minecraft:stone_stairs", 67);
        BLOCK_NAME_TO_ID.put("minecraft:wall_sign", 68);
        BLOCK_NAME_TO_ID.put("minecraft:lever", 69);
        BLOCK_NAME_TO_ID.put("minecraft:stone_pressure_plate", 70);
        BLOCK_NAME_TO_ID.put("minecraft:iron_door", 71);
        BLOCK_NAME_TO_ID.put("minecraft:wooden_pressure_plate", 72);
        BLOCK_NAME_TO_ID.put("minecraft:redstone_ore", 73);
        BLOCK_NAME_TO_ID.put("minecraft:lit_redstone_ore", 74);
        BLOCK_NAME_TO_ID.put("minecraft:unlit_redstone_torch", 75);
        BLOCK_NAME_TO_ID.put("minecraft:redstone_torch", 76);
        BLOCK_NAME_TO_ID.put("minecraft:stone_button", 77);
        BLOCK_NAME_TO_ID.put("minecraft:snow_layer", 78);
        BLOCK_NAME_TO_ID.put("minecraft:ice", 79);
        BLOCK_NAME_TO_ID.put("minecraft:snow", 80);
        BLOCK_NAME_TO_ID.put("minecraft:cactus", 81);
        BLOCK_NAME_TO_ID.put("minecraft:clay", 82);
        BLOCK_NAME_TO_ID.put("minecraft:reeds", 83);
        BLOCK_NAME_TO_ID.put("minecraft:jukebox", 84);
        BLOCK_NAME_TO_ID.put("minecraft:fence", 85);
        BLOCK_NAME_TO_ID.put("minecraft:pumpkin", 86);
        BLOCK_NAME_TO_ID.put("minecraft:netherrack", 87);
        BLOCK_NAME_TO_ID.put("minecraft:soul_sand", 88);
        BLOCK_NAME_TO_ID.put("minecraft:glowstone", 89);
        BLOCK_NAME_TO_ID.put("minecraft:portal", 90);
        BLOCK_NAME_TO_ID.put("minecraft:lit_pumpkin", 91);
        BLOCK_NAME_TO_ID.put("minecraft:cake", 92);
        BLOCK_NAME_TO_ID.put("minecraft:unpowered_repeater", 93);
        BLOCK_NAME_TO_ID.put("minecraft:powered_repeater", 94);
        BLOCK_NAME_TO_ID.put("minecraft:stained_glass", 95);
        BLOCK_NAME_TO_ID.put("minecraft:trapdoor", 96);
        BLOCK_NAME_TO_ID.put("minecraft:monster_egg", 97);
        BLOCK_NAME_TO_ID.put("minecraft:stonebrick", 98);
        BLOCK_NAME_TO_ID.put("minecraft:brown_mushroom_block", 99);
        BLOCK_NAME_TO_ID.put("minecraft:red_mushroom_block", 100);
        BLOCK_NAME_TO_ID.put("minecraft:iron_bars", 101);
        BLOCK_NAME_TO_ID.put("minecraft:glass_pane", 102);
        BLOCK_NAME_TO_ID.put("minecraft:melon_block", 103);
        BLOCK_NAME_TO_ID.put("minecraft:pumpkin_stem", 104);
        BLOCK_NAME_TO_ID.put("minecraft:melon_stem", 105);
        BLOCK_NAME_TO_ID.put("minecraft:vine", 106);
        BLOCK_NAME_TO_ID.put("minecraft:fence_gate", 107);
        BLOCK_NAME_TO_ID.put("minecraft:brick_stairs", 108);
        BLOCK_NAME_TO_ID.put("minecraft:stone_brick_stairs", 109);
        BLOCK_NAME_TO_ID.put("minecraft:mycelium", 110);
        BLOCK_NAME_TO_ID.put("minecraft:waterlily", 111);
        BLOCK_NAME_TO_ID.put("minecraft:nether_brick", 112);
        BLOCK_NAME_TO_ID.put("minecraft:nether_brick_fence", 113);
        BLOCK_NAME_TO_ID.put("minecraft:nether_brick_stairs", 114);
        BLOCK_NAME_TO_ID.put("minecraft:nether_wart", 115);
        BLOCK_NAME_TO_ID.put("minecraft:enchanting_table", 116);
        BLOCK_NAME_TO_ID.put("minecraft:brewing_stand", 117);
        BLOCK_NAME_TO_ID.put("minecraft:cauldron", 118);
        BLOCK_NAME_TO_ID.put("minecraft:end_portal", 119);
        BLOCK_NAME_TO_ID.put("minecraft:end_portal_frame", 120);
        BLOCK_NAME_TO_ID.put("minecraft:end_stone", 121);
        BLOCK_NAME_TO_ID.put("minecraft:dragon_egg", 122);
        BLOCK_NAME_TO_ID.put("minecraft:redstone_lamp", 123);
        BLOCK_NAME_TO_ID.put("minecraft:lit_redstone_lamp", 124);
        BLOCK_NAME_TO_ID.put("minecraft:double_wooden_slab", 125);
        BLOCK_NAME_TO_ID.put("minecraft:wooden_slab", 126);
        BLOCK_NAME_TO_ID.put("minecraft:cocoa", 127);
        BLOCK_NAME_TO_ID.put("minecraft:sandstone_stairs", 128);
        BLOCK_NAME_TO_ID.put("minecraft:emerald_ore", 129);
        BLOCK_NAME_TO_ID.put("minecraft:ender_chest", 130);
        BLOCK_NAME_TO_ID.put("minecraft:tripwire_hook", 131);
        BLOCK_NAME_TO_ID.put("minecraft:tripwire", 132);
        BLOCK_NAME_TO_ID.put("minecraft:emerald_block", 133);
        BLOCK_NAME_TO_ID.put("minecraft:spruce_stairs", 134);
        BLOCK_NAME_TO_ID.put("minecraft:birch_stairs", 135);
        BLOCK_NAME_TO_ID.put("minecraft:jungle_stairs", 136);
        BLOCK_NAME_TO_ID.put("minecraft:command_block", 137);
        BLOCK_NAME_TO_ID.put("minecraft:beacon", 138);
        BLOCK_NAME_TO_ID.put("minecraft:cobblestone_wall", 139);
        BLOCK_NAME_TO_ID.put("minecraft:flower_pot", 140);
        BLOCK_NAME_TO_ID.put("minecraft:carrots", 141);
        BLOCK_NAME_TO_ID.put("minecraft:potatoes", 142);
        BLOCK_NAME_TO_ID.put("minecraft:wooden_button", 143);
        BLOCK_NAME_TO_ID.put("minecraft:skull", 144);
        BLOCK_NAME_TO_ID.put("minecraft:anvil", 145);
        BLOCK_NAME_TO_ID.put("minecraft:trapped_chest", 146);
        BLOCK_NAME_TO_ID.put("minecraft:light_weighted_pressure_plate", 147);
        BLOCK_NAME_TO_ID.put("minecraft:heavy_weighted_pressure_plate", 148);
        BLOCK_NAME_TO_ID.put("minecraft:unpowered_comparator", 149);
        BLOCK_NAME_TO_ID.put("minecraft:powered_comparator", 150);
        BLOCK_NAME_TO_ID.put("minecraft:daylight_detector", 151);
        BLOCK_NAME_TO_ID.put("minecraft:redstone_block", 152);
        BLOCK_NAME_TO_ID.put("minecraft:quartz_ore", 153);
        BLOCK_NAME_TO_ID.put("minecraft:hopper", 154);
        BLOCK_NAME_TO_ID.put("minecraft:quartz_block", 155);
        BLOCK_NAME_TO_ID.put("minecraft:quartz_stairs", 156);
        BLOCK_NAME_TO_ID.put("minecraft:activator_rail", 157);
        BLOCK_NAME_TO_ID.put("minecraft:dropper", 158);
        BLOCK_NAME_TO_ID.put("minecraft:stained_hardened_clay", 159);
        BLOCK_NAME_TO_ID.put("minecraft:stained_glass_pane", 160);
        BLOCK_NAME_TO_ID.put("minecraft:leaves2", 161);
        BLOCK_NAME_TO_ID.put("minecraft:log2", 162);
        BLOCK_NAME_TO_ID.put("minecraft:acacia_stairs", 163);
        BLOCK_NAME_TO_ID.put("minecraft:dark_oak_stairs", 164);
        BLOCK_NAME_TO_ID.put("minecraft:slime", 165);
        BLOCK_NAME_TO_ID.put("minecraft:barrier", 166);
        BLOCK_NAME_TO_ID.put("minecraft:iron_trapdoor", 167);
        BLOCK_NAME_TO_ID.put("minecraft:prismarine", 168);
        BLOCK_NAME_TO_ID.put("minecraft:sea_lantern", 169);
        BLOCK_NAME_TO_ID.put("minecraft:hay_block", 170);
        BLOCK_NAME_TO_ID.put("minecraft:carpet", 171);
        BLOCK_NAME_TO_ID.put("minecraft:hardened_clay", 172);
        BLOCK_NAME_TO_ID.put("minecraft:coal_block", 173);
        BLOCK_NAME_TO_ID.put("minecraft:packed_ice", 174);
        BLOCK_NAME_TO_ID.put("minecraft:double_plant", 175);
        BLOCK_NAME_TO_ID.put("minecraft:standing_banner", 176);
        BLOCK_NAME_TO_ID.put("minecraft:wall_banner", 177);
        BLOCK_NAME_TO_ID.put("minecraft:daylight_detector_inverted", 178);
        BLOCK_NAME_TO_ID.put("minecraft:red_sandstone", 179);
        BLOCK_NAME_TO_ID.put("minecraft:red_sandstone_stairs", 180);
        BLOCK_NAME_TO_ID.put("minecraft:double_stone_slab2", 181);
        BLOCK_NAME_TO_ID.put("minecraft:stone_slab2", 182);
        BLOCK_NAME_TO_ID.put("minecraft:spruce_fence_gate", 183);
        BLOCK_NAME_TO_ID.put("minecraft:birch_fence_gate", 184);
        BLOCK_NAME_TO_ID.put("minecraft:jungle_fence_gate", 185);
        BLOCK_NAME_TO_ID.put("minecraft:dark_oak_fence_gate", 186);
        BLOCK_NAME_TO_ID.put("minecraft:acacia_fence_gate", 187);
        BLOCK_NAME_TO_ID.put("minecraft:spruce_fence", 188);
        BLOCK_NAME_TO_ID.put("minecraft:birch_fence", 189);
        BLOCK_NAME_TO_ID.put("minecraft:jungle_fence", 190);
        BLOCK_NAME_TO_ID.put("minecraft:dark_oak_fence", 191);
        BLOCK_NAME_TO_ID.put("minecraft:acacia_fence", 192);
        BLOCK_NAME_TO_ID.put("minecraft:spruce_door", 193);
        BLOCK_NAME_TO_ID.put("minecraft:birch_door", 194);
        BLOCK_NAME_TO_ID.put("minecraft:jungle_door", 195);
        BLOCK_NAME_TO_ID.put("minecraft:acacia_door", 196);
        BLOCK_NAME_TO_ID.put("minecraft:dark_oak_door", 197);
        BLOCK_NAME_TO_ID.put("minecraft:end_rod", 198);
        BLOCK_NAME_TO_ID.put("minecraft:chorus_plant", 199);
        BLOCK_NAME_TO_ID.put("minecraft:chorus_flower", 200);
        BLOCK_NAME_TO_ID.put("minecraft:purpur_block", 201);
        BLOCK_NAME_TO_ID.put("minecraft:purpur_pillar", 202);
        BLOCK_NAME_TO_ID.put("minecraft:purpur_stairs", 203);
        BLOCK_NAME_TO_ID.put("minecraft:purpur_double_slab", 204);
        BLOCK_NAME_TO_ID.put("minecraft:purpur_slab", 205);
        BLOCK_NAME_TO_ID.put("minecraft:end_bricks", 206);
        BLOCK_NAME_TO_ID.put("minecraft:beetroots", 207);
        BLOCK_NAME_TO_ID.put("minecraft:grass_path", 208);
        BLOCK_NAME_TO_ID.put("minecraft:end_gateway", 209);
        BLOCK_NAME_TO_ID.put("minecraft:repeating_command_block", 210);
        BLOCK_NAME_TO_ID.put("minecraft:chain_command_block", 211);
        BLOCK_NAME_TO_ID.put("minecraft:frosted_ice", 212);
        BLOCK_NAME_TO_ID.put("minecraft:magma", 213);
        BLOCK_NAME_TO_ID.put("minecraft:nether_wart_block", 214);
        BLOCK_NAME_TO_ID.put("minecraft:red_nether_brick", 215);
        BLOCK_NAME_TO_ID.put("minecraft:bone_block", 216);
        BLOCK_NAME_TO_ID.put("minecraft:structure_void", 217);
        BLOCK_NAME_TO_ID.put("minecraft:observer", 218);
        BLOCK_NAME_TO_ID.put("minecraft:white_shulker_box", 219);
        BLOCK_NAME_TO_ID.put("minecraft:orange_shulker_box", 220);
        BLOCK_NAME_TO_ID.put("minecraft:magenta_shulker_box", 221);
        BLOCK_NAME_TO_ID.put("minecraft:light_blue_shulker_box", 222);
        BLOCK_NAME_TO_ID.put("minecraft:yellow_shulker_box", 223);
        BLOCK_NAME_TO_ID.put("minecraft:lime_shulker_box", 224);
        BLOCK_NAME_TO_ID.put("minecraft:pink_shulker_box", 225);
        BLOCK_NAME_TO_ID.put("minecraft:gray_shulker_box", 226);
        BLOCK_NAME_TO_ID.put("minecraft:silver_shulker_box", 227);
        BLOCK_NAME_TO_ID.put("minecraft:cyan_shulker_box", 228);
        BLOCK_NAME_TO_ID.put("minecraft:purple_shulker_box", 229);
        BLOCK_NAME_TO_ID.put("minecraft:blue_shulker_box", 230);
        BLOCK_NAME_TO_ID.put("minecraft:brown_shulker_box", 231);
        BLOCK_NAME_TO_ID.put("minecraft:green_shulker_box", 232);
        BLOCK_NAME_TO_ID.put("minecraft:red_shulker_box", 233);
        BLOCK_NAME_TO_ID.put("minecraft:black_shulker_box", 234);
        BLOCK_NAME_TO_ID.put("minecraft:white_glazed_terracotta", 235);
        BLOCK_NAME_TO_ID.put("minecraft:orange_glazed_terracotta", 236);
        BLOCK_NAME_TO_ID.put("minecraft:magenta_glazed_terracotta", 237);
        BLOCK_NAME_TO_ID.put("minecraft:light_blue_glazed_terracotta", 238);
        BLOCK_NAME_TO_ID.put("minecraft:yellow_glazed_terracotta", 239);
        BLOCK_NAME_TO_ID.put("minecraft:lime_glazed_terracotta", 240);
        BLOCK_NAME_TO_ID.put("minecraft:pink_glazed_terracotta", 241);
        BLOCK_NAME_TO_ID.put("minecraft:gray_glazed_terracotta", 242);
        BLOCK_NAME_TO_ID.put("minecraft:silver_glazed_terracotta", 243);
        BLOCK_NAME_TO_ID.put("minecraft:cyan_glazed_terracotta", 244);
        BLOCK_NAME_TO_ID.put("minecraft:purple_glazed_terracotta", 245);
        BLOCK_NAME_TO_ID.put("minecraft:blue_glazed_terracotta", 246);
        BLOCK_NAME_TO_ID.put("minecraft:brown_glazed_terracotta", 247);
        BLOCK_NAME_TO_ID.put("minecraft:green_glazed_terracotta", 248);
        BLOCK_NAME_TO_ID.put("minecraft:red_glazed_terracotta", 249);
        BLOCK_NAME_TO_ID.put("minecraft:black_glazed_terracotta", 250);
        BLOCK_NAME_TO_ID.put("minecraft:concrete", 251);
        BLOCK_NAME_TO_ID.put("minecraft:concrete_powder", 252);
        BLOCK_NAME_TO_ID.put("minecraft:structure_block", 255);
    }

    protected static final int VERSION = MCVersions.V17W47A;

    protected final String[] paths;

    public ConverterFlattenEntity(final String... paths) {
        super(VERSION, 3);
        this.paths = paths;
    }

    private static void register(final String id, final String... paths) {
        MCTypeRegistry.ENTITY.addConverterForId(id, new ConverterFlattenEntity(paths));
    }

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:falling_block", new DataConverter<>(VERSION, 3) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int blockId;
                if (data.hasKey("Block")) {
                    final Number id = data.getNumber("Block");
                    if (id != null) {
                        blockId = id.intValue();
                    } else {
                        blockId = getBlockId(data.getString("Block"));
                    }
                } else {
                    final Number tileId = data.getNumber("TileID");
                    if (tileId != null) {
                        blockId = tileId.intValue();
                    } else {
                        blockId = data.getByte("Tile") & 255;
                    }
                }

                final int blockData = data.getInt("Data") & 15;

                data.remove("Block"); // from type update
                data.remove("Data");
                data.remove("TileID");
                data.remove("Tile");

                // key is from type update
                data.setMap("BlockState", HelperBlockFlatteningV1450.getNBTForId((blockId << 4) | blockData).copy()); // copy to avoid problems with later state datafixers

                return null;
            }
        });
        register("minecraft:enderman", "carried", "carriedData", "carriedBlockState");
        register("minecraft:arrow", "inTile", "inData", "inBlockState");
        register("minecraft:spectral_arrow", "inTile", "inData", "inBlockState");
        register("minecraft:egg", "inTile");
        register("minecraft:ender_pearl", "inTile");
        register("minecraft:fireball", "inTile");
        register("minecraft:potion", "inTile");
        register("minecraft:small_fireball", "inTile");
        register("minecraft:snowball", "inTile");
        register("minecraft:wither_skull", "inTile");
        register("minecraft:xp_bottle", "inTile");
        register("minecraft:commandblock_minecart", "DisplayTile", "DisplayData", "DisplayState");
        register("minecraft:minecart", "DisplayTile", "DisplayData", "DisplayState");
        register("minecraft:chest_minecart", "DisplayTile", "DisplayData", "DisplayState");
        register("minecraft:furnace_minecart", "DisplayTile", "DisplayData", "DisplayState");
        register("minecraft:tnt_minecart", "DisplayTile", "DisplayData", "DisplayState");
        register("minecraft:hopper_minecart", "DisplayTile", "DisplayData", "DisplayState");
        register("minecraft:spawner_minecart", "DisplayTile", "DisplayData", "DisplayState");
    }

    public static int getBlockId(final String block) {
        final Integer ret = BLOCK_NAME_TO_ID.get(block);
        return ret == null ? 0 : ret.intValue();
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        if (this.paths.length == 1) {
            data.remove(this.paths[0]);
            return null;
        }
        final String idPath = this.paths[0];
        final String dataPath = this.paths[1];
        final String outputStatePath = this.paths[2];

        final int blockId;
        if (data.hasKey(idPath, ObjectType.NUMBER)) {
            blockId = data.getInt(idPath);
        } else {
            blockId = getBlockId(data.getString(idPath));
        }

        final int blockData = data.getInt(dataPath) & 15;

        data.remove(idPath); // from type update
        data.remove(dataPath);

        data.setMap(outputStatePath, HelperBlockFlatteningV1450.getNBTForId((blockId << 4) | blockData).copy()); // copy to avoid problems with later state datafixers

        return null;
    }
}
