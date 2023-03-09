package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R2.event.CraftEventFactory;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Objects;

public class BlockEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onProjectileHit(ProjectileImpactEvent event) {
        HitResult hitResult = event.getRayTraceResult();
        Block block = event.getProjectile().getBlockStateOn().getBlock();
        Level level = event.getProjectile().getLevel();
        Projectile projectile = event.getProjectile();
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (block instanceof AbstractCandleBlock) {
                // CraftBukkit start
                if (CraftEventFactory.callBlockIgniteEvent(level,
                        projectile.getOnPos(), projectile).isCancelled()) {
                    event.setCanceled(true);
                }
                // CraftBukkit end
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        BlockState state = event.getState();
        // CraftBukkit start - fire BlockBreakEvent
        org.bukkit.block.Block bblock = CraftBlock.at(level, pos);
        BlockBreakEvent bukkitEvent = null;
        if (player instanceof final ServerPlayer serverPlayer) {
            if (level instanceof final ServerLevel serverLevel) {
                // Sword + Creative mode pre-cancel
                boolean isSwordNoBreak = !serverPlayer.getMainHandItem().getItem().canAttackBlock(state, serverLevel, pos, serverPlayer);

                // Tell client the block is gone immediately then process events
                // Don't tell the client if its a creative sword break because its not broken!
                if (level.getBlockEntity(pos) == null && !isSwordNoBreak) {
                    ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(pos, Blocks.AIR.defaultBlockState());
                    serverPlayer.connection.send(packet);
                }

                bukkitEvent = new BlockBreakEvent(bblock, serverPlayer.getBukkitEntity());

                // Sword + Creative mode pre-cancel
                bukkitEvent.setCancelled(isSwordNoBreak);
                event.setCanceled(isSwordNoBreak);

                // Calculate default block experience
                BlockState nmsData = serverLevel.getBlockState(pos);
                Block nmsBlock = nmsData.getBlock();

                ItemStack itemstack = serverPlayer.getItemBySlot(EquipmentSlot.MAINHAND);

                if (nmsBlock != null
                        && !bukkitEvent.isCancelled()
                        && !event.isCanceled()
                        && !serverPlayer.isCreative()
                        && serverPlayer.hasCorrectToolForDrops(nmsBlock.defaultBlockState())) {
                    bukkitEvent.setExpToDrop(event.getExpToDrop());
                }

                serverLevel.getCraftServer().getPluginManager().callEvent(bukkitEvent);

                if (bukkitEvent.isCancelled()) {
                    if (isSwordNoBreak) {
                        bukkitEvent.setCancelled(true);
                        event.setCanceled(true);
                    }

                    // Let the client know the block still exists
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(serverLevel, pos));

                    // Brute force all possible updates
                    for (Direction dir : Direction.values()) {
                        serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, pos.relative(dir)));
                    }

                    // Update any tile entity data for this block
                    BlockEntity tileentity = serverLevel.getBlockEntity(pos);
                    if (tileentity != null) {
                        serverPlayer.connection.send(Objects.requireNonNull(tileentity.getUpdatePacket()));
                    }
                    event.setCanceled(true);
                }
            }
            // CraftBukkit end
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onBlockGrow(BlockEvent.CropGrowEvent event) {
        if (event.getLevel() instanceof final ServerLevel serverLevel) {
            event.setCanceled(CraftEventFactory.handleBlockGrowEvent(serverLevel, event.getPos(), event.getState()));
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onRightClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (player instanceof final ServerPlayer serverPlayer) {
            // CraftBukkit start - fire PlayerInteractEvent
            CraftEventFactory.callPlayerInteractEvent(serverPlayer, Action.LEFT_CLICK_BLOCK,
                    event.getPos(), event.getFace(), serverPlayer.getInventory().getSelected(), InteractionHand.MAIN_HAND);
            // Update any tile entity data for this block
            BlockEntity tileentity = event.getLevel().getBlockEntity(event.getPos());
            if (tileentity != null) {
                serverPlayer.connection.send(Objects.requireNonNull(tileentity.getUpdatePacket()));
            }
            // CraftBukkit end

            // CraftBukkit start
            org.bukkit.event.player.PlayerInteractEvent bukkitEvent = CraftEventFactory.callPlayerInteractEvent(serverPlayer,
                    Action.LEFT_CLICK_BLOCK, event.getPos(), event.getFace(),
                    serverPlayer.getInventory().getSelected(), InteractionHand.MAIN_HAND);
            if (bukkitEvent.isCancelled()) {
                // Let the client know the block still exists
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(event.getLevel().getMinecraftWorld(), event.getPos()));
                // Update any tile entity data for this block
                BlockEntity tileentity1 = event.getLevel().getBlockEntity(event.getPos());
                if (tileentity1 != null) {
                    serverPlayer.connection.send(tileentity.getUpdatePacket());
                }
                event.setCancellationResult(InteractionResult.FAIL);
            }
            // CraftBukkit end
        }
    }
}
