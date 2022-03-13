package org.bukkit.craftbukkit.v1_18_R2.block;

import net.minecraft.world.level.block.entity.BedBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.block.Bed;

public class CraftBed extends CraftBlockEntityState<BedBlockEntity> implements Bed {

    public CraftBed(World world, BedBlockEntity te) {
        super(world, te);
    }

    @Override
    public DyeColor getColor() {
        switch (getType()) {
            case BLACK_BED:
                return DyeColor.BLACK;
            case BLUE_BED:
                return DyeColor.BLUE;
            case BROWN_BED:
                return DyeColor.BROWN;
            case CYAN_BED:
                return DyeColor.CYAN;
            case GRAY_BED:
                return DyeColor.GRAY;
            case GREEN_BED:
                return DyeColor.GREEN;
            case LIGHT_BLUE_BED:
                return DyeColor.LIGHT_BLUE;
            case LIGHT_GRAY_BED:
                return DyeColor.LIGHT_GRAY;
            case LIME_BED:
                return DyeColor.LIME;
            case MAGENTA_BED:
                return DyeColor.MAGENTA;
            case ORANGE_BED:
                return DyeColor.ORANGE;
            case PINK_BED:
                return DyeColor.PINK;
            case PURPLE_BED:
                return DyeColor.PURPLE;
            case RED_BED:
                return DyeColor.RED;
            case WHITE_BED:
                return DyeColor.WHITE;
            case YELLOW_BED:
                return DyeColor.YELLOW;
            default:
                throw new IllegalArgumentException("Unknown DyeColor for " + getType());
        }
    }

    @Override
    public void setColor(DyeColor color) {
        throw new UnsupportedOperationException("Must set block type to appropriate bed colour");
    }
}
