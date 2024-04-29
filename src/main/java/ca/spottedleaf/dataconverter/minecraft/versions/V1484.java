package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V1484 {

    protected static final int VERSION = MCVersions.V18W19A;

    public static void register() {
        final Map<String, String> renamed = ImmutableMap.of(
                "minecraft:sea_grass", "minecraft:seagrass",
                "minecraft:tall_sea_grass", "minecraft:tall_seagrass"
        );

        ConverterAbstractItemRename.register(VERSION, renamed::get);
        ConverterAbstractBlockRename.register(VERSION, renamed::get);

        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> level = data.getMap("Level");

                if (level == null) {
                    return null;
                }

                final MapType<String> heightmaps = level.getMap("Heightmaps");

                if (heightmaps == null) {
                    return null;
                }

                final Object liquid = heightmaps.getGeneric("LIQUID");
                if (liquid != null) {
                    heightmaps.remove("LIQUID");
                    heightmaps.setGeneric("WORLD_SURFACE_WG", liquid);
                }

                final Object solid = heightmaps.getGeneric("SOLID");
                if (solid != null) {
                    heightmaps.remove("SOLID");
                    heightmaps.setGeneric("OCEAN_FLOOR_WG", solid);
                    heightmaps.setGeneric("OCEAN_FLOOR", solid);
                }

                final Object light = heightmaps.getGeneric("LIGHT");
                if (light != null) {
                    heightmaps.remove("LIGHT");
                    heightmaps.setGeneric("LIGHT_BLOCKING", light);
                }

                final Object rain = heightmaps.getGeneric("RAIN");
                if (rain != null) {
                    heightmaps.remove("RAIN");
                    heightmaps.setGeneric("MOTION_BLOCKING", rain);
                    heightmaps.setGeneric("MOTION_BLOCKING_NO_LEAVES", rain);
                }

                return null;
            }
        });
    }

    private V1484() {}

}
