package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.util.math.Direction;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging {
    public CraftHanging(CraftServer server, AbstractDecorationEntity entity) {
        super(server, entity);
    }

    @Override
    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        AbstractDecorationEntity hanging = getHandle();
        Direction dir = hanging.getHorizontalFacing();
        switch (face) {
            case SOUTH:
            default:
                getHandle().setFacing(Direction.SOUTH);
                break;
            case WEST:
                getHandle().setFacing(Direction.WEST);
                break;
            case NORTH:
                getHandle().setFacing(Direction.NORTH);
                break;
            case EAST:
                getHandle().setFacing(Direction.EAST);
                break;
        }
        if (!force && !hanging.canStayAttached()) {
            // Revert since it doesn't fit
            hanging.setFacing(dir);
            return false;
        }
        return true;
    }

    @Override
    public BlockFace getFacing() {
        Direction direction = this.getHandle().getHorizontalFacing();
        if (direction == null) return BlockFace.SELF;
        return CraftBlock.notchToBlockFace(direction);
    }

    @Override
    public AbstractDecorationEntity getHandle() {
        return (AbstractDecorationEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
