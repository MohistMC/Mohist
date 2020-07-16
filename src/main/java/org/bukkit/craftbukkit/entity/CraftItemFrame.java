package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang.Validate;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame {
    public CraftItemFrame(CraftServer server, ItemFrameEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        AbstractDecorationEntity hanging = getHandle();
        Direction oldDir = hanging.getHorizontalFacing();
        Direction newDir = CraftBlock.blockFaceToNotch(face);

        getHandle().setFacing(newDir);
        if (!force && !hanging.canStayAttached()) {
            hanging.setFacing(oldDir);
            return false;
        }

        update();

        return true;
    }

    private void update() {
        ItemFrameEntity old = this.getHandle();

        ServerWorld world = ((CraftWorld) getWorld()).getHandle();
        BlockPos position = old.getDecorationBlockPos();
        Direction direction = old.getHorizontalFacing();
        ItemStack item = old.getHeldItemStack() != null ? old.getHeldItemStack().copy() : null;

        old.remove();

        ItemFrameEntity frame = new ItemFrameEntity(world, position, direction);
        frame.setHeldItemStack(item);
        world.spawnEntity(frame);
        this.entity = frame;
    }

    @Override
    public void setItem(org.bukkit.inventory.ItemStack item) {
        setItem(item, true);
    }

    @Override
    public void setItem(org.bukkit.inventory.ItemStack item, boolean playSound) {
        getHandle().setItem(CraftItemStack.asNMSCopy(item), true, playSound);
    }

    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getHeldItemStack());
    }

    @Override
    public Rotation getRotation() {
        return toBukkitRotation(getHandle().getRotation());
    }

    Rotation toBukkitRotation(int value) {
        // Translate NMS rotation integer to Bukkit API
        switch (value) {
        case 0:
            return Rotation.NONE;
        case 1:
            return Rotation.CLOCKWISE_45;
        case 2:
            return Rotation.CLOCKWISE;
        case 3:
            return Rotation.CLOCKWISE_135;
        case 4:
            return Rotation.FLIPPED;
        case 5:
            return Rotation.FLIPPED_45;
        case 6:
            return Rotation.COUNTER_CLOCKWISE;
        case 7:
            return Rotation.COUNTER_CLOCKWISE_45;
        default:
            throw new AssertionError("Unknown rotation " + value + " for " + getHandle());
        }
    }

    @Override
    public void setRotation(Rotation rotation) {
        Validate.notNull(rotation, "Rotation cannot be null");
        getHandle().setRotation(toInteger(rotation));
    }

    static int toInteger(Rotation rotation) {
        // Translate Bukkit API rotation to NMS integer
        switch (rotation) {
        case NONE:
            return 0;
        case CLOCKWISE_45:
            return 1;
        case CLOCKWISE:
            return 2;
        case CLOCKWISE_135:
            return 3;
        case FLIPPED:
            return 4;
        case FLIPPED_45:
            return 5;
        case COUNTER_CLOCKWISE:
            return 6;
        case COUNTER_CLOCKWISE_45:
            return 7;
        default:
            throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
        }
    }

    @Override
    public ItemFrameEntity getHandle() {
        return (ItemFrameEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftItemFrame{item=" + getItem() + ", rotation=" + getRotation() + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }
}
