package org.bukkit.craftbukkit.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryJukebox;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.JukeboxInventory;

public class CraftJukebox extends CraftBlockEntityState<JukeboxBlockEntity> implements Jukebox {

    public CraftJukebox(World world, JukeboxBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftJukebox(CraftJukebox state, Location location) {
        super(state, location);
    }

    @Override
    public JukeboxInventory getSnapshotInventory() {
        return new CraftInventoryJukebox(this.getSnapshot());
    }

    @Override
    public JukeboxInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryJukebox(this.getTileEntity());
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            Material record = this.getPlaying();
            this.getWorldHandle().setBlock(this.getPosition(), this.data, 3);

            BlockEntity tileEntity = this.getTileEntityFromWorld();
            if (tileEntity instanceof JukeboxBlockEntity jukebox) {
                CraftWorld world = (CraftWorld) this.getWorld();
                if (record.isAir()) {
                    jukebox.setRecordWithoutPlaying(ItemStack.EMPTY);
                    world.playEffect(this.getLocation(), Effect.IRON_DOOR_CLOSE, 0); // TODO: Fix this enum constant. This stops jukeboxes
                } else {
                    world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record);
                }
            }
        }

        return result;
    }

    @Override
    public Material getPlaying() {
        return this.getRecord().getType();
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftItemType.bukkitToMinecraft(record) == null) {
            record = Material.AIR;
        }

        this.setRecord(new org.bukkit.inventory.ItemStack(record));
    }

    @Override
    public boolean hasRecord() {
        return this.getHandle().getValue(JukeboxBlock.HAS_RECORD) && !this.getPlaying().isAir();
    }

    @Override
    public org.bukkit.inventory.ItemStack getRecord() {
        ItemStack record = this.getSnapshot().getTheItem();
        return CraftItemStack.asBukkitCopy(record);
    }

    @Override
    public void setRecord(org.bukkit.inventory.ItemStack record) {
        ItemStack nms = CraftItemStack.asNMSCopy(record);

        JukeboxBlockEntity snapshot = this.getSnapshot();
        snapshot.setRecordWithoutPlaying(nms);
        snapshot.recordStartedTick = snapshot.tickCount;
        snapshot.isPlaying = !nms.isEmpty();

        this.data = this.data.setValue(JukeboxBlock.HAS_RECORD, !nms.isEmpty());
    }

    @Override
    public boolean isPlaying() {
        this.requirePlaced();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        return tileEntity instanceof JukeboxBlockEntity jukebox && jukebox.isRecordPlaying();
    }

    @Override
    public boolean startPlaying() {
        this.requirePlaced();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof JukeboxBlockEntity jukebox)) {
            return false;
        }

        ItemStack record = jukebox.getTheItem();
        if (record.isEmpty() || this.isPlaying()) {
            return false;
        }

        jukebox.isPlaying = true;
        jukebox.recordStartedTick = jukebox.tickCount;
        this.getWorld().playEffect(this.getLocation(), Effect.RECORD_PLAY, CraftItemType.minecraftToBukkit(record.getItem()));
        return true;
    }

    @Override
    public void stopPlaying() {
        this.requirePlaced();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof JukeboxBlockEntity jukebox)) {
            return;
        }

        jukebox.isPlaying = false;
        this.getWorld().playEffect(this.getLocation(), Effect.IRON_DOOR_CLOSE, 0); // TODO: Fix this enum constant. This stops jukeboxes
    }

    @Override
    public boolean eject() {
        this.ensureNoWorldGeneration();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof JukeboxBlockEntity)) return false;

        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) tileEntity;
        boolean result = !jukebox.getTheItem().isEmpty();
        jukebox.popOutRecord();
        return result;
    }

    @Override
    public CraftJukebox copy() {
        return new CraftJukebox(this, null);
    }

    @Override
    public CraftJukebox copy(Location location) {
        return new CraftJukebox(this, location);
    }
}
