package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.util.Mth;

public final class V2527 {

    protected static final int VERSION = MCVersions.V20W16A + 1;

    private V2527() {}

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> level = data.getMap("Level");

                if (level == null) {
                    return null;
                }

                final ListType sections = level.getList("Sections", ObjectType.MAP);
                if (sections != null) {
                    for (int i = 0, len = sections.size(); i < len; ++i) {
                        final MapType<String> section = sections.getMap(i);

                        final ListType palette = section.getList("Palette", ObjectType.MAP);

                        if (palette == null) {
                            continue;
                        }

                        final int bits = Math.max(4, DataFixUtils.ceillog2(palette.size()));

                        if (Mth.isPowerOfTwo(bits)) {
                            // fits perfectly
                            continue;
                        }

                        final long[] states = section.getLongs("BlockStates");
                        if (states == null) {
                            // wat
                            continue;
                        }

                        section.setLongs("BlockStates", addPadding(4096, bits, states));
                    }
                }

                final MapType<String> heightMaps = level.getMap("Heightmaps");
                if (heightMaps != null) {
                    for (final String key : heightMaps.keys()) {
                        final long[] old = heightMaps.getLongs(key);
                        heightMaps.setLongs(key, addPadding(256, 9, old));
                    }
                }

                return null;
            }
        });
    }

    public static long[] addPadding(final int indices, final int bits, final long[] old) {
        int k = old.length;
        if (k == 0) {
            return old;
        } else {
            long l = (1L << bits) - 1L;
            int m = 64 / bits;
            int n = (indices + m - 1) / m;
            long[] padded = new long[n];
            int o = 0;
            int p = 0;
            long q = 0L;
            int r = 0;
            long s = old[0];
            long t = k > 1 ? old[1] : 0L;

            for(int u = 0; u < indices; ++u) {
                int v = u * bits;
                int w = v >> 6;
                int x = (u + 1) * bits - 1 >> 6;
                int y = v ^ w << 6;
                if (w != r) {
                    s = t;
                    t = w + 1 < k ? old[w + 1] : 0L;
                    r = w;
                }

                long ab;
                int ac;
                if (w == x) {
                    ab = s >>> y & l;
                } else {
                    ac = 64 - y;
                    ab = (s >>> y | t << ac) & l;
                }

                ac = p + bits;
                if (ac >= 64) {
                    padded[o++] = q;
                    q = ab;
                    p = bits;
                } else {
                    q |= ab << p;
                    p = ac;
                }
            }

            if (q != 0L) {
                padded[o] = q;
            }

            return padded;
        }
    }
}
