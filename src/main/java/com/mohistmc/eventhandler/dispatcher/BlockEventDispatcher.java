package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockEventDispatcher {

    public static boolean isDropItems;

    //For BlockBreakEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onBreakBlockEvent(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isRemote()) {
            CraftBlock craftBlock = CraftBlock.at(event.getWorld(), event.getPos());
            BlockBreakEvent breakEvent = new BlockBreakEvent(craftBlock, ((ServerPlayerEntity) event.getPlayer()).getBukkitEntity());
            breakEvent.setCancelled(event.isCanceled());
            breakEvent.setExpToDrop(event.getExpToDrop());
            Bukkit.getPluginManager().callEvent(breakEvent);
            event.setCanceled(breakEvent.isCancelled());
            event.setExpToDrop(breakEvent.getExpToDrop());
            isDropItems = breakEvent.isDropItems();
        }
    }

    //For PlayerInteractEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onTrampleFarmlandEvent(BlockEvent.FarmlandTrampleEvent event){
        // CraftBukkit start - Interact soil
        org.bukkit.event.Cancellable cancellable = null;
        if (event.getEntity() instanceof PlayerEntity) {
            cancellable = CraftEventFactory.callPlayerInteractEvent((PlayerEntity) event.getEntity(), org.bukkit.event.block.Action.PHYSICAL, event.getPos(), null, null, null);
        } else {
            cancellable = new EntityInteractEvent(event.getEntity().getBukkitEntity(), event.getWorld().getMinecraftWorld().getCBWorld().getBlockAt(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()));
            event.getWorld().getMinecraftWorld().getCBServer().getPluginManager().callEvent((EntityInteractEvent) cancellable);
            if (cancellable != null && cancellable.isCancelled()) {
                return;
            }
            if (CraftEventFactory.callEntityChangeBlockEvent(event.getEntity(), event.getPos(), Blocks.DIRT.getDefaultState()).isCancelled()) {
                return;
            }
            // CraftBukkit end
        }
    }
}
