package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import java.util.HashMap;
import java.util.Map;

public final class V3447 {

    private static final int VERSION = MCVersions.V23W14A + 2;

    public static void register() {
        final String[] targets = new String[] {
                "minecraft:angler_pottery_shard",
                "minecraft:archer_pottery_shard",
                "minecraft:arms_up_pottery_shard",
                "minecraft:blade_pottery_shard",
                "minecraft:brewer_pottery_shard",
                "minecraft:burn_pottery_shard",
                "minecraft:danger_pottery_shard",
                "minecraft:explorer_pottery_shard",
                "minecraft:friend_pottery_shard",
                "minecraft:heart_pottery_shard",
                "minecraft:heartbreak_pottery_shard",
                "minecraft:howl_pottery_shard",
                "minecraft:miner_pottery_shard",
                "minecraft:mourner_pottery_shard",
                "minecraft:plenty_pottery_shard",
                "minecraft:prize_pottery_shard",
                "minecraft:sheaf_pottery_shard",
                "minecraft:shelter_pottery_shard",
                "minecraft:skull_pottery_shard",
                "minecraft:snort_pottery_shard"
        };
        // shard->sherd
        final Map<String, String> rename = new HashMap<>(targets.length);

        for (final String target : targets) {
            final String replace = target.replace("_pottery_shard", "_pottery_sherd");
            if (rename.put(target, replace) != null) {
                throw new IllegalArgumentException("Duplicate target " + target);
            }
        }

        ConverterAbstractItemRename.register(VERSION, rename::get);
    }
}
