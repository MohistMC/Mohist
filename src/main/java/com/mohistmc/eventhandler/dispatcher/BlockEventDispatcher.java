package com.mohistmc.eventhandler.dispatcher;

import com.mohistmc.bukkit.block.MohistBlockSnapshot;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        LevelAccessor level = event.getWorld();
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        BlockState state = event.getState();
        // CraftBukkit start - fire BlockBreakEvent
        org.bukkit.block.Block bblock = CraftBlock.at(level, pos);
        BlockBreakEvent bukkitEvent;
        if (player instanceof ServerPlayer serverPlayer && !(player instanceof FakePlayer)) {
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
                        serverPlayer.connection.send(tileentity.getUpdatePacket());
                    }
                    event.setCanceled(true);
                }
            }
            // CraftBukkit end
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer && !(entity instanceof FakePlayer)) {
            org.bukkit.entity.Player player = serverPlayer.getBukkitEntity();
            Direction direction = event.getPlaceEventDirection();
            if (direction != null) {
                InteractionHand hand = event.getPlaceEventHand();
                CraftBlock placedBlock = MohistBlockSnapshot.fromBlockSnapshot(event.getBlockSnapshot(), true);
                CraftBlock againstBlock = CraftBlock.at(event.getWorld(), event.getPos().relative(direction.getOpposite()));
                org.bukkit.inventory.ItemStack bukkitStack;
                org.bukkit.inventory.EquipmentSlot bukkitHand;
                if (hand == InteractionHand.MAIN_HAND) {
                    bukkitStack = player.getInventory().getItemInMainHand();
                    bukkitHand = org.bukkit.inventory.EquipmentSlot.HAND;
                } else {
                    bukkitStack = player.getInventory().getItemInOffHand();
                    bukkitHand = org.bukkit.inventory.EquipmentSlot.OFF_HAND;
                }
                BlockPlaceEvent placeEvent = new BlockPlaceEvent(placedBlock, placedBlock.getState(), againstBlock, bukkitStack, player, !event.isCanceled(), bukkitHand);
                placeEvent.setCancelled(event.isCanceled());
                Bukkit.getPluginManager().callEvent(placeEvent);
                event.setCanceled(placeEvent.isCancelled() || !placeEvent.canBuild());
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onMultiPlace(BlockEvent.EntityMultiPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer && !(entity instanceof FakePlayer)) {
            org.bukkit.entity.Player player = serverPlayer.getBukkitEntity();
            Direction direction = event.getPlaceEventDirection();
            if (direction != null) {
                InteractionHand hand = event.getPlaceEventHand();
                List<org.bukkit.block.BlockState> placedBlocks = new ArrayList<>(event.getReplacedBlockSnapshots().size());
                for (BlockSnapshot snapshot : event.getReplacedBlockSnapshots()) {
                    placedBlocks.add(MohistBlockSnapshot.fromBlockSnapshot(snapshot, true).getState());
                }
                CraftBlock againstBlock = CraftBlock.at(event.getWorld(), event.getPos().relative(direction.getOpposite()));
                org.bukkit.inventory.ItemStack bukkitStack;
                if (hand == InteractionHand.MAIN_HAND) {
                    bukkitStack = player.getInventory().getItemInMainHand();
                } else {
                    bukkitStack = player.getInventory().getItemInOffHand();
                }
                BlockPlaceEvent placeEvent = new BlockMultiPlaceEvent(placedBlocks, againstBlock, bukkitStack, player, !event.isCanceled()
                );
                placeEvent.setCancelled(event.isCanceled());
                Bukkit.getPluginManager().callEvent(placeEvent);
                event.setCanceled(placeEvent.isCancelled() || !placeEvent.canBuild());
            }
        }
    }
}
