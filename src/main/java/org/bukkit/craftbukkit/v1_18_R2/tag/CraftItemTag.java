package org.bukkit.craftbukkit.v1_18_R2.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;

public class CraftItemTag extends CraftTag<Item, Material> {

    public CraftItemTag(net.minecraft.core.Registry<Item> registry, TagKey<Item> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Material item) {
        return CraftMagicNumbers.getItem(item).builtInRegistryHolder().is(tag);
    }

    @Override
    public Set<Material> getValues() {
        return Collections.unmodifiableSet(getHandle().stream().map((item) -> CraftMagicNumbers.getMaterial(item.value())).collect(Collectors.toSet()));
    }
}
