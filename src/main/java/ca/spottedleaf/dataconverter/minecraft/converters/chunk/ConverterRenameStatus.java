package ca.spottedleaf.dataconverter.minecraft.converters.chunk;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.RenameHelper;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.util.NamespaceUtil;
import java.util.function.Function;

public final class ConverterRenameStatus extends DataConverter<MapType<String>, MapType<String>> {

    private final Function<String, String> renamer;

    public ConverterRenameStatus(final int toVersion, final Function<String, String> renamer) {
        this(toVersion, 0, renamer);
    }

    public ConverterRenameStatus(final int toVersion, final int versionStep, final Function<String, String> renamer) {
        super(toVersion, versionStep);
        this.renamer = renamer;
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        // Note: DFU technically enforces namespace due to how they wrote their converter, so we will do the same.
        NamespaceUtil.enforceForPath(data, "Status");
        RenameHelper.renameString(data, "Status", this.renamer);

        NamespaceUtil.enforceForPath(data.getMap("below_zero_retrogen"), "target_status");
        RenameHelper.renameString(data.getMap("below_zero_retrogen"), "target_status", this.renamer);
        return null;
    }
}
