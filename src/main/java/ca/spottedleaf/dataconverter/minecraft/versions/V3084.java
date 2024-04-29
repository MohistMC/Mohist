package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.ConverterAbstractStringValueTypeRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.util.NamespaceUtil;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public final class V3084 {

    protected static final int VERSION = MCVersions.V22W12A + 2;

    protected static final Map<String, String> GAME_EVENT_RENAMES = new HashMap<>(ImmutableMap.<String, String>builder()
            .put("minecraft:block_press", "minecraft:block_activate")
            .put("minecraft:block_switch", "minecraft:block_activate")
            .put("minecraft:block_unpress", "minecraft:block_deactivate")
            .put("minecraft:block_unswitch", "minecraft:block_deactivate")
            .put("minecraft:drinking_finish", "minecraft:drink")
            .put("minecraft:elytra_free_fall", "minecraft:elytra_glide")
            .put("minecraft:entity_damaged", "minecraft:entity_damage")
            .put("minecraft:entity_dying", "minecraft:entity_die")
            .put("minecraft:entity_killed", "minecraft:entity_die")
            .put("minecraft:mob_interact", "minecraft:entity_interact")
            .put("minecraft:ravager_roar", "minecraft:entity_roar")
            .put("minecraft:ring_bell", "minecraft:block_change")
            .put("minecraft:shulker_close", "minecraft:container_close")
            .put("minecraft:shulker_open", "minecraft:container_open")
            .put("minecraft:wolf_shaking", "minecraft:entity_shake")
            .build()
    );

    public static void register() {
        ConverterAbstractStringValueTypeRename.register(VERSION, MCTypeRegistry.GAME_EVENT_NAME, (final String name) -> {
            return GAME_EVENT_RENAMES.get(NamespaceUtil.correctNamespace(name));
        });
    }
}
