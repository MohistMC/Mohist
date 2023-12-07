package com.mohistmc.forge;

import com.mohistmc.MohistMC;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.eventbus.ASMEventHandler;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IGenericEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
