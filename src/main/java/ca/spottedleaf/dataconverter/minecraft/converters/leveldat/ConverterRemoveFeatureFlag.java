package ca.spottedleaf.dataconverter.minecraft.converters.leveldat;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import java.util.Set;

public final class ConverterRemoveFeatureFlag extends DataConverter<MapType<String>, MapType<String>> {

    private final Set<String> flags;

    public ConverterRemoveFeatureFlag(final int toVersion, final Set<String> flags) {
        this(toVersion, 0, flags);
    }

    public ConverterRemoveFeatureFlag(final int toVersion, final int versionStep, final Set<String> flags) {
        super(toVersion, versionStep);
        this.flags = flags;
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        final ListType enabledFeatures = data.getList("enabled_features", ObjectType.STRING);
        if (enabledFeatures == null) {
            return null;
        }

        ListType removedFeatures = null;

        for (int i = 0; i < enabledFeatures.size(); ++i) {
            final String flag = enabledFeatures.getString(i);
            if (!this.flags.contains(flag)) {
                continue;
            }
            enabledFeatures.remove(i--);

            if (removedFeatures == null) {
                removedFeatures = data.getOrCreateList("removed_features", ObjectType.STRING);
            }
            removedFeatures.addString(flag);
        }

        return null;
    }
}
