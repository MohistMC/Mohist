package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import com.google.gson.JsonParseException;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.datafix.fixes.BlockEntitySignTextStrictJsonFix;

public final class V101 {

    protected static final int VERSION = MCVersions.V15W32A + 1;

    protected static void updateLine(final MapType<String> data, final String path) {
        final String textString = data.getString(path);
        if (textString == null || textString.isEmpty() || "null".equals(textString)) {
            data.setString(path, Component.Serializer.toJson(CommonComponents.EMPTY));
            return;
        }

        Component component = null;

        if (textString.charAt(0) == '"' && textString.charAt(textString.length() - 1) == '"'
                || textString.charAt(0) == '{' && textString.charAt(textString.length() - 1) == '}') {
            try {
                component = GsonHelper.fromNullableJson(BlockEntitySignTextStrictJsonFix.GSON, textString, Component.class, true);
                if (component == null) {
                    component = CommonComponents.EMPTY;
                }
            } catch (final JsonParseException ignored) {}

            if (component == null) {
                try {
                    component = Component.Serializer.fromJson(textString);
                } catch (final JsonParseException ignored) {}
            }

            if (component == null) {
                try {
                    component = Component.Serializer.fromJsonLenient(textString);
                } catch (final JsonParseException ignored) {}
            }

            if (component == null) {
                component = Component.literal(textString);
            }
        } else {
            component = Component.literal(textString);
        }

        data.setString(path, Component.Serializer.toJson(component));
    }

    public static void register() {
        MCTypeRegistry.TILE_ENTITY.addConverterForId("Sign", new DataConverter<>(VERSION) {

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                updateLine(data, "Text1");
                updateLine(data, "Text2");
                updateLine(data, "Text3");
                updateLine(data, "Text4");
                return null;
            }
        });
    }

    private V101() {}

}
