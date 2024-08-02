package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V1022 {

    protected static final int VERSION = MCVersions.V17W06A;

    public static void register() {
        MCTypeRegistry.PLAYER.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final MapType<String> rootVehicle = data.getMap("RootVehicle");
            if (rootVehicle != null) {
                WalkerUtils.convert(MCTypeRegistry.ENTITY, rootVehicle, "Entity", fromVersion, toVersion);
            }

            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "Inventory", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "EnderItems", fromVersion, toVersion);

            WalkerUtils.convert(MCTypeRegistry.ENTITY, data, "ShoulderEntityLeft", fromVersion, toVersion);
            WalkerUtils.convert(MCTypeRegistry.ENTITY, data, "ShoulderEntityRight", fromVersion, toVersion);

            final MapType<String> recipeBook = data.getMap("recipeBook");
            if (recipeBook != null) {
                WalkerUtils.convertList(MCTypeRegistry.RECIPE, recipeBook, "recipes", fromVersion, toVersion);
                WalkerUtils.convertList(MCTypeRegistry.RECIPE, recipeBook, "toBeDisplayed", fromVersion, toVersion);
            }

            return null;
        });

        MCTypeRegistry.HOTBAR.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            for (final String key : data.keys()) {
                WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, key, fromVersion, toVersion);
            }

            return null;
        });
    }

    private V1022() {}

}
