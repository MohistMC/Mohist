package ca.spottedleaf.dataconverter.minecraft.converters.entity;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.function.IntFunction;

public final class ConverterEntityToVariant extends DataConverter<MapType<String>, MapType<String>> {

    public final String path;
    public final IntFunction<String> conversion;

    public ConverterEntityToVariant(final int toVersion, final String path, final IntFunction<String> conversion) {
        super(toVersion);
        this.path = path;
        this.conversion = conversion;
    }

    public ConverterEntityToVariant(final int toVersion, final int versionStep, final String path, final IntFunction<String> conversion) {
        super(toVersion, versionStep);
        this.path = path;
        this.conversion = conversion;
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        final Number value = data.getNumber(this.path);
        if (value == null) {
            // nothing to do, DFU does the same
            return null;
        }

        final String converted = this.conversion.apply(value.intValue());

        if (converted == null) {
            throw new NullPointerException("Conversion " + this.conversion + " cannot return null value!");
        }

        // DFU doesn't appear to remove the old field, so why should we?

        data.setString("variant", converted);

        return null;
    }
}
