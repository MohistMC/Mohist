package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.ConverterAbstractStringValueTypeRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V2553 {

    protected static final int VERSION = MCVersions.V20W20B + 16;

    public static final Map<String, String> BIOME_RENAMES = ImmutableMap.<String, String>builder()
            .put("minecraft:extreme_hills", "minecraft:mountains")
            .put("minecraft:swampland", "minecraft:swamp")
            .put("minecraft:hell", "minecraft:nether_wastes")
            .put("minecraft:sky", "minecraft:the_end")
            .put("minecraft:ice_flats", "minecraft:snowy_tundra")
            .put("minecraft:ice_mountains", "minecraft:snowy_mountains")
            .put("minecraft:mushroom_island", "minecraft:mushroom_fields")
            .put("minecraft:mushroom_island_shore", "minecraft:mushroom_field_shore")
            .put("minecraft:beaches", "minecraft:beach")
            .put("minecraft:forest_hills", "minecraft:wooded_hills")
            .put("minecraft:smaller_extreme_hills", "minecraft:mountain_edge")
            .put("minecraft:stone_beach", "minecraft:stone_shore")
            .put("minecraft:cold_beach", "minecraft:snowy_beach")
            .put("minecraft:roofed_forest", "minecraft:dark_forest")
            .put("minecraft:taiga_cold", "minecraft:snowy_taiga")
            .put("minecraft:taiga_cold_hills", "minecraft:snowy_taiga_hills")
            .put("minecraft:redwood_taiga", "minecraft:giant_tree_taiga")
            .put("minecraft:redwood_taiga_hills", "minecraft:giant_tree_taiga_hills")
            .put("minecraft:extreme_hills_with_trees", "minecraft:wooded_mountains")
            .put("minecraft:savanna_rock", "minecraft:savanna_plateau")
            .put("minecraft:mesa", "minecraft:badlands")
            .put("minecraft:mesa_rock", "minecraft:wooded_badlands_plateau")
            .put("minecraft:mesa_clear_rock", "minecraft:badlands_plateau")
            .put("minecraft:sky_island_low", "minecraft:small_end_islands")
            .put("minecraft:sky_island_medium", "minecraft:end_midlands")
            .put("minecraft:sky_island_high", "minecraft:end_highlands")
            .put("minecraft:sky_island_barren", "minecraft:end_barrens")
            .put("minecraft:void", "minecraft:the_void")
            .put("minecraft:mutated_plains", "minecraft:sunflower_plains")
            .put("minecraft:mutated_desert", "minecraft:desert_lakes")
            .put("minecraft:mutated_extreme_hills", "minecraft:gravelly_mountains")
            .put("minecraft:mutated_forest", "minecraft:flower_forest")
            .put("minecraft:mutated_taiga", "minecraft:taiga_mountains")
            .put("minecraft:mutated_swampland", "minecraft:swamp_hills")
            .put("minecraft:mutated_ice_flats", "minecraft:ice_spikes")
            .put("minecraft:mutated_jungle", "minecraft:modified_jungle")
            .put("minecraft:mutated_jungle_edge", "minecraft:modified_jungle_edge")
            .put("minecraft:mutated_birch_forest", "minecraft:tall_birch_forest")
            .put("minecraft:mutated_birch_forest_hills", "minecraft:tall_birch_hills")
            .put("minecraft:mutated_roofed_forest", "minecraft:dark_forest_hills")
            .put("minecraft:mutated_taiga_cold", "minecraft:snowy_taiga_mountains")
            .put("minecraft:mutated_redwood_taiga", "minecraft:giant_spruce_taiga")
            .put("minecraft:mutated_redwood_taiga_hills", "minecraft:giant_spruce_taiga_hills")
            .put("minecraft:mutated_extreme_hills_with_trees", "minecraft:modified_gravelly_mountains")
            .put("minecraft:mutated_savanna", "minecraft:shattered_savanna")
            .put("minecraft:mutated_savanna_rock", "minecraft:shattered_savanna_plateau")
            .put("minecraft:mutated_mesa", "minecraft:eroded_badlands")
            .put("minecraft:mutated_mesa_rock", "minecraft:modified_wooded_badlands_plateau")
            .put("minecraft:mutated_mesa_clear_rock", "minecraft:modified_badlands_plateau")
            .put("minecraft:warm_deep_ocean", "minecraft:deep_warm_ocean")
            .put("minecraft:lukewarm_deep_ocean", "minecraft:deep_lukewarm_ocean")
            .put("minecraft:cold_deep_ocean", "minecraft:deep_cold_ocean")
            .put("minecraft:frozen_deep_ocean", "minecraft:deep_frozen_ocean")
            .build();


    private V2553() {}

    public static void register() {
        ConverterAbstractStringValueTypeRename.register(VERSION, MCTypeRegistry.BIOME, BIOME_RENAMES::get);
    }
}
