package com.mohistmc.test;

import com.mohistmc.api.event.MohistNetworkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MohistNetworkEventTest {

    @SubscribeEvent
    public void test(MohistNetworkEvent event) {
        if (event.getUrl().toString().contains("baidu.com")) {
            event.setCancelled(true);
            event.setMsg("Biu Biu Biu ...");
        }
    }
}
