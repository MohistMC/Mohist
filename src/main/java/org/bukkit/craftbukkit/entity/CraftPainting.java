package org.bukkit.craftbukkit.entity;


import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, net.minecraft.entity.item.EntityPainting entity) {
        super(server, entity);
    }

    public Art getArt() {
        net.minecraft.entity.item.EntityPainting.EnumArt art = getHandle().art;
        return CraftArt.NotchToBukkit(art);
    }

    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    public boolean setArt(Art art, boolean force) {
        net.minecraft.entity.item.EntityPainting painting = this.getHandle();
        net.minecraft.entity.item.EntityPainting.EnumArt oldArt = painting.art;
        painting.art = CraftArt.BukkitToNotch(art);
        painting.setDirection(painting.hangingDirection);
        if (!force && !painting.onValidSurface()) {
            // Revert painting since it doesn't fit
            painting.art = oldArt;
            painting.setDirection(painting.hangingDirection);
            return false;
        }
        this.update();
        return true;
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    private void update() {
        net.minecraft.world.WorldServer world = ((CraftWorld) getWorld()).getHandle();
        net.minecraft.entity.item.EntityPainting painting = new net.minecraft.entity.item.EntityPainting(world);
        painting.field_146063_b = getHandle().field_146063_b;
        painting.field_146064_c = getHandle().field_146064_c;
        painting.field_146062_d = getHandle().field_146062_d;
        painting.art = getHandle().art;
        painting.setDirection(getHandle().hangingDirection);
        getHandle().setDead();
        getHandle().velocityChanged = true; // because this occurs when the painting is broken, so it might be important
        world.spawnEntityInWorld(painting);
        this.entity = painting;
    }

    @Override
    public net.minecraft.entity.item.EntityPainting getHandle() {
        return (net.minecraft.entity.item.EntityPainting) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
