package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BucketEventDispatcher {

    //For FillBucketEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onFillBucketEvent(FillBucketEvent event) {
        if (event.getResult() == Event.Result.ALLOW) {
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)event.getTarget();
            BlockPos blockpos = blockraytraceresult.getBlockPos();
            Direction direction = blockraytraceresult.getDirection();
            PlayerBucketFillEvent eventCB = CraftEventFactory.callPlayerBucketFillEvent(event.getWorld(), event.getPlayer(), blockpos, blockpos, direction, event.getEmptyBucket(), event.getFilledBucket().getItem());
            eventCB.setCancelled(event.isCanceled());
            eventCB.callEvent();
            event.setCanceled(eventCB.isCancelled());
            event.setFilledBucket(CraftItemStack.asNMSCopy(eventCB.getItemStack()));
        }

    }
}
