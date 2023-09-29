package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
import org.bukkit.World;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<DaylightDetectorBlockEntity> implements DaylightDetector {

    public CraftDaylightDetector(World world, final DaylightDetectorBlockEntity te) {
        super(world, te);
    }

    protected CraftDaylightDetector(CraftDaylightDetector state) {
        super(state);
    }

    @Override
    public CraftDaylightDetector copy() {
        return new CraftDaylightDetector(this);
    }
}
