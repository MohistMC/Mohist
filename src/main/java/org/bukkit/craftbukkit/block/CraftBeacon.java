package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.LockCode;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftBeacon extends CraftBlockEntityState<BeaconTileEntity> implements Beacon {

    public CraftBeacon(final Block block) {
        super(block, BeaconTileEntity.class);
    }

    public CraftBeacon(final Material material, final BeaconTileEntity te) {
        super(material, te);
    }

    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (tileEntity instanceof BeaconTileEntity) {
            BeaconTileEntity beacon = (BeaconTileEntity) tileEntity;

            Collection<PlayerEntity> nms = beacon.getHumansInRange();
            Collection<LivingEntity> bukkit = new ArrayList<LivingEntity>(nms.size());

            for (PlayerEntity human : nms) {
                bukkit.add(human.getBukkitEntity());
            }

            return bukkit;
        }

        // block is no longer a beacon
        return new ArrayList<LivingEntity>();
    }

    @Override
    public int getTier() {
        return this.getSnapshot().levels;
    }

    @Override
    public PotionEffect getPrimaryEffect() {
        return this.getSnapshot().getPrimaryEffect();
    }

    @Override
    public void setPrimaryEffect(PotionEffectType effect) {
        this.getSnapshot().primaryEffect = (effect != null) ? Effect.get(effect.getId()) : null;
    }

    @Override
    public PotionEffect getSecondaryEffect() {
        return this.getSnapshot().getSecondaryEffect();
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        this.getSnapshot().secondaryEffect = (effect != null) ? Effect.get(effect.getId()) : null;
    }

    @Override
    public String getCustomName() {
        BeaconTileEntity beacon = this.getSnapshot();
        return beacon.customName != null ? CraftChatMessage.fromComponent(beacon.customName) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public boolean isLocked() {
        return !this.getSnapshot().lockCode.lock.isEmpty();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().lockCode.lock;
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().lockCode = (key == null) ? LockCode.EMPTY_CODE : new LockCode(key);
    }
}
