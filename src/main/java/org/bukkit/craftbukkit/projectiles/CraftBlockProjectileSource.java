package org.bukkit.craftbukkit.projectiles;

import java.util.Random;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.thrown.ThrownEggEntity;
import net.minecraft.entity.thrown.ThrownEnderpearlEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.entity.thrown.ThrownExperienceBottleEntity;
import net.minecraft.entity.thrown.ThrownPotionEntity;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final DispenserBlockEntity dispenserBlock;

    public CraftBlockProjectileSource(DispenserBlockEntity dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.getWorld().getCraftWorld().getBlockAt(dispenserBlock.getPos().getX(), dispenserBlock.getPos().getY(), dispenserBlock.getPos().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        BlockPointerImpl isourceblock = new BlockPointerImpl(dispenserBlock.getWorld(), dispenserBlock.getPos());
        // Copied from DispenseBehaviorProjectile
        Position iposition = DispenserBlock.getOutputLocation(isourceblock);
        Direction enumdirection = (Direction) isourceblock.getBlockState().get(DispenserBlock.FACING);
        net.minecraft.world.World world = dispenserBlock.getWorld();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new SnowballEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new ThrownEggEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new ThrownEnderpearlEntity(world, null);
            launch.updatePosition(iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new ThrownExperienceBottleEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new ThrownPotionEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
                ((ThrownPotionEntity) launch).setItemStack(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
            } else {
                launch = new ThrownPotionEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
                ((ThrownPotionEntity) launch).setItemStack(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
            }
        } else if (AbstractArrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new ArrowEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
                ((ArrowEntity) launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new SpectralArrowEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
            } else {
                launch = new ArrowEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
            }
            ((ProjectileEntity) launch).pickupType = ProjectileEntity.PickupPermission.ALLOWED;
            ((ProjectileEntity) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumdirection.getOffsetX() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumdirection.getOffsetY() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumdirection.getOffsetZ() * 0.3F);
            Random random = world.random;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getOffsetX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getOffsetY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getOffsetZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new SmallFireballEntity(world, null, d0, d1, d2);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = EntityType.WITHER_SKULL.create(world);
                launch.updatePosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((ExplosiveProjectileEntity) launch).posX = d3 / d6 * 0.1D;
                ((ExplosiveProjectileEntity) launch).posY = d4 / d6 * 0.1D;
                ((ExplosiveProjectileEntity) launch).posZ = d5 / d6 * 0.1D;
            } else {
                launch = EntityType.FIREBALL.create(world);
                launch.updatePosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((ExplosiveProjectileEntity) launch).posX = d3 / d6 * 0.1D;
                ((ExplosiveProjectileEntity) launch).posY = d4 / d6 * 0.1D;
                ((ExplosiveProjectileEntity) launch).posZ = d5 / d6 * 0.1D;
            }

            ((ExplosiveProjectileEntity) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof net.minecraft.entity.projectile.Projectile) {
            if (launch instanceof ThrownEntity) {
                ((ThrownEntity) launch).projectileSource = this;
            }
            // Values from DispenseBehaviorProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof ThrownPotionEntity || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseBehavior classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseBehaviorProjectile
            ((net.minecraft.entity.projectile.Projectile) launch).setVelocity((double) enumdirection.getOffsetX(), (double) ((float) enumdirection.getOffsetY() + 0.1F), (double) enumdirection.getOffsetZ(), b, a);
        }

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.spawnEntity(launch);
        return (T) launch.getBukkitEntity();
    }
}
