package ca.spottedleaf.dataconverter.minecraft.converters.helpers;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCValueType;
import java.util.function.Function;

public final class ConverterAbstractStringValueTypeRename {

    private ConverterAbstractStringValueTypeRename() {}

    public static void register(final int version, final MCValueType type, final Function<String, String> renamer) {
        register(version, 0, type, renamer);
    }
    public static void register(final int version, final int subVersion, final MCValueType type, final Function<String, String> renamer) {
        type.addConverter(new DataConverter<>(version, subVersion) {
            @Override
            public Object convert(final Object data, final long sourceVersion, final long toVersion) {
                final String ret = (data instanceof String) ? renamer.apply((String)data) : null;
                return ret == data ? null : ret;
            }
        });
    }

}
