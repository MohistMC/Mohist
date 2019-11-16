package org.bukkit.craftbukkit.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging {
    public CraftHanging(CraftServer server, net.minecraft.entity.EntityHanging entity) {
        super(server, entity);
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        Block block = getLocation().getBlock().getRelative(getAttachedFace()).getRelative(face.getOppositeFace()).getRelative(getFacing());
        net.minecraft.entity.EntityHanging hanging = getHandle();
        int x = hanging.field_146063_b, y = hanging.field_146064_c, z = hanging.field_146062_d, dir = hanging.hangingDirection;
        hanging.field_146063_b = block.getX();
        hanging.field_146064_c = block.getY();
        hanging.field_146062_d = block.getZ();
        switch (face) {
            case SOUTH:
            default:
                getHandle().setDirection(0);
                break;
            case WEST:
                getHandle().setDirection(1);
                break;
            case NORTH:
                getHandle().setDirection(2);
                break;
            case EAST:
                getHandle().setDirection(3);
                break;
        }
        if (!force && !hanging.onValidSurface()) {
            // Revert since it doesn't fit
            hanging.field_146063_b = x;
            hanging.field_146064_c = y;
            hanging.field_146062_d = z;
            hanging.setDirection(dir);
            return false;
        }
        return true;
    }

    public BlockFace getFacing() {
        switch (this.getHandle().hangingDirection) {
            case 0:
            default:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.EAST;
        }
    }

    @Override
    public net.minecraft.entity.EntityHanging getHandle() {
        return (net.minecraft.entity.EntityHanging) entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
