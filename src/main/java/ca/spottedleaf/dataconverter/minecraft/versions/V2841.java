package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;
import ca.spottedleaf.dataconverter.util.IntegerUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class V2841 {

    protected static final int VERSION = MCVersions.V21W42A + 1;

    protected static final Set<String> ALWAYS_WATERLOGGED = new HashSet<>(Arrays.asList(
            "minecraft:bubble_column",
            "minecraft:kelp",
            "minecraft:kelp_plant",
            "minecraft:seagrass",
            "minecraft:tall_seagrass"
    ));

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> root, final long sourceVersion, final long toVersion) {
                final MapType<String> level = root.getMap("Level");
                if (level == null) {
                    return null;
                }

                {
                    // Why it's renamed here and not the next data version is beyond me.
                    final MapType<String> liquidTicks = level.getMap("LiquidTicks");
                    if (liquidTicks != null) {
                        level.remove("LiquidTicks");
                        level.setMap("fluid_ticks", liquidTicks);
                    }
                }

                final Int2ObjectOpenHashMap<SimplePaletteReader> sectionBlocks = new Int2ObjectOpenHashMap<>();
                final ListType sections = level.getList("Sections", ObjectType.MAP);
                int minSection = 0; // TODO wtf is this
                if (sections != null) {
                    for (int i = 0, len = sections.size(); i < len; ++i) {
                        final MapType<String> section = sections.getMap(i);

                        final int sectionY = section.getInt("Y");
                        if (sectionY < minSection && section.hasKey("biomes")) {
                            minSection = sectionY;
                        }

                        final MapType<String> blockStates = section.getMap("block_states");
                        if (blockStates == null) {
                            continue;
                        }

                        sectionBlocks.put(sectionY, new SimplePaletteReader(section.getList("palette", ObjectType.MAP), section.getLongs("data")));
                    }
                }

                level.setByte("yPos", (byte)minSection); // TODO ???????????????????????????????????????

                if (level.hasKey("fluid_ticks") || level.hasKey("TileTicks")) {
                    return null;
                }

                final int sectionX = level.getInt("xPos");
                final int sectionZ = level.getInt("zPos");

                final ListType fluidTicks = level.getList("LiquidsToBeTicked", ObjectType.LIST);
                final ListType blockTicks = level.getList("ToBeTicked", ObjectType.LIST);
                level.remove("LiquidsToBeTicked");
                level.remove("ToBeTicked");

                level.setList("fluid_ticks", migrateTickList(fluidTicks, false, sectionBlocks, sectionX, minSection, sectionZ));
                level.setList("TileTicks", migrateTickList(blockTicks, true, sectionBlocks, sectionX, minSection, sectionZ));

                return null;
            }
        });
    }

    public static ListType migrateTickList(final ListType ticks, final boolean blockTicks, final Int2ObjectOpenHashMap<SimplePaletteReader> sectionBlocks,
                                           final int sectionX, final int minSection, final int sectionZ) {
        final ListType ret = Types.NBT.createEmptyList();

        if (ticks == null) {
            return ret;
        }

        for (int sectionIndex = 0, totalSections = ticks.size(); sectionIndex < totalSections; ++sectionIndex) {
            final int sectionY = sectionIndex + minSection;
            final ListType sectionTicks = ticks.getList(sectionIndex);
            final SimplePaletteReader palette = sectionBlocks.get(sectionY);

            for (int i = 0, len = sectionTicks.size(); i < len; ++i) {
                final int localIndex = sectionTicks.getShort(i) & 0xFFFF;
                final MapType<String> blockState = palette == null ? null : palette.getState(localIndex);
                final String subjectId = blockTicks ? getBlockId(blockState) : getLiquidId(blockState);

                ret.addMap(createNewTick(subjectId, localIndex, sectionX, sectionY, sectionZ));
            }
        }

        return ret;
    }

    public static MapType<String> createNewTick(final String subjectId, final int localIndex, final int sectionX, final int sectionY, final int sectionZ) {
        final int newX = (localIndex & 15) + (sectionX << 4);
        final int newZ = ((localIndex >> 4) & 15) + (sectionZ << 4);
        final int newY = ((localIndex >> 8) & 15) + (sectionY << 4);

        final MapType<String> ret = Types.NBT.createEmptyMap();

        ret.setString("i", subjectId);
        ret.setInt("x", newX);
        ret.setInt("y", newY);
        ret.setInt("z", newZ);
        ret.setInt("t", 0);
        ret.setInt("p", 0);

        return ret;
    }

    public static String getBlockId(final MapType<String> blockState) {
        return blockState == null ? "minecraft:air" : blockState.getString("Name", "minecraft:air");
    }

    private static String getLiquidId(final MapType<String> blockState) {
        if (blockState == null) {
            return "minecraft:empty";
        }

        final String name = blockState.getString("Name");
        if (ALWAYS_WATERLOGGED.contains(name)) {
            return "minecraft:water";
        }

        final MapType<String> properties = blockState.getMap("Properties");
        if ("minecraft:water".equals(name)) {
            return properties != null && properties.getInt("level") == 0 ? "minecraft:water" : "minecraft:flowing_water";
        } else if ("minecraft:lava".equals(name)) {
            return properties != null && properties.getInt("level") == 0 ? "minecraft:lava" : "minecraft:flowing_lava";
        }

        return (properties != null && properties.getBoolean("waterlogged")) ? "minecraft:water" : "minecraft:empty";
    }

    public static final class SimplePaletteReader {

        public final ListType palette;
        public final long[] data;
        private final int bitsPerValue;
        private final long mask;
        private final int valuesPerLong;

        public SimplePaletteReader(final ListType palette, final long[] data) {
            this.palette = palette == null ? null : (palette.size() == 0 ? null : palette);
            this.data = data;
            this.bitsPerValue = Math.max(4, IntegerUtil.ceilLog2(this.palette == null ? 0 : this.palette.size()));
            this.mask = (1L << this.bitsPerValue) - 1L;
            this.valuesPerLong = (int)(64L / this.bitsPerValue);
        }

        public MapType<String> getState(final int x, final int y, final int z) {
            final int index = x | (z << 4) | (y << 8);
            return this.getState(index);
        }

        public MapType<String> getState(final int index) {
            final ListType palette = this.palette;
            if (palette == null) {
                return null;
            }

            final int paletteSize = palette.size();
            if (paletteSize == 1) {
                return palette.getMap(0);
            }

            // x86 div computes mod. no loss here using mod
            // if needed, can compute magic mul and shift for div values using IntegerUtil
            final int dataIndex = index / this.valuesPerLong;
            final int localIndex = (index % this.valuesPerLong) * this.bitsPerValue;
            final long[] data = this.data;
            if (dataIndex < 0 || dataIndex >= data.length) {
                return null;
            }

            final long value = data[dataIndex];
            final int paletteIndex = (int)((value >>> localIndex) & this.mask);
            if (paletteIndex < 0 || paletteIndex >= paletteSize) {
                return null;
            }

            return palette.getMap(paletteIndex);
        }
    }
}
