package org.bukkit.craftbukkit.v1_20_R1.attribute;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;

public class CraftAttributeMap implements Attributable {

    private final net.minecraft.world.entity.ai.attributes.AttributeMap handle;
    // Paper start - convert legacy attributes
    private static final com.google.common.collect.ImmutableMap<String, String> legacyNMS = com.google.common.collect.ImmutableMap.<String, String>builder().put("generic.maxHealth", "generic.max_health").put("Max Health", "generic.max_health").put("zombie.spawnReinforcements", "zombie.spawn_reinforcements").put("Spawn Reinforcements Chance", "zombie.spawn_reinforcements").put("horse.jumpStrength", "horse.jump_strength").put("Jump Strength", "horse.jump_strength").put("generic.followRange", "generic.follow_range").put("Follow Range", "generic.follow_range").put("generic.knockbackResistance", "generic.knockback_resistance").put("Knockback Resistance", "generic.knockback_resistance").put("generic.movementSpeed", "generic.movement_speed").put("Movement Speed", "generic.movement_speed").put("generic.flyingSpeed", "generic.flying_speed").put("Flying Speed", "generic.flying_speed").put("generic.attackDamage", "generic.attack_damage").put("generic.attackKnockback", "generic.attack_knockback").put("generic.attackSpeed", "generic.attack_speed").put("generic.armorToughness", "generic.armor_toughness").build();

    public static String convertIfNeeded(String nms) {
        if (nms == null) {
            return null;
        }
        nms = legacyNMS.getOrDefault(nms, nms);
        if (!nms.toLowerCase().equals(nms) || nms.indexOf(' ') != -1) {
            return null;
        }
        return nms;
    }
    // Paper end

    public CraftAttributeMap(net.minecraft.world.entity.ai.attributes.AttributeMap handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        net.minecraft.world.entity.ai.attributes.AttributeInstance nms = handle.getInstance(toMinecraft(attribute));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }

    // Paper start
    @Override
    public void registerAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        handle.registerAttribute(CraftAttributeMap.toMinecraft(attribute));
    }
    // Paper end

    public static net.minecraft.world.entity.ai.attributes.Attribute toMinecraft(Attribute attribute) {
        return BuiltInRegistries.ATTRIBUTE.get(CraftNamespacedKey.toMinecraft(attribute.getKey()));
    }

    public static Attribute fromMinecraft(String nms) {
        return Registry.ATTRIBUTE.get(CraftNamespacedKey.fromString(nms));
    }
}
