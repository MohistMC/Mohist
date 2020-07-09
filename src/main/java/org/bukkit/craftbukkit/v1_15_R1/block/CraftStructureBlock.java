package org.bukkit.craftbukkit.v1_15_R1.block;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.Rotation;
import net.minecraft.tileentity.StructureBlockTileEntity;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;

public class CraftStructureBlock extends CraftBlockEntityState<StructureBlockTileEntity> implements Structure {

    private static final int MAX_SIZE = 32;

    public CraftStructureBlock(Block block) {
        super(block, StructureBlockTileEntity.class);
    }

    public CraftStructureBlock(Material material, StructureBlockTileEntity structure) {
        super(material, structure);
    }

    @Override
    public String getStructureName() {
        return getSnapshot().getName();
    }

    @Override
    public void setStructureName(String name) {
        Preconditions.checkArgument(name != null, "Structure Name cannot be null");
        getSnapshot().setName(name);
    }

    @Override
    public String getAuthor() {
        return getSnapshot().author;
    }

    @Override
    public void setAuthor(String author) {
        Preconditions.checkArgument(author != null && !author.isEmpty(), "Author name cannot be null nor empty");
        getSnapshot().author = author;
    }

    @Override
    public void setAuthor(LivingEntity entity) {
        Preconditions.checkArgument(entity != null, "Structure Block author entity cannot be null");
        getSnapshot().createdBy(((CraftLivingEntity) entity).getHandle());
    }

    @Override
    public BlockVector getRelativePosition() {
        return new BlockVector(getSnapshot().position.getX(), getSnapshot().position.getY(), getSnapshot().position.getZ());
    }

    @Override
    public void setRelativePosition(BlockVector vector) {
        Validate.isTrue(isBetween(vector.getBlockX(), -MAX_SIZE, MAX_SIZE), "Structure Size (X) must be between -" + MAX_SIZE + " and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockY(), -MAX_SIZE, MAX_SIZE), "Structure Size (Y) must be between -" + MAX_SIZE + " and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockZ(), -MAX_SIZE, MAX_SIZE), "Structure Size (Z) must be between -" + MAX_SIZE + " and " + MAX_SIZE);
        getSnapshot().position = new BlockPos(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @Override
    public BlockVector getStructureSize() {
        return new BlockVector(getSnapshot().size.getX(), getSnapshot().size.getY(), getSnapshot().size.getZ());
    }

    @Override
    public void setStructureSize(BlockVector vector) {
        Validate.isTrue(isBetween(vector.getBlockX(), 0, MAX_SIZE), "Structure Size (X) must be between 0 and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockY(), 0, MAX_SIZE), "Structure Size (Y) must be between 0 and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockZ(), 0, MAX_SIZE), "Structure Size (Z) must be between 0 and " + MAX_SIZE);
        getSnapshot().size = new BlockPos(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @Override
    public void setMirror(Mirror mirror) {
        getSnapshot().mirror = net.minecraft.util.Mirror.valueOf(mirror.name());
    }

    @Override
    public Mirror getMirror() {
        return Mirror.valueOf(getSnapshot().mirror.name());
    }

    @Override
    public void setRotation(StructureRotation rotation) {
        getSnapshot().rotation = Rotation.valueOf(rotation.name());
    }

    @Override
    public StructureRotation getRotation() {
        return StructureRotation.valueOf(getSnapshot().rotation.name());
    }

    @Override
    public void setUsageMode(UsageMode mode) {
        getSnapshot().mode = StructureMode.valueOf(mode.name());
    }

    @Override
    public UsageMode getUsageMode() {
        return UsageMode.valueOf(getSnapshot().getMode().name());
    }

    @Override
    public void setIgnoreEntities(boolean flag) {
        getSnapshot().ignoreEntities = flag;
    }

    @Override
    public boolean isIgnoreEntities() {
        return getSnapshot().ignoreEntities;
    }

    @Override
    public void setShowAir(boolean showAir) {
        getSnapshot().showAir = showAir;
    }

    @Override
    public boolean isShowAir() {
        return getSnapshot().showAir;
    }

    @Override
    public void setBoundingBoxVisible(boolean showBoundingBox) {
        getSnapshot().showBoundingBox = showBoundingBox;
    }

    @Override
    public boolean isBoundingBoxVisible() {
        return getSnapshot().showBoundingBox;
    }

    @Override
    public void setIntegrity(float integrity) {
        Validate.isTrue(isBetween(integrity, 0.0f, 1.0f), "Integrity must be between 0.0f and 1.0f");
        getSnapshot().integrity = integrity;
    }

    @Override
    public float getIntegrity() {
        return getSnapshot().integrity;
    }

    @Override
    public void setSeed(long seed) {
        getSnapshot().seed = seed;
    }

    @Override
    public long getSeed() {
        return getSnapshot().seed;
    }

    @Override
    public void setMetadata(String metadata) {
        Validate.notNull(metadata, "Structure metadata cannot be null");
        if (getUsageMode() == UsageMode.DATA) {
            getSnapshot().metadata = metadata;
        }
    }

    @Override
    public String getMetadata() {
        return getSnapshot().metadata;
    }

    @Override
    protected void applyTo(StructureBlockTileEntity tileEntity) {
        super.applyTo(tileEntity);

        // Ensure block type is correct
        tileEntity.setMode(tileEntity.getMode());
    }

    private static boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }

    private static boolean isBetween(float num, float min, float max) {
        return num >= min && num <= max;
    }
}
