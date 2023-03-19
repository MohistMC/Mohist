package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.craftbukkit.v1_18_R2.event.CraftEventFactory;
import org.bukkit.event.block.NotePlayEvent;

/**
 * @author Mgazul by MohistMC
 * @date 2023/3/19 14:51:40
 */
public class NoteBlockEventHook {

    @SubscribeEvent(receiveCanceled = true)
    public void onProjectileHit(NoteBlockEvent.Play event) {
        NotePlayEvent event_CB = CraftEventFactory.callNotePlayEvent((Level) event.getWorld(), event.getPos(), event.getInstrument(), event.getVanillaNoteId());
        if (event_CB.isCancelled()) {
            event.setCanceled(true);
        } else {
            if (event.isCancelable()) {
                event_CB.setCancelled(true);
            }
        }
    }
}
