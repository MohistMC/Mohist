package ca.spottedleaf.dataconverter.minecraft.converters.entity;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.function.Function;

public final class ConverterEntityVariantRename extends DataConverter<MapType<String>, MapType<String>> {

    private final Function<String, String> renamer;

    public ConverterEntityVariantRename(final int toVersion, final Function<String, String> renamer) {
        super(toVersion);
        this.renamer = renamer;
    }

    public ConverterEntityVariantRename(final int toVersion, final int versionStep, final Function<String, String> renamer) {
        super(toVersion, versionStep);
        this.renamer = renamer;
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        final String variant = data.getString("variant");

        if (variant == null) {
            return null;
        }

        final String rename = this.renamer.apply(variant);

        if (rename != null) {
            data.setString("variant", rename);
        }

        return null;
    }
}
