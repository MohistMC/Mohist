package red.mohist.forge.event;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.craftbukkit.v1_15_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public class EntityPotionEffectEventDispatcher {

    // todo 再检查一遍
    @SubscribeEvent(receiveCanceled = true)
    public void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
        if (event.getPotionEffect() == null) {
            return;
        }
        EntityPotionEffectEvent.Cause cause = ((LivingEntity) event.getEntityLiving()).getEffectCause().orElse(EntityPotionEffectEvent.Cause.UNKNOWN);
        EntityPotionEffectEvent.Action action = ((LivingEntity) event.getEntityLiving()).getAndResetAction();
        EntityPotionEffectEvent bukkitEvent = CraftEventFactory.callEntityPotionEffectChangeEvent(event.getEntityLiving(), event.getPotionEffect(), null, cause, action);
        event.setCanceled(bukkitEvent.isCancelled());
    }
}
