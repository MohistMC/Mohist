package org.bukkit.craftbukkit.v1_20_R4.block;

import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<DaylightDetectorBlockEntity> implements DaylightDetector {

    public CraftDaylightDetector(World world, final DaylightDetectorBlockEntity te) {
        super(world, te);
    }

    protected CraftDaylightDetector(CraftDaylightDetector state, Location location) {
        super(state, location);
    }

    @Override
    public CraftDaylightDetector copy() {
        return new CraftDaylightDetector(this, null);
    }

    @Override
    public CraftDaylightDetector copy(Location location) {
        return new CraftDaylightDetector(this, location);
    }
}
