package org.bukkit.craftbukkit.v1_12_R1.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.BlockSnapshot;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Objects;

public class CraftBlockState<T extends TileEntity> implements BlockState {
    private final CraftWorld world;
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    private NBTTagCompound snapshotNBT;
    protected int type;
    protected MaterialData data;
    protected int flag;
    private boolean blockSnapshot;
    private boolean init;

    public CraftBlockState(Block block) {
        this(block, 3);
    }

    public CraftBlockState(final Block block, int flag) {
        this.world = (CraftWorld) block.getWorld();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.type = block.getTypeId();
        this.chunk = (CraftChunk) block.getChunk();
        this.flag = flag;
        createData(block.getData());
    }

    public CraftBlockState(Material material, TileEntity tileEntity) {
        this(tileEntity.getWorld().getWorld().getBlockAt(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()));
        type = material.getId();
    }

    public CraftBlockState(BlockSnapshot blocksnapshot) {
        this.blockSnapshot = true;
        this.world = blocksnapshot.getWorld().getWorld();
        this.x = blocksnapshot.getPos().getX();
        this.y = blocksnapshot.getPos().getY();
        this.z = blocksnapshot.getPos().getZ();
        this.type = net.minecraft.block.Block.getIdFromBlock(blocksnapshot.getReplacedBlock().getBlock());
        this.chunk = (CraftChunk) this.world.getBlockAt(this.x, this.y, this.z).getChunk();
        this.flag = 3;
        this.snapshotNBT = blocksnapshot.getNbt();
        this.createData((byte) blocksnapshot.getMeta());
    }

    /**
     * 根据当前坐标,刷新快照数据
     * 可以反复调用,但只会初始化一次
     */
    protected void load() {
        if (isBlockSnapshot()) {
            return;
        }
        if (init) {
            return;
        }
        init = true;
        captureSnapshotFromWorld();
    }

    /**
     * 回调,在这里面获取自己所需的数据
     *
     * @param tileEntity 真实对象
     */
    protected void load(T tileEntity) {

    }

    /**
     * 根据当前坐标,刷新快照数据
     * 可以反复调用,但只会初始化一次
     */
    protected void reload() {
        if (isBlockSnapshot()) {
            return;
        }
        init = false;
        load();
    }

    /**
     * 从World中获取TileEntity对象
     */
    protected T captureTileEntityFromWorld() {
//        子类需要TileEntity请复写此方法
        T tileEntity = (T) world.getHandle().getTileEntity(new BlockPos(this.x, this.y, this.z));
        setTileEntity(tileEntity);
        return tileEntity;
    }

    /**
     * 从世界中读取数据创建快照
     */
    protected void captureSnapshotFromWorld() {
        captureSnapshotFromTileEntity(captureTileEntityFromWorld());
    }

    /**
     * 用给定的TileEntity创建快照
     */
    protected void captureSnapshotFromTileEntity(T tileEntity) {
        captureSnapshotTileEntityFromTileEntity(tileEntity);
    }

    /**
     * 从world中获取当前位置的快照TileEntity
     * @return
     */
    protected T captureSnapshotTileEntityFromTileEntity(T tileEntity) {
        NBTTagCompound nbt = captureSnapshotNBTFromTileEntity(tileEntity);
        T snapshotTileEntity = null;
        if (nbt != null) {
            snapshotTileEntity = (T) TileEntity.create(tileEntity.getWorld(), nbt);
            setSnapshotTileEntity(snapshotTileEntity);
        }
        load(tileEntity);
        return snapshotTileEntity;
    }

    /**
     * 从TileEntity中获取快照
     */
    protected NBTTagCompound captureSnapshotNBTFromTileEntity(T tileEntity) {
        TileEntity te = tileEntity;
        if (te != null) {
            snapshotNBT = new NBTTagCompound();
            te.writeToNBT(snapshotNBT);
        } else {
            snapshotNBT = null;
        }
        return snapshotNBT;
    }

    protected void setTileEntity(T tileEntity) {

    }

    protected void setSnapshotTileEntity(T tileEntity) {

    }

    public static CraftBlockState getBlockState(net.minecraft.world.World world, int x, int y, int z) {
        return new CraftBlockState(world.getWorld().getBlockAt(x, y, z));
    }

    public static CraftBlockState getBlockState(net.minecraft.world.World world, int x, int y, int z, int flag) {
        return new CraftBlockState(world.getWorld().getBlockAt(x, y, z), flag);
    }

