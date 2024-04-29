package org.bukkit.craftbukkit.block.banner;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftPatternType {

    public static PatternType minecraftToBukkit(BannerPattern minecraft) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<BannerPattern> registry = CraftRegistry.getMinecraftRegistry(Registries.BANNER_PATTERN);
        PatternType bukkit = Registry.BANNER_PATTERN.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static PatternType minecraftHolderToBukkit(Holder<BannerPattern> minecraft) {
        return CraftPatternType.minecraftToBukkit(minecraft.value());
    }

    public static BannerPattern bukkitToMinecraft(PatternType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.BANNER_PATTERN)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }

    public static Holder<BannerPattern> bukkitToMinecraftHolder(PatternType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        net.minecraft.core.Registry<BannerPattern> registry = CraftRegistry.getMinecraftRegistry(Registries.BANNER_PATTERN);

        if (registry.wrapAsHolder(CraftPatternType.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<BannerPattern> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own banner pattern without properly registering it.");
    }
}
