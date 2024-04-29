package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V813 {

    protected static final int VERSION = MCVersions.V16W40A;

    public static final String[] SHULKER_ID_BY_COLOUR = new String[] {
            "minecraft:white_shulker_box",
            "minecraft:orange_shulker_box",
            "minecraft:magenta_shulker_box",
            "minecraft:light_blue_shulker_box",
            "minecraft:yellow_shulker_box",
            "minecraft:lime_shulker_box",
            "minecraft:pink_shulker_box",
            "minecraft:gray_shulker_box",
            "minecraft:silver_shulker_box",
            "minecraft:cyan_shulker_box",
            "minecraft:purple_shulker_box",
            "minecraft:blue_shulker_box",
            "minecraft:brown_shulker_box",
            "minecraft:green_shulker_box",
            "minecraft:red_shulker_box",
            "minecraft:black_shulker_box"
    };

    public static void register() {
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:shulker_box", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    return null;
                }

                final MapType<String> blockEntity = tag.getMap("BlockEntityTag");
                if (blockEntity == null) {
                    return null;
                }

                final int color = blockEntity.getInt("Color");
                blockEntity.remove("Color");

                data.setString("id", SHULKER_ID_BY_COLOUR[color % SHULKER_ID_BY_COLOUR.length]);

                return null;
            }
        });

        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:shulker_box", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                data.remove("Color");
                return null;
            }
        });
    }

    private V813() {}
}
