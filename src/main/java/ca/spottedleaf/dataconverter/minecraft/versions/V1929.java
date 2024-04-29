package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

public final class V1929 {

    protected static final int VERSION = MCVersions.V19W04B + 2;

    private V1929() {}

    public static void register() {
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:wandering_trader", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "Inventory", fromVersion, toVersion);

            final MapType<String> offers = data.getMap("Offers");
            if (offers != null) {
                final ListType recipes = offers.getList("Recipes", ObjectType.MAP);
                if (recipes != null) {
                    for (int i = 0, len = recipes.size(); i < len; ++i) {
                        final MapType<String> recipe = recipes.getMap(i);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "buy", fromVersion, toVersion);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "buyB", fromVersion, toVersion);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "sell", fromVersion, toVersion);
                    }
                }
            }

            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "ArmorItems", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "HandItems", fromVersion, toVersion);

            return null;
        });
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:trader_llama", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, data, "SaddleItem", fromVersion, toVersion);
            WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, data, "DecorItem", fromVersion, toVersion);

            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "Items", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "ArmorItems", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "HandItems", fromVersion, toVersion);

            return null;
        });
    }
}
