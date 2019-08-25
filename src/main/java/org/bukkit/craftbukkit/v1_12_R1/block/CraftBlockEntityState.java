package org.bukkit.craftbukkit.v1_12_R1.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftBlockEntityState<T extends TileEntity> extends CraftBlockState<T> {

    private final Class<T> tileEntityClass;
    private T tileEntity;
    private T snapshotTileEntity;

    public CraftBlockEntityState(Block block, Class<T> tileEntityClass) {
        super(block);
        this.tileEntityClass = tileEntityClass;
    }

    public CraftBlockEntityState(Material material, T tileEntity) {
        super(material, tileEntity);
        this.tileEntityClass = (Class<T>) tileEntity.getClass();
    }

    // gets the wrapped TileEntity
    @Override
    public T getTileEntity() { // Paper - protected -> public
        load();
        return tileEntity;
    }

    // gets the cloned TileEntity which is used to store the captured data
    protected T getSnapshot() {
        load();
        return snapshotTileEntity;
    }

    // gets the current TileEntity from the world at this position
    protected TileEntity getTileEntityFromWorld() {
        requirePlaced();
        return captureTileEntityFromWorld();
    }

    // gets the NBT data of the TileEntity represented by this block state
    public NBTTagCompound getSnapshotNBT() {
        return super.getSnapshotNBT();
    }

    // copies the data of the given tile entity to this block state
    protected void load(T tileEntity) {
        super.load(tileEntity);
    }

    // applies the TileEntity data of this block state to the given TileEntity
    protected void applyTo(T tileEntity) {
        load();
        if (tileEntity != null && tileEntity != getSnapshot()) {
            BlockPos pos = tileEntity.getPos();
            tileEntity.readFromNBT(getSnapshotNBT());
            tileEntity.setPos(pos);
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
    public void setTileEntity(T tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void setSnapshotTileEntity(T snapshotTileEntity) {
        this.snapshotTileEntity = snapshotTileEntity;
    }
}
