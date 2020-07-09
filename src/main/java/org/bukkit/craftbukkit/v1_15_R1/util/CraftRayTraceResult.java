package org.bukkit.craftbukkit.v1_15_R1.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CraftRayTraceResult {

    private CraftRayTraceResult() {}

    public static RayTraceResult fromNMS(World world, net.minecraft.util.math.RayTraceResult nmsHitResult) {
        if (nmsHitResult == null || nmsHitResult.getType() == net.minecraft.util.math.RayTraceResult.Type.MISS) return null;

        Vec3d nmsHitPos = nmsHitResult.getHitVec();
        Vector hitPosition = new Vector(nmsHitPos.x, nmsHitPos.y, nmsHitPos.z);
        BlockFace hitBlockFace = null;

        if (nmsHitResult.getType() == net.minecraft.util.math.RayTraceResult.Type.ENTITY) {
            Entity hitEntity = ((EntityRayTraceResult) nmsHitResult).getEntity().getBukkitEntity();
            return new RayTraceResult(hitPosition, hitEntity, null);
        }

        Block hitBlock = null;
        BlockPos nmsBlockPos = null;
        if (nmsHitResult.getType() == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockHitResult = (BlockRayTraceResult) nmsHitResult;
            hitBlockFace = CraftBlock.notchToBlockFace(blockHitResult.getFace());
            nmsBlockPos = blockHitResult.getPos();
        }
        if (nmsBlockPos != null && world != null) {
            hitBlock = world.getBlockAt(nmsBlockPos.getX(), nmsBlockPos.getY(), nmsBlockPos.getZ());
        }
        return new RayTraceResult(hitPosition, hitBlock, hitBlockFace);
    }
}
