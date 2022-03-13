package org.bukkit.craftbukkit.v1_18_R2.util;

public final class CraftVector {

    private CraftVector() {
    }

    public static org.bukkit.util.Vector toBukkit(net.minecraft.world.phys.Vec3 nms) {
        return new org.bukkit.util.Vector(nms.x, nms.y, nms.z);
    }

    public static net.minecraft.world.phys.Vec3 toNMS(org.bukkit.util.Vector bukkit) {
        return new net.minecraft.world.phys.Vec3(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
}
