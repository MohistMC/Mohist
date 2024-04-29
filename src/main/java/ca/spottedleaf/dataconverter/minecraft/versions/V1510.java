package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterAbstractEntityRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import ca.spottedleaf.dataconverter.minecraft.converters.recipe.ConverterAbstractRecipeRename;
import ca.spottedleaf.dataconverter.minecraft.converters.stats.ConverterAbstractStatsRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class V1510 {

    public static final Map<String, String> RENAMED_ENTITY_IDS = ImmutableMap.<String, String>builder()
            .put("minecraft:commandblock_minecart", "minecraft:command_block_minecart")
            .put("minecraft:ender_crystal", "minecraft:end_crystal")
            .put("minecraft:snowman", "minecraft:snow_golem")
            .put("minecraft:evocation_illager", "minecraft:evoker")
            .put("minecraft:evocation_fangs", "minecraft:evoker_fangs")
            .put("minecraft:illusion_illager", "minecraft:illusioner")
            .put("minecraft:vindication_illager", "minecraft:vindicator")
            .put("minecraft:villager_golem", "minecraft:iron_golem")
            .put("minecraft:xp_orb", "minecraft:experience_orb")
            .put("minecraft:xp_bottle", "minecraft:experience_bottle")
            .put("minecraft:eye_of_ender_signal", "minecraft:eye_of_ender")
            .put("minecraft:fireworks_rocket", "minecraft:firework_rocket")
            .build();

    public static final Map<String, String> RENAMED_BLOCKS = ImmutableMap.<String, String>builder()
            .put("minecraft:portal", "minecraft:nether_portal")
            .put("minecraft:oak_bark", "minecraft:oak_wood")
            .put("minecraft:spruce_bark", "minecraft:spruce_wood")
            .put("minecraft:birch_bark", "minecraft:birch_wood")
            .put("minecraft:jungle_bark", "minecraft:jungle_wood")
            .put("minecraft:acacia_bark", "minecraft:acacia_wood")
            .put("minecraft:dark_oak_bark", "minecraft:dark_oak_wood")
            .put("minecraft:stripped_oak_bark", "minecraft:stripped_oak_wood")
            .put("minecraft:stripped_spruce_bark", "minecraft:stripped_spruce_wood")
            .put("minecraft:stripped_birch_bark", "minecraft:stripped_birch_wood")
            .put("minecraft:stripped_jungle_bark", "minecraft:stripped_jungle_wood")
            .put("minecraft:stripped_acacia_bark", "minecraft:stripped_acacia_wood")
            .put("minecraft:stripped_dark_oak_bark", "minecraft:stripped_dark_oak_wood")
            .put("minecraft:mob_spawner", "minecraft:spawner")
            .build();

    public static final Map<String, String> RENAMED_ITEMS = ImmutableMap.<String, String>builder()
            .putAll(RENAMED_BLOCKS)
            .put("minecraft:clownfish", "minecraft:tropical_fish")
            .put("minecraft:chorus_fruit_popped", "minecraft:popped_chorus_fruit")
            .put("minecraft:evocation_illager_spawn_egg", "minecraft:evoker_spawn_egg")
            .put("minecraft:vindication_illager_spawn_egg", "minecraft:vindicator_spawn_egg")
            .build();

    private static final Map<String, String> RECIPES_UPDATES = ImmutableMap.<String, String>builder()
            .put("minecraft:acacia_bark", "minecraft:acacia_wood")
            .put("minecraft:birch_bark", "minecraft:birch_wood")
            .put("minecraft:dark_oak_bark", "minecraft:dark_oak_wood")
            .put("minecraft:jungle_bark", "minecraft:jungle_wood")
            .put("minecraft:oak_bark", "minecraft:oak_wood")
            .put("minecraft:spruce_bark", "minecraft:spruce_wood")
            .build();

    protected static final int VERSION = MCVersions.V1_13_PRE4 + 6;

    private V1510() {}

    public static void register() {
        ConverterAbstractBlockRename.register(VERSION, RENAMED_BLOCKS::get);
        ConverterAbstractItemRename.register(VERSION, RENAMED_ITEMS::get);
        ConverterAbstractRecipeRename.register(VERSION, RECIPES_UPDATES::get);

        ConverterAbstractEntityRename.register(VERSION, (String input) -> {
            if (input.startsWith("minecraft:bred_")) {
                input = "minecraft:".concat(input.substring("minecraft:bred_".length()));
            }

            return RENAMED_ENTITY_IDS.get(input);
        });

        ConverterAbstractStatsRename.register(VERSION, ImmutableMap.of(
                "minecraft:swim_one_cm", "minecraft:walk_on_water_one_cm",
                "minecraft:dive_one_cm", "minecraft:walk_under_water_one_cm"
        )::get);


        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:commandblock_minecart", "minecraft:command_block_minecart");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:ender_crystal", "minecraft:end_crystal");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:snowman", "minecraft:snow_golem");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:evocation_illager", "minecraft:evoker");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:evocation_fangs", "minecraft:evoker_fangs");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:illusion_illager", "minecraft:illusioner");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:vindication_illager", "minecraft:vindicator");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:villager_golem", "minecraft:iron_golem");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:xp_orb", "minecraft:experience_orb");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:xp_bottle", "minecraft:experience_bottle");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:eye_of_ender_signal", "minecraft:eye_of_ender");
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:fireworks_rocket", "minecraft:firework_rocket");
    }
}
