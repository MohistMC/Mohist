package org.bukkit.craftbukkit.v1_18_R2.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;

public class CraftAxolotl extends CraftAnimals implements Axolotl {

    public CraftAxolotl(CraftServer server, net.minecraft.world.entity.animal.axolotl.Axolotl entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.axolotl.Axolotl getHandle() {
        return (net.minecraft.world.entity.animal.axolotl.Axolotl) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.AXOLOTL;
    }

    @Override
    public String toString() {
        return "CraftAxolotl";
    }

    @Override
    public boolean isPlayingDead() {
        return getHandle().isPlayingDead();
    }

    @Override
    public void setPlayingDead(boolean playingDead) {
        getHandle().setPlayingDead(playingDead);
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.BY_ID[variant.ordinal()]);
    }
}
