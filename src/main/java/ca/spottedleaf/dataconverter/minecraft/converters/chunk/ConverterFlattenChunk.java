package ca.spottedleaf.dataconverter.minecraft.converters.chunk;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.HelperBlockFlatteningV1450;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.HelperItemNameV102;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import net.minecraft.util.datafix.PackedBitStorage;
import org.slf4j.Logger;


import static it.unimi.dsi.fastutil.HashCommon.arraySize;

public final class ConverterFlattenChunk extends DataConverter<MapType<String>, MapType<String>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    static final BitSet VIRTUAL_SET = new BitSet(256);
    static final BitSet IDS_NEEDING_FIX_SET = new BitSet(256);

    static {
        IDS_NEEDING_FIX_SET.set(2);
        IDS_NEEDING_FIX_SET.set(3);
        IDS_NEEDING_FIX_SET.set(110);
        IDS_NEEDING_FIX_SET.set(140);
        IDS_NEEDING_FIX_SET.set(144);
        IDS_NEEDING_FIX_SET.set(25);
        IDS_NEEDING_FIX_SET.set(86);
        IDS_NEEDING_FIX_SET.set(26);
        IDS_NEEDING_FIX_SET.set(176);
        IDS_NEEDING_FIX_SET.set(177);
        IDS_NEEDING_FIX_SET.set(175);
        IDS_NEEDING_FIX_SET.set(64);
        IDS_NEEDING_FIX_SET.set(71);
        IDS_NEEDING_FIX_SET.set(193);
        IDS_NEEDING_FIX_SET.set(194);
        IDS_NEEDING_FIX_SET.set(195);
        IDS_NEEDING_FIX_SET.set(196);
        IDS_NEEDING_FIX_SET.set(197);

        VIRTUAL_SET.set(54);
        VIRTUAL_SET.set(146);
        VIRTUAL_SET.set(25);
        VIRTUAL_SET.set(26);
        VIRTUAL_SET.set(51);
        VIRTUAL_SET.set(53);
        VIRTUAL_SET.set(67);
        VIRTUAL_SET.set(108);
        VIRTUAL_SET.set(109);
        VIRTUAL_SET.set(114);
        VIRTUAL_SET.set(128);
        VIRTUAL_SET.set(134);
        VIRTUAL_SET.set(135);
        VIRTUAL_SET.set(136);
        VIRTUAL_SET.set(156);
        VIRTUAL_SET.set(163);
        VIRTUAL_SET.set(164);
        VIRTUAL_SET.set(180);
        VIRTUAL_SET.set(203);
        VIRTUAL_SET.set(55);
        VIRTUAL_SET.set(85);
        VIRTUAL_SET.set(113);
        VIRTUAL_SET.set(188);
        VIRTUAL_SET.set(189);
        VIRTUAL_SET.set(190);
        VIRTUAL_SET.set(191);
        VIRTUAL_SET.set(192);
        VIRTUAL_SET.set(93);
        VIRTUAL_SET.set(94);
        VIRTUAL_SET.set(101);
        VIRTUAL_SET.set(102);
        VIRTUAL_SET.set(160);
        VIRTUAL_SET.set(106);
        VIRTUAL_SET.set(107);
        VIRTUAL_SET.set(183);
        VIRTUAL_SET.set(184);
        VIRTUAL_SET.set(185);
        VIRTUAL_SET.set(186);
        VIRTUAL_SET.set(187);
        VIRTUAL_SET.set(132);
        VIRTUAL_SET.set(139);
        VIRTUAL_SET.set(199);
    }

    static final boolean[] VIRTUAL = toBooleanArray(VIRTUAL_SET);
    static final boolean[] IDS_NEEDING_FIX = toBooleanArray(IDS_NEEDING_FIX_SET);

    private static boolean[] toBooleanArray(final BitSet set) {
        final boolean[] ret = new boolean[4096];
        for (int i = 0; i < 4096; ++i) {
            ret[i] = set.get(i);
        }

        return ret;
    }

    static final MapType<String> PUMPKIN          = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:pumpkin'}");
    static final MapType<String> SNOWY_PODZOL     = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:podzol',Properties:{snowy:'true'}}");
    static final MapType<String> SNOWY_GRASS      = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:grass_block',Properties:{snowy:'true'}}");
    static final MapType<String> SNOWY_MYCELIUM   = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:mycelium',Properties:{snowy:'true'}}");
    static final MapType<String> UPPER_SUNFLOWER  = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:sunflower',Properties:{half:'upper'}}");
    static final MapType<String> UPPER_LILAC      = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:lilac',Properties:{half:'upper'}}");
    static final MapType<String> UPPER_TALL_GRASS = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:tall_grass',Properties:{half:'upper'}}");
    static final MapType<String> UPPER_LARGE_FERN = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:large_fern',Properties:{half:'upper'}}");
    static final MapType<String> UPPER_ROSE_BUSH  = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:rose_bush',Properties:{half:'upper'}}");
    static final MapType<String> UPPER_PEONY      = HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:peony',Properties:{half:'upper'}}");

    static final Map<String, MapType<String>> FLOWER_POT_MAP = new HashMap<>();
    static {
        FLOWER_POT_MAP.put("minecraft:air0",            HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:flower_pot'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower0",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_poppy'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower1",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_blue_orchid'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower2",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_allium'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower3",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_azure_bluet'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower4",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_red_tulip'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower5",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_orange_tulip'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower6",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_white_tulip'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower7",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_pink_tulip'}"));
        FLOWER_POT_MAP.put("minecraft:red_flower8",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_oxeye_daisy'}"));
        FLOWER_POT_MAP.put("minecraft:yellow_flower0",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_dandelion'}"));
        FLOWER_POT_MAP.put("minecraft:sapling0",        HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_oak_sapling'}"));
        FLOWER_POT_MAP.put("minecraft:sapling1",        HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_spruce_sapling'}"));
        FLOWER_POT_MAP.put("minecraft:sapling2",        HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_birch_sapling'}"));
        FLOWER_POT_MAP.put("minecraft:sapling3",        HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_jungle_sapling'}"));
        FLOWER_POT_MAP.put("minecraft:sapling4",        HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_acacia_sapling'}"));
        FLOWER_POT_MAP.put("minecraft:sapling5",        HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_dark_oak_sapling'}"));
        FLOWER_POT_MAP.put("minecraft:red_mushroom0",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_red_mushroom'}"));
        FLOWER_POT_MAP.put("minecraft:brown_mushroom0", HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_brown_mushroom'}"));
        FLOWER_POT_MAP.put("minecraft:deadbush0",       HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_dead_bush'}"));
        FLOWER_POT_MAP.put("minecraft:tallgrass2",      HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_fern'}"));
        FLOWER_POT_MAP.put("minecraft:cactus0",         HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:potted_cactus'}")); // we change default to empty
    }

    static final Map<String, MapType<String>> SKULL_MAP = new HashMap<>();
    static {
        mapSkull(SKULL_MAP, 0, "skeleton", "skull");
        mapSkull(SKULL_MAP, 1, "wither_skeleton", "skull");
        mapSkull(SKULL_MAP, 2, "zombie", "head");
        mapSkull(SKULL_MAP, 3, "player", "head");
        mapSkull(SKULL_MAP, 4, "creeper", "head");
        mapSkull(SKULL_MAP, 5, "dragon", "head");
    };

    private static void mapSkull(final Map<String, MapType<String>> into, final int oldId, final String newId, final String skullType) {
        into.put(oldId + "north", HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + newId + "_wall_" + skullType + "',Properties:{facing:'north'}}"));
        into.put(oldId + "east",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + newId + "_wall_" + skullType + "',Properties:{facing:'east'}}"));
        into.put(oldId + "south", HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + newId + "_wall_" + skullType + "',Properties:{facing:'south'}}"));
        into.put(oldId + "west",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + newId + "_wall_" + skullType + "',Properties:{facing:'west'}}"));

        for (int rotation = 0; rotation < 16; ++rotation) {
            into.put(oldId + "" + rotation,
                    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + newId + "_" + skullType + "',Properties:{rotation:'" + rotation + "'}}"));
        }
    }

    static final Map<String, MapType<String>> DOOR_MAP = new HashMap<>();
    static {
        mapDoor(DOOR_MAP, "oak_door", 1024);
        mapDoor(DOOR_MAP, "iron_door", 1136);
        mapDoor(DOOR_MAP, "spruce_door", 3088);
        mapDoor(DOOR_MAP, "birch_door", 3104);
        mapDoor(DOOR_MAP, "jungle_door", 3120);
        mapDoor(DOOR_MAP, "acacia_door", 3136);
        mapDoor(DOOR_MAP, "dark_oak_door", 3152);
    };

    private static void mapDoor(final Map<String, MapType<String>> into, final String type, final int oldId) {
        into.put("minecraft:" + type + "eastlowerleftfalsefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "eastlowerleftfalsetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "eastlowerlefttruefalse",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "eastlowerlefttruetrue",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "eastlowerrightfalsefalse",  Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId)));
        into.put("minecraft:" + type + "eastlowerrightfalsetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "eastlowerrighttruefalse",   Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 4)));
        into.put("minecraft:" + type + "eastlowerrighttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "eastupperleftfalsefalse",   Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 8)));
        into.put("minecraft:" + type + "eastupperleftfalsetrue",    Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 10)));
        into.put("minecraft:" + type + "eastupperlefttruefalse",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "eastupperlefttruetrue",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "eastupperrightfalsefalse",  Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 9)));
        into.put("minecraft:" + type + "eastupperrightfalsetrue",   Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 11)));
        into.put("minecraft:" + type + "eastupperrighttruefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "eastupperrighttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "northlowerleftfalsefalse",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "northlowerleftfalsetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "northlowerlefttruefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "northlowerlefttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "northlowerrightfalsefalse", Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 3)));
        into.put("minecraft:" + type + "northlowerrightfalsetrue",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "northlowerrighttruefalse",  Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 7)));
        into.put("minecraft:" + type + "northlowerrighttruetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "northupperleftfalsefalse",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "northupperleftfalsetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "northupperlefttruefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "northupperlefttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "northupperrightfalsefalse", HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "northupperrightfalsetrue",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "northupperrighttruefalse",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "northupperrighttruetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "southlowerleftfalsefalse",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "southlowerleftfalsetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "southlowerlefttruefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "southlowerlefttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "southlowerrightfalsefalse", Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 1)));
        into.put("minecraft:" + type + "southlowerrightfalsetrue",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "southlowerrighttruefalse",  Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 5)));
        into.put("minecraft:" + type + "southlowerrighttruetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "southupperleftfalsefalse",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "southupperleftfalsetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "southupperlefttruefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "southupperlefttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "southupperrightfalsefalse", HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "southupperrightfalsetrue",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "southupperrighttruefalse",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "southupperrighttruetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "westlowerleftfalsefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "westlowerleftfalsetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "westlowerlefttruefalse",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "westlowerlefttruetrue",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "westlowerrightfalsefalse",  Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 2)));
        into.put("minecraft:" + type + "westlowerrightfalsetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "westlowerrighttruefalse",   Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(oldId + 6)));
        into.put("minecraft:" + type + "westlowerrighttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "westupperleftfalsefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "westupperleftfalsetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "westupperlefttruefalse",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "westupperlefttruetrue",     HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        into.put("minecraft:" + type + "westupperrightfalsefalse",  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'false'}}"));
        into.put("minecraft:" + type + "westupperrightfalsetrue",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'true'}}"));
        into.put("minecraft:" + type + "westupperrighttruefalse",   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        into.put("minecraft:" + type + "westupperrighttruetrue",    HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + type + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
    }

    static final Map<String, MapType<String>> NOTE_BLOCK_MAP = new HashMap<>();
    static {
        for(int note = 0; note < 26; ++note) {
            NOTE_BLOCK_MAP.put("true" + note,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:note_block',Properties:{powered:'true',note:'" + note + "'}}"));
            NOTE_BLOCK_MAP.put("false" + note, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:note_block',Properties:{powered:'false',note:'" + note + "'}}"));
        }
    }

    static final Int2ObjectOpenHashMap<String> DYE_COLOR_MAP = new Int2ObjectOpenHashMap<>();
    static {
        DYE_COLOR_MAP.put(0, "white");
        DYE_COLOR_MAP.put(1, "orange");
        DYE_COLOR_MAP.put(2, "magenta");
        DYE_COLOR_MAP.put(3, "light_blue");
        DYE_COLOR_MAP.put(4, "yellow");
        DYE_COLOR_MAP.put(5, "lime");
        DYE_COLOR_MAP.put(6, "pink");
        DYE_COLOR_MAP.put(7, "gray");
        DYE_COLOR_MAP.put(8, "light_gray");
        DYE_COLOR_MAP.put(9, "cyan");
        DYE_COLOR_MAP.put(10, "purple");
        DYE_COLOR_MAP.put(11, "blue");
        DYE_COLOR_MAP.put(12, "brown");
        DYE_COLOR_MAP.put(13, "green");
        DYE_COLOR_MAP.put(14, "red");
        DYE_COLOR_MAP.put(15, "black");
    }

    static final Map<String, MapType<String>> BED_BLOCK_MAP = new HashMap<>();

    static {
        for (final Int2ObjectMap.Entry<String> entry : DYE_COLOR_MAP.int2ObjectEntrySet()) {
            if (!Objects.equals(entry.getValue(), "red")) {
                addBeds(BED_BLOCK_MAP, entry.getIntKey(), entry.getValue());
            }
        }
    }

    private static void addBeds(final Map<String, MapType<String>> into, final int colourId, final String colourName) {
        into.put("southfalsefoot" + colourId, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'south',occupied:'false',part:'foot'}}"));
        into.put("westfalsefoot" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'west',occupied:'false',part:'foot'}}"));
        into.put("northfalsefoot" + colourId, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'north',occupied:'false',part:'foot'}}"));
        into.put("eastfalsefoot" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'east',occupied:'false',part:'foot'}}"));
        into.put("southfalsehead" + colourId, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'south',occupied:'false',part:'head'}}"));
        into.put("westfalsehead" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'west',occupied:'false',part:'head'}}"));
        into.put("northfalsehead" + colourId, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'north',occupied:'false',part:'head'}}"));
        into.put("eastfalsehead" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'east',occupied:'false',part:'head'}}"));
        into.put("southtruehead" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'south',occupied:'true',part:'head'}}"));
        into.put("westtruehead" + colourId,   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'west',occupied:'true',part:'head'}}"));
        into.put("northtruehead" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'north',occupied:'true',part:'head'}}"));
        into.put("easttruehead" + colourId,   HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_bed',Properties:{facing:'east',occupied:'true',part:'head'}}"));
    }

    static final Map<String, MapType<String>> BANNER_BLOCK_MAP = new HashMap<>();

    static {
        for (final Int2ObjectMap.Entry<String> entry : DYE_COLOR_MAP.int2ObjectEntrySet()) {
            if (!Objects.equals(entry.getValue(), "white")) {
                addBanners(BANNER_BLOCK_MAP, 15 - entry.getIntKey(), entry.getValue());
            }
        }
    }

    private static void addBanners(final Map<String, MapType<String>> into, final int colourId, final String colourName) {
        for(int rotation = 0; rotation < 16; ++rotation) {
            into.put("" + rotation + "_" + colourId, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_banner',Properties:{rotation:'" + rotation + "'}}"));
        }

        into.put("north_" + colourId, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_wall_banner',Properties:{facing:'north'}}"));
        into.put("south_" + colourId, HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_wall_banner',Properties:{facing:'south'}}"));
        into.put("west_" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_wall_banner',Properties:{facing:'west'}}"));
        into.put("east_" + colourId,  HelperBlockFlatteningV1450.parseTag("{Name:'minecraft:" + colourName + "_wall_banner',Properties:{facing:'east'}}"));
    }

    static final MapType<String> AIR = Objects.requireNonNull(HelperBlockFlatteningV1450.getNBTForId(0));

    public ConverterFlattenChunk() {
        super(MCVersions.V17W47A, 1);
    }

    static String getName(final MapType<String> blockState) {
        return blockState.getString("Name");
    }

    static String getProperty(final MapType<String> blockState, final String propertyName) {
        final MapType<String> properties = blockState.getMap("Properties");
        if (properties == null) {
            return "";
        }

        return properties.getString(propertyName, "");
    }

    static int getSideMask(final boolean noLeft, final boolean noRight, final boolean noBack, final boolean noForward) {
        if (noBack) {
            if (noRight) {
                return 2;
            } else if (noLeft) {
                return 128;
            } else {
                return 1;
            }
        } else if (noForward) {
            if (noLeft) {
                return 32;
            } else if (noRight) {
                return 8;
            } else {
                return 16;
            }
        } else if (noRight) {
            return 4;
        } else if (noLeft) {
            return 64;
        } else {
            return 0;
        }
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        final MapType<String> level = data.getMap("Level");
        if (level == null) {
            return null;
        }

        if (!level.hasKey("Sections", ObjectType.LIST)) {
            return null;
        }

        data.setMap("Level", new UpgradeChunk(level).writeBackToLevel());

        return null;
    }

    static enum Direction {
        DOWN(AxisDirection.NEGATIVE, Axis.Y),
        UP(AxisDirection.POSITIVE, Axis.Y),
        NORTH(AxisDirection.NEGATIVE, Axis.Z),
        SOUTH(AxisDirection.POSITIVE, Axis.Z),
        WEST(AxisDirection.NEGATIVE, Axis.X),
        EAST(AxisDirection.POSITIVE, Axis.X);

        private final Axis axis;
        private final AxisDirection axisDirection;

        private Direction(final AxisDirection axisDirection, final Axis axis) {
            this.axis = axis;
            this.axisDirection = axisDirection;
        }

        public AxisDirection getAxisDirection() {
            return this.axisDirection;
        }

        public Axis getAxis() {
            return this.axis;
        }

        public static enum AxisDirection {
            POSITIVE(1),
            NEGATIVE(-1);

            private final int step;

            private AxisDirection(final int step) {
                this.step = step;
            }

            public int getStep() {
                return this.step;
            }
        }

        public static enum Axis {
            X, Y, Z;
        }
    }

    static class DataLayer {
        private final byte[] data;

        public DataLayer() {
            this.data = new byte[2048];
        }

        public DataLayer(final byte[] data) {
            this.data = data;
            if (data.length != 2048) {
                throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + data.length);
            }
        }

        public static DataLayer getOrNull(final byte[] data) {
            return data == null ? null : new DataLayer(data);
        }

        public static DataLayer getOrCreate(final byte[] data) {
            return data == null ? new DataLayer() : new DataLayer(data);
        }

        public int get(final int index) {
            final byte value = this.data[index >>> 1];

            // if we are an even index, we want lower 4 bits
            // if we are an odd index, we want upper 4 bits
            return ((value >>> ((index & 1) << 2)) & 0xF);
        }

        public int get(final int x, final int y, final int z) {
            final int index = y << 8 | z << 4 | x;
            final byte value = this.data[index >>> 1];

            // if we are an even index, we want lower 4 bits
            // if we are an odd index, we want upper 4 bits
            return ((value >>> ((index & 1) << 2)) & 0xF);
        }
    }

    static final class UpgradeChunk {
        int sides;

        final Section[] sections = new Section[16];
        final MapType<String> level;
        final int blockX;
        final int blockZ;
        final Int2ObjectLinkedOpenHashMap<MapType<String>> tileEntities = new Int2ObjectLinkedOpenHashMap<>(16);

        public UpgradeChunk(final MapType<String> level) {
            this.level = level;
            this.blockX = level.getInt("xPos") << 4;
            this.blockZ = level.getInt("zPos") << 4;

            final ListType tileEntities = level.getList("TileEntities", ObjectType.MAP);
            if (tileEntities != null) {
                for (int i = 0, len = tileEntities.size(); i < len; ++i) {
                    final MapType<String> tileEntity = tileEntities.getMap(i);

                    final int x = (tileEntity.getInt("x") - this.blockX) & 15;
                    final int y = tileEntity.getInt("y");
                    final int z = (tileEntity.getInt("z") - this.blockZ) & 15;
                    final int index = (y << 8) | (z << 4) | x;
                    if (this.tileEntities.put(index, tileEntity) != null) {
                        LOGGER.warn("In chunk: {}x{} found a duplicate block entity at position (ConverterFlattenChunk): [{}, {}, {}]", this.blockX, this.blockZ, x, y, z);
                    }
                }
            }

            final boolean convertedFromAlphaFormat = level.getBoolean("convertedFromAlphaFormat");
            final ListType sections = level.getList("Sections", ObjectType.MAP);
            if (sections != null) {
                for (int i = 0, len = sections.size(); i < len; ++i) {
                    final MapType<String> sectionData = sections.getMap(i);
                    final Section section = new Section(sectionData);

                    if (section.y < 0 || section.y > 15) {
                        LOGGER.warn("In chunk: {}x{} found an invalid chunk section y (ConverterFlattenChunk): {}", this.blockX, this.blockZ, section.y);
                        continue;
                    }

                    if (this.sections[section.y] != null) {
                        LOGGER.warn("In chunk: {}x{} found a duplicate chunk section (ConverterFlattenChunk): {}", this.blockX, this.blockZ, section.y);
                    }

                    this.sides = section.upgrade(this.sides);
                    this.sections[section.y] = section;
                }
            }

            for (final Section section : this.sections) {
                if (section == null) {
                    continue;
                }

                final int yIndex = section.y << (8 + 4);

                for (final Iterator<Int2ObjectMap.Entry<IntArrayList>> iterator = section.toFix.int2ObjectEntrySet().fastIterator(); iterator.hasNext();) {
                    final Int2ObjectMap.Entry<IntArrayList> fixEntry = iterator.next();
                    final IntIterator positionIterator = fixEntry.getValue().iterator();
                    switch (fixEntry.getIntKey()) {
                        case 2: { // grass block
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> blockState = this.getBlock(position);
                                if (!"minecraft:grass_block".equals(getName(blockState))) {
                                    continue;
                                }

                                final String blockAbove = getName(getBlock(relative(position, Direction.UP)));
                                if ("minecraft:snow".equals(blockAbove) || "minecraft:snow_layer".equals(blockAbove)) {
                                    this.setBlock(position, SNOWY_GRASS);
                                }
                            }
                            break;
                        }
                        case 3: { // dirt
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> blockState = this.getBlock(position);
                                if (!"minecraft:podzol".equals(getName(blockState))) {
                                    continue;
                                }

                                final String blockAbove = getName(getBlock(relative(position, Direction.UP)));
                                if ("minecraft:snow".equals(blockAbove) || "minecraft:snow_layer".equals(blockAbove)) {
                                    this.setBlock(position, SNOWY_PODZOL);
                                }
                            }
                            break;
                        }
                        case 25: { // note block
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> tile = this.removeBlockEntity(position);
                                if (tile != null) {
                                    final String state = Boolean.toString(tile.getBoolean("powered")) + (byte) Math.min(Math.max(tile.getInt("note"), 0), 24);
                                    this.setBlock(position, NOTE_BLOCK_MAP.getOrDefault(state, NOTE_BLOCK_MAP.get("false0")));
                                }
                            }
                            break;
                        }
                        case 26: { // bed
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> tile = this.getBlockEntity(position);

                                if (tile == null) {
                                    continue;
                                }

                                final MapType<String> blockState = this.getBlock(position);

                                final int colour = tile.getInt("color");
                                if (colour != 14 && colour >= 0 && colour < 16) {
                                    final String state = getProperty(blockState, "facing") + getProperty(blockState, "occupied") + getProperty(blockState, "part") + colour;

                                    final MapType<String> update = BED_BLOCK_MAP.get(state);
                                    if (update != null) {
                                        this.setBlock(position, update);
                                    }
                                }
                            }
                            break;
                        }
                        case 64: // oak door
                        case 71: // iron door
                        case 193: // spruce door
                        case 194: // birch door
                        case 195: // jungle door
                        case 196: // acacia door
                        case 197: { // dark oak door
                            // aka the door updater
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> blockState = this.getBlock(position);
                                if (!getName(blockState).endsWith("_door")) {
                                    continue;
                                }

                                if (!"lower".equals(getProperty(blockState, "half"))) {
                                    continue;
                                }

                                final int positionAbove = relative(position, Direction.UP);
                                final MapType<String> blockStateAbove = this.getBlock(positionAbove);

                                final String name = getName(blockState);
                                if (name.equals(getName(blockStateAbove))) {
                                    final String facingBelow = getProperty(blockState, "facing");
                                    final String openBelow = getProperty(blockState, "open");
                                    final String hingeAbove = convertedFromAlphaFormat ? "left" : getProperty(blockStateAbove, "hinge");
                                    final String poweredAbove = convertedFromAlphaFormat ? "false" : getProperty(blockStateAbove, "powered");

                                    this.setBlock(position, DOOR_MAP.get(name + facingBelow + "lower" + hingeAbove + openBelow + poweredAbove));
                                    this.setBlock(positionAbove, DOOR_MAP.get(name + facingBelow + "upper" + hingeAbove + openBelow + poweredAbove));
                                }
                            }
                            break;
                        }
                        case 86: { // pumpkin
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> blockState = this.getBlock(position);

                                // I guess this is some terrible hack to convert carved pumpkins from world gen into
                                // regular pumpkins?

                                if ("minecraft:carved_pumpkin".equals(getName(blockState))) {
                                    final String downName = getName(this.getBlock(relative(position, Direction.DOWN)));
                                    if ("minecraft:grass_block".equals(downName) || "minecraft:dirt".equals(downName)) {
                                        this.setBlock(position, PUMPKIN);
                                    }
                                }
                            }
                            break;
                        }
                        case 110: { // mycelium
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> blockState = this.getBlock(position);
                                if ("minecraft:mycelium".equals(getName(blockState))) {
                                    final String nameAbove = getName(this.getBlock(relative(position, Direction.UP)));
                                    if ("minecraft:snow".equals(nameAbove) || "minecraft:snow_layer".equals(nameAbove)) {
                                        this.setBlock(position, SNOWY_MYCELIUM);
                                    }
                                }
                            }
                            break;
                        }
                        case 140: { // flower pot
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> tile = this.removeBlockEntity(position);
                                if (tile == null) {
                                    continue;
                                }

                                final String item;
                                if (tile.hasKey("Item", ObjectType.NUMBER)) {
                                    // the item name converter should have migrated to number, however no legacy converter
                                    // ever did this. so we can get data with versions above v102 (old worlds, converted prior to DFU)
                                    // that didn't convert. so just do it here.
                                    item = HelperItemNameV102.getNameFromId(tile.getInt("Item"));
                                } else {
                                    item = tile.getString("Item", "");
                                }

                                final String state = item + tile.getInt("Data");
                                this.setBlock(position, FLOWER_POT_MAP.getOrDefault(state, FLOWER_POT_MAP.get("minecraft:air0")));
                            }
                            break;
                        }
                        case 144: { // mob head
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> tile = this.getBlockEntity(position);
                                if (tile == null) {
                                    continue;
                                }

                                final String typeString = Integer.toString(tile.getInt("SkullType"));
                                final String facing = getProperty(this.getBlock(position), "facing");
                                final String state;
                                if (!"up".equals(facing) && !"down".equals(facing)) {
                                    state = typeString + facing;
                                } else {
                                    state = typeString + tile.getInt("Rot");
                                }

                                tile.remove("SkullType");
                                tile.remove("facing");
                                tile.remove("Rot");

                                this.setBlock(position, SKULL_MAP.getOrDefault(state, SKULL_MAP.get("0north")));
                            }
                            break;
                        }
                        case 175: { // sunflower
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> blockState = this.getBlock(position);
                                if (!"upper".equals(getProperty(blockState, "half"))) {
                                    continue;
                                }

                                final MapType<String> blockStateBelow = this.getBlock(relative(position, Direction.DOWN));
                                final String nameBelow = getName(blockStateBelow);
                                switch (nameBelow) {
                                    case "minecraft:sunflower":
                                        this.setBlock(position, UPPER_SUNFLOWER);
                                        break;
                                    case "minecraft:lilac":
                                        this.setBlock(position, UPPER_LILAC);
                                        break;
                                    case "minecraft:tall_grass":
                                        this.setBlock(position, UPPER_TALL_GRASS);
                                        break;
                                    case "minecraft:large_fern":
                                        this.setBlock(position, UPPER_LARGE_FERN);
                                        break;
                                    case "minecraft:rose_bush":
                                        this.setBlock(position, UPPER_ROSE_BUSH);
                                        break;
                                    case "minecraft:peony":
                                        this.setBlock(position, UPPER_PEONY);
                                        break;
                                }
                            }
                            break;
                        }
                        case 176: // free standing banner
                        case 177: { // wall mounted banner
                            while (positionIterator.hasNext()) {
                                final int position = positionIterator.nextInt() | yIndex;
                                final MapType<String> tile = this.getBlockEntity(position);

                                if (tile == null) {
                                    continue;
                                }

                                final MapType<String> blockState = this.getBlock(position);

                                final int base = tile.getInt("Base");
                                if (base != 15 && base >= 0 && base < 16) {
                                    final String state = getProperty(blockState, fixEntry.getIntKey() == 176 ? "rotation" : "facing") + "_" + base;
                                    final MapType<String> update = BANNER_BLOCK_MAP.get(state);
                                    if (update != null) {
                                        this.setBlock(position, update);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }

        private MapType<String> getBlockEntity(final int index) {
            return this.tileEntities.get(index);
        }

        private MapType<String> removeBlockEntity(final int index) {
            return this.tileEntities.remove(index);
        }

        public static int relative(final int index, final Direction direction) {
            switch (direction.getAxis()) {
            case X:
                int j = (index & 15) + direction.getAxisDirection().getStep();
                return j >= 0 && j <= 15 ? index & -16 | j : -1;
            case Y:
                int k = (index >> 8) + direction.getAxisDirection().getStep();
                return k >= 0 && k <= 255 ? index & 255 | k << 8 : -1;
            case Z:
                int l = (index >> 4 & 15) + direction.getAxisDirection().getStep();
                return l >= 0 && l <= 15 ? index & -241 | l << 4 : -1;
            default:
                return -1;
            }
        }

        private void setBlock(final int index, final MapType<String> blockState) {
            if (index >= 0 && index <= 65535) {
                final Section section = this.getSection(index);
                if (section != null) {
                    section.setBlock(index & 4095, blockState);
                }
            }
        }

        private Section getSection(final int index) {
            final int y = index >> 12;
            return y < this.sections.length ? this.sections[y] : null;
        }

        public MapType<String> getBlock(int i) {
            if (i >= 0 && i <= 65535) {
                final Section section = this.getSection(i);
                return section == null ? AIR : section.getBlock(i & 4095);
            } else {
                return AIR;
            }
        }

        public MapType<String> writeBackToLevel() {
            if (this.tileEntities.isEmpty()) {
                this.level.remove("TileEntities");
            } else {
                final ListType tileEntities = Types.NBT.createEmptyList();
                this.tileEntities.values().forEach(tileEntities::addMap);
                this.level.setList("TileEntities", tileEntities);
            }

            final MapType<String> indices = Types.NBT.createEmptyMap();
            final ListType sections = Types.NBT.createEmptyList();
            for (final Section section : this.sections) {
                if (section == null) {
                    continue;
                }

                sections.addMap(section.writeBackToSection());
                indices.setInts(Integer.toString(section.y), Arrays.copyOf(section.update.elements(), section.update.size()));
            }

            this.level.setList("Sections", sections);

            final MapType<String> upgradeData = Types.NBT.createEmptyMap();
            upgradeData.setByte("Sides", (byte)this.sides);
            upgradeData.setMap("Indices", indices);

            this.level.setMap("UpgradeData", upgradeData);

            return this.level;
        }
    }

    static class Section {
        final Palette palette = new Palette();

        static final class Palette extends Reference2IntOpenHashMap<MapType<String>> {

            final ListType paletteStates = Types.NBT.createEmptyList();

            private int find(final MapType<String> k) {
                if (((k) == (null)))
                    return containsNullKey ? n : -(n + 1);
                MapType<String> curr;
                final Object[] key = this.key;
                int pos;
                // The starting point.
                if (((curr = (MapType<String>)key[pos = (it.unimi.dsi.fastutil.HashCommon.mix(System.identityHashCode(k))) & mask]) == (null)))
                    return -(pos + 1);
                if (((k) == (curr)))
                    return pos;
                // There's always an unused entry.
                while (true) {
                    if (((curr = (MapType<String>)key[pos = (pos + 1) & mask]) == (null)))
                        return -(pos + 1);
                    if (((k) == (curr)))
                        return pos;
                }
            }

            private void insert(final int pos, final MapType<String> k, final int v) {
                if (pos == n)
                    containsNullKey = true;
                ((Object[])key)[pos] = k;
                value[pos] = v;
                if (size++ >= maxFill)
                    rehash(arraySize(size + 1, f));
            }

            private MapType<String>[] byId = new MapType[4];
            private MapType<String> last = null;

            public int getOrCreateId(final MapType<String> k) {
                if (k == this.last) {
                    return this.size - 1;
                }
                final int pos = find(k);
                if (pos >= 0) {
                    return this.value[pos];
                }

                final int insert = this.size;
                MapType<String> inPalette = k;

                if ("%%FILTER_ME%%".equals(getName(k))) {
                    inPalette = AIR;
                }

                if (insert >= this.byId.length) {
                    this.byId = Arrays.copyOf(this.byId, this.byId.length * 2);
                    this.byId[insert] = k;
                } else {
                    this.byId[insert] = k;
                }
                this.paletteStates.addMap(inPalette);

                this.last = k;

                this.insert(-pos - 1, k, insert);

                return insert;
            }

        }

        final MapType<String> section;
        final boolean hasData;
        final Int2ObjectLinkedOpenHashMap<IntArrayList> toFix = new Int2ObjectLinkedOpenHashMap<>();
        final IntArrayList update = new IntArrayList();
        final int y;
        final int[] buffer = new int[4096];

        public Section(final MapType<String> section) {
            this.section = section;
            this.y = section.getInt("Y");
            this.hasData = section.hasKey("Blocks", ObjectType.BYTE_ARRAY);
        }

        public MapType<String> getBlock(final int index) {
            if (index >= 0 && index <= 4095) {
                final MapType<String> state = this.palette.byId[this.buffer[index]];
                return state == null ? AIR : state;
            } else {
                return AIR;
            }
        }

        public void setBlock(final int index, final MapType<String> blockState) {
            this.buffer[index] = this.palette.getOrCreateId(blockState);
        }

        public int upgrade(int sides) {
            if (!this.hasData) {
                return sides;
            }

            final byte[] blocks = this.section.getBytes("Blocks");
            final DataLayer data = DataLayer.getOrNull(this.section.getBytes("Data"));
            final DataLayer add = DataLayer.getOrNull(this.section.getBytes("Add"));

            this.palette.getOrCreateId(AIR);

            for (int index = 0; index < 4096; ++index) {
                final int x = index & 15;
                final int z = index >> 4 & 15;

                int blockStateId = (blocks[index] & 255) << 4;
                if (data != null) {
                    blockStateId |= data.get(index);
                }
                if (add != null) {
                    blockStateId |= add.get(index) << 12;
                }
                if (IDS_NEEDING_FIX[blockStateId >>> 4]) {
                    this.addFix(blockStateId >>> 4, index);
                }

                if (VIRTUAL[blockStateId >>> 4]) {
                    final int additionalSides = getSideMask(x == 0, x == 15, z == 0, z == 15);
                    if (additionalSides == 0) {
                        this.update.add(index);
                    } else {
                        sides |= additionalSides;
                    }
                }

                this.setBlock(index, HelperBlockFlatteningV1450.getNBTForId(blockStateId));
            }

            return sides;
        }

        private void addFix(final int block, final int index) {
            this.toFix.computeIfAbsent(block, (final int keyInMap) -> {
                return new IntArrayList();
            }).add(index);
        }

        // Note: modifies the current section and returns it.
        public MapType<String> writeBackToSection() {
            if (!this.hasData) {
                return this.section;
            }

            this.section.setList("Palette", this.palette.paletteStates.copy()); // deep copy to ensure palette compound tags are NOT shared

            final int bitSize = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
            final PackedBitStorage packedIds = new PackedBitStorage(bitSize, 4096);

            for(int index = 0; index < this.buffer.length; ++index) {
                packedIds.set(index, this.buffer[index]);
            }

            this.section.setLongs("BlockStates", packedIds.getRaw());

            this.section.remove("Blocks");
            this.section.remove("Data");
            this.section.remove("Add");

            return this.section;
        }
    }
}
