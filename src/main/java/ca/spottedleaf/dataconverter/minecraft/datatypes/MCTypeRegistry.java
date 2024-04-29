package ca.spottedleaf.dataconverter.minecraft.datatypes;

import ca.spottedleaf.dataconverter.minecraft.versions.*;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class MCTypeRegistry {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final MCDataType LEVEL              = new MCDataType("Level");
    public static final MCDataType PLAYER             = new MCDataType("Player");
    public static final MCDataType CHUNK              = new MCDataType("Chunk");
    public static final MCDataType HOTBAR             = new MCDataType("CreativeHotbar");
    public static final MCDataType OPTIONS            = new MCDataType("Options");
    public static final MCDataType STRUCTURE          = new MCDataType("Structure");
    public static final MCDataType STATS              = new MCDataType("Stats");
    public static final MCDataType SAVED_DATA         = new MCDataType("SavedData");
    public static final MCDataType ADVANCEMENTS       = new MCDataType("Advancements");
    public static final MCDataType POI_CHUNK          = new MCDataType("PoiChunk");
    public static final MCDataType ENTITY_CHUNK       = new MCDataType("EntityChunk");
    public static final IDDataType TILE_ENTITY        = new IDDataType("TileEntity");
    public static final IDDataType ITEM_STACK         = new IDDataType("ItemStack");
    public static final MCDataType BLOCK_STATE        = new MCDataType("BlockState");
    public static final MCValueType ENTITY_NAME       = new MCValueType("EntityName");
    public static final IDDataType ENTITY             = new IDDataType("Entity");
    public static final MCValueType BLOCK_NAME        = new MCValueType("BlockName");
    public static final MCValueType ITEM_NAME         = new MCValueType("ItemName");
    public static final MCDataType UNTAGGED_SPAWNER   = new MCDataType("Spawner");
    public static final MCDataType STRUCTURE_FEATURE  = new MCDataType("StructureFeature");
    public static final MCDataType OBJECTIVE          = new MCDataType("Objective");
    public static final MCDataType TEAM               = new MCDataType("Team");
    public static final MCValueType RECIPE            = new MCValueType("RecipeName");
    public static final MCValueType BIOME             = new MCValueType("Biome");
    public static final MCDataType WORLD_GEN_SETTINGS = new MCDataType("WorldGenSettings");
    public static final MCValueType GAME_EVENT_NAME   = new MCValueType("GameEventName");
    public static final MCValueType MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST = new MCValueType("MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST");

    static {
        try {
            registerAll();
        } catch (final ThreadDeath thr) {
            throw thr;
        } catch (final Throwable thr) {
            LOGGER.error(LogUtils.FATAL_MARKER, "Failed to register data converters", thr);
            throw new RuntimeException(thr);
        }
    }

    private static void registerAll() {
        // General notes:
        // - Structure converters run before everything.
        // - ID specific converters run after structure converters.
        // - Structure walkers run after id specific converters.
        // - ID specific walkers run after structure walkers.

        V99.register(); // all legacy data before converters existed
        V100.register(); // first version with version id
        V101.register();
        V102.register();
        V105.register();
        V106.register();
        V107.register();
        V108.register();
        V109.register();
        V110.register();
        V111.register();
        V113.register();
        V135.register();
        V143.register();
        V147.register();
        V165.register();
        V501.register();
        V502.register();
        V505.register();
        V700.register();
        V701.register();
        V702.register();
        V703.register();
        V704.register();
        V705.register();
        V804.register();
        V806.register();
        V808.register();
        V813.register();
        V816.register();
        V820.register();
        V1022.register();
        V1125.register();
        // END OF LEGACY DATA CONVERTERS

        // V1.13
        V1344.register();
        V1446.register();
        // START THE FLATTENING
        V1450.register();
        V1451.register();
        // END THE FLATTENING

        V1456.register();
        V1458.register();
        V1460.register();
        V1466.register();
        V1470.register();
        V1474.register();
        V1475.register();
        V1480.register();
        // V1481 is adding simple block entity
        V1483.register();
        V1484.register();
        V1486.register();
        V1487.register();
        V1488.register();
        V1490.register();
        V1492.register();
        V1494.register();
        V1496.register();
        V1500.register();
        V1501.register();
        V1502.register();
        V1506.register();
        V1510.register();
        V1514.register();
        V1515.register();
        V1624.register();
        // V1.14
        V1800.register();
        V1801.register();
        V1802.register();
        V1803.register();
        V1904.register();
        V1905.register();
        V1906.register();
        // V1909 is just adding a simple block entity (jigsaw)
        V1911.register();
        V1914.register();
        V1917.register();
        V1918.register();
        V1920.register();
        V1925.register();
        V1928.register();
        V1929.register();
        V1931.register();
        V1936.register();
        V1946.register();
        V1948.register();
        V1953.register();
        V1955.register();
        V1961.register();
        V1963.register();
        // V1.15
        V2100.register();
        V2202.register();
        V2209.register();
        V2211.register();
        V2218.register();
        // V1.16
        V2501.register();
        V2502.register();
        V2503.register();
        V2505.register();
        V2508.register();
        V2509.register();
        V2511.register();
        V2514.register();
        V2516.register();
        V2518.register();
        V2519.register();
        V2522.register();
        V2523.register();
        V2527.register();
        V2528.register();
        V2529.register();
        V2531.register();
        V2533.register();
        V2535.register();
        V2550.register();
        V2551.register();
        V2552.register();
        V2553.register();
        V2558.register();
        V2568.register();
        // V1.17
        // WARN: Mojang registers V2671 under 2571, but that version predates 1.16.5? So it looks like a typo...
        // I changed it to 2671, just so that it's after 1.16.5, but even then this looks misplaced... Thankfully this is
        // the first datafixer, and all it does is add a walker, so I think even if the version here is just wrong it will
        // work.
        V2671.register();
        V2679.register();
        V2680.register();
        V2684.register();
        V2686.register();
        V2688.register();
        V2690.register();
        V2691.register();
        V2693.register();
        V2696.register();
        V2700.register();
        V2701.register();
        V2702.register();
        // In reference to V2671, why the fuck is goat being registered again? For this obvious reason, V2704 is absent.
        V2707.register();
        V2710.register();
        V2717.register();
        // V1.18
        V2825.register();
        V2831.register();
        V2832.register();
        V2833.register();
        V2838.register();
        V2841.register();
        V2842.register();
        V2843.register();
        V2846.register();
        V2852.register();
        V2967.register();
        V2970.register();
        // V1.19
        // V3076 is registering a simple tile entity (sculk_catalyst)
        V3077.register();
        V3078.register();
        V3081.register();
        V3082.register();
        V3083.register();
        V3084.register();
        V3086.register();
        V3087.register();
        V3088.register();
        V3090.register();
        V3093.register();
        V3094.register();
        V3097.register();
        V3108.register();
        V3201.register();
        // V3202 registers a simple tile entity
        V3203.register();
        V3204.register();
        V3209.register();
        V3214.register();
        V3319.register();
        V3322.register();
        V3325.register();
        V3326.register();
        V3327.register();
        V3328.register();
        // V1.20
        V3438.register();
        V3439.register();
        V3440.register();
        V3441.register();
        V3447.register();
        V3448.register();
        V3450.register();
        V3451.register();
        V3459.register();
    }

    private MCTypeRegistry() {}
}
