package ca.spottedleaf.dataconverter.minecraft.converters.poi;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import java.util.function.Function;

public final class ConverterAbstractPOIRename {

    private ConverterAbstractPOIRename() {}

    public static void register(final int version, final Function<String, String> renamer) {
        register(version, 0, renamer);
    }

    public static void register(final int version, final int subVersion, final Function<String, String> renamer) {
        MCTypeRegistry.POI_CHUNK.addStructureConverter(new DataConverter<>(version, subVersion) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> sections = data.getMap("Sections");
                if (sections == null) {
                    return null;
                }

                for (final String key : sections.keys()) {
                    final MapType<String> section = sections.getMap(key);

                    final ListType records = section.getList("Records", ObjectType.MAP);

                    if (records == null) {
                        continue;
                    }

                    for (int i = 0, len = records.size(); i < len; ++i) {
                        final MapType<String> record = records.getMap(i);

                        final String type = record.getString("type");
                        if (type != null) {
                            final String converted = renamer.apply(type);
                            if (converted != null) {
                                record.setString("type", converted);
                            }
                        }
                    }
                }

                return null;
            }
        });
    }
}
