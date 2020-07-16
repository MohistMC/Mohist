package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ContainerLock;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftBeacon extends CraftBlockEntityState<BeaconBlockEntity> implements Beacon {

    public CraftBeacon(final Block block) {
        super(block, BeaconBlockEntity.class);
    }

    public CraftBeacon(final Material material, final BeaconBlockEntity te) {
        super(material, te);
    }

    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (tileEntity instanceof BeaconBlockEntity) {
            BeaconBlockEntity beacon = (BeaconBlockEntity) tileEntity;

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
        return this.getSnapshot().level;
    }

    @Override
    public PotionEffect getPrimaryEffect() {
        return this.getSnapshot().getPrimaryEffect();
    }

    @Override
    public void setPrimaryEffect(PotionEffectType effect) {
        this.getSnapshot().primary = (effect != null) ? StatusEffect.byRawId(effect.getId()) : null;
    }

    @Override
    public PotionEffect getSecondaryEffect() {
        return this.getSnapshot().getSecondaryEffect();
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        this.getSnapshot().secondary = (effect != null) ? StatusEffect.byRawId(effect.getId()) : null;
    }

    @Override
    public String getCustomName() {
        BeaconBlockEntity beacon = this.getSnapshot();
        return beacon.customName != null ? CraftChatMessage.fromComponent(beacon.customName) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public boolean isLocked() {
        return !this.getSnapshot().lock.key.isEmpty();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().lock.key;
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().lock = (key == null) ? ContainerLock.EMPTY : new ContainerLock(key);
    }
}
