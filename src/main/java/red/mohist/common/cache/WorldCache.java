package red.mohist.common.cache;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorldCache {

    // From performant and uses GPL-3.0 LICENSE
    public static Cache<AxisAlignedBB, List<AxisAlignedBB>> bbCache = new Cache2kBuilder<AxisAlignedBB, List<AxisAlignedBB>>() {
    }.expireAfterWrite(500, TimeUnit.MILLISECONDS).build();
    public static Cache<AxisAlignedBB, List<Entity>> eeCache = new Cache2kBuilder<AxisAlignedBB, List<Entity>>() {
    }.expireAfterWrite(500, TimeUnit.MILLISECONDS).build();
}
