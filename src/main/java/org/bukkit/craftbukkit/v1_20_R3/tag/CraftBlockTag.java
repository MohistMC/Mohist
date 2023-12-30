package org.bukkit.craftbukkit.v1_20_R3.tag;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlockType;

public class CraftBlockTag extends CraftTag<Block, Material> {

    public CraftBlockTag(net.minecraft.core.Registry<Block> registry, TagKey<Block> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Material item) {
        Block block = CraftBlockType.bukkitToMinecraft(item);

        // SPIGOT-6952: A Material is not necessary a block, in this case return false
        if (block == null) {
            return false;
        }

        return block.builtInRegistryHolder().is(tag);
    }

    @Override
    public Set<Material> getValues() {
        return  getHandle().stream().map((block) -> CraftBlockType.minecraftToBukkit(block.value())).collect(Collectors.toUnmodifiableSet());
    }
}
