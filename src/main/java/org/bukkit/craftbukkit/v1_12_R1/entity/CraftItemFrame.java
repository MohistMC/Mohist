package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.Validate;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame {
    public CraftItemFrame(CraftServer server, EntityItemFrame entity) {
        super(server, entity);
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

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (!super.setFacingDirection(face, force)) {
            return false;
        }

        update();

        return true;
    }

    private void update() {
        EntityItemFrame old = this.getHandle();

        WorldServer world = ((CraftWorld) getWorld()).getHandle();
        BlockPos position = old.getHangingPosition();
        EnumFacing direction = old.getHorizontalFacing();
        ItemStack item = old.getDisplayedItem() != null ? old.getDisplayedItem().copy() : null;

        old.setDead();

        EntityItemFrame frame = new EntityItemFrame(world, position, direction);
        frame.setDisplayedItem(item);
        world.spawnEntity(frame);
        this.entity = frame;
    }

    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getDisplayedItem());
    }

    public void setItem(org.bukkit.inventory.ItemStack item) {
        getHandle().setDisplayedItem(CraftItemStack.asNMSCopy(item));
    }

    public Rotation getRotation() {
        return toBukkitRotation(getHandle().getRotation());
    }

    public void setRotation(Rotation rotation) {
        Validate.notNull(rotation, "Rotation cannot be null");
        getHandle().setItemRotation(toInteger(rotation));
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
    public EntityItemFrame getHandle() {
        return (EntityItemFrame) entity;
    }

    @Override
    public String toString() {
        return "CraftItemFrame{item=" + getItem() + ", rotation=" + getRotation() + "}";
    }

    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }
}
