package cc.uraniummc.util;

import com.google.common.base.Objects;
import net.minecraft.util.MathHelper;

public class Vec3i implements Comparable<Vec3i>
{
    /** The Null vector constant (0, 0, 0) */
    public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);
    /** X coordinate */
    private final int x;
    /** Y coordinate */
    private final int y;
    /** Z coordinate */
    private final int z;

    public Vec3i(int xIn, int yIn, int zIn)
    {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }

    public Vec3i(double xIn, double yIn, double zIn)
    {
        this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof Vec3i))
        {
            return false;
        }
        else
        {
            Vec3i vec3i = (Vec3i)p_equals_1_;
            return this.getX() != vec3i.getX() ? false : (this.getY() != vec3i.getY() ? false : this.getZ() == vec3i.getZ());
        }
    }

    public int hashCode()
    {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(Vec3i p_compareTo_1_)
    {
        return this.getY() == p_compareTo_1_.getY() ? (this.getZ() == p_compareTo_1_.getZ() ? this.getX() - p_compareTo_1_.getX() : this.getZ() - p_compareTo_1_.getZ()) : this.getY() - p_compareTo_1_.getY();
    }

    /**
     * Get the X coordinate
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Get the Y coordinate
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * Get the Z coordinate
     */
    public int getZ()
    {
        return this.z;
    }

    /**
     * Calculate the cross product of this and the given Vector
     */
    public Vec3i crossProduct(Vec3i vec)
    {
        return new Vec3i(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    /**
     * Calculate squared distance to the given coordinates
     */
    public double distanceSq(double toX, double toY, double toZ)
    {
        double d0 = (double)this.getX() - toX;
        double d1 = (double)this.getY() - toY;
        double d2 = (double)this.getZ() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Compute square of distance from point x, y, z to center of this Block
     */
    public double distanceSqToCenter(double xIn, double yIn, double zIn)
    {
        double d0 = (double)this.getX() + 0.5D - xIn;
        double d1 = (double)this.getY() + 0.5D - yIn;
        double d2 = (double)this.getZ() + 0.5D - zIn;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Calculate squared distance to the given Vector
     */
    public double distanceSq(Vec3i to)
    {
        return this.distanceSq((double)to.getX(), (double)to.getY(), (double)to.getZ());
    }

    public String toString()
    {
        return Objects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }
}