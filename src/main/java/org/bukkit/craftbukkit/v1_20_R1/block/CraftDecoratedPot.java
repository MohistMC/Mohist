package org.bukkit.craftbukkit.v1_20_R1.block;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.DecoratedPot;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;

public class CraftDecoratedPot extends CraftBlockEntityState<DecoratedPotBlockEntity> implements DecoratedPot {

    public CraftDecoratedPot(World world, DecoratedPotBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public List<Material> getShards() {
        return getSnapshot().getDecorations().sorted().map(CraftMagicNumbers::getMaterial).collect(Collectors.toUnmodifiableList());
    }
}
