package org.bukkit.craftbukkit.projectiles;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;


public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final net.minecraft.tileentity.TileEntityDispenser dispenserBlock;

    public CraftBlockProjectileSource(net.minecraft.tileentity.TileEntityDispenser dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.getWorldObj().getWorld().getBlockAt(dispenserBlock.xCoord, dispenserBlock.yCoord, dispenserBlock.zCoord);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        net.minecraft.block.BlockSourceImpl isourceblock = new net.minecraft.block.BlockSourceImpl(dispenserBlock.getWorldObj(), dispenserBlock.xCoord, dispenserBlock.yCoord, dispenserBlock.zCoord);
        // Copied from DispenseBehaviorProjectile
        net.minecraft.dispenser.IPosition iposition = net.minecraft.block.BlockDispenser.func_149939_a(isourceblock);
        net.minecraft.util.EnumFacing enumfacing = net.minecraft.block.BlockDispenser.func_149937_b(isourceblock.getBlockMetadata());
        net.minecraft.world.World world = dispenserBlock.getWorldObj();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.item.EntityEnderPearl(world);
            launch.setPosition(iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.item.EntityExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntityArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            ((net.minecraft.entity.projectile.EntityArrow) launch).canBePickedUp = 1;
            ((net.minecraft.entity.projectile.EntityArrow) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumfacing.getFrontOffsetX() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumfacing.getFrontOffsetX() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumfacing.getFrontOffsetZ() * 0.3F);
            Random random = world.rand;
            double d3 = random.nextGaussian() * 0.05D + (double) enumfacing.getFrontOffsetX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumfacing.getFrontOffsetY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumfacing.getFrontOffsetZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.entity.projectile.EntitySmallFireball(world, d0, d1, d2, d3, d4, d5);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.entity.projectile.EntityWitherSkull(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) net.minecraft.util.MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);

                ((net.minecraft.entity.projectile.EntityFireball) launch).accelerationX = d3 / d6 * 0.1D;
                ((net.minecraft.entity.projectile.EntityFireball) launch).accelerationY = d4 / d6 * 0.1D;
                ((net.minecraft.entity.projectile.EntityFireball) launch).accelerationZ = d5 / d6 * 0.1D;
            } else {
                launch = new net.minecraft.entity.projectile.EntityLargeFireball(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) net.minecraft.util.MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);

                ((net.minecraft.entity.projectile.EntityFireball) launch).accelerationX = d3 / d6 * 0.1D;
                ((net.minecraft.entity.projectile.EntityFireball) launch).accelerationY = d4 / d6 * 0.1D;
                ((net.minecraft.entity.projectile.EntityFireball) launch).accelerationZ = d5 / d6 * 0.1D;
            }
            
            ((net.minecraft.entity.projectile.EntityFireball) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof net.minecraft.entity.IProjectile) {
            if (launch instanceof net.minecraft.entity.projectile.EntityThrowable) {
                ((net.minecraft.entity.projectile.EntityThrowable) launch).projectileSource = this;
            }
            // Values from DispenseBehaviorProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof net.minecraft.entity.projectile.EntityPotion || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseBehavior classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseBehaviorProjectile
            ((net.minecraft.entity.IProjectile) launch).setThrowableHeading((double) enumfacing.getFrontOffsetX(), (double) ((float) enumfacing.getFrontOffsetY() + 0.1F), (double) enumfacing.getFrontOffsetZ(), b, a);
        }

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.spawnEntityInWorld(launch);
        return (T) launch.getBukkitEntity();
    }
}
