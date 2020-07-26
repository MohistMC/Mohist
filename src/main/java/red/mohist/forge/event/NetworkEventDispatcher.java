package red.mohist.forge.event;

import net.minecraft.tags.NetworkTagCollection;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NetworkEventDispatcher {

    @SubscribeEvent
    public void onTagUpdate(TagsUpdatedEvent event) {
        ((NetworkTagCollection) event.getTagManager().getBlocks()).increaseTag();
        ((NetworkTagCollection) event.getTagManager().getEntityTypes()).increaseTag();
        ((NetworkTagCollection) event.getTagManager().getFluids()).increaseTag();
        ((NetworkTagCollection) event.getTagManager().getItems()).increaseTag();
    }
}
