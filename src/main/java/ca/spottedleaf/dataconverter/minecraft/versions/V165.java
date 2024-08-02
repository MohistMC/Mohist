package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import com.google.gson.JsonParseException;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.datafix.fixes.BlockEntitySignTextStrictJsonFix;
import org.apache.commons.lang3.StringUtils;

public final class V165 {

    protected static final int VERSION = MCVersions.V1_9_PRE2;

    public static void register() {
        MCTypeRegistry.ITEM_STACK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    return null;
                }

                final ListType pages = tag.getList("pages", ObjectType.STRING);
                if (pages == null) {
                    return null;
                }

                for (int i = 0, len = pages.size(); i < len; ++i) {
                    final String page = pages.getString(i);
                    Component component = null;

                    if (!"null".equals(page) && !StringUtils.isEmpty(page)) {
                        if (page.charAt(0) == '"' && page.charAt(page.length() - 1) == '"' || page.charAt(0) == '{' && page.charAt(page.length() - 1) == '}') {
                            try {
                                component = GsonHelper.fromNullableJson(BlockEntitySignTextStrictJsonFix.GSON, page, Component.class, true);
                                if (component == null) {
                                    component = CommonComponents.EMPTY;
                                }
                            } catch (final JsonParseException ignored) {}

                            if (component == null) {
                                try {
                                    component = Component.Serializer.fromJson(page);
                                } catch (final JsonParseException ignored) {}
                            }

                            if (component == null) {
                                try {
                                    component = Component.Serializer.fromJsonLenient(page);
                                } catch (JsonParseException ignored) {}
                            }

                            if (component == null) {
                                component = Component.literal(page);
                            }
                        } else {
                            component = Component.literal(page);
                        }
                    } else {
                        component = CommonComponents.EMPTY;
                    }

                    pages.setString(i, Component.Serializer.toJson(component));
                }

                return null;
            }
        });
    }

    private V165() {}

}
