package ca.spottedleaf.dataconverter.minecraft.converters.entity;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.ConverterAbstractStringValueTypeRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.function.Function;

public final class ConverterAbstractEntityRename {

    private ConverterAbstractEntityRename() {}

    public static void register(final int version, final Function<String, String> renamer) {
        register(version, 0, renamer);
    }

    public static void register(final int version, final int subVersion, final Function<String, String> renamer) {
        MCTypeRegistry.ENTITY.addStructureConverter(new DataConverter<>(version, subVersion) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String id = data.getString("id");
                if (id == null) {
                    return null;
                }

                final String converted = renamer.apply(id);

                if (converted != null) {
                    data.setString("id", converted);
                }

                return null;
            }
        });
        ConverterAbstractStringValueTypeRename.register(version, subVersion, MCTypeRegistry.ENTITY_NAME, renamer);
    }

}
