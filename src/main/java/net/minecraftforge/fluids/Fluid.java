/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.FMLLog;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Locale;

/**
 * Minecraft Forge Fluid Implementation
 * <p>
 * This class is a fluid (liquid or gas) equivalent to "Item." It describes the nature of a fluid
 * and contains its general properties.
 * <p>
 * These properties do not have inherent gameplay mechanics - they are provided so that mods may
 * choose to take advantage of them.
 * <p>
 * Fluid implementations are not required to actively use these properties, nor are objects
 * interfacing with fluids required to make use of them, but it is encouraged.
 * <p>
 * The default values can be used as a reference point for mods adding fluids such as oil or heavy
 * water.
 */
public class Fluid {
    public static final int BUCKET_VOLUME = 1000;

    /**
     * The unique identification name for this fluid.
     */
    protected final String fluidName;
    protected final ResourceLocation still;
    protected final ResourceLocation flowing;
    @Nullable
    protected final ResourceLocation overlay;
    /**
     * The unlocalized name of this fluid.
     */
    protected String unlocalizedName;
    /**
     * The light level emitted by this fluid.
     * <p>
     * Default value is 0, as most fluids do not actively emit light.
     */
    protected int luminosity = 0;
    /**
     * Density of the fluid - completely arbitrary; negative density indicates that the fluid is
     * lighter than air.
     * <p>
     * Default value is approximately the real-life density of water in kg/m^3.
     */
    protected int density = 1000;
    /**
     * Temperature of the fluid - completely arbitrary; higher temperature indicates that the fluid is
     * hotter than air.
     * <p>
     * Default value is approximately the real-life room temperature of water in degrees Kelvin.
     */
    protected int temperature = 300;
    /**
     * Viscosity ("thickness") of the fluid - completely arbitrary; negative values are not
     * permissible.
     * <p>
     * Default value is approximately the real-life density of water in m/s^2 (x10^-3).
     * <p>
     * Higher viscosity means that a fluid flows more slowly, like molasses.
     * Lower viscosity means that a fluid flows more quickly, like helium.
     */
    protected int viscosity = 1000;
    /**
     * This indicates if the fluid is gaseous.
     * <p>
     * Generally this is associated with negative density fluids.
     */
    protected boolean isGaseous;
    /**
     * The rarity of the fluid.
     * <p>
     * Used primarily in tool tips.
     */
    protected EnumRarity rarity = EnumRarity.COMMON;
    /**
     * If there is a Block implementation of the Fluid, the Block is linked here.
     * <p>
     * The default value of null should remain for any Fluid without a Block implementation.
     */
    protected Block block = null;
    /**
     * Color used by universal bucket and the ModelFluid baked model.
     * Note that this int includes the alpha so converting this to RGB with alpha would be
     * float r = ((color >> 16) & 0xFF) / 255f; // red
     * float g = ((color >> 8) & 0xFF) / 255f; // green
     * float b = ((color >> 0) & 0xFF) / 255f; // blue
     * float a = ((color >> 24) & 0xFF) / 255f; // alpha
     */
    protected int color = 0xFFFFFFFF;
    private SoundEvent fillSound;
    private SoundEvent emptySound;

    public Fluid(String fluidName, ResourceLocation still, ResourceLocation flowing, Color color) {
        this(fluidName, still, flowing, null, color);
    }

    public Fluid(String fluidName, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay, Color color) {
        this(fluidName, still, flowing, overlay);
        this.setColor(color);
    }

