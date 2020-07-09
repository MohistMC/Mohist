package org.bukkit.craftbukkit.v1_15_R1.block;

import com.google.common.base.Preconditions;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_15_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CraftBlockState implements BlockState {
    protected final CraftWorld world;
    private final CraftChunk chunk;
    private final BlockPos position;
    protected net.minecraft.block.BlockState data;
    protected int flag;

    public CraftBlockState(final Block block) {
        this.world = (CraftWorld) block.getWorld();
        this.position = ((CraftBlock) block).getPosition();
        this.data = ((CraftBlock) block).getNMS();
        this.chunk = (CraftChunk) block.getChunk();
        this.flag = 3;
    }

    public CraftBlockState(final Block block, int flag) {
        this(block);
        this.flag = flag;
    }

    public CraftBlockState(Material material) {
        world = null;
        data = CraftMagicNumbers.getBlock(material).getDefaultState();
        chunk = null;
        position = BlockPos.ZERO;
    }

    public static CraftBlockState getBlockState(IWorld world, net.minecraft.util.math.BlockPos pos) {
        return new CraftBlockState(CraftBlock.at(world, pos));
    }

    public static CraftBlockState getBlockState(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, int flag) {
        return new CraftBlockState(world.getWorldCB().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), flag);
    }

    @Override
    public World getWorld() {
        requirePlaced();
        return world;
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public int getZ() {
        return position.getZ();
    }

    @Override
    public Chunk getChunk() {
        requirePlaced();
        return chunk;
    }

    public void setData(net.minecraft.block.BlockState data) {
        this.data = data;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public net.minecraft.block.BlockState getHandle() {
        return this.data;
    }

    @Override
    public BlockData getBlockData() {
        return CraftBlockData.fromData(data);
    }

    @Override
    public void setBlockData(BlockData data) {
        Preconditions.checkArgument(data != null, "BlockData cannot be null");
        this.data = ((CraftBlockData) data).getState();
    }

    @Override
    public void setData(final MaterialData data) {
        Material mat = CraftMagicNumbers.getMaterial(this.data).getItemType();

        if ((mat == null) || (mat.getData() == null)) {
            this.data = CraftMagicNumbers.getBlock(data);
        } else {
            if ((data.getClass() == mat.getData()) || (data.getClass() == MaterialData.class)) {
                this.data = CraftMagicNumbers.getBlock(data);
            } else {
                throw new IllegalArgumentException("Provided data is not of type "
                        + mat.getData().getName() + ", found " + data.getClass().getName());
            }
        }
    }

    @Override
    public MaterialData getData() {
        return CraftMagicNumbers.getMaterial(data);
    }

    @Override
    public void setType(final Material type) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        Preconditions.checkArgument(type.isBlock(), "Material must be a block!");

        if (this.getType() != type) {
            this.data = CraftMagicNumbers.getBlock(type).getDefaultState();
        }
    }

    @Override
    public Material getType() {
        return CraftMagicNumbers.getMaterial(data.getBlock());
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public byte getLightLevel() {
        return getBlock().getLightLevel();
    }

    @Override
    public CraftBlock getBlock() {
        requirePlaced();
        return CraftBlock.at(world.getHandle(), position);
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
        CraftBlock block = getBlock();

        if (block.getType() != getType()) {
            if (!force) {
                return false;
            }
        }

        net.minecraft.block.BlockState newBlock = this.data;
        block.setBlockState(newBlock, applyPhysics);
        world.getHandle().notifyBlockUpdate(
                position,
                block.getNMS(),
                newBlock,
                3
        );

        /*
        // Update levers etc
        if (false && applyPhysics && getData() instanceof Attachable) { // Call does not map to new API
            world.getHandle().notifyBlockUpdate(position.offset(CraftBlock.blockFaceToNotch(((Attachable) getData()).getAttachedFace())), newBlock.getBlock());
        }
         */

        return true;
    }

    @Override
    public byte getRawData() {
        return CraftMagicNumbers.toLegacyData(data);
    }

    @Override
    public Location getLocation() {
        return new Location(world, getX(), getY(), getZ());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(world);
            loc.setX(getX());
            loc.setY(getY());
            loc.setZ(getZ());
            loc.setYaw(0);
            loc.setPitch(0);
        }

        return loc;
    }

    @Override
    public void setRawData(byte data) {
        this.data = CraftMagicNumbers.getBlock(getType(), data);
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
        if (this.position != other.position && (this.position == null || !this.position.equals(other.position))) {
            return false;
        }
        if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 73 * hash + (this.position != null ? this.position.hashCode() : 0);
        hash = 73 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
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

    protected void requirePlaced() {
        if (!isPlaced()) {
            throw new IllegalStateException("The blockState must be placed to call this method");
        }
    }
}
