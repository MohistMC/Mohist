package com.mohistmc.optimizations;

import com.mohistmc.optimizations.utils.ChunkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.chunk.ChunkAccess;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/12 5:23:34
 */
public class OptVillager {

    public static OptVillager getInstance() {
        return new OptVillager();
    }

    private boolean isLobotomized = false;
    private int notLobotomizedCount = 0;

    public boolean isLobotomized(Villager villager) {
        return !this.checkLobotomize(villager) || villager.tickCount % 20 == 0;
    }

    private boolean checkLobotomize(Villager villager) {
        // Check half as often if not lobotomized for the last 3+ consecutive checks
        if (villager.tickCount % (this.notLobotomizedCount > 3 ? 600 : 300) == 0) {
            // Offset Y for short blocks like dirt_path/farmland
            this.isLobotomized = villager.isPassenger() || !this.canTravel(BlockPos.containing(villager.getX(), villager.getY() + 0.0625D, villager.getZ()), villager);

            if (this.isLobotomized) {
                this.notLobotomizedCount = 0;
            } else {
                this.notLobotomizedCount++;
            }
        }

        return this.isLobotomized;
    }
    private boolean canTravel(BlockPos center, Villager villager) {
        ChunkAccess chunk = ChunkManager.getChunkNow(villager.level(), center);
        if (chunk == null) {
            return false;
        }

        BlockPos.MutableBlockPos mutable = center.mutable();
        boolean canJump = !this.hasCollisionAt(chunk, mutable.move(Direction.UP, 2));

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.canTravelTo(mutable.setWithOffset(center, direction), canJump, villager)) {
                return true;
            }
        }
        return false;
    }

    private boolean canTravelTo(BlockPos.MutableBlockPos mutable, boolean canJump, Villager villager) {
        ChunkAccess chunk = ChunkManager.getChunkNow(villager.level(), mutable);
        if (chunk == null) {
            return false;
        }

        Block bottom = chunk.getBlockState(mutable).getBlock();
        if (bottom instanceof BedBlock) {
            // Allows iron farms to function normally
            return true;
        }

        if (this.hasCollisionAt(chunk, mutable.move(Direction.UP))) {
            // Early return if the top block has collision.
            return false;
        }

        // The villager can only jump if:
        // - There is no collision above the villager
        // - There is no collision above the top block
        // - The bottom block is short enough to jump on
        boolean isTallBlock = bottom instanceof FenceBlock || bottom instanceof FenceGateBlock || bottom instanceof WallBlock;
        return !bottom.hasCollision || (canJump && !isTallBlock && !this.hasCollisionAt(chunk, mutable.move(Direction.UP)));
    }

    private boolean hasCollisionAt(ChunkAccess chunk, BlockPos pos) {
        return chunk.getBlockState(pos).getBlock().hasCollision;
    }
}
