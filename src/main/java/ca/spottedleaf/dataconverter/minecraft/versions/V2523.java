package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V2523 {

    protected static final int VERSION = MCVersions.V20W13B + 2;

    private static final Map<String, String> RENAMES = ImmutableMap.<String, String>builder()
            .put("generic.maxHealth", "generic.max_health")
            .put("Max Health", "generic.max_health")
            .put("zombie.spawnReinforcements", "zombie.spawn_reinforcements")
            .put("Spawn Reinforcements Chance", "zombie.spawn_reinforcements")
            .put("horse.jumpStrength", "horse.jump_strength")
            .put("Jump Strength", "horse.jump_strength")
            .put("generic.followRange", "generic.follow_range")
            .put("Follow Range", "generic.follow_range")
            .put("generic.knockbackResistance", "generic.knockback_resistance")
            .put("Knockback Resistance", "generic.knockback_resistance")
            .put("generic.movementSpeed", "generic.movement_speed")
            .put("Movement Speed", "generic.movement_speed")
            .put("generic.flyingSpeed", "generic.flying_speed")
            .put("Flying Speed", "generic.flying_speed")
            .put("generic.attackDamage", "generic.attack_damage")
            .put("generic.attackKnockback", "generic.attack_knockback")
            .put("generic.attackSpeed", "generic.attack_speed")
            .put("generic.armorToughness", "generic.armor_toughness")
            .build();

    private V2523() {}

    private static void updateName(final MapType<String> data, final String path) {
        if (data == null) {
            return;
        }

        final String name = data.getString(path);
        if (name != null) {
            final String renamed = RENAMES.get(name);
            if (renamed != null) {
                data.setString(path, renamed);
            }
        }
    }

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> entityConverter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final ListType attributes = data.getList("Attributes", ObjectType.MAP);

                if (attributes == null) {
                    return null;
                }

                for (int i = 0, len = attributes.size(); i < len; ++i) {
                    updateName(attributes.getMap(i), "Name");
                }

                return null;
            }
        };

        MCTypeRegistry.ENTITY.addStructureConverter(entityConverter);
        MCTypeRegistry.PLAYER.addStructureConverter(entityConverter);

        MCTypeRegistry.ITEM_STACK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final ListType attributes = data.getList("AttributeModifiers", ObjectType.MAP);

                if (attributes == null) {
                    return null;
                }

                for (int i = 0, len = attributes.size(); i < len; ++i) {
                    updateName(attributes.getMap(i), "AttributeName");
                }

                return null;
            }
        });
    }

}
