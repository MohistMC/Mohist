package ca.spottedleaf.dataconverter.minecraft.converters.itemstack;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.HashMap;
import java.util.Map;

public final class ConverterFlattenSpawnEgg extends DataConverter<MapType<String>, MapType<String>> {

    private static final Map<String, String> ENTITY_ID_TO_NEW_EGG_ID = new HashMap<>();
    static {
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:bat", "minecraft:bat_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:blaze", "minecraft:blaze_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:cave_spider", "minecraft:cave_spider_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:chicken", "minecraft:chicken_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:cow", "minecraft:cow_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:creeper", "minecraft:creeper_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:donkey", "minecraft:donkey_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:elder_guardian", "minecraft:elder_guardian_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:enderman", "minecraft:enderman_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:endermite", "minecraft:endermite_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:evocation_illager", "minecraft:evocation_illager_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:ghast", "minecraft:ghast_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:guardian", "minecraft:guardian_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:ender_dragon", "minecraft:ender_dragon_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:horse", "minecraft:horse_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:husk", "minecraft:husk_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:iron_golem", "minecraft:iron_golem_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:llama", "minecraft:llama_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:magma_cube", "minecraft:magma_cube_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:mooshroom", "minecraft:mooshroom_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:mule", "minecraft:mule_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:ocelot", "minecraft:ocelot_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:pufferfish", "minecraft:pufferfish_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:parrot", "minecraft:parrot_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:pig", "minecraft:pig_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:polar_bear", "minecraft:polar_bear_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:rabbit", "minecraft:rabbit_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:sheep", "minecraft:sheep_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:shulker", "minecraft:shulker_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:silverfish", "minecraft:silverfish_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:skeleton", "minecraft:skeleton_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:skeleton_horse", "minecraft:skeleton_horse_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:slime", "minecraft:slime_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:snow_golem", "minecraft:snow_golem_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:spider", "minecraft:spider_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:squid", "minecraft:squid_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:stray", "minecraft:stray_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:turtle", "minecraft:turtle_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:vex", "minecraft:vex_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:villager", "minecraft:villager_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:vindication_illager", "minecraft:vindication_illager_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:witch", "minecraft:witch_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:wither", "minecraft:wither_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:wither_skeleton", "minecraft:wither_skeleton_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:wolf", "minecraft:wolf_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:zombie", "minecraft:zombie_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:zombie_horse", "minecraft:zombie_horse_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:zombie_pigman", "minecraft:zombie_pigman_spawn_egg");
        ENTITY_ID_TO_NEW_EGG_ID.put("minecraft:zombie_villager", "minecraft:zombie_villager_spawn_egg");
    }

    public ConverterFlattenSpawnEgg(final int version, final int versionStep) {
        super(version, versionStep);
    }

    @Override
    public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
        final MapType<String> tag = data.getMap("tag");
        if (tag == null) {
            return null;
        }

        final MapType<String> entityTag = tag.getMap("EntityTag");
        if (entityTag == null) {
            return null;
        }

        final String id = entityTag.getString("id");
        if (id != null) {
            data.setString("id", ENTITY_ID_TO_NEW_EGG_ID.getOrDefault(id, "minecraft:pig_spawn_egg"));
        }

        return null;
    }
}
