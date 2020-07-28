package red.mohist.extra.player;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import red.mohist.extra.entity.ExtraEntity;

public interface ExtraServerEntityPlayer extends ExtraEntity {

    public void reset();

    public BlockPos getSpawnPoint(World world);

}
