package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.game_event.GameEventListenerWalker;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;

public final class V3081 {

    protected static final int VERSION = MCVersions.V22W11A + 1;

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    public static void register() {
        registerMob("minecraft:warden");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:warden", new GameEventListenerWalker());
    }
}
