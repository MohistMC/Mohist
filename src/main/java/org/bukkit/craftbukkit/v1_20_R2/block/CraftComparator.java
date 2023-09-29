package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<ComparatorBlockEntity> implements Comparator {

    public CraftComparator(World world, final ComparatorBlockEntity te) {
        super(world, te);
    }

    protected CraftComparator(CraftComparator state) {
        super(state);
    }

    @Override
    public CraftComparator copy() {
        return new CraftComparator(this);
    }
}
