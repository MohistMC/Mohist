package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import net.minecraft.network.chat.Component;

public final class V1458 {

    protected static final int VERSION = MCVersions.V17W50A + 1;

    public static MapType<String> updateCustomName(final MapType<String> data) {
        final String customName = data.getString("CustomName", "");

        if (customName.isEmpty()) {
            data.remove("CustomName");
        } else {
            data.setString("CustomName", Component.Serializer.toJson(Component.literal(customName)));
        }

        return null;
    }

    public static void register() {
        // From CB
        MCTypeRegistry.PLAYER.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                return updateCustomName(data);
            }
        });

        MCTypeRegistry.ENTITY.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if ("minecraft:commandblock_minecart".equals(data.getString("id"))) {
                    return null;
                }

                return updateCustomName(data);
            }
        });

        MCTypeRegistry.ITEM_STACK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    return null;
                }

                final MapType<String> display = tag.getMap("display");
                if (display == null) {
                    return null;
                }

                final String name = display.getString("Name");
                if (name != null) {
                    display.setString("Name", Component.Serializer.toJson(Component.literal(name)));
                } else {
                    final String localisedName = display.getString("LocName");
                    if (localisedName != null) {
                        display.setString("Name", Component.Serializer.toJson(Component.translatable(localisedName)));
                        display.remove("LocName");
                    }
                }

                return null;
            }
        });

        MCTypeRegistry.TILE_ENTITY.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if ("minecraft:command_block".equals(data.getString("id"))) {
                    return null;
                }

                return updateCustomName(data);
            }
        });

    }

    private V1458() {}

}
