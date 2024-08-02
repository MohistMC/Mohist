package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.advancements.ConverterAbstractAdvancementsRename;
import ca.spottedleaf.dataconverter.minecraft.converters.recipe.ConverterAbstractRecipeRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V2100 {

    protected static final int VERSION = MCVersions.V1_14_4 + 124;
    protected static final Map<String, String> RECIPE_RENAMES = ImmutableMap.of(
            "minecraft:sugar", "sugar_from_sugar_cane"
    );

    private V2100() {}

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    public static void register() {
        ConverterAbstractRecipeRename.register(VERSION, RECIPE_RENAMES::get);
        ConverterAbstractAdvancementsRename.register(VERSION, ImmutableMap.of(
                "minecraft:recipes/misc/sugar", "minecraft:recipes/misc/sugar_from_sugar_cane"
        )::get);

        registerMob("minecraft:bee");
        registerMob("minecraft:bee_stinger");
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:beehive", (data, fromVersion, toVersion) -> {
            final ListType bees = data.getList("Bees", ObjectType.MAP);
            if (bees != null) {
                for (int i = 0, len = bees.size(); i < len; ++i) {
                    WalkerUtils.convert(MCTypeRegistry.ENTITY, bees.getMap(i), "EntityData", fromVersion, toVersion);
                }
            }

            return null;
        });
    }
}
