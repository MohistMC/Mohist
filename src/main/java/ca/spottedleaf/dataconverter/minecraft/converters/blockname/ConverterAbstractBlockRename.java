package ca.spottedleaf.dataconverter.minecraft.converters.blockname;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.ConverterAbstractStringValueTypeRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.function.Function;

public final class ConverterAbstractBlockRename {

    private ConverterAbstractBlockRename() {}

    public static void register(final int version, final Function<String, String> renamer) {
        register(version, 0, renamer);
    }

    public static void register(final int version, final int subVersion, final Function<String, String> renamer) {
        ConverterAbstractStringValueTypeRename.register(version, subVersion, MCTypeRegistry.BLOCK_NAME, renamer);
        MCTypeRegistry.BLOCK_STATE.addStructureConverter(new DataConverter<>(version, subVersion) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String name = data.getString("Name");
                if (name != null) {
                    final String converted = renamer.apply(name);
                    if (converted != null) {
                        data.setString("Name", converted);
                    }
                }
                return null;
            }
        });
    }

    public static void registerAndFixJigsaw(final int version, final Function<String, String> renamer) {
        registerAndFixJigsaw(version, 0, renamer);
    }

    public static void registerAndFixJigsaw(final int version, final int subVersion, final Function<String, String> renamer) {
        register(version, subVersion, renamer);
        // TODO check on update, minecraft:jigsaw can change
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:jigsaw", new DataConverter<>(version, subVersion) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String finalState = data.getString("final_state");
                if (finalState == null || finalState.isEmpty()) {
                    return null;
                }

                final int nbtStart1 = finalState.indexOf('[');
                final int nbtStart2 = finalState.indexOf('{');
                int stateNameEnd = finalState.length();
                if (nbtStart1 > 0) {
                    stateNameEnd = Math.min(stateNameEnd, nbtStart1);
                }

                if (nbtStart2 > 0) {
                    stateNameEnd = Math.min(stateNameEnd, nbtStart2);
                }

                final String blockStateName = finalState.substring(0, stateNameEnd);
                final String converted = renamer.apply(blockStateName);
                if (converted == null) {
                    return null;
                }

                final String convertedState = converted.concat(finalState.substring(stateNameEnd));
                data.setString("final_state", convertedState);

                return null;
            }
        });
    }
}
