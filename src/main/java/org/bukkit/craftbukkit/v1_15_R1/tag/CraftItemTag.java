package org.bukkit.craftbukkit.v1_15_R1.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tags.NetworkTagCollection;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;

public class CraftItemTag extends CraftTag<Item, Material> {

    public CraftItemTag(NetworkTagCollection<Item> registry, ResourceLocation tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Material item) {
        return getHandle().contains(CraftMagicNumbers.getItem(item));
    }

    @Override
    public Set<Material> getValues() {
        return Collections.unmodifiableSet(getHandle().getAllElements().stream().map((item) -> CraftMagicNumbers.getMaterial(item)).collect(Collectors.toSet()));
    }
}
