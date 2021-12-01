package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<DaylightDetectorBlockEntity> implements DaylightDetector {

    public CraftDaylightDetector(World world, final DaylightDetectorBlockEntity te) {
        super(world, te);
    }
}
