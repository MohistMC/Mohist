package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.options.ConverterAbstractOptionsRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;
import com.google.common.collect.ImmutableMap;

public final class V2558 {

    protected static final int VERSION = MCVersions.V1_16_PRE2 + 1;

    private V2558() {}

    public static void register() {
        ConverterAbstractOptionsRename.register(VERSION, ImmutableMap.of(
                "key_key.swapHands", "key_key.swapOffhand"
        )::get);

        MCTypeRegistry.WORLD_GEN_SETTINGS.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                MapType<String> dimensions = data.getMap("dimensions");
                if (dimensions == null) {
                    dimensions = Types.NBT.createEmptyMap();
                    data.setMap("dimensions", dimensions);
                }

                if (dimensions.isEmpty()) {
                    data.setMap("dimensions", recreateSettings(data));
                }

                return null;
            }
        });
    }

    private static MapType<String> recreateSettings(final MapType<String> data) {
        final long seed = data.getLong("seed");

        return V2550.vanillaLevels(seed, V2550.defaultOverworld(seed), false);
    }
}