    public Fluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int color) {
        this(fluidName, still, flowing, null, color);
    }

    public Fluid(String fluidName, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay, int color) {
        this(fluidName, still, flowing, overlay);
        this.setColor(color);
    }

    public Fluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        this(fluidName, still, flowing, (ResourceLocation) null);
    }

    public Fluid(String fluidName, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay) {
        this.fluidName = fluidName.toLowerCase(Locale.ENGLISH);
        this.unlocalizedName = fluidName;
        this.still = still;
        this.flowing = flowing;
        this.overlay = overlay;
    }

    public final String getName() {
        return this.fluidName;
    }

    public final Block getBlock() {
        return block;
    }

    public Fluid setBlock(Block block) {
        if (this.block == null || this.block == block) {
            this.block = block;
        } else {
            FMLLog.log.warn("A mod has attempted to assign Block {} to the Fluid '{}' but this Fluid has already been linked to the Block {}. "
                    + "You may have duplicate Fluid Blocks as a result. It *may* be possible to configure your mods to avoid this.", block, fluidName, this.block);
        }
        return this;
    }

    public final boolean canBePlacedInWorld() {
        return block != null;
    }

    public final boolean isLighterThanAir() {
        int density = this.density;
        if (block instanceof BlockFluidBase) {
            density = ((BlockFluidBase) block).getDensity();
        }
        return density <= 0;
    }

    /**
     * Determines if this fluid should vaporize in dimensions where water vaporizes when placed.
     * To preserve the intentions of vanilla, fluids that can turn lava into obsidian should vaporize.
     * This prevents players from making the nether safe with a single bucket.
     * Based on {@link net.minecraft.item.ItemBucket#tryPlaceContainedLiquid(EntityPlayer, World, BlockPos)}
     *
     * @param fluidStack The fluidStack is trying to be placed.
     * @return true if this fluid should vaporize in dimensions where water vaporizes when placed.
     */
    public boolean doesVaporize(FluidStack fluidStack) {
        if (block == null)
            return false;
        return block.getDefaultState().getMaterial() == Material.WATER;
    }

    /**
     * Called instead of placing the fluid block if {@link WorldProvider#doesWaterVaporize()} and {@link #doesVaporize(FluidStack)} are true.
     * Override this to make your explosive liquid blow up instead of the default smoke, etc.
     * Based on {@link net.minecraft.item.ItemBucket#tryPlaceContainedLiquid(EntityPlayer, World, BlockPos)}
     *
     * @param player     Player who tried to place the fluid. May be null for blocks like dispensers.
     * @param worldIn    World to vaporize the fluid in.
     * @param pos        The position in the world the fluid block was going to be placed.
     * @param fluidStack The fluidStack that was going to be placed.
     */
    public void vaporize(@Nullable EntityPlayer player, World worldIn, BlockPos pos, FluidStack fluidStack) {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) pos.getX() + Math.random(), (double) pos.getY() + Math.random(), (double) pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * Returns the localized name of this fluid.
     */
    public String getLocalizedName(FluidStack stack) {
        String s = this.getUnlocalizedName();
        return s == null ? "" : I18n.translateToLocal(s);
    }

    /**
     * A FluidStack sensitive version of getUnlocalizedName
     */
    public String getUnlocalizedName(FluidStack stack) {
        return this.getUnlocalizedName();
    }

    /**
     * Returns the unlocalized name of this fluid.
     */
    public String getUnlocalizedName() {
        return "fluid." + this.unlocalizedName;
    }

    public Fluid setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    /* Default Accessors */
    public final int getLuminosity() {
        return this.luminosity;
    }

    public Fluid setLuminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }

    public final int getDensity() {
        return this.density;
    }

    public Fluid setDensity(int density) {
        this.density = density;
        return this;
    }

    public final int getTemperature() {
        return this.temperature;
    }

    public Fluid setTemperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public final int getViscosity() {
        return this.viscosity;
    }

    public Fluid setViscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    public final boolean isGaseous() {
        return this.isGaseous;
    }

    public Fluid setGaseous(boolean isGaseous) {
        this.isGaseous = isGaseous;
        return this;
    }

    public EnumRarity getRarity() {
        return rarity;
    }

    public Fluid setRarity(EnumRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public int getColor() {
        return color;
    }

    public Fluid setColor(Color color) {
        this.color = color.getRGB();
        return this;
    }

    public Fluid setColor(int color) {
        this.color = color;
        return this;
    }

    public ResourceLocation getStill() {
        return still;
    }

    public ResourceLocation getFlowing() {
        return flowing;
    }

    @Nullable
    public ResourceLocation getOverlay() {
        return overlay;
    }

    public SoundEvent getFillSound() {
        if (fillSound == null) {
            if (getBlock() != null && getBlock().getDefaultState().getMaterial() == Material.LAVA) {
                fillSound = SoundEvents.ITEM_BUCKET_FILL_LAVA;
            } else {
                fillSound = SoundEvents.ITEM_BUCKET_FILL;
            }
        }

        return fillSound;
    }

    public Fluid setFillSound(SoundEvent fillSound) {
        this.fillSound = fillSound;
        return this;
    }

    public SoundEvent getEmptySound() {
        if (emptySound == null) {
            if (getBlock() != null && getBlock().getDefaultState().getMaterial() == Material.LAVA) {
                emptySound = SoundEvents.ITEM_BUCKET_EMPTY_LAVA;
            } else {
                emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
            }
        }

        return emptySound;
    }

    public Fluid setEmptySound(SoundEvent emptySound) {
        this.emptySound = emptySound;
        return this;
    }

    /* Stack-based Accessors */
    public int getLuminosity(FluidStack stack) {
        return getLuminosity();
    }

    public int getDensity(FluidStack stack) {
        return getDensity();
    }

    public int getTemperature(FluidStack stack) {
        return getTemperature();
    }

    public int getViscosity(FluidStack stack) {
        return getViscosity();
    }

    public boolean isGaseous(FluidStack stack) {
        return isGaseous();
    }

    public EnumRarity getRarity(FluidStack stack) {
        return getRarity();
    }

    public int getColor(FluidStack stack) {
        return getColor();
    }

    public ResourceLocation getStill(FluidStack stack) {
        return getStill();
    }

    public ResourceLocation getFlowing(FluidStack stack) {
        return getFlowing();
    }

    public SoundEvent getFillSound(FluidStack stack) {
        return getFillSound();
    }

    public SoundEvent getEmptySound(FluidStack stack) {
        return getEmptySound();
    }

    /* World-based Accessors */
    public int getLuminosity(World world, BlockPos pos) {
        return getLuminosity();
    }

    public int getDensity(World world, BlockPos pos) {
        return getDensity();
    }

    public int getTemperature(World world, BlockPos pos) {
        return getTemperature();
    }

    public int getViscosity(World world, BlockPos pos) {
        return getViscosity();
    }

    public boolean isGaseous(World world, BlockPos pos) {
        return isGaseous();
    }

    public EnumRarity getRarity(World world, BlockPos pos) {
        return getRarity();
    }

    public int getColor(World world, BlockPos pos) {
        return getColor();
    }

    public ResourceLocation getStill(World world, BlockPos pos) {
        return getStill();
    }

    public ResourceLocation getFlowing(World world, BlockPos pos) {
        return getFlowing();
    }

    public SoundEvent getFillSound(World world, BlockPos pos) {
        return getFillSound();
    }

    public SoundEvent getEmptySound(World world, BlockPos pos) {
        return getEmptySound();
    }

}
