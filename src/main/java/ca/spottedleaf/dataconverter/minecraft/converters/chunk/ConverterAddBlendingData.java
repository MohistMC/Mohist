package ca.spottedleaf.dataconverter.minecraft.converters.chunk;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;
import ca.spottedleaf.dataconverter.util.NamespaceUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ConverterAddBlendingData extends DataConverter<MapType<String>, MapType<String>> {

    private static final Set<String> STATUSES_TO_SKIP_BLENDING = new HashSet<>(
            Arrays.asList(
                    "minecraft:empty",
                    "minecraft:structure_starts",
                    "minecraft:structure_references",
                    "minecraft:biomes"
            )
    );

    public ConverterAddBlendingData(final int toVersion) {
        super(toVersion);
    }

    public ConverterAddBlendingData(final int toVersion, final int versionStep) {
        super(toVersion, versionStep);
    }

    private static MapType<String> createBlendingData(final int height, final int minY) {
        final MapType<String> ret = Types.NBT.createEmptyMap();

        ret.setInt("min_section", minY >> 4);
        ret.setInt("max_section", (minY + height) >> 4);

        return ret;
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        data.remove("blending_data");
        final MapType<String> context = data.getMap("__context");
        if (!"minecraft:overworld".equals(context == null ? null : context.getString("dimension"))) {
            return null;
        }

        final String status = NamespaceUtil.correctNamespace(data.getString("Status"));
        if (status == null) {
            return null;
        }

        final MapType<String> belowZeroRetrogen = data.getMap("below_zero_retrogen");

        if (!STATUSES_TO_SKIP_BLENDING.contains(status)) {
            data.setMap("blending_data", createBlendingData(384, -64));
        } else if (belowZeroRetrogen != null) {
            final String realStatus = NamespaceUtil.correctNamespace(belowZeroRetrogen.getString("target_status", "empty"));
            if (!STATUSES_TO_SKIP_BLENDING.contains(realStatus)) {
                data.setMap("blending_data", createBlendingData(256, 0));
            }
        }

        return null;
    }
}
