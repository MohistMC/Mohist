package me.jellysquid.mods.lithium.common.shapes;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.AxisRotation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapePart;

import java.util.List;

/**
 * An efficient implementation of {@link VoxelShape} for a shape with one simple cuboid.
 */
public class VoxelShapeSimpleCube extends VoxelShape {
    private static final double EPSILON = 1.0E-7D;

    private final double x1, y1, z1, x2, y2, z2;

    public VoxelShapeSimpleCube(VoxelShapePart voxels, double x1, double y1, double z1, double x2, double y2, double z2) {
        super(voxels);

        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    @Override
    public VoxelShape withOffset(double x, double y, double z) {
        return new VoxelShapeSimpleCube(this.part, this.x1 + x, this.y1 + y, this.z1 + z, this.x2 + x, this.y2 + y, this.z2 + z);
    }

    @Override
    public double getAllowedOffset(AxisRotation cycleDirection, AxisAlignedBB box, double maxDist) {
        if (Math.abs(maxDist) < EPSILON) {
            return 0.0D;
        }

        double penetration = this.calculatePenetration(cycleDirection, box, maxDist);

        if (penetration != maxDist && this.intersects(cycleDirection, box)) {
            return penetration;
        }

        return maxDist;
    }

    private double calculatePenetration(AxisRotation dir, AxisAlignedBB box, double maxDist) {
        switch (dir) {
            case NONE:
                return this.calculatePenetration(this.x1, this.x2, box.minX, box.maxX, maxDist);
            case FORWARD:
                return this.calculatePenetration(this.z1, this.z2, box.minZ, box.maxZ, maxDist);
            case BACKWARD:
                return this.calculatePenetration(this.y1, this.y2, box.minY, box.maxY, maxDist);
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean intersects(AxisRotation dir, AxisAlignedBB box) {
        switch (dir) {
            case NONE:
                return lessThan(this.y1, box.maxY) && lessThan(box.minY, this.y2) && lessThan(this.z1, box.maxZ) && lessThan(box.minZ, this.z2);
            case FORWARD:
                return lessThan(this.x1, box.maxX) && lessThan(box.minX, this.x2) && lessThan(this.y1, box.maxY) && lessThan(box.minY, this.y2);
            case BACKWARD:
                return lessThan(this.z1, box.maxZ) && lessThan(box.minZ, this.z2) && lessThan(this.x1, box.maxX) && lessThan(box.minX, this.x2);
            default:
                throw new IllegalArgumentException();
        }
    }

    private double calculatePenetration(double a1, double a2, double b1, double b2, double maxDist) {
        double penetration;

        if (maxDist > 0.0D) {
            penetration = a1 - b2;

            if (penetration < -EPSILON || maxDist < penetration) {
                return maxDist;
            }

            if (penetration < EPSILON) {
                return 0.0D;
            }
        } else {
            penetration = a2 - b1;

            if (penetration > EPSILON || maxDist > penetration) {
                return maxDist;
            }

            if (penetration > -EPSILON) {
                return 0.0D;
            }
        }

        return penetration;
    }

    @Override
    public List<AxisAlignedBB> toBoundingBoxList() {
        return Lists.newArrayList(this.getBoundingBox());
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    @Override
    public double getStart(Direction.Axis axis) {
        return axis.getCoordinate(this.x1, this.y1, this.z1);
    }

    @Override
    public double getEnd(Direction.Axis axis) {
        return axis.getCoordinate(this.x2, this.y2, this.z2);
    }

    @Override
    protected double getValueUnchecked(Direction.Axis axis, int index) {
        if (index < 0 || index > 1) {
            throw new ArrayIndexOutOfBoundsException();
        }

        switch (axis) {
            case X:
                return index == 0 ? this.x1 : this.x2;
            case Y:
                return index == 0 ? this.y1 : this.y2;
            case Z:
                return index == 0 ? this.z1 : this.z2;
        }

        throw new IllegalArgumentException();
    }

    @Override
    protected DoubleList getValues(Direction.Axis axis) {
        switch (axis) {
            case X:
                return DoubleArrayList.wrap(new double[]{this.x1, this.x2});
            case Y:
                return DoubleArrayList.wrap(new double[]{this.y1, this.y2});
            case Z:
                return DoubleArrayList.wrap(new double[]{this.z1, this.z2});
        }

        throw new IllegalArgumentException();
    }

    @Override
    protected boolean contains(double x, double y, double z) {
        return x >= this.x1 && x < this.x2 && y >= this.y1 && y < this.y2 && z >= this.z1 && z < this.z2;
    }

    @Override
    public boolean isEmpty() {
        return this.x1 + EPSILON > this.x2 || this.y1 + EPSILON > this.y2 || this.z1 + EPSILON > this.z2;
    }

    @Override
    protected int getClosestIndex(Direction.Axis axis, double coord) {
        if (coord < this.getStart(axis)) {
            return -1;
        }

        if (coord >= this.getEnd(axis)) {
            return 1;
        }

        return 0;
    }

    private static boolean lessThan(double a, double b) {
        return a + EPSILON < b;
    }
}
