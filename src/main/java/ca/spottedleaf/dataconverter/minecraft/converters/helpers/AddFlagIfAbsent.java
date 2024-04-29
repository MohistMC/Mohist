package ca.spottedleaf.dataconverter.minecraft.converters.helpers;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.types.MapType;

public final class AddFlagIfAbsent extends DataConverter<MapType<String>, MapType<String>> {

    public final String path;
    public final boolean dfl;

    public AddFlagIfAbsent(final int toVersion, final String path, final boolean dfl) {
        super(toVersion);
        this.path = path;
        this.dfl = dfl;
    }

    public AddFlagIfAbsent(final int toVersion, final int versionStep, final String path, final boolean dfl) {
        super(toVersion, versionStep);
        this.path = path;
        this.dfl = dfl;
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        if (!data.hasKey(this.path)) {
            data.setBoolean(this.path, this.dfl);
        }
        return null;
    }
}
