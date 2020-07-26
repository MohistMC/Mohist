package red.mohist.forge.event;

import net.minecraftforge.common.MinecraftForge;
import red.mohist.forge.MohistMod;

public abstract class MohistEventDispatcherRegistry {

    public static void registerAllEventDispatchers() {
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new BlockPlaceEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityPotionEffectEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new NetworkEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityTeleportEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new ItemEntityEventDispatcher());
        MohistMod.LOGGER.info("registry all forge-events");
    }

}
