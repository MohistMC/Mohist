package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mohistmc.bukkit.entity.MohistModsAbstractHorse;
import com.mohistmc.bukkit.entity.MohistModsAnimals;
import com.mohistmc.bukkit.entity.MohistModsChestHorse;
import com.mohistmc.bukkit.entity.MohistModsEntity;
import com.mohistmc.bukkit.entity.MohistModsGolem;
import com.mohistmc.bukkit.entity.MohistModsMinecart;
import com.mohistmc.bukkit.entity.MohistModsMinecartContainer;
import com.mohistmc.bukkit.entity.MohistModsSkeleton;
import com.mohistmc.bukkit.entity.MohistModsTameableEntity;
import com.mohistmc.bukkit.entity.MohistModsThrowableEntity;
import com.mohistmc.bukkit.entity.MohistModsThrowableProjectile;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.phys.AABB;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.CraftSound;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_20_R2.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftLocation;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftSpawnCategory;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftVector;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
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
    private final EntityType entityType;
    private EntityDamageEvent lastDamageEvent;
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;

        this.entityType = CraftEntityType.minecraftToBukkit(entity.getType());
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        /*
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        // CHECKSTYLE:OFF
        if (entity instanceof LivingEntity) {
            // Players
            if (entity instanceof net.minecraft.world.entity.player.Player) {
                if (entity instanceof ServerPlayer) {
                    return new CraftPlayer(server, (ServerPlayer) entity);
                } else { return new CraftHumanEntity(server, (net.minecraft.world.entity.player.Player) entity); }
            }
            // Water Animals
            else if (entity instanceof WaterAnimal) {
                if (entity instanceof net.minecraft.world.entity.animal.Squid) {
                    if (entity instanceof GlowSquid) { return new CraftGlowSquid(server, (GlowSquid) entity); }
                    else { return new CraftSquid(server, (net.minecraft.world.entity.animal.Squid) entity); }
                }
                else if (entity instanceof AbstractFish) {
                    if (entity instanceof net.minecraft.world.entity.animal.Cod) { return new CraftCod(server, (net.minecraft.world.entity.animal.Cod) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Pufferfish) { return new CraftPufferFish(server, (net.minecraft.world.entity.animal.Pufferfish) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Salmon) { return new CraftSalmon(server, (net.minecraft.world.entity.animal.Salmon) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.TropicalFish) { return new CraftTropicalFish(server, (net.minecraft.world.entity.animal.TropicalFish) entity); }
                    else if (entity instanceof Tadpole) { return new CraftTadpole(server, (Tadpole) entity); }
                    else { return new CraftFish(server, (AbstractFish) entity); }
                }
                else if (entity instanceof net.minecraft.world.entity.animal.Dolphin) { return new CraftDolphin(server, (net.minecraft.world.entity.animal.Dolphin) entity); }
                else { return new CraftWaterMob(server, (WaterAnimal) entity); }
            }
            else if (entity instanceof net.minecraft.world.entity.PathfinderMob) {
                // Animals
                if (entity instanceof Animal) {
                    if (entity instanceof net.minecraft.world.entity.animal.Chicken) { return new CraftChicken(server, (net.minecraft.world.entity.animal.Chicken) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Cow) {
                        if (entity instanceof net.minecraft.world.entity.animal.MushroomCow) { return new CraftMushroomCow(server, (net.minecraft.world.entity.animal.MushroomCow) entity); }
                        else { return new CraftCow(server, (net.minecraft.world.entity.animal.Cow) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.animal.Pig) { return new CraftPig(server, (net.minecraft.world.entity.animal.Pig) entity); }
                    else if (entity instanceof net.minecraft.world.entity.TamableAnimal) {
                        if (entity instanceof net.minecraft.world.entity.animal.Wolf) { return new CraftWolf(server, (net.minecraft.world.entity.animal.Wolf) entity); }
                        else if (entity instanceof net.minecraft.world.entity.animal.Cat) { return new CraftCat(server, (net.minecraft.world.entity.animal.Cat) entity); }
                        else if (entity instanceof net.minecraft.world.entity.animal.Parrot) { return new CraftParrot(server, (net.minecraft.world.entity.animal.Parrot) entity); }
                        else { return new MohistModsTameableEntity( server, ( TamableAnimal ) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.animal.Sheep) { return new CraftSheep(server, (net.minecraft.world.entity.animal.Sheep) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.horse.AbstractHorse) {
                        if (entity instanceof net.minecraft.world.entity.animal.horse.AbstractChestedHorse){
                            if (entity instanceof net.minecraft.world.entity.animal.horse.Donkey) { return new CraftDonkey(server, (net.minecraft.world.entity.animal.horse.Donkey) entity); }
                            else if (entity instanceof net.minecraft.world.entity.animal.horse.Mule) { return new CraftMule(server, (net.minecraft.world.entity.animal.horse.Mule) entity); }
                            else if (entity instanceof net.minecraft.world.entity.animal.horse.TraderLlama) { return new CraftTraderLlama(server, (net.minecraft.world.entity.animal.horse.TraderLlama) entity); }
                            else if (entity instanceof net.minecraft.world.entity.animal.horse.Llama) { return new CraftLlama(server, (net.minecraft.world.entity.animal.horse.Llama) entity); }
                            else { return new MohistModsChestHorse( server, ( AbstractChestedHorse ) entity); }
                        } else if (entity instanceof net.minecraft.world.entity.animal.horse.Horse) { return new CraftHorse(server, (net.minecraft.world.entity.animal.horse.Horse) entity); }
                        else if (entity instanceof net.minecraft.world.entity.animal.horse.SkeletonHorse) { return new CraftSkeletonHorse(server, (net.minecraft.world.entity.animal.horse.SkeletonHorse) entity); }
                        else if (entity instanceof net.minecraft.world.entity.animal.horse.ZombieHorse) { return new CraftZombieHorse(server, (net.minecraft.world.entity.animal.horse.ZombieHorse) entity); }
                        else if (entity instanceof net.minecraft.world.entity.animal.camel.Camel) { return new CraftCamel(server, (net.minecraft.world.entity.animal.camel.Camel) entity); }
                        else { return new MohistModsAbstractHorse( server, ( AbstractHorse ) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.animal.Rabbit) { return new CraftRabbit(server, (net.minecraft.world.entity.animal.Rabbit) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.PolarBear) { return new CraftPolarBear(server, (net.minecraft.world.entity.animal.PolarBear) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Turtle) { return new CraftTurtle(server, (net.minecraft.world.entity.animal.Turtle) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Ocelot) { return new CraftOcelot(server, (net.minecraft.world.entity.animal.Ocelot) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Panda) { return new CraftPanda(server, (net.minecraft.world.entity.animal.Panda) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Fox) { return new CraftFox(server, (net.minecraft.world.entity.animal.Fox) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.Bee) { return new CraftBee(server, (net.minecraft.world.entity.animal.Bee) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.hoglin.Hoglin) { return new CraftHoglin(server, (net.minecraft.world.entity.monster.hoglin.Hoglin) entity); }
                    else if (entity instanceof Strider) { return new CraftStrider(server, (Strider) entity); }
                    else if (entity instanceof Axolotl) { return new CraftAxolotl(server, (Axolotl) entity); }
                    else if (entity instanceof Goat) { return new CraftGoat(server, (Goat) entity); }
                    else if (entity instanceof Frog) { return new CraftFrog(server, (Frog) entity); }
                    else if (entity instanceof Sniffer) { return new CraftSniffer(server, (Sniffer) entity); }
                    else  { return new MohistModsAnimals(server, (Animal) entity); }
                }
                // Monsters
                else if (entity instanceof Monster) {
                    if (entity instanceof net.minecraft.world.entity.monster.Zombie) {
                        if (entity instanceof net.minecraft.world.entity.monster.ZombifiedPiglin) { return new CraftPigZombie(server, (net.minecraft.world.entity.monster.ZombifiedPiglin) entity); }
                        else if (entity instanceof net.minecraft.world.entity.monster.Husk) { return new CraftHusk(server, (net.minecraft.world.entity.monster.Husk) entity); }
                        else if (entity instanceof net.minecraft.world.entity.monster.ZombieVillager) { return new CraftVillagerZombie(server, (net.minecraft.world.entity.monster.ZombieVillager) entity); }
                        else if (entity instanceof net.minecraft.world.entity.monster.Drowned) { return new CraftDrowned(server, (net.minecraft.world.entity.monster.Drowned) entity); }
                        else { return new CraftZombie(server, (net.minecraft.world.entity.monster.Zombie) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.monster.Creeper) { return new CraftCreeper(server, (net.minecraft.world.entity.monster.Creeper) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.EnderMan) { return new CraftEnderman(server, (net.minecraft.world.entity.monster.EnderMan) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.Silverfish) { return new CraftSilverfish(server, (net.minecraft.world.entity.monster.Silverfish) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.Giant) { return new CraftGiant(server, (net.minecraft.world.entity.monster.Giant) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.AbstractSkeleton) {
                        if (entity instanceof net.minecraft.world.entity.monster.Stray) { return new CraftStray(server, (net.minecraft.world.entity.monster.Stray) entity); }
                        else if (entity instanceof net.minecraft.world.entity.monster.WitherSkeleton) { return new CraftWitherSkeleton(server, (net.minecraft.world.entity.monster.WitherSkeleton) entity); }
                        else if (entity instanceof net.minecraft.world.entity.monster.Skeleton){ return new CraftSkeleton(server, (net.minecraft.world.entity.monster.Skeleton) entity); }
                        else { return new MohistModsSkeleton(server, (net.minecraft.world.entity.monster.AbstractSkeleton) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.monster.Blaze) { return new CraftBlaze(server, (net.minecraft.world.entity.monster.Blaze) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.Witch) { return new CraftWitch(server, (net.minecraft.world.entity.monster.Witch) entity); }
                    else if (entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss) { return new CraftWither(server, (net.minecraft.world.entity.boss.wither.WitherBoss) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.Spider) {
                        if (entity instanceof net.minecraft.world.entity.monster.CaveSpider) { return new CraftCaveSpider(server, (net.minecraft.world.entity.monster.CaveSpider) entity); }
                        else { return new CraftSpider(server, (net.minecraft.world.entity.monster.Spider) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.monster.Endermite) { return new CraftEndermite(server, (net.minecraft.world.entity.monster.Endermite) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.Guardian) {
                        if (entity instanceof net.minecraft.world.entity.monster.ElderGuardian) { return new CraftElderGuardian(server, (net.minecraft.world.entity.monster.ElderGuardian) entity); }
                        else { return new CraftGuardian(server, (net.minecraft.world.entity.monster.Guardian) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.monster.Vex) { return new CraftVex(server, (net.minecraft.world.entity.monster.Vex) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.AbstractIllager) {
                        if (entity instanceof SpellcasterIllager) {
                            if (entity instanceof net.minecraft.world.entity.monster.Evoker) { return new CraftEvoker(server, (net.minecraft.world.entity.monster.Evoker) entity); }
                            else if (entity instanceof net.minecraft.world.entity.monster.Illusioner) { return new CraftIllusioner(server, (net.minecraft.world.entity.monster.Illusioner) entity); }
                            else {  return new CraftSpellcaster(server, (SpellcasterIllager) entity); }
                        }
                        else if (entity instanceof net.minecraft.world.entity.monster.Vindicator) { return new CraftVindicator(server, (net.minecraft.world.entity.monster.Vindicator) entity); }
                        else if (entity instanceof net.minecraft.world.entity.monster.Pillager) { return new CraftPillager(server, (net.minecraft.world.entity.monster.Pillager) entity); }
                        else { return new CraftIllager(server, (net.minecraft.world.entity.monster.AbstractIllager) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.monster.Ravager) { return new CraftRavager(server, (net.minecraft.world.entity.monster.Ravager) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.piglin.AbstractPiglin) {
                        if (entity instanceof Piglin) return new CraftPiglin(server, (Piglin) entity);
                        else if (entity instanceof PiglinBrute) { return new CraftPiglinBrute(server, (PiglinBrute) entity); }
                        else { return new CraftPiglinAbstract(server, (net.minecraft.world.entity.monster.piglin.AbstractPiglin) entity); }
                    }
                    else if (entity instanceof net.minecraft.world.entity.monster.Zoglin) { return new CraftZoglin(server, (net.minecraft.world.entity.monster.Zoglin) entity); }

                    else if (entity instanceof Warden) { return new CraftWarden(server, (Warden) entity); }

                    else  { return new CraftMonster(server, (net.minecraft.world.entity.monster.Monster) entity); }
                }
                else if (entity instanceof AbstractGolem abstractGolem) {
                    if (entity instanceof net.minecraft.world.entity.animal.SnowGolem) { return new CraftSnowman(server, (net.minecraft.world.entity.animal.SnowGolem) entity); }
                    else if (entity instanceof net.minecraft.world.entity.animal.IronGolem) { return new CraftIronGolem(server, (net.minecraft.world.entity.animal.IronGolem) entity); }
                    else if (entity instanceof net.minecraft.world.entity.monster.Shulker) { return new CraftShulker(server, (net.minecraft.world.entity.monster.Shulker) entity); }
                    else { return new MohistModsGolem(server, abstractGolem); }
                }
                else if (entity instanceof AbstractVillager) {
                    if (entity instanceof Villager) { return new CraftVillager(server, (Villager) entity); }
                    else if (entity instanceof net.minecraft.world.entity.npc.WanderingTrader) { return new CraftWanderingTrader(server, (net.minecraft.world.entity.npc.WanderingTrader) entity); }
                    else { return new CraftAbstractVillager(server, (AbstractVillager) entity); }
                }
                else if (entity instanceof Allay) { return new CraftAllay(server, (Allay) entity); }
                else { return new CraftCreature(server, (net.minecraft.world.entity.PathfinderMob) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof Slime) {
                if (entity instanceof net.minecraft.world.entity.monster.MagmaCube) { return new CraftMagmaCube(server, (net.minecraft.world.entity.monster.MagmaCube) entity); }
                else { return new CraftSlime(server, (Slime) entity); }
            }
            // Flying
            else if (entity instanceof net.minecraft.world.entity.FlyingMob) {
                if (entity instanceof Ghast) { return new CraftGhast(server, (Ghast) entity); }
                else if (entity instanceof net.minecraft.world.entity.monster.Phantom) { return new CraftPhantom(server, (net.minecraft.world.entity.monster.Phantom) entity); }
                else { return new CraftFlying(server, (net.minecraft.world.entity.FlyingMob) entity); }
            }
            else if (entity instanceof EnderDragon) {
                return new CraftEnderDragon(server, (EnderDragon) entity);
            }
            // Ambient
            else if (entity instanceof net.minecraft.world.entity.ambient.AmbientCreature) {
                if (entity instanceof net.minecraft.world.entity.ambient.Bat) { return new CraftBat(server, (net.minecraft.world.entity.ambient.Bat) entity); }
                else { return new CraftAmbient(server, (net.minecraft.world.entity.ambient.AmbientCreature) entity); }
            }
            else if (entity instanceof net.minecraft.world.entity.decoration.ArmorStand) { return new CraftArmorStand(server, (net.minecraft.world.entity.decoration.ArmorStand) entity); }
            else  { return new CraftLivingEntity(server, (LivingEntity) entity); }
        }
        else if (entity instanceof net.minecraft.world.entity.boss.EnderDragonPart) {
            net.minecraft.world.entity.boss.EnderDragonPart part = (net.minecraft.world.entity.boss.EnderDragonPart) entity;
            if (part.parentMob instanceof EnderDragon) { return new CraftEnderDragonPart(server, (net.minecraft.world.entity.boss.EnderDragonPart) entity); }
            else { return new CraftComplexPart(server, (net.minecraft.world.entity.boss.EnderDragonPart) entity); }
        }
        else if (entity instanceof ExperienceOrb) { return new CraftExperienceOrb(server, (ExperienceOrb) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.Arrow) { return new CraftTippedArrow(server, (net.minecraft.world.entity.projectile.Arrow) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.SpectralArrow) { return new CraftSpectralArrow(server, (net.minecraft.world.entity.projectile.SpectralArrow) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.AbstractArrow) {
            if (entity instanceof net.minecraft.world.entity.projectile.ThrownTrident) { return new CraftTrident(server, (net.minecraft.world.entity.projectile.ThrownTrident) entity); }
            else { return new CraftArrow(server, (net.minecraft.world.entity.projectile.AbstractArrow) entity); }
        }
        else if (entity instanceof Boat) {
            if (entity instanceof ChestBoat) { return new CraftChestBoat(server, (ChestBoat) entity); }
            else { return new CraftBoat(server, (Boat) entity); }
        }
        else if (entity instanceof net.minecraft.world.entity.projectile.ThrowableProjectile throwableProjectile) {
            if (entity instanceof net.minecraft.world.entity.projectile.ThrownEgg) { return new CraftEgg(server, (net.minecraft.world.entity.projectile.ThrownEgg) entity); }
            else if (entity instanceof net.minecraft.world.entity.projectile.Snowball) { return new CraftSnowball(server, (net.minecraft.world.entity.projectile.Snowball) entity); }
            else if (entity instanceof net.minecraft.world.entity.projectile.ThrownPotion) { return new CraftThrownPotion(server, (net.minecraft.world.entity.projectile.ThrownPotion) entity); }
            else if (entity instanceof net.minecraft.world.entity.projectile.ThrownEnderpearl) { return new CraftEnderPearl(server, (net.minecraft.world.entity.projectile.ThrownEnderpearl) entity); }
            else if (entity instanceof net.minecraft.world.entity.projectile.ThrownExperienceBottle) { return new CraftThrownExpBottle(server, (net.minecraft.world.entity.projectile.ThrownExperienceBottle) entity); }
            else if (entity instanceof ThrowableItemProjectile) { return new MohistModsThrowableProjectile(server, (ThrowableItemProjectile) entity); }
            else { return new MohistModsThrowableEntity( server, throwableProjectile); }
        }
        else if (entity instanceof FallingBlockEntity) { return new CraftFallingBlock(server, (FallingBlockEntity) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.AbstractHurtingProjectile) {
            if (entity instanceof net.minecraft.world.entity.projectile.SmallFireball) { return new CraftSmallFireball(server, (net.minecraft.world.entity.projectile.SmallFireball) entity); }
            else if (entity instanceof net.minecraft.world.entity.projectile.LargeFireball) { return new CraftLargeFireball(server, (net.minecraft.world.entity.projectile.LargeFireball) entity); }
            else if (entity instanceof net.minecraft.world.entity.projectile.WitherSkull) { return new CraftWitherSkull(server, (net.minecraft.world.entity.projectile.WitherSkull) entity); }
            else if (entity instanceof net.minecraft.world.entity.projectile.DragonFireball) { return new CraftDragonFireball(server, (net.minecraft.world.entity.projectile.DragonFireball) entity); }
            else { return new CraftFireball(server, (net.minecraft.world.entity.projectile.AbstractHurtingProjectile) entity); }
        }
        else if (entity instanceof net.minecraft.world.entity.projectile.EyeOfEnder) { return new CraftEnderSignal(server, (net.minecraft.world.entity.projectile.EyeOfEnder) entity); }
        else if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EndCrystal) { return new CraftEnderCrystal(server, (net.minecraft.world.entity.boss.enderdragon.EndCrystal) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.FishingHook) { return new CraftFishHook(server, (net.minecraft.world.entity.projectile.FishingHook) entity); }
        else if (entity instanceof net.minecraft.world.entity.item.ItemEntity) { return new CraftItem(server, (net.minecraft.world.entity.item.ItemEntity) entity); }
        else if (entity instanceof net.minecraft.world.entity.LightningBolt) { return new CraftLightningStrike(server, (net.minecraft.world.entity.LightningBolt) entity); }
        else if (entity instanceof net.minecraft.world.entity.vehicle.AbstractMinecart) {
            if (entity instanceof net.minecraft.world.entity.vehicle.MinecartFurnace) { return new CraftMinecartFurnace(server, (net.minecraft.world.entity.vehicle.MinecartFurnace) entity); }
            else if (entity instanceof net.minecraft.world.entity.vehicle.MinecartChest) { return new CraftMinecartChest(server, (net.minecraft.world.entity.vehicle.MinecartChest) entity); }
            else if (entity instanceof net.minecraft.world.entity.vehicle.MinecartTNT) { return new CraftMinecartTNT(server, (net.minecraft.world.entity.vehicle.MinecartTNT) entity); }
            else if (entity instanceof net.minecraft.world.entity.vehicle.MinecartHopper) { return new CraftMinecartHopper(server, (net.minecraft.world.entity.vehicle.MinecartHopper) entity); }
            else if (entity instanceof net.minecraft.world.entity.vehicle.MinecartSpawner) { return new CraftMinecartMobSpawner(server, (net.minecraft.world.entity.vehicle.MinecartSpawner) entity); }
            else if (entity instanceof net.minecraft.world.entity.vehicle.Minecart) { return new CraftMinecartRideable(server, (net.minecraft.world.entity.vehicle.Minecart) entity); }
            else if (entity instanceof MinecartCommandBlock) { return new CraftMinecartCommand(server, (MinecartCommandBlock) entity); }
            else if (entity instanceof AbstractMinecartContainer) { return new MohistModsMinecartContainer(server, (AbstractMinecartContainer) entity); }
            else { return new MohistModsMinecart(server, (AbstractMinecart) entity); }
        } else if (entity instanceof net.minecraft.world.entity.decoration.HangingEntity) {
            if (entity instanceof net.minecraft.world.entity.decoration.Painting) { return new CraftPainting(server, (net.minecraft.world.entity.decoration.Painting) entity); }
            else if (entity instanceof net.minecraft.world.entity.decoration.ItemFrame) {
                if (entity instanceof GlowItemFrame) { return new CraftGlowItemFrame(server, (GlowItemFrame) entity); }
                else { return new CraftItemFrame(server, (net.minecraft.world.entity.decoration.ItemFrame) entity); }
            }
            else if (entity instanceof net.minecraft.world.entity.decoration.LeashFenceKnotEntity) { return new CraftLeash(server, (net.minecraft.world.entity.decoration.LeashFenceKnotEntity) entity); }
            else { return new CraftHanging(server, (net.minecraft.world.entity.decoration.HangingEntity) entity); }
        }
        else if (entity instanceof net.minecraft.world.entity.item.PrimedTnt) { return new CraftTNTPrimed(server, (net.minecraft.world.entity.item.PrimedTnt) entity); }
        else if (entity instanceof FireworkRocketEntity) { return new CraftFirework(server, (FireworkRocketEntity) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.ShulkerBullet) { return new CraftShulkerBullet(server, (net.minecraft.world.entity.projectile.ShulkerBullet) entity); }
        else if (entity instanceof AreaEffectCloud) { return new CraftAreaEffectCloud(server, (AreaEffectCloud) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.EvokerFangs) { return new CraftEvokerFangs(server, (net.minecraft.world.entity.projectile.EvokerFangs) entity); }
        else if (entity instanceof net.minecraft.world.entity.projectile.LlamaSpit) { return new CraftLlamaSpit(server, (net.minecraft.world.entity.projectile.LlamaSpit) entity); }
        else if (entity instanceof Marker) { return new CraftMarker(server, (Marker) entity); }
        else if (entity instanceof Interaction) { return new CraftInteraction(server, (Interaction) entity); }
        else if (entity instanceof Display) {
            if (entity instanceof Display.BlockDisplay) { return new CraftBlockDisplay(server, (Display.BlockDisplay) entity); }
            else if (entity instanceof Display.ItemDisplay) { return new CraftItemDisplay(server, (Display.ItemDisplay) entity); }
            else if (entity instanceof Display.TextDisplay) { return new CraftTextDisplay(server, (Display.TextDisplay) entity); }
            else { return new CraftDisplay(server, (Display) entity); }
        }
        return new MohistModsEntity(server, entity);
    }

    @Override
    public Location getLocation() {
        return CraftLocation.toBukkit(entity.position(), getWorld(), entity.getBukkitYaw(), entity.getXRot());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.getX());
            loc.setY(entity.getY());
            loc.setZ(entity.getZ());
            loc.setYaw(entity.getBukkitYaw());
            loc.setPitch(entity.getXRot());
        }

        return loc;
    }

    @Override
    public Vector getVelocity() {
        return CraftVector.toBukkit(entity.getDeltaMovement());
    }

    @Override
    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument(velocity != null, "velocity");
        // velocity.checkFinite();
        entity.setDeltaMovement(CraftVector.toNMS(velocity));
        entity.hurtMarked = true;
    }

    @Override
    public double getHeight() {
        return getHandle().getBbHeight();
    }

    @Override
    public double getWidth() {
        return getHandle().getBbWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        AABB bb = getHandle().getBoundingBox();
        return new BoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    @Override
    public boolean isOnGround() {
        if (entity instanceof net.minecraft.world.entity.projectile.AbstractArrow) {
            return ((net.minecraft.world.entity.projectile.AbstractArrow) entity).inGround;
        }
        return entity.onGround();
    }

    @Override
    public boolean isInWater() {
        return entity.isInWater();
    }

    @Override
    public World getWorld() {
        return entity.level().getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        yaw = Location.normalizeYaw(yaw);
        pitch = Location.normalizePitch(pitch);

        entity.setYRot(yaw);
        entity.setXRot(pitch);
        entity.yRotO = yaw;
        entity.xRotO = pitch;
        entity.setYHeadRot(yaw);
    }

    @Override
    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        location.checkFinite();

        if (entity.isVehicle() || entity.isRemoved()) {
            return false;
        }

        // If this entity is riding another entity, we must dismount before teleporting.
        entity.stopRiding();

        // Let the server handle cross world teleports
        if (location.getWorld() != null && !location.getWorld().equals(getWorld())) {
            // Prevent teleportation to an other world during world generation
            Preconditions.checkState(!entity.generation, "Cannot teleport entity to an other world during world generation");
            entity.teleportTo(((CraftWorld) location.getWorld()).getHandle(), CraftLocation.toVec3D(location));
            return true;
        }

        // entity.setLocation() throws no event, and so cannot be cancelled
        entity.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // SPIGOT-619: Force sync head rotation also
        entity.setYHeadRot(location.getYaw());

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
        Preconditions.checkState(!entity.generation, "Cannot get nearby entities during world generation");
        List<Entity> notchEntityList = entity.level.getEntities(entity, entity.getBoundingBox().inflate(x, y, z), Predicates.alwaysTrue());
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<org.bukkit.entity.Entity>(notchEntityList.size());

        for (Entity e : notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    @Override
    public int getEntityId() {
        return entity.getId();
    }

    @Override
    public int getFireTicks() {
        return entity.getRemainingFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return entity.getFireImmuneTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        entity.setRemainingFireTicks(ticks);
    }

    @Override
    public void setVisualFire(boolean fire) {
        getHandle().hasVisualFire = fire;
    }

    @Override
    public boolean isVisualFire() {
        return getHandle().hasVisualFire;
    }

    @Override
    public int getFreezeTicks() {
        return getHandle().getTicksFrozen();
    }

    @Override
    public int getMaxFreezeTicks() {
        return getHandle().getTicksRequiredToFreeze();
    }

    @Override
    public void setFreezeTicks(int ticks) {
        Preconditions.checkArgument(0 <= ticks, "Ticks (%s) cannot be less than 0", ticks);

        getHandle().setTicksFrozen(ticks);
    }

    @Override
    public boolean isFrozen() {
        return getHandle().isFullyFrozen();
    }

    @Override
    public void remove() {
        entity.discard();
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
        return isEmpty() ? null : getHandle().passengers.get(0).getBukkitEntity();
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
        return Lists.newArrayList(Lists.transform(getHandle().passengers, (Function<Entity, org.bukkit.entity.Entity>) input -> input.getBukkitEntity()));
    }

    @Override
    public boolean addPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");

        return ((CraftEntity) passenger).getHandle().startRiding(getHandle(), true);
    }

    @Override
    public boolean removePassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");

        ((CraftEntity) passenger).getHandle().stopRiding();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return !getHandle().isVehicle();
    }

    @Override
    public boolean eject() {
        if (isEmpty()) {
            return false;
        }

        getHandle().ejectPassengers();
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
        return getHandle().getUUID();
    }

    @Override
    public int getTicksLived() {
        return getHandle().tickCount;
    }

    @Override
    public void setTicksLived(int value) {
        Preconditions.checkArgument(value > 0, "Age value (%s) must be greater than 0", value);
        getHandle().tickCount = value;
    }

    public Entity getHandle() {
        return entity;
    }

    @Override
    public final EntityType getType() {
        return entityType;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkArgument(type != null, "Type cannot be null");
        Preconditions.checkState(!entity.generation, "Cannot play effect during world generation");

        if (type.getApplicable().isInstance(this)) {
            this.getHandle().level.broadcastEntityEvent(getHandle(), type.getData());
        }
    }

    @Override
    public Sound getSwimSound() {
        return CraftSound.minecraftToBukkit(getHandle().getSwimSound0());
    }

    @Override
    public Sound getSwimSplashSound() {
        return CraftSound.minecraftToBukkit(getHandle().getSwimSplashSound0());
    }

    @Override
    public Sound getSwimHighSpeedSplashSound() {
        return CraftSound.minecraftToBukkit(getHandle().getSwimHighSpeedSplashSound0());
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
        return getHandle().isPassenger();
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
        Component name = getHandle().getCustomName();

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
    public void setVisibleByDefault(boolean visible) {
        if (getHandle().visibleByDefault != visible) {
            if (visible) {
                // Making visible by default, reset and show to all players
                for (Player player : server.getOnlinePlayers()) {
                    ((CraftPlayer) player).resetAndShowEntity(this);
                }
            } else {
                // Hiding by default, reset and hide from all players
                for (Player player : server.getOnlinePlayers()) {
                    ((CraftPlayer) player).resetAndHideEntity(this);
                }
            }

            getHandle().visibleByDefault = visible;
        }
    }

    @Override
    public boolean isVisibleByDefault() {
        return getHandle().visibleByDefault;
    }

    @Override
    public Set<Player> getTrackedBy() {
        Preconditions.checkState(!entity.generation, "Cannot get tracking players during world generation");
        ImmutableSet.Builder<Player> players = ImmutableSet.builder();

        ServerLevel world = ((CraftWorld) getWorld()).getHandle();
        ChunkMap.TrackedEntity entityTracker = world.getChunkSource().chunkMap.entityMap.get(getEntityId());

        if (entityTracker != null) {
            for (ServerPlayerConnection connection : entityTracker.seenBy) {
                players.add(connection.getPlayer().getBukkitEntity());
            }
        }

        return players.build();
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String... messages) {

    }

    @Override
    public void sendMessage(UUID sender, String message) {
        this.sendMessage(message); // Most entities don't know about senders
    }

    @Override
    public void sendMessage(UUID sender, String... messages) {
        this.sendMessage(messages); // Most entities don't know about senders
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(getHandle().getName());
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
        getHandle().setGlowingTag(flag);
    }

    @Override
    public boolean isGlowing() {
        return getHandle().isCurrentlyGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getHandle().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getHandle().isInvulnerableTo(getHandle().damageSources().generic());
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
        return !getHandle().isNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return getHandle().portalCooldown;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getHandle().portalCooldown = cooldown;
    }

    @Override
    public Set<String> getScoreboardTags() {
        return getHandle().getTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return getHandle().addTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return getHandle().removeTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getHandle().getPistonPushReaction().ordinal());
    }

    @Override
    public BlockFace getFacing() {
        // Use this method over getDirection because it handles boats and minecarts.
        return CraftBlock.notchToBlockFace(getHandle().getMotionDirection());
    }

    @Override
    public CraftPersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @Override
    public Pose getPose() {
        return Pose.values()[getHandle().getPose().ordinal()];
    }

    // Paper start
    @Override
    public void setSneaking(boolean sneak) {
        this.getHandle().setShiftKeyDown(sneak);
    }

    @Override
    public boolean isSneaking() {
        return this.getHandle().isShiftKeyDown();
    }
    // Paper end

    @Override
    public SpawnCategory getSpawnCategory() {
        return CraftSpawnCategory.toBukkit(getHandle().getType().getCategory());
    }

    public void storeBukkitValues(CompoundTag c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(CompoundTag c) {
        Tag base = c.get("BukkitValues");
        if (base instanceof CompoundTag) {
            this.persistentDataContainer.putAll((CompoundTag) base);
        }
    }

    protected CompoundTag save() {
        CompoundTag nbttagcompound = new CompoundTag();

        nbttagcompound.putString("id", getHandle().getEncodeId());
        getHandle().saveWithoutId(nbttagcompound);

        return nbttagcompound;
    }

    // re-sends the spawn entity packet to updated values which cannot be updated otherwise
    protected void update() {
        if (!getHandle().isAlive()) {
            return;
        }

        ServerLevel world = ((CraftWorld) getWorld()).getHandle();
        ChunkMap.TrackedEntity entityTracker = world.getChunkSource().chunkMap.entityMap.get(getEntityId());

        if (entityTracker == null) {
            return;
        }

        entityTracker.broadcast(getHandle().getAddEntityPacket());
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

    // Spigot start
    private final org.bukkit.entity.Entity.Spigot spigot = new org.bukkit.entity.Entity.Spigot()
    {
    };

    public org.bukkit.entity.Entity.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
