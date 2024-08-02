package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.RenameHelper;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V3090 {

    protected static final int VERSION = MCVersions.V22W15A + 1;

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:painting", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                RenameHelper.renameSingle(data, "Motive", "variant");
                RenameHelper.renameSingle(data, "Facing", "facing");
                return null;
            }
        });
    }
}
