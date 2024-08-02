package ca.spottedleaf.dataconverter.minecraft.converters.stats;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.RenameHelper;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.function.Function;

public final class ConverterAbstractStatsRename  {

    private ConverterAbstractStatsRename() {}

    public static void register(final int version, final Function<String, String> renamer) {
        register(version, 0, renamer);
    }

    public static void register(final int version, final int subVersion, final Function<String, String> renamer) {
        MCTypeRegistry.OBJECTIVE.addStructureConverter(new DataConverter<>(version, subVersion) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> criteriaType = data.getMap("CriteriaType");
                if (criteriaType == null) {
                    return null;
                }

                final String type = criteriaType.getString("type");
                if (!"minecraft:custom".equals(type)) {
                    return null;
                }

                final String id = criteriaType.getString("id");
                if (id == null) {
                    return null;
                }

                final String rename = renamer.apply(id);
                if (rename != null) {
                    criteriaType.setString("id", rename);
                }

                return null;
            }
        });

        MCTypeRegistry.STATS.addStructureConverter(new DataConverter<>(version, subVersion) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> stats = data.getMap("stats");

                if (stats == null) {
                    return null;
                }

                final MapType<String> custom = stats.getMap("minecraft:custom");
                if (custom == null) {
                    return null;
                }

                RenameHelper.renameKeys(custom, renamer);

                return null;
            }
        });
    }
}
