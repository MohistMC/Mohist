package ca.spottedleaf.dataconverter.minecraft.converters.poi;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import java.util.function.Predicate;

public final class ConverterPoiDelete extends DataConverter<MapType<String>, MapType<String>> {

    private final Predicate<String> delete;

    public ConverterPoiDelete(final int toVersion, final Predicate<String> delete) {
        super(toVersion);
        this.delete = delete;
    }

    public ConverterPoiDelete(final int toVersion, final int versionStep, final Predicate<String> delete) {
        super(toVersion, versionStep);
        this.delete = delete;
    }

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

            for (int i = 0; i < records.size();) {
                final MapType<String> record = records.getMap(i);

                final String type = record.getString("type");
                if (type != null && this.delete.test(type)) {
                    records.remove(i);
                    continue;
                }
                ++i;
            }
        }

        return null;
    }
}
