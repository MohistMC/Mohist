package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    public String entityName;
    private CraftEntityEquipment equipment;

    public CraftLivingEntity(final CraftServer server, final EntityLivingBase entity) {
        super(server, entity);

        if (entity instanceof EntityLiving || entity instanceof EntityArmorStand) {
            equipment = new CraftEntityEquipment(this);
        }
        this.entityName = EntityRegistry.getCustomEntityTypeName(entity.getClass());
        if (entityName == null) {
            entityName = entity.getName();
        }
    }

    public double getHealth() {
        return Math.min(Math.max(0, getHandle().getHealth()), getMaxHealth());
    }

    public void setHealth(double health) {
        health = (float) health;
        if ((health < 0) || (health > getMaxHealth())) {
            // Paper - Be more informative
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth() + ", but was " + health
                    + ". (attribute base value: " + this.getHandle().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue()
                    + (this instanceof CraftPlayer ? ", player: " + this.getName() + ')' : ')'));
        }
        // Cauldron start - setHealth must be set before onDeath to respect events that may prevent death.
        getHandle().setHealth((float) health);

        if (entity instanceof EntityPlayerMP && health == 0) {
            ((EntityPlayerMP) entity).onDeath(DamageSource.GENERIC);
        }
        // Cauldron end
    }

    public double getMaxHealth() {
        return getHandle().getMaxHealth();
    }

    public void setMaxHealth(double amount) {
        Validate.isTrue(amount > 0, "Max health must be greater than 0");

        getHandle().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(amount);

        if (getHealth() > amount) {
            setHealth(amount);
        }
    }

    public void resetMaxHealth() {
        setMaxHealth(getHandle().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttribute().getDefaultValue());
    }

    public double getEyeHeight() {
        return getHandle().getEyeHeight();
    }

    public double getEyeHeight(boolean ignorePose) {
        return getEyeHeight();
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (transparent == null) {
                if (!material.equals(Material.AIR)) {
                    break;
                }
            } else {
                if (!transparent.contains(material)) {
                    break;
                }
            }
        }
        return blocks;
    }

    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    public int getRemainingAir() {
        return getHandle().getAir();
    }

    public void setRemainingAir(int ticks) {
        getHandle().setAir(ticks);
    }

    public int getMaximumAir() {
        return getHandle().maxAirTicks;
    }

    public void setMaximumAir(int ticks) {
        getHandle().maxAirTicks = ticks;
    }

    public void damage(double amount) {
        damage(amount, null);
    }

    public void damage(double amount, Entity source) {
        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.causePlayerDamage(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.causeMobDamage(((CraftLivingEntity) source).getHandle());
        }

        entity.attackEntityFrom(reason, (float) amount);
    }

    public Location getEyeLocation() {
        Location loc = getLocation();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    public int getMaximumNoDamageTicks() {
        return getHandle().maxHurtResistantTime;
    }

    public void setMaximumNoDamageTicks(int ticks) {
        getHandle().maxHurtResistantTime = ticks;
    }

    public double getLastDamage() {
        return getHandle().lastDamage;
    }

    public void setLastDamage(double damage) {
        getHandle().lastDamage = (float) damage;
    }

    public int getNoDamageTicks() {
        return getHandle().hurtResistantTime;
    }

    public void setNoDamageTicks(int ticks) {
        getHandle().hurtResistantTime = ticks;
    }

    @Override
    public EntityLivingBase getHandle() {
        return (EntityLivingBase) entity;
    }

    public void setHandle(final EntityLiving entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + getEntityId() + ", name=" + this.entityName + "}";
    }

    public Player getKiller() {
        return getHandle().attackingPlayer == null ? null : (Player) getHandle().attackingPlayer.getBukkitEntity();
    }

    public boolean addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            removePotionEffect(effect.getType());
        }
        getHandle().addPotionEffect(new net.minecraft.potion.PotionEffect(Potion.getPotionById(effect.getType().getId()), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
        return true;
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return getHandle().isPotionActive(Potion.getPotionById(type.getId()));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        net.minecraft.potion.PotionEffect handle = getHandle().getActivePotionEffect(Potion.getPotionById(type.getId()));
        return (handle == null) ? null : new PotionEffect(PotionEffectType.getById(Potion.getIdFromPotion(handle.getPotion())), handle.getDuration(), handle.getAmplifier(), handle.getIsAmbient(), handle.doesShowParticles());
    }

    public void removePotionEffect(PotionEffectType type) {
        getHandle().removePotionEffect(Potion.getPotionById(type.getId()));
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<>();
        for (net.minecraft.potion.PotionEffect handle : getHandle().getActivePotionMap().values()) {
            if (PotionEffectType.getById(Potion.getIdFromPotion(handle.getPotion())) == null) {
                continue; // Cauldron - ignore null types
            }
            effects.add(new PotionEffect(PotionEffectType.getById(Potion.getIdFromPotion(handle.getPotion())), handle.getDuration(), handle.getAmplifier(), handle.getIsAmbient(), handle.doesShowParticles()));
        }
        return effects;
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        net.minecraft.world.World world = ((CraftWorld) getWorld()).getHandle();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, getHandle());
            ((EntityThrowable) launch).shoot(getHandle(), getHandle().rotationPitch, getHandle().rotationYaw, 0.0F, 1.5F, 1.0F); // ItemSnowball
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, getHandle());
            ((EntityThrowable) launch).shoot(getHandle(), getHandle().rotationPitch, getHandle().rotationYaw, 0.0F, 1.5F, 1.0F); // ItemEgg
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, getHandle());
            ((EntityThrowable) launch).shoot(getHandle(), getHandle().rotationPitch, getHandle().rotationYaw, 0.0F, 1.5F, 1.0F); // ItemEnderPearl
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new EntityTippedArrow(world, getHandle());
                ((EntityTippedArrow) launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new EntitySpectralArrow(world, getHandle());
            } else {
                launch = new EntityTippedArrow(world, getHandle());
            }
            ((EntityArrow) launch).shoot(getHandle(), getHandle().rotationPitch, getHandle().rotationYaw, 0.0F, 3.0F, 1.0F); // ItemBow
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new EntityPotion(world, getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1)));
            } else {
                launch = new EntityPotion(world, getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            }
            ((EntityThrowable) launch).shoot(getHandle(), getHandle().rotationPitch, getHandle().rotationYaw, -20.0F, 0.5F, 1.0F); // ItemSplashPotion
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, getHandle());
            ((EntityThrowable) launch).shoot(getHandle(), getHandle().rotationPitch, getHandle().rotationYaw, -20.0F, 0.7F, 1.0F); // ItemExpBottle
        } else if (Fish.class.isAssignableFrom(projectile) && getHandle() instanceof EntityPlayer) {
            launch = new EntityFishHook(world, (EntityPlayer) getHandle());
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = new EntityDragonFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else {
                launch = new EntityLargeFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }

            ((EntityFireball) launch).projectileSource = this;
            launch.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (LlamaSpit.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection();

            launch = new EntityLlamaSpit(world);

            ((EntityLlamaSpit) launch).owner = getHandle();
            ((EntityLlamaSpit) launch).shoot(direction.getX(), direction.getY(), direction.getZ(), 1.5F, 10.0F); // EntityLlama
            launch.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (ShulkerBullet.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();

            launch = new EntityShulkerBullet(world, getHandle(), null, null);
            launch.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Validate.notNull(launch, "Projectile not supported");

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.spawnEntity(launch);
        return (T) launch.getBukkitEntity();
    }

    public EntityType getType() {
        // Cauldron start
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        }
        return EntityType.FORGE_MOD;
        // Cauldron end
    }

    public boolean hasLineOfSight(Entity other) {
        return getHandle().canEntityBeSeen(((CraftEntity) other).getHandle());
    }

    public boolean getRemoveWhenFarAway() {
        return getHandle() instanceof EntityLiving && !((EntityLiving) getHandle()).persistenceRequired;
    }

    public void setRemoveWhenFarAway(boolean remove) {
        if (getHandle() instanceof EntityLiving) {
            ((EntityLiving) getHandle()).persistenceRequired = !remove;
        }
    }

    public EntityEquipment getEquipment() {
        return equipment;
    }

    public boolean getCanPickupItems() {
        return getHandle().canPickUpLoot;
    }

    public void setCanPickupItems(boolean pickup) {
        getHandle().canPickUpLoot = pickup;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (getHealth() == 0) {
            return false;
        }

        return super.teleport(location, cause);
    }

    public boolean isLeashed() {
        if (!(getHandle() instanceof EntityLiving)) {
            return false;
        }
        return ((EntityLiving) getHandle()).getLeashHolder() != null;
    }

    public Entity getLeashHolder() throws IllegalStateException {
        if (!isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        }
        return ((EntityLiving) getHandle()).getLeashHolder().getBukkitEntity();
    }

    private boolean unleash() {
        if (!isLeashed()) {
            return false;
        }
        ((EntityLiving) getHandle()).clearLeashed(true, false);
        return true;
    }

    public boolean setLeashHolder(Entity holder) {
        if ((getHandle() instanceof EntityWither) || !(getHandle() instanceof EntityLiving)) {
            return false;
        }

        if (holder == null) {
            return unleash();
        }

        if (holder.isDead()) {
            return false;
        }

        unleash();
        ((EntityLiving) getHandle()).setLeashHolder(((CraftEntity) holder).getHandle(), true);
        return true;
    }

    @Override
    public boolean isGliding() {
        return getHandle().getFlag(7);
    }

    @Override
    public void setGliding(boolean gliding) {
        getHandle().setFlag(7, gliding);
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return getHandle().craftAttributes.getAttribute(attribute);
    }

    @Override
    public void setAI(boolean ai) {
        if (this.getHandle() instanceof EntityLiving) {
            ((EntityLiving) this.getHandle()).setNoAI(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return (this.getHandle() instanceof EntityLiving) && !((EntityLiving) this.getHandle()).isAIDisabled();
    }

    @Override
    public boolean isCollidable() {
        return getHandle().collides;
    }

    @Override
    public void setCollidable(boolean collidable) {
        getHandle().collides = collidable;
    }
}
