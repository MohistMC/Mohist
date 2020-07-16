package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.ComparatorBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<ComparatorBlockEntity> implements Comparator {

    public CraftComparator(final Block block) {
        super(block, ComparatorBlockEntity.class);
    }

    public CraftComparator(final Material material, final ComparatorBlockEntity te) {
        super(material, te);
    }
}
