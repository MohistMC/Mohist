package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<ComparatorBlockEntity> implements Comparator {

    public CraftComparator(World world, final ComparatorBlockEntity te) {
        super(world, te);
    }
}
