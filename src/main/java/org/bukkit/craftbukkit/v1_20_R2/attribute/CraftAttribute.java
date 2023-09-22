package org.bukkit.craftbukkit.v1_20_R2.attribute;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R2.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;

public class CraftAttribute {

    public static Attribute minecraftToBukkit(net.minecraft.world.entity.ai.attributes.Attribute minecraft) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<net.minecraft.world.entity.ai.attributes.Attribute> registry = CraftRegistry.getMinecraftRegistry(Registries.ATTRIBUTE);
        Attribute bukkit = Registry.ATTRIBUTE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static Attribute stringToBukkit(String bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return Registry.ATTRIBUTE.get(NamespacedKey.fromString(bukkit));
    }

    public static net.minecraft.world.entity.ai.attributes.Attribute bukkitToMinecraft(Attribute bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.ATTRIBUTE)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }
}