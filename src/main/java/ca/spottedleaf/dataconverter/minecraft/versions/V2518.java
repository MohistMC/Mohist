package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V2518 {

    protected static final int VERSION = MCVersions.V20W12A + 3;

    private static final Map<String, String> FACING_RENAMES = ImmutableMap.<String, String>builder()
            .put("down", "down_south")
            .put("up", "up_north")
            .put("north", "north_up")
            .put("south", "south_up")
            .put("west", "west_up")
            .put("east", "east_up")
            .build();


    private V2518() {}

    public static void register() {
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:jigsaw", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String type = data.getString("attachement_type", "minecraft:empty");
                final String pool = data.getString("target_pool", "minecraft:empty");
                data.remove("attachement_type");
                data.remove("target_pool");

                data.setString("name", type);
                data.setString("target", type);
                data.setString("pool", pool);

                return null;
            }
        });

        MCTypeRegistry.BLOCK_STATE.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (!"minecraft:jigsaw".equals(data.getString("Name"))) {
                    return null;
                }

                final MapType<String> properties = data.getMap("Properties");
                if (properties == null) {
                    return null;
                }

                final String facing = properties.getString("facing", "north");
                properties.remove("facing");
                properties.setString("orientation", FACING_RENAMES.getOrDefault(facing, facing));

                return null;
            }
        });
    }
}
