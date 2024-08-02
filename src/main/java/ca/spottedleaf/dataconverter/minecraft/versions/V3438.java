package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.RenameHelper;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import ca.spottedleaf.dataconverter.minecraft.converters.tileentity.ConverterAbstractTileEntityRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

import java.util.HashMap;
import java.util.Map;

public final class V3438 {

    public static final int VERSION = MCVersions.V1_19_4 + 101;

    public static void register() {
        // brushable block rename
        MCTypeRegistry.TILE_ENTITY.copyWalkers(VERSION,"minecraft:suspicious_sand", "minecraft:brushable_block");

        ConverterAbstractTileEntityRename.register(VERSION, new HashMap<>(Map.of(
                "minecraft:suspicious_sand", "minecraft:brushable_block"
        ))::get);


        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:brushable_block", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                RenameHelper.renameSingle(data, "loot_table", "LootTable");
                RenameHelper.renameSingle(data, "loot_table_seed", "LootTableSeed");
                return null;
            }
        });

        ConverterAbstractItemRename.register(VERSION, new HashMap<>(
                Map.of(
                        "minecraft:pottery_shard_archer", "minecraft:archer_pottery_shard",
                        "minecraft:pottery_shard_prize", "minecraft:prize_pottery_shard",
                        "minecraft:pottery_shard_arms_up", "minecraft:arms_up_pottery_shard",
                        "minecraft:pottery_shard_skull", "minecraft:skull_pottery_shard"
                )
        )::get);
    }
}
