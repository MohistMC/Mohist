package org.bukkit.craftbukkit.v1_15_R1.entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effect;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.potion.CraftPotionUtil;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class CraftAreaEffectCloud extends CraftEntity implements AreaEffectCloud {

    public CraftAreaEffectCloud(CraftServer server, AreaEffectCloudEntity entity) {
        super(server, entity);
    }

    @Override
    public AreaEffectCloudEntity getHandle() {
        return (AreaEffectCloudEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftAreaEffectCloud";
    }

    @Override
    public EntityType getType() {
        return EntityType.AREA_EFFECT_CLOUD;
    }

    @Override
    public int getDuration() {
        return getHandle().getDuration();
    }

    @Override
    public void setDuration(int duration) {
        getHandle().setDuration(duration);
    }

    @Override
    public int getWaitTime() {
        return getHandle().waitTime;
    }

    @Override
    public void setWaitTime(int waitTime) {
        getHandle().setWaitTime(waitTime);
    }

    @Override
    public int getReapplicationDelay() {
        return getHandle().reapplicationDelay;
    }

    @Override
    public void setReapplicationDelay(int delay) {
        getHandle().reapplicationDelay = delay;
    }

    @Override
    public int getDurationOnUse() {
        return getHandle().durationOnUse;
    }

    @Override
    public void setDurationOnUse(int duration) {
        getHandle().durationOnUse = duration;
    }

    @Override
    public float getRadius() {
        return getHandle().getRadius();
    }

    @Override
    public void setRadius(float radius) {
        getHandle().setRadius(radius);
    }

    @Override
    public float getRadiusOnUse() {
        return getHandle().radiusOnUse;
    }

    @Override
    public void setRadiusOnUse(float radius) {
        getHandle().setRadiusOnUse(radius);
    }

    @Override
    public float getRadiusPerTick() {
        return getHandle().radiusPerTick;
    }

    @Override
    public void setRadiusPerTick(float radius) {
        getHandle().setRadiusPerTick(radius);
    }

    @Override
    public Particle getParticle() {
        return CraftParticle.toBukkit(getHandle().getParticleData());
    }

    @Override
    public void setParticle(Particle particle) {
        setParticle(particle, null);
    }

    @Override
    public <T> void setParticle(Particle particle, T data) {
        getHandle().setParticleData(CraftParticle.toNMS(particle, data));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(getHandle().getColor());
    }

    @Override
    public void setColor(Color color) {
        getHandle().setColor(color.asRGB());
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        int effectId = effect.getType().getId();
        EffectInstance existing = null;
        for (EffectInstance mobEffect : getHandle().effects) {
            if (Effect.getId(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            getHandle().effects.remove(existing);
        }
        getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        getHandle().effects.clear();
        getHandle().refreshEffects();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (EffectInstance effect : getHandle().effects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (EffectInstance effect : getHandle().effects) {
            if (CraftPotionUtil.equals(effect.getPotion(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !getHandle().effects.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        int effectId = effect.getId();
        EffectInstance existing = null;
        for (EffectInstance mobEffect : getHandle().effects) {
            if (Effect.getId(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        getHandle().effects.remove(existing);
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull(data, "PotionData cannot be null");
        getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(getHandle().getTypeCB());
    }

    @Override
    public ProjectileSource getSource() {
        net.minecraft.entity.LivingEntity source = getHandle().getOwner();
        return (source == null) ? null : (LivingEntity) source.getBukkitEntity();
    }

    @Override
    public void setSource(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().setOwner((net.minecraft.entity.LivingEntity) ((CraftLivingEntity) shooter).getHandle());
        } else {
            getHandle().setOwner((net.minecraft.entity.LivingEntity) null);
        }
    }
}