    @Override
    public World getWorld() {
        requirePlaced();
        return world;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public Chunk getChunk() {
        requirePlaced();
        return chunk;
    }

    @Override
    public void setData(final MaterialData data) {
        Material mat = getType();

        if ((mat == null) || (mat.getData() == null)) {
            this.data = data;
        } else {
            if ((data.getClass() == mat.getData()) || (data.getClass() == MaterialData.class)) {
                this.data = data;
            } else {
                throw new IllegalArgumentException("Provided data is not of type "
                        + mat.getData().getName() + ", found " + data.getClass().getName());
            }
        }
    }

    @Override
    public MaterialData getData() {
        return data;
    }

    @Override
    public void setType(final Material type) {
        setTypeId(type.getId());
    }

    @Override
    public boolean setTypeId(final int type) {
        if (this.type != type) {
            this.type = type;

            createData((byte) 0);
        }
        return true;
    }

    @Override
    public Material getType() {
        return Material.getMaterial(getTypeId());
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public int getTypeId() {
        return type;
    }

    @Override
    public byte getLightLevel() {
        return getBlock().getLightLevel();
    }

    @Override
    public Block getBlock() {
        requirePlaced();
        return world.getBlockAt(x, y, z);
    }

    @Override
    public boolean update() {
        return update(false);
    }

    @Override
    public boolean update(boolean force) {
        return update(force, true);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        if (!isPlaced()) {
            return true;
        }
        Block block = getBlock();

        if (block.getType() != getType()) {
            if (!force) {
                return false;
            }
        }

        BlockPos pos = new BlockPos(x, y, z);
        IBlockState newBlock = CraftMagicNumbers.getBlock(getType()).getStateFromMeta(this.getRawData());
        block.setTypeIdAndData(getTypeId(), getRawData(), applyPhysics);
        world.getHandle().notifyBlockUpdate(
                pos,
                CraftMagicNumbers.getBlock(block).getStateFromMeta(block.getData()),
                newBlock,
                3
        );

        // Update levers etc
        if (applyPhysics && getData() instanceof Attachable) {
            world.getHandle().notifyNeighborsOfStateChange(pos.offset(CraftBlock.blockFaceToNotch(((Attachable) getData()).getAttachedFace())), newBlock.getBlock(), false);
        }
        // Cauldron start - restore TE data from snapshot
        if (getSnapshotNBT() != null) {
            TileEntity te = world.getHandle().getTileEntity(new BlockPos(this.x, this.y, this.z));
            if (te != null) {
                NBTTagCompound nbt2 = new NBTTagCompound();
                te.writeToNBT(nbt2);
                if (!nbt2.equals(this.getSnapshotNBT())) {
                    te.readFromNBT(this.getSnapshotNBT());
                }
            }
        }
        // Cauldron end
        return true;
    }

    private void createData(final byte data) {
        Material mat = getType();
        if (mat == null || mat.getData() == null) {
            this.data = new MaterialData(type, data);
        } else {
            this.data = mat.getNewData(data);
        }
    }

    @Override
    public byte getRawData() {
        return data.getData();
    }

    @Override
    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(world);
            loc.setX(x);
            loc.setY(y);
            loc.setZ(z);
            loc.setYaw(0);
            loc.setPitch(0);
        }

        return loc;
    }

    @Override
    public void setRawData(byte data) {
        this.data.setData(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftBlockState other = (CraftBlockState) obj;
        if (this.world != other.world && (this.world == null || !this.world.equals(other.world))) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
            return false;
        }
        return Objects.equals(this.getSnapshotNBT(), other.getSnapshotNBT());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 73 * hash + this.x;
        hash = 73 * hash + this.y;
        hash = 73 * hash + this.z;
        hash = 73 * hash + this.type;
        hash = 73 * hash + (this.data != null ? this.data.hashCode() : 0);
        hash = 73 * hash + (this.getSnapshotNBT() != null ? this.getSnapshotNBT().hashCode() : 0);
        return hash;
    }

    public TileEntity getTileEntity() {
        if (isBlockSnapshot()) {
            NBTTagCompound nbt = getSnapshotNBT();
            if (nbt != null) {
                return TileEntity.create(this.world.getHandle(), nbt);
            } else {
                return null;
            }
        } else {
            return captureTileEntityFromWorld();
        }
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        requirePlaced();
        chunk.getCraftWorld().getBlockMetadata().setMetadata(getBlock(), metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        requirePlaced();
        return chunk.getCraftWorld().getBlockMetadata().getMetadata(getBlock(), metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        requirePlaced();
        return chunk.getCraftWorld().getBlockMetadata().hasMetadata(getBlock(), metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        requirePlaced();
        chunk.getCraftWorld().getBlockMetadata().removeMetadata(getBlock(), metadataKey, owningPlugin);
    }

    @Override
    public boolean isPlaced() {
        return world != null;
    }

    /**
     * 获取快照nbt
     * @return
     */
    protected NBTTagCompound getSnapshotNBT() {
        load();
        return snapshotNBT;
    }

    /**
     * 获取快照nbt
     * @return
     */
    protected NBTTagCompound directGetSnapshotNBT() {
        return snapshotNBT;
    }

    protected boolean isBlockSnapshot() {
        return blockSnapshot;
    }

    protected void requirePlaced() {
        if (!isPlaced()) {
            throw new IllegalStateException("The blockState must be placed to call this method");
        }
    }
}