package org.bukkit.craftbukkit.util;

public final class CraftVector {

    private CraftVector() {
    }

    public static org.bukkit.util.Vector toBukkit(net.minecraft.util.math.Vec3d nms) {
        return new org.bukkit.util.Vector(nms.x, nms.y, nms.z);
    }

    public static net.minecraft.util.math.Vec3d toNMS(org.bukkit.util.Vector bukkit) {
        return new net.minecraft.util.math.Vec3d(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
}
