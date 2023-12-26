package com.mohistmc.mohist.forge;

import net.minecraftforge.eventbus.EventBus;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/19 6:31:08
 */
public class MohistEventBus {

    @SuppressWarnings("unchecked")
    public static void register(EventBus bus, Object target) {
        /*
        ConcurrentHashMap<Object, ?> listeners = bus.listeners;
        if (!listeners.containsKey(target)) {
            if (target.getClass() == Class.class) {
                registerClass((Class<?>) target, bus);
            } else {
                registerObject(target, bus);
            }
        }
         */
    }
}
