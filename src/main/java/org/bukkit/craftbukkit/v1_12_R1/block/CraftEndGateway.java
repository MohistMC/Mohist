package org.bukkit.craftbukkit.v1_12_R1.block;

import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;

import java.util.Objects;

public class CraftEndGateway extends CraftBlockEntityState<TileEntityEndGateway> implements EndGateway {

    public CraftEndGateway(Block block) {
        super(block, TileEntityEndGateway.class);
    }

    public CraftEndGateway(final Material material, TileEntityEndGateway te) {
        super(material, te);
    }

    @Override
    public Location getExitLocation() {
        BlockPos pos = this.getSnapshot().exitPortal;
        return pos == null ? null : new Location(this.isPlaced() ? this.getWorld() : null, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setExitLocation(Location location) {
        if (location == null) {
            this.getSnapshot().exitPortal = null;
        } else if (!Objects.equals(location.getWorld(), this.isPlaced() ? this.getWorld() : null)) {
            throw new IllegalArgumentException("Cannot set exit location to different world");
        } else {
            this.getSnapshot().exitPortal = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }

    @Override
    public boolean isExactTeleport() {
        return this.getSnapshot().exactTeleport;
    }

    @Override
    public void setExactTeleport(boolean exact) {
        this.getSnapshot().exactTeleport = exact;
    }

    @Override
    public void applyTo(TileEntityEndGateway endGateway) {
        super.applyTo(endGateway);

        if (this.getSnapshot().exitPortal == null) {
            endGateway.exitPortal = null;
        }
    }
}
