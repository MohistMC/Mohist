package org.bukkit.craftbukkit.v1_16_R1.block;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;

public class CraftStructureBlock extends CraftBlockEntityState<StructureBlockBlockEntity> implements Structure {

    private static final int MAX_SIZE = 32;

    public CraftStructureBlock(Block block) {
        super(block, StructureBlockBlockEntity.class);
    }

    public CraftStructureBlock(Material material, StructureBlockBlockEntity structure) {
        super(material, structure);
    }

    @Override
    public String getStructureName() {
        return getSnapshot().getStructureName();
    }

    @Override
    public void setStructureName(String name) {
        getSnapshot().setStructureName(name);
    }

    @Override
    public String getAuthor() {
        return getSnapshot().author;
    }

    @Override
    public void setAuthor(String author) {
        getSnapshot().author = author;
    }

    @Override
    public void setAuthor(LivingEntity entity) {
        getSnapshot().author = ((CraftLivingEntity) entity).getHandle().getEntityName();
    }

    @Override
    public BlockVector getRelativePosition() {
        return new BlockVector(getSnapshot().getOffset().getX(), getSnapshot().getOffset().getY(), getSnapshot().getOffset().getZ());
    }

    @Override
    public void setRelativePosition(BlockVector vector) {
        getSnapshot().setOffset(new BlockPos(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()));
    }

    @Override
    public BlockVector getStructureSize() {
        return new BlockVector(getSnapshot().size.getX(), getSnapshot().size.getY(), getSnapshot().size.getZ());
    }

    @Override
    public void setStructureSize(BlockVector vector) {
        getSnapshot().size = new BlockPos(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @Override
    public void setMirror(Mirror mirror) {
        getSnapshot().mirror = BlockMirror.valueOf(mirror.name());
    }

    @Override
    public Mirror getMirror() {
        return Mirror.valueOf(getSnapshot().getMirror().name());
    }

    @Override
    public void setRotation(StructureRotation rotation) {
        getSnapshot().setRotation(BlockRotation.valueOf(rotation.name()));
    }

    @Override
    public StructureRotation getRotation() {
        return StructureRotation.valueOf(getSnapshot().getRotation().name());
    }

    @Override
    public void setUsageMode(UsageMode mode) {
        getSnapshot().setMode(StructureBlockMode.valueOf(mode.name()));
    }

    @Override
    public UsageMode getUsageMode() {
        return UsageMode.valueOf(getSnapshot().getMode().name());
    }

    @Override
    public void setIgnoreEntities(boolean flag) {
        getSnapshot().setIgnoreEntities(flag);
    }

    @Override
    public boolean isIgnoreEntities() {
        return getSnapshot().shouldIgnoreEntities();
    }

    @Override
    public void setShowAir(boolean showAir) {
        getSnapshot().setShowAir(showAir);
    }

    @Override
    public boolean isShowAir() {
        return getSnapshot().shouldShowAir();
    }

    @Override
    public void setBoundingBoxVisible(boolean showBoundingBox) {
        getSnapshot().showBoundingBox = showBoundingBox;
    }

    @Override
    public boolean isBoundingBoxVisible() {
        return getSnapshot().shouldShowBoundingBox();
    }

    @Override
    public void setIntegrity(float integrity) {
        getSnapshot().integrity = integrity;
    }

    @Override
    public float getIntegrity() {
        return getSnapshot().getIntegrity();
    }

    @Override
    public void setSeed(long seed) {
        getSnapshot().seed = seed;
    }

    @Override
    public long getSeed() {
        return getSnapshot().getSeed();
    }

    @Override
    public void setMetadata(String metadata) {
        if (getUsageMode() == UsageMode.DATA)
            getSnapshot().metadata = metadata;
    }

    @Override
    public String getMetadata() {
        return getSnapshot().getMetadata();
    }

    @Override
    protected void applyTo(StructureBlockBlockEntity tileEntity) {
        super.applyTo(tileEntity);
        tileEntity.setMode(tileEntity.getMode());
    }

    private static boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }

    private static boolean isBetween(float num, float min, float max) {
        return num >= min && num <= max;
    }

}
