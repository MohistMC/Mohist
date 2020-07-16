package org.bukkit.craftbukkit.entity.memory;

import net.minecraft.util.GlobalPos;
import net.minecraft.util.Timestamp;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

public final class CraftMemoryMapper {

    private CraftMemoryMapper() {}

    public static Object fromNms(Object object) {
        if (object instanceof GlobalPos) {
            return fromNms((GlobalPos) object);
        } else if (object instanceof Timestamp) {
            return ((Timestamp) object).getTime();
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }

    public static Object toNms(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof Location) {
            return toNms((Location) object);
        } else if (object instanceof Long) {
            return Timestamp.of((Long) object);
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }

    public static Location fromNms(GlobalPos globalPos) {
        return new org.bukkit.Location(((CraftServer) Bukkit.getServer()).getServer().getWorld(globalPos.getDimension()).getCraftWorld(), globalPos.getPos().getX(), globalPos.getPos().getY(), globalPos.getPos().getZ());
    }

    public static GlobalPos toNms(Location location) {
        return GlobalPos.create(((CraftWorld) location.getWorld()).getHandle().getDimension().getType(), new BlockPos(location.getX(), location.getY(), location.getZ()));
    }
}
