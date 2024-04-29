package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterAbstractEntityRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import com.google.common.collect.ImmutableMap;

public final class V2509 {

    protected static final int VERSION = MCVersions.V20W08A + 2;

    private V2509() {}

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    public static void register() {
        ConverterAbstractItemRename.register(VERSION, ImmutableMap.of(
                "minecraft:zombie_pigman_spawn_egg", "minecraft:zombified_piglin_spawn_egg"
        )::get);
        ConverterAbstractEntityRename.register(VERSION, ImmutableMap.of(
                "minecraft:zombie_pigman", "minecraft:zombified_piglin"
        )::get);

        registerMob("minecraft:zombified_piglin");
    }
}
