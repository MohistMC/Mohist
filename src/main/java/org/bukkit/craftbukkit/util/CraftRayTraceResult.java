package org.bukkit.craftbukkit.util;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class CraftRayTraceResult {

    private CraftRayTraceResult() {}

    public static RayTraceResult fromNMS(World world, HitResult nmsHitResult) {
        if (nmsHitResult == null || nmsHitResult.getType() == Type.MISS) return null;

        Vec3d nmsHitPos = nmsHitResult.getPos();
        Vector hitPosition = new Vector(nmsHitPos.x, nmsHitPos.y, nmsHitPos.z);
        BlockFace hitBlockFace = null;

        if (nmsHitResult.getType() == Type.ENTITY) {
            Entity hitEntity = ((EntityHitResult) nmsHitResult).getEntity().getBukkitEntity();
            return new RayTraceResult(hitPosition, hitEntity, null);
        }

        Block hitBlock = null;
        BlockPos nmsBlockPos = null;
        if (nmsHitResult.getType() == Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) nmsHitResult;
            hitBlockFace = CraftBlock.notchToBlockFace(blockHitResult.getSide());
            nmsBlockPos = blockHitResult.getBlockPos();
        }
        if (nmsBlockPos != null && world != null) {
            hitBlock = world.getBlockAt(nmsBlockPos.getX(), nmsBlockPos.getY(), nmsBlockPos.getZ());
        }
        return new RayTraceResult(hitPosition, hitBlock, hitBlockFace);
    }
}
