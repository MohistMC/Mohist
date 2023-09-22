package org.bukkit.craftbukkit.v1_20_R2.tag;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.tags.TagKey;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntityType;
import org.bukkit.entity.EntityType;

public class CraftEntityTag extends CraftTag<net.minecraft.world.entity.EntityType<?>, EntityType> {

    public CraftEntityTag(net.minecraft.core.Registry<net.minecraft.world.entity.EntityType<?>> registry, TagKey<net.minecraft.world.entity.EntityType<?>> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(EntityType entity) {
        return CraftEntityType.bukkitToMinecraft(entity).is(tag);
    }

    @Override
    public Set<EntityType> getValues() {
        return getHandle().stream().map(net.minecraft.core.Holder::value).map(CraftEntityType::minecraftToBukkit).collect(Collectors.toUnmodifiableSet());
    }
}