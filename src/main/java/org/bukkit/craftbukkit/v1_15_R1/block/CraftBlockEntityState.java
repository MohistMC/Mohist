package org.bukkit.craftbukkit.v1_15_R1.block;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.persistence.PersistentDataContainer;

public class CraftBlockEntityState<T extends TileEntity> extends CraftBlockState implements TileState {

    private final Class<T> tileEntityClass;
    private final T tileEntity;
    private final T snapshot;

    public CraftBlockEntityState(Block block, Class<T> tileEntityClass) {
        super(block);

        this.tileEntityClass = tileEntityClass;

        // get tile entity from block:
        CraftWorld world = (CraftWorld) this.getWorld();
        this.tileEntity = tileEntityClass.cast(world.getHandle().getTileEntity(this.getPosition()));
        Preconditions.checkState(this.tileEntity != null, "Tile is null, asynchronous access? " + block);

        // copy tile entity data:
        this.snapshot = this.createSnapshot(tileEntity);
        this.load(snapshot);
    }

    public CraftBlockEntityState(Material material, T tileEntity) {
        super(material);

        this.tileEntityClass = (Class<T>) tileEntity.getClass();
        this.tileEntity = tileEntity;

        // copy tile entity data:
        this.snapshot = this.createSnapshot(tileEntity);
        this.load(snapshot);
    }

    private T createSnapshot(T tileEntity) {
        if (tileEntity == null) {
            return null;
        }

        CompoundNBT nbtTagCompound = tileEntity.write(new CompoundNBT());
        T snapshot = (T) TileEntity.create(nbtTagCompound);

        return snapshot;
    }

    // copies the TileEntity-specific data, retains the position
    private void copyData(T from, T to) {
        BlockPos pos = to.getPos();
        CompoundNBT nbtTagCompound = from.write(new CompoundNBT());
        to.read(nbtTagCompound);

        // reset the original position:
        to.setPos(pos);
    }

    // gets the wrapped TileEntity
    protected T getTileEntity() {
        return tileEntity;
    }

    // gets the cloned TileEntity which is used to store the captured data
    protected T getSnapshot() {
        return snapshot;
    }

    // gets the current TileEntity from the world at this position
    protected TileEntity getTileEntityFromWorld() {
        requirePlaced();

        return ((CraftWorld) this.getWorld()).getHandle().getTileEntity(this.getPosition());
    }

    // gets the NBT data of the TileEntity represented by this block state
    public CompoundNBT getSnapshotNBT() {
        // update snapshot
        applyTo(snapshot);

        return snapshot.write(new CompoundNBT());
    }

    // copies the data of the given tile entity to this block state
    protected void load(T tileEntity) {
        if (tileEntity != null && tileEntity != snapshot) {
            copyData(tileEntity, snapshot);
        }
    }

    // applies the TileEntity data of this block state to the given TileEntity
    protected void applyTo(T tileEntity) {
        if (tileEntity != null && tileEntity != snapshot) {
            copyData(snapshot, tileEntity);
        }
    }

    protected boolean isApplicable(TileEntity tileEntity) {
        return tileEntityClass.isInstance(tileEntity);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            TileEntity tile = getTileEntityFromWorld();

            if (isApplicable(tile)) {
                applyTo(tileEntityClass.cast(tile));
                tile.markDirty();
            }
        }

        return result;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.getSnapshot().persistentDataContainer;
    }
}
