package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V2501 {

    protected static final int VERSION = MCVersions.V1_15_2 + 271;

    private V2501() {}

    private static void registerFurnace(final String id) {
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, id, (data, fromVersion, toVersion) -> {
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "Items", fromVersion, toVersion);

            WalkerUtils.convertKeys(MCTypeRegistry.RECIPE, data, "RecipesUsed", fromVersion, toVersion);

            return null;
        });
    }

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> converter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int recipesUsedSize = data.getInt("RecipesUsedSize");
                data.remove("RecipesUsedSize");

                if (recipesUsedSize <= 0) {
                    return null;
                }

                final MapType<String> newRecipes = Types.NBT.createEmptyMap();
                data.setMap("RecipesUsed", newRecipes);

                for (int i = 0; i < recipesUsedSize; ++i) {
                    final String recipeKey = data.getString("RecipeLocation" + i);
                    data.remove("RecipeLocation" + i);
                    final int recipeAmount = data.getInt("RecipeAmount" + i);
                    data.remove("RecipeAmount" + i);

                    if (i <= 0 || recipeKey == null) {
                        continue;
                    }

                    newRecipes.setInt(recipeKey, recipeAmount);
                }

                return null;
            }
        };

        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:furnace", converter);
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:blast_furnace", converter);
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:smoker", converter);

        registerFurnace("minecraft:furnace");
        registerFurnace("minecraft:smoker");
        registerFurnace("minecraft:blast_furnace");
    }
}
