package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public final class V3439 {

    private static final int VERSION = MCVersions.V1_19_4 + 102;

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> signTileUpdater = new DataConverter<>(VERSION) {
            private static final String BLANK_TEXT_LINE = Component.Serializer.toJson(CommonComponents.EMPTY);
            private static final String DEFAULT_COLOR = "black";

            private static ListType migrateToList(final MapType<String> root, final String prefix) {
                if (root == null) {
                    return null;
                }

                final ListType ret = root.getTypeUtil().createEmptyList();

                ret.addString(root.getString(prefix.concat("1"), BLANK_TEXT_LINE));
                ret.addString(root.getString(prefix.concat("2"), BLANK_TEXT_LINE));
                ret.addString(root.getString(prefix.concat("3"), BLANK_TEXT_LINE));
                ret.addString(root.getString(prefix.concat("4"), BLANK_TEXT_LINE));

                return ret;
            }

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> frontText = data.getTypeUtil().createEmptyMap();
                data.setMap("front_text", frontText);

                frontText.setList("messages", migrateToList(data, "Text"));
                frontText.setList("filtered_messages", migrateToList(data, "FilteredText"));
                frontText.setString("color", data.getString("Color", DEFAULT_COLOR));
                frontText.setBoolean("has_glowing_text", data.getBoolean("GlowingText", false));

                final MapType<String> backText = data.getTypeUtil().createEmptyMap();
                data.setMap("back_text", backText);

                final ListType blankMessages = data.getTypeUtil().createEmptyList();
                for (int i = 0; i < 4; ++i) {
                    blankMessages.addString(BLANK_TEXT_LINE);
                }

                backText.setList("messages", blankMessages);
                backText.setList("filtered_messages", blankMessages.copy()); // need to copy so that the value isn't mapped to twice, so that updates to one do not reflect in the other
                backText.setString("color", DEFAULT_COLOR);
                backText.setBoolean("has_glowing_text", false);

                return null;
            }
        };

        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:sign", signTileUpdater);
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:hanging_sign", signTileUpdater);
    }
}
