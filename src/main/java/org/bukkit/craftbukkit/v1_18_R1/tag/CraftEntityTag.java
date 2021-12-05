package org.bukkit.craftbukkit.v1_18_R1.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagCollection;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;

public class CraftEntityTag extends CraftTag<net.minecraft.world.entity.EntityType<?>, EntityType> {

    public CraftEntityTag(TagCollection<net.minecraft.world.entity.EntityType<?>> registry, ResourceLocation tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(EntityType entity) {
        return getHandle().contains(net.minecraft.core.Registry.ENTITY_TYPE.get(CraftNamespacedKey.toMinecraft(entity.getKey())));
    }

    @Override
    public Set<EntityType> getValues() {
        return Collections.unmodifiableSet(getHandle().getValues().stream().map((nms) -> Registry.ENTITY_TYPE.get(CraftNamespacedKey.fromMinecraft(net.minecraft.world.entity.EntityType.getKey(nms)))).collect(Collectors.toSet()));
    }
}