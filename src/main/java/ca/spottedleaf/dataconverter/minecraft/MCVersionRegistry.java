package ca.spottedleaf.dataconverter.minecraft;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongComparator;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import org.slf4j.Logger;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;

public final class MCVersionRegistry {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final Int2ObjectLinkedOpenHashMap<String> VERSION_NAMES = new Int2ObjectLinkedOpenHashMap<>();
    protected static final IntArrayList VERSION_LIST;
    protected static final LongArrayList DATA_VERSION_LIST;

    protected static final IntArrayList DATACONVERTER_VERSIONS_LIST;
    protected static final IntLinkedOpenHashSet DATACONVERTER_VERSIONS_MAJOR = new IntLinkedOpenHashSet();
    protected static final LongLinkedOpenHashSet DATACONVERTER_VERSIONS = new LongLinkedOpenHashSet();
    protected static final Int2ObjectLinkedOpenHashMap<IntArrayList> SUBVERSIONS = new Int2ObjectLinkedOpenHashMap<>();
    protected static final LongArrayList BREAKPOINTS = new LongArrayList();
    static {
        // Note: Some of these are nameless.
        // Unless a data version is specified here, it will NOT have converters ran for it. Please add them on update!
        final int[] converterVersions = new int[] {
                99,
                100,
                101,
                102,
                105,
                106,
                107,
                108,
                109,
                110,
                111,
                113,
                135,
                143,
                147,
                165,
                501,
                502,
                505,
                700,
                701,
                702,
                703,
                704,
                705,
                804,
                806,
                808,
                808,
                813,
                816,
                820,
                1022,
                1125,
                1344,
                1446,
                1450,
                1451,
                1451,
                1451,
                1451,
                1451,
                1451,
                1451,
                1451,
                1451,
                1456,
                1458,
                1460,
                1466,
                1470,
                1474,
                1475,
                1480,
                1481,
                1483,
                1484,
                1486,
                1487,
                1488,
                1490,
                1492,
                1494,
                1496,
                1500,
                1501,
                1502,
                1506,
                1510,
                1514,
                1515,
                1624,
                1800,
                1801,
                1802,
                1803,
                1904,
                1905,
                1906,
                1909,
                1911,
                1914,
                1917,
                1918,
                1920,
                1925,
                1928,
                1929,
                1931,
                1936,
                1946,
                1948,
                1953,
                1955,
                1961,
                1963,
                2100,
                2202,
                2209,
                2211,
                2218,
                2501,
                2502,
                2503,
                2505,
                2508,
                2509,
                2511,
                2514,
                2516,
                2518,
                2519,
                2522,
                2523,
                2527,
                2528,
                2529,
                2531,
                2533,
                2535,
                2550,
                2551,
                2552,
                2553,
                2558,
                2568,
                2671,
                2679,
                2680,
                2684,
                2686,
                2688,
                2690,
                2691,
                2693,
                2696,
                2700,
                2701,
                2702,
                2704,
                2707,
                2710,
                2717,
                2825,
                2831,
                2832,
                2833,
                2838,
                2841,
                2842,
                2843,
                2846,
                2852,
                2967,
                2970,
                3077,
                3078,
                3081,
                3082,
                3083,
                3084,
                3086,
                3087,
                3088,
                3090,
                3093,
                3094,
                3097,
                3108,
                3201,
                3203,
                3204,
                3209,
                3214,
                3319,
                3322,
                3438,
                3439,
                3440,
                3441,
                3447,
                3448,
                3450,
                3451,
                3459
                // All up to 1.20-rc1
        };
        Arrays.sort(converterVersions);

        DATACONVERTER_VERSIONS_MAJOR.addAll(DATACONVERTER_VERSIONS_LIST = new IntArrayList(converterVersions));

        // add sub versions
        registerSubVersion(MCVersions.V16W38A + 1, 1);

        registerSubVersion(MCVersions.V17W47A, 1);
        registerSubVersion(MCVersions.V17W47A, 2);
        registerSubVersion(MCVersions.V17W47A, 3);
        registerSubVersion(MCVersions.V17W47A, 4);
        registerSubVersion(MCVersions.V17W47A, 5);
        registerSubVersion(MCVersions.V17W47A, 6);
        registerSubVersion(MCVersions.V17W47A, 7);

        // register breakpoints here
        // for all major releases after 1.16, add them. this reduces the work required to determine if a breakpoint
        // is needed for new converters

        // Too much changed in this version.
        registerBreakpoint(MCVersions.V17W47A);
        registerBreakpoint(MCVersions.V17W47A, Integer.MAX_VALUE);

        // final release of major version
        registerBreakpoint(MCVersions.V1_17_1, Integer.MAX_VALUE);

        // final release of major version
        registerBreakpoint(MCVersions.V1_18_2, Integer.MAX_VALUE);

        // final release of major version
        registerBreakpoint(MCVersions.V1_19_4, Integer.MAX_VALUE);
    }

