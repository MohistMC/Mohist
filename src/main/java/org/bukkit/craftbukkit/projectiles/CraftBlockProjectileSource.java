package org.bukkit.craftbukkit.projectiles;

import java.util.Random;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProxyBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.ExperienceBottleEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
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
    private final DispenserTileEntity dispenserBlock;

    public CraftBlockProjectileSource(DispenserTileEntity dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.getWorld().getCBWorld().getBlockAt(dispenserBlock.getPos().getX(), dispenserBlock.getPos().getY(), dispenserBlock.getPos().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from DispenserBlock.dispense()
        ProxyBlockSource isourceblock = new ProxyBlockSource(dispenserBlock.getWorld().getMinecraftWorld(), dispenserBlock.getPos());
        // Copied from DispenseTaskProjectile
        IPosition iposition = DispenserBlock.getDispensePosition(isourceblock);
        Direction enumdirection = (Direction) isourceblock.getBlockState().get(DispenserBlock.FACING);
        net.minecraft.world.World world = dispenserBlock.getWorld();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new SnowballEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EggEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EnderPearlEntity(world, null);
            launch.setPosition(iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new ExperienceBottleEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new PotionEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
                ((PotionEntity) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
            } else {
                launch = new PotionEntity(world, iposition.getX(), iposition.getY(), iposition.getZ());
                ((PotionEntity) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
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
            ((AbstractArrowEntity) launch).pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
            ((AbstractArrowEntity) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumdirection.getXOffset() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumdirection.getYOffset() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumdirection.getZOffset() * 0.3F);
            Random random = world.rand;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getXOffset();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getYOffset();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getZOffset();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new SmallFireballEntity(world, null, d0, d1, d2);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = EntityType.WITHER_SKULL.create(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((DamagingProjectileEntity) launch).accelerationX = d3 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).accelerationY = d4 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).accelerationZ = d5 / d6 * 0.1D;
            } else {
                launch = EntityType.FIREBALL.create(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((DamagingProjectileEntity) launch).accelerationX = d3 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).accelerationY = d4 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).accelerationZ = d5 / d6 * 0.1D;
            }

            ((DamagingProjectileEntity) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof ProjectileEntity) {
            if (launch instanceof ThrowableEntity) {
                ((ThrowableEntity) launch).projectileSource = this;
            }
            // Values from DispenseTaskProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof PotionEntity || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseTask classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseTaskProjectile
            ((ProjectileEntity) launch).shoot((double) enumdirection.getXOffset(), (double) ((float) enumdirection.getYOffset() + 0.1F), (double) enumdirection.getZOffset(), b, a);
        }

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.addEntity(launch);
        return (T) launch.getBukkitEntity();
    }
}
