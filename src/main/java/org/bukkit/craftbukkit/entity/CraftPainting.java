package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, PaintingEntity entity) {
        super(server, entity);
    }

    @Override
    public Art getArt() {
        PaintingType art = getHandle().art;
        return CraftArt.NotchToBukkit(art);
    }

    @Override
    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    @Override
    public boolean setArt(Art art, boolean force) {
        PaintingEntity painting = this.getHandle();
        PaintingType oldArt = painting.art;
        painting.art = CraftArt.BukkitToNotch(art);
        painting.updateFacingWithBoundingBox(painting.getHorizontalFacing());
        if (!force && !painting.onValidSurface()) {
            // Revert painting since it doesn't fit
            painting.art = oldArt;
            painting.updateFacingWithBoundingBox(painting.getHorizontalFacing());
            return false;
        }
        this.update();
        return true;
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    private void update() {
        ServerWorld world = ((CraftWorld) getWorld()).getHandle();
        PaintingEntity painting = net.minecraft.entity.EntityType.PAINTING.create(world);
        painting.hangingPosition = getHandle().hangingPosition;
        painting.art = getHandle().art;
        painting.updateFacingWithBoundingBox(getHandle().getHorizontalFacing());
        getHandle().remove();
        getHandle().velocityChanged = true; // because this occurs when the painting is broken, so it might be important
        world.addEntity(painting);
        this.entity = painting;
    }

    @Override
    public PaintingEntity getHandle() {
        return (PaintingEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