    static {
        final Field[] fields = MCVersions.class.getDeclaredFields();
        for (final Field field : fields) {
            final String name = field.getName();
            final int value;
            try {
                 value = field.getInt(null);
            } catch (final Exception ex) {
                throw new RuntimeException(ex);
            }

            if (VERSION_NAMES.containsKey(value) && value != MCVersions.V15W33B) { // Mojang registered 15w33a and 15w33b under the same id.
                LOGGER.warn("Error registering version \"" + name + "\", version number '" + value + "' is already associated with \"" + VERSION_NAMES.get(value) + "\"");
            }

            VERSION_NAMES.put(value, name.substring(1).replace("_PRE", "-PRE").replace("_RC", "-RC").replace('_', '.').toLowerCase(Locale.ROOT));
        }

        for (final int version : DATACONVERTER_VERSIONS_MAJOR) {
            if (VERSION_NAMES.containsKey(version)) {
                continue;
            }

            // find closest greatest version above this one
            int closest = Integer.MAX_VALUE;
            String closestName = null;
            for (final int v : VERSION_NAMES.keySet()) {
                if (v > version && v < closest) {
                    closest = v;
                    closestName = VERSION_NAMES.get(v);
                }
            }

            if (closestName == null) {
                VERSION_NAMES.put(version, "unregistered_v" + version);
            } else {
                VERSION_NAMES.put(version, closestName + "-dev" + (closest - version));
            }
        }

        // Explicit override for V99, as 99 is very special.
        VERSION_NAMES.put(99, "pre_converter");

        VERSION_LIST = new IntArrayList(new IntRBTreeSet(VERSION_NAMES.keySet()));

        DATA_VERSION_LIST = new LongArrayList();
        for (final int version : VERSION_LIST) {
            DATA_VERSION_LIST.add(DataConverter.encodeVersions(version, 0));

            final IntArrayList subVersions = SUBVERSIONS.get(version);
            if (subVersions == null) {
                continue;
            }

            for (final int step : subVersions) {
                DATA_VERSION_LIST.add(DataConverter.encodeVersions(version, step));
            }
        }

        DATA_VERSION_LIST.sort((LongComparator)null);

        for (final int version : DATACONVERTER_VERSIONS_MAJOR) {
            DATACONVERTER_VERSIONS.add(DataConverter.encodeVersions(version, 0));

            final IntArrayList subVersions = SUBVERSIONS.get(version);
            if (subVersions == null) {
                continue;
            }

            for (final int step : subVersions) {
                DATACONVERTER_VERSIONS.add(DataConverter.encodeVersions(version, step));
            }
        }
    }

    private static void registerSubVersion(final int version, final int step) {
        if (DATA_VERSION_LIST != null) {
            throw new IllegalStateException("Added too late!");
        }
        SUBVERSIONS.computeIfAbsent(version, (final int keyInMap) -> {
            return new IntArrayList();
        }).add(step);
    }

    private static void registerBreakpoint(final int version) {
        registerBreakpoint(version, 0);
    }

    private static void registerBreakpoint(final int version, final int step) {
        BREAKPOINTS.add(DataConverter.encodeVersions(version, step));
    }

    // returns only versions that have dataconverters
    public static boolean hasDataConverters(final int version) {
        return DATACONVERTER_VERSIONS_MAJOR.contains(version);
    }

    public String getVersionName(final int version) {
        return VERSION_NAMES.get(version);
    }

    public boolean isRegisteredVersion(final int version) {
        return VERSION_NAMES.containsKey(version);
    }

    public static IntArrayList getVersionList() {
        return VERSION_LIST;
    }

    public static LongArrayList getDataVersionList() {
        return DATA_VERSION_LIST;
    }

    public static int getMaxVersion() {
        return VERSION_LIST.getInt(VERSION_LIST.size() - 1);
    }

    public static LongArrayList getBreakpoints() {
        return BREAKPOINTS;
    }

    public static void checkVersion(final long version) {
        if (!DATACONVERTER_VERSIONS.contains(version)) {
            throw new IllegalStateException("Version " + DataConverter.encodedToString(version) + " is not registered to have dataconverters, yet has a dataconverter");
        }
    }
}
