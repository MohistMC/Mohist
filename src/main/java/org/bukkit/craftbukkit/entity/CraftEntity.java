package org.bukkit.craftbukkit.entity;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EnderEyeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EnderCrystalEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.LeadKnotEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.mob.ZombiePigmanEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.thrown.ThrownEggEntity;
import net.minecraft.entity.thrown.ThrownEnderpearlEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.entity.thrown.ThrownExperienceBottleEntity;
import net.minecraft.entity.thrown.ThrownPotionEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.vehicle.SpawnerMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    private static PermissibleBase perm;
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    protected final CraftServer server;
    protected Entity entity;
    private EntityDamageEvent lastDamageEvent;
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        /*
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        // CHECKSTYLE:OFF
        if (entity instanceof LivingEntity) {
            // Players
            if (entity instanceof PlayerEntity) {
                if (entity instanceof ServerPlayerEntity) { return new CraftPlayer(server, (ServerPlayerEntity) entity); }
                else { return new CraftHumanEntity(server, (PlayerEntity) entity); }
            }
            // Water Animals
            else if (entity instanceof WaterCreatureEntity) {
                if (entity instanceof SquidEntity) { return new CraftSquid(server, (SquidEntity) entity); }
                else if (entity instanceof FishEntity) {
                    if (entity instanceof CodEntity) { return new CraftCod(server, (CodEntity) entity); }
                    else if (entity instanceof PufferfishEntity) { return new CraftPufferFish(server, (PufferfishEntity) entity); }
                    else if (entity instanceof SalmonEntity) { return new CraftSalmon(server, (SalmonEntity) entity); }
                    else if (entity instanceof TropicalFishEntity) { return new CraftTropicalFish(server, (TropicalFishEntity) entity); }
                    else { return new CraftFish(server, (FishEntity) entity); }
                }
                else if (entity instanceof DolphinEntity) { return new CraftDolphin(server, (DolphinEntity) entity); }
                else { return new CraftWaterMob(server, (WaterCreatureEntity) entity); }
            }
            else if (entity instanceof MobEntityWithAi) {
                // Animals
                if (entity instanceof AnimalEntity) {
                    if (entity instanceof ChickenEntity) { return new CraftChicken(server, (ChickenEntity) entity); }
                    else if (entity instanceof CowEntity) {
                        if (entity instanceof MooshroomEntity) { return new CraftMushroomCow(server, (MooshroomEntity) entity); }
                        else { return new CraftCow(server, (CowEntity) entity); }
                    }
                    else if (entity instanceof PigEntity) { return new CraftPig(server, (PigEntity) entity); }
                    else if (entity instanceof TameableEntity) {
                        if (entity instanceof WolfEntity) { return new CraftWolf(server, (WolfEntity) entity); }
                        else if (entity instanceof CatEntity) { return new CraftCat(server, (CatEntity) entity); }
                        else if (entity instanceof ParrotEntity) { return new CraftParrot(server, (ParrotEntity) entity); }
                    }
                    else if (entity instanceof SheepEntity) { return new CraftSheep(server, (SheepEntity) entity); }
                    else if (entity instanceof HorseBaseEntity) {
                        if (entity instanceof AbstractDonkeyEntity){
                            if (entity instanceof DonkeyEntity) { return new CraftDonkey(server, (DonkeyEntity) entity); }
                            else if (entity instanceof MuleEntity) { return new CraftMule(server, (MuleEntity) entity); }
                            else if (entity instanceof TraderLlamaEntity) { return new CraftTraderLlama(server, (TraderLlamaEntity) entity); }
                            else if (entity instanceof LlamaEntity) { return new CraftLlama(server, (LlamaEntity) entity); }
                        } else if (entity instanceof HorseEntity) { return new CraftHorse(server, (HorseEntity) entity); }
                        else if (entity instanceof SkeletonHorseEntity) { return new CraftSkeletonHorse(server, (SkeletonHorseEntity) entity); }
                        else if (entity instanceof ZombieHorseEntity) { return new CraftZombieHorse(server, (ZombieHorseEntity) entity); }
                    }
                    else if (entity instanceof RabbitEntity) { return new CraftRabbit(server, (RabbitEntity) entity); }
                    else if (entity instanceof PolarBearEntity) { return new CraftPolarBear(server, (PolarBearEntity) entity); }
                    else if (entity instanceof TurtleEntity) { return new CraftTurtle(server, (TurtleEntity) entity); }
                    else if (entity instanceof OcelotEntity) { return new CraftOcelot(server, (OcelotEntity) entity); }
                    else if (entity instanceof PandaEntity) { return new CraftPanda(server, (PandaEntity) entity); }
                    else if (entity instanceof FoxEntity) { return new CraftFox(server, (FoxEntity) entity); }
                    else if (entity instanceof BeeEntity) { return new CraftBee(server, (BeeEntity) entity); }
                    else  { return new CraftAnimals(server, (AnimalEntity) entity); }
                }
                // Monsters
                else if (entity instanceof HostileEntity) {
                    if (entity instanceof ZombieEntity) {
                        if (entity instanceof ZombiePigmanEntity) { return new CraftPigZombie(server, (ZombiePigmanEntity) entity); }
                        else if (entity instanceof HuskEntity) { return new CraftHusk(server, (HuskEntity) entity); }
                        else if (entity instanceof ZombieVillagerEntity) { return new CraftVillagerZombie(server, (ZombieVillagerEntity) entity); }
                        else if (entity instanceof DrownedEntity) { return new CraftDrowned(server, (DrownedEntity) entity); }
                        else { return new CraftZombie(server, (ZombieEntity) entity); }
                    }
                    else if (entity instanceof CreeperEntity) { return new CraftCreeper(server, (CreeperEntity) entity); }
                    else if (entity instanceof EndermanEntity) { return new CraftEnderman(server, (EndermanEntity) entity); }
                    else if (entity instanceof SilverfishEntity) { return new CraftSilverfish(server, (SilverfishEntity) entity); }
                    else if (entity instanceof GiantEntity) { return new CraftGiant(server, (GiantEntity) entity); }
                    else if (entity instanceof AbstractSkeletonEntity) {
                        if (entity instanceof StrayEntity) { return new CraftStray(server, (StrayEntity) entity); }
                        else if (entity instanceof WitherSkeletonEntity) { return new CraftWitherSkeleton(server, (WitherSkeletonEntity) entity); }
                        else { return new CraftSkeleton(server, (AbstractSkeletonEntity) entity); }
                    }
                    else if (entity instanceof BlazeEntity) { return new CraftBlaze(server, (BlazeEntity) entity); }
                    else if (entity instanceof WitchEntity) { return new CraftWitch(server, (WitchEntity) entity); }
                    else if (entity instanceof WitherEntity) { return new CraftWither(server, (WitherEntity) entity); }
                    else if (entity instanceof SpiderEntity) {
                        if (entity instanceof CaveSpiderEntity) { return new CraftCaveSpider(server, (CaveSpiderEntity) entity); }
                        else { return new CraftSpider(server, (SpiderEntity) entity); }
                    }
                    else if (entity instanceof EndermiteEntity) { return new CraftEndermite(server, (EndermiteEntity) entity); }
                    else if (entity instanceof GuardianEntity) {
                        if (entity instanceof ElderGuardianEntity) { return new CraftElderGuardian(server, (ElderGuardianEntity) entity); }
                        else { return new CraftGuardian(server, (GuardianEntity) entity); }
                    }
                    else if (entity instanceof VexEntity) { return new CraftVex(server, (VexEntity) entity); }
                    else if (entity instanceof IllagerEntity) {
                        if (entity instanceof SpellcastingIllagerEntity) {
                            if (entity instanceof EvokerEntity) { return new CraftEvoker(server, (EvokerEntity) entity); }
                            else if (entity instanceof IllusionerEntity) { return new CraftIllusioner(server, (IllusionerEntity) entity); }
                            else {  return new CraftSpellcaster(server, (SpellcastingIllagerEntity) entity); }
                        }
                        else if (entity instanceof VindicatorEntity) { return new CraftVindicator(server, (VindicatorEntity) entity); }
                        else if (entity instanceof PillagerEntity) { return new CraftPillager(server, (PillagerEntity) entity); }
                        else { return new CraftIllager(server, (IllagerEntity) entity); }
                    }
                    else if (entity instanceof RavagerEntity) { return new CraftRavager(server, (RavagerEntity) entity); }

                    else  { return new CraftMonster(server, (HostileEntity) entity); }
                }
                else if (entity instanceof GolemEntity) {
                    if (entity instanceof SnowGolemEntity) { return new CraftSnowman(server, (SnowGolemEntity) entity); }
                    else if (entity instanceof IronGolemEntity) { return new CraftIronGolem(server, (IronGolemEntity) entity); }
                    else if (entity instanceof ShulkerEntity) { return new CraftShulker(server, (ShulkerEntity) entity); }
                }
                else if (entity instanceof AbstractTraderEntity) {
                    if (entity instanceof VillagerEntity) { return new CraftVillager(server, (VillagerEntity) entity); }
                    else if (entity instanceof WanderingTraderEntity) { return new CraftWanderingTrader(server, (WanderingTraderEntity) entity); }
                    else { return new CraftAbstractVillager(server, (AbstractTraderEntity) entity); }
                }
                else { return new CraftCreature(server, (MobEntityWithAi) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof SlimeEntity) {
                if (entity instanceof MagmaCubeEntity) { return new CraftMagmaCube(server, (MagmaCubeEntity) entity); }
                else { return new CraftSlime(server, (SlimeEntity) entity); }
            }
            // Flying
            else if (entity instanceof FlyingEntity) {
                if (entity instanceof GhastEntity) { return new CraftGhast(server, (GhastEntity) entity); }
                else if (entity instanceof PhantomEntity) { return new CraftPhantom(server, (PhantomEntity) entity); }
                else { return new CraftFlying(server, (FlyingEntity) entity); }
            }
            else if (entity instanceof EnderDragonEntity) {
                return new CraftEnderDragon(server, (EnderDragonEntity) entity);
            }
            // Ambient
            else if (entity instanceof AmbientEntity) {
                if (entity instanceof BatEntity) { return new CraftBat(server, (BatEntity) entity); }
                else { return new CraftAmbient(server, (AmbientEntity) entity); }
            }
            else if (entity instanceof ArmorStandEntity) { return new CraftArmorStand(server, (ArmorStandEntity) entity); }
            else  { return new CraftLivingEntity(server, (LivingEntity) entity); }
        }
        else if (entity instanceof EnderDragonPart) {
            EnderDragonPart part = (EnderDragonPart) entity;
            if (part.owner instanceof EnderDragonEntity) { return new CraftEnderDragonPart(server, (EnderDragonPart) entity); }
            else { return new CraftComplexPart(server, (EnderDragonPart) entity); }
        }
        else if (entity instanceof ExperienceOrbEntity) { return new CraftExperienceOrb(server, (ExperienceOrbEntity) entity); }
        else if (entity instanceof ArrowEntity) { return new CraftTippedArrow(server, (ArrowEntity) entity); }
        else if (entity instanceof SpectralArrowEntity) { return new CraftSpectralArrow(server, (SpectralArrowEntity) entity); }
        else if (entity instanceof ProjectileEntity) {
            if (entity instanceof TridentEntity) { return new CraftTrident(server, (TridentEntity) entity); }
            else { return new CraftArrow(server, (ProjectileEntity) entity); }
        }
        else if (entity instanceof BoatEntity) { return new CraftBoat(server, (BoatEntity) entity); }
        else if (entity instanceof ThrownEntity) {
            if (entity instanceof ThrownEggEntity) { return new CraftEgg(server, (ThrownEggEntity) entity); }
            else if (entity instanceof SnowballEntity) { return new CraftSnowball(server, (SnowballEntity) entity); }
            else if (entity instanceof ThrownPotionEntity) { return new CraftThrownPotion(server, (ThrownPotionEntity) entity); }
            else if (entity instanceof ThrownEnderpearlEntity) { return new CraftEnderPearl(server, (ThrownEnderpearlEntity) entity); }
            else if (entity instanceof ThrownExperienceBottleEntity) { return new CraftThrownExpBottle(server, (ThrownExperienceBottleEntity) entity); }
        }
        else if (entity instanceof FallingBlockEntity) { return new CraftFallingBlock(server, (FallingBlockEntity) entity); }
        else if (entity instanceof ExplosiveProjectileEntity) {
            if (entity instanceof SmallFireballEntity) { return new CraftSmallFireball(server, (SmallFireballEntity) entity); }
            else if (entity instanceof FireballEntity) { return new CraftLargeFireball(server, (FireballEntity) entity); }
            else if (entity instanceof WitherSkullEntity) { return new CraftWitherSkull(server, (WitherSkullEntity) entity); }
            else if (entity instanceof DragonFireballEntity) { return new CraftDragonFireball(server, (DragonFireballEntity) entity); }
            else { return new CraftFireball(server, (ExplosiveProjectileEntity) entity); }
        }
        else if (entity instanceof EnderEyeEntity) { return new CraftEnderSignal(server, (EnderEyeEntity) entity); }
        else if (entity instanceof EnderCrystalEntity) { return new CraftEnderCrystal(server, (EnderCrystalEntity) entity); }
        else if (entity instanceof FishingBobberEntity) { return new CraftFishHook(server, (FishingBobberEntity) entity); }
        else if (entity instanceof ItemEntity) { return new CraftItem(server, (ItemEntity) entity); }
        else if (entity instanceof LightningEntity) { return new CraftLightningStrike(server, (LightningEntity) entity); }
        else if (entity instanceof AbstractMinecartEntity) {
            if (entity instanceof FurnaceMinecartEntity) { return new CraftMinecartFurnace(server, (FurnaceMinecartEntity) entity); }
            else if (entity instanceof ChestMinecartEntity) { return new CraftMinecartChest(server, (ChestMinecartEntity) entity); }
            else if (entity instanceof TntMinecartEntity) { return new CraftMinecartTNT(server, (TntMinecartEntity) entity); }
            else if (entity instanceof HopperMinecartEntity) { return new CraftMinecartHopper(server, (HopperMinecartEntity) entity); }
            else if (entity instanceof SpawnerMinecartEntity) { return new CraftMinecartMobSpawner(server, (SpawnerMinecartEntity) entity); }
            else if (entity instanceof MinecartEntity) { return new CraftMinecartRideable(server, (MinecartEntity) entity); }
            else if (entity instanceof CommandBlockMinecartEntity) { return new CraftMinecartCommand(server, (CommandBlockMinecartEntity) entity); }
        } else if (entity instanceof AbstractDecorationEntity) {
            if (entity instanceof PaintingEntity) { return new CraftPainting(server, (PaintingEntity) entity); }
            else if (entity instanceof ItemFrameEntity) { return new CraftItemFrame(server, (ItemFrameEntity) entity); }
            else if (entity instanceof LeadKnotEntity) { return new CraftLeash(server, (LeadKnotEntity) entity); }
            else { return new CraftHanging(server, (AbstractDecorationEntity) entity); }
        }
        else if (entity instanceof TntEntity) { return new CraftTNTPrimed(server, (TntEntity) entity); }
        else if (entity instanceof FireworkEntity) { return new CraftFirework(server, (FireworkEntity) entity); }
        else if (entity instanceof ShulkerBulletEntity) { return new CraftShulkerBullet(server, (ShulkerBulletEntity) entity); }
        else if (entity instanceof AreaEffectCloudEntity) { return new CraftAreaEffectCloud(server, (AreaEffectCloudEntity) entity); }
        else if (entity instanceof EvokerFangsEntity) { return new CraftEvokerFangs(server, (EvokerFangsEntity) entity); }
        else if (entity instanceof LlamaSpitEntity) { return new CraftLlamaSpit(server, (LlamaSpitEntity) entity); }
        // CHECKSTYLE:ON

        throw new AssertionError("Unknown entity " + (entity == null ? null : entity.getClass()));
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), entity.getX(), entity.getY(), entity.getZ(), entity.getBukkitYaw(), entity.pitch);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.getX());
            loc.setY(entity.getY());
            loc.setZ(entity.getZ());
            loc.setYaw(entity.getBukkitYaw());
            loc.setPitch(entity.pitch);
        }

        return loc;
    }

    @Override
    public Vector getVelocity() {
        return CraftVector.toBukkit(entity.getVelocity());
    }

    @Override
    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument(velocity != null, "velocity");
        velocity.checkFinite();
        entity.setVelocity(CraftVector.toNMS(velocity));
        entity.velocityModified = true;
    }

    @Override
    public double getHeight() {
        return getHandle().getHeight();
    }

    @Override
    public double getWidth() {
        return getHandle().getWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        Box bb = getHandle().getBoundingBox();
        return new BoundingBox(bb.x1, bb.y1, bb.z1, bb.x2, bb.y2, bb.z2);
    }

    @Override
    public boolean isOnGround() {
        if (entity instanceof ProjectileEntity) {
            return ((ProjectileEntity) entity).inGround;
        }
        return entity.onGround;
    }

    @Override
    public World getWorld() {
        return entity.world.getCraftWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        yaw = Location.normalizeYaw(yaw);
        pitch = Location.normalizePitch(pitch);

        entity.yaw = yaw;
        entity.pitch = pitch;
        entity.prevYaw = yaw;
        entity.prevPitch = pitch;
        entity.setHeadYaw(yaw);
    }

    @Override
    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location");
        location.checkFinite();

        if (entity.hasPassengers() || entity.removed) {
            return false;
        }

        // If this entity is riding another entity, we must dismount before teleporting.
        entity.stopRiding();

        // Let the server handle cross world teleports
        if (!location.getWorld().equals(getWorld())) {
            entity.teleportTo(((CraftWorld) location.getWorld()).getHandle().getDimension().getType(), new BlockPos(location.getX(), location.getY(), location.getZ()));
            return true;
        }

        // entity.setLocation() throws no event, and so cannot be cancelled
        entity.updatePositionAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // SPIGOT-619: Force sync head rotation also
        entity.setHeadYaw(location.getYaw());

        return true;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    @Override
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        List<Entity> notchEntityList = entity.world.getEntities(entity, entity.getBoundingBox().expand(x, y, z), null);
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<org.bukkit.entity.Entity>(notchEntityList.size());

        for (Entity e : notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    @Override
    public int getEntityId() {
        return entity.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return entity.fireTicks;
    }

    @Override
    public int getMaxFireTicks() {
        return entity.getBurningDuration();
    }

    @Override
    public void setFireTicks(int ticks) {
        entity.fireTicks = ticks;
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public boolean isDead() {
        return !entity.isAlive();
    }

    @Override
    public boolean isValid() {
        return entity.isAlive() && entity.valid && entity.isChunkLoaded();
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public boolean isPersistent() {
        return entity.persist;
    }

    @Override
    public void setPersistent(boolean persistent) {
        entity.persist = persistent;
    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(Vector value) {
        setVelocity(value);
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        return isEmpty() ? null : getHandle().passengerList.get(0).getBukkitEntity();
    }

    @Override
    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");
        if (passenger instanceof CraftEntity) {
            eject();
            return ((CraftEntity) passenger).getHandle().startRiding(getHandle());
        } else {
            return false;
        }
    }

    @Override
    public List<org.bukkit.entity.Entity> getPassengers() {
        return Lists.newArrayList(Lists.transform(getHandle().passengerList, new Function<Entity, org.bukkit.entity.Entity>() {
            @Override
            public org.bukkit.entity.Entity apply(Entity input) {
                return input.getBukkitEntity();
            }
        }));
    }

    @Override
    public boolean addPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "passenger == null");

        return ((CraftEntity) passenger).getHandle().startRiding(getHandle(), true);
    }

    @Override
    public boolean removePassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "passenger == null");

        ((CraftEntity) passenger).getHandle().stopRiding();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return !getHandle().hasPassengers();
    }

    @Override
    public boolean eject() {
        if (isEmpty()) {
            return false;
        }

        getHandle().removeAllPassengers();
        return true;
    }

    @Override
    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    @Override
    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    @Override
    public UUID getUniqueId() {
        return getHandle().getUuid();
    }

    @Override
    public int getTicksLived() {
        return getHandle().age;
    }

    @Override
    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        getHandle().age = value;
    }

    public Entity getHandle() {
        return entity;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkArgument(type != null, "type");

        if (type.getApplicable().isInstance(this)) {
            this.getHandle().world.sendEntityStatus(getHandle(), type.getData());
        }
    }

    public void setHandle(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "CraftEntity{" + "id=" + getEntityId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftEntity other = (CraftEntity) obj;
        return (this.getEntityId() == other.getEntityId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean isInsideVehicle() {
        return getHandle().hasVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        if (!isInsideVehicle()) {
            return false;
        }

        getHandle().stopRiding();
        return true;
    }

    @Override
    public org.bukkit.entity.Entity getVehicle() {
        if (!isInsideVehicle()) {
            return null;
        }

        return getHandle().getVehicle().getBukkitEntity();
    }

    @Override
    public void setCustomName(String name) {
        // sane limit for name length
        if (name != null && name.length() > 256) {
            name = name.substring(0, 256);
        }

        getHandle().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public String getCustomName() {
        Text name = getHandle().getCustomName();

        if (name == null) {
            return null;
        }

        return CraftChatMessage.fromComponent(name);
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getHandle().setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getHandle().isCustomNameVisible();
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {

    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(getHandle().getName(), Formatting.WHITE);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return getPermissibleBase().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return CraftEntity.getPermissibleBase().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return getPermissibleBase().hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return getPermissibleBase().hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return getPermissibleBase().addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return getPermissibleBase().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return getPermissibleBase().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return getPermissibleBase().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        getPermissibleBase().removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        getPermissibleBase().recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getPermissibleBase().getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return getPermissibleBase().isOp();
    }

    @Override
    public void setOp(boolean value) {
        getPermissibleBase().setOp(value);
    }

    @Override
    public void setGlowing(boolean flag) {
        getHandle().glowing = flag;
        Entity e = getHandle();
        if (e.getFlag(6) != flag) {
            e.setFlag(6, flag);
        }
    }

    @Override
    public boolean isGlowing() {
        return getHandle().glowing;
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getHandle().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getHandle().isInvulnerableTo(DamageSource.GENERIC);
    }

    @Override
    public boolean isSilent() {
        return getHandle().isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        getHandle().setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return !getHandle().hasNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return getHandle().netherPortalCooldown;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getHandle().netherPortalCooldown = cooldown;
    }

    @Override
    public Set<String> getScoreboardTags() {
        return getHandle().getScoreboardTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return getHandle().addScoreboardTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return getHandle().removeScoreboardTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getHandle().getPistonBehavior().ordinal());
    }

    @Override
    public BlockFace getFacing() {
        // Use this method over getDirection because it handles boats and minecarts.
        return CraftBlock.notchToBlockFace(getHandle().getMovementDirection());
    }

    @Override
    public CraftPersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @Override
    public Pose getPose() {
        return Pose.values()[getHandle().getPose().ordinal()];
    }

    public void storeBukkitValues(CompoundTag c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(CompoundTag c) {
        CompoundTag base = c.getCompound("BukkitValues");
        if (base != null) {
            this.persistentDataContainer.putAll(base);
        }
    }

    protected CompoundTag save() {
        CompoundTag nbttagcompound = new CompoundTag();

        nbttagcompound.putString("id", getHandle().getSavedEntityId());
        getHandle().toTag(nbttagcompound);

        return nbttagcompound;
    }

    private static PermissibleBase getPermissibleBase() {
        if (perm == null) {
            perm = new PermissibleBase(new ServerOperator() {

                @Override
                public boolean isOp() {
                    return false;
                }

                @Override
                public void setOp(boolean value) {

                }
            });
        }
        return perm;
    }
}
