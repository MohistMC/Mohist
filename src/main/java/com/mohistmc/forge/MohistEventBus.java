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
        ConcurrentHashMap<Object, ?> listeners = bus.listeners;
        if (!listeners.containsKey(target)) {
            if (target.getClass() == Class.class) {
                registerClass((Class<?>) target, bus);
            } else {
                registerObject(target, bus);
            }
        }
    }

    private static void registerClass(final Class<?> clazz, EventBus bus) {
        Arrays.stream(clazz.getMethods()).
                filter(m -> Modifier.isStatic(m.getModifiers())).
                filter(m -> m.isAnnotationPresent(SubscribeEvent.class)).
                forEach(m -> registerListener(clazz, m, m, bus));
    }

    private static void registerObject(final Object obj, EventBus bus) {
        final HashSet<Class<?>> classes = new HashSet<>();
        bus.typesFor(obj.getClass(), classes);
        Arrays.stream(obj.getClass().getMethods()).
                filter(m -> !Modifier.isStatic(m.getModifiers())).
                forEach(m -> classes.stream().
                        map(c -> bus.getDeclMethod(c, m)).
                        filter(rm -> rm.isPresent() && rm.get().isAnnotationPresent(SubscribeEvent.class)).
                        findFirst().
                        ifPresent(rm -> registerListener(obj, m, rm.get(), bus)));
    }

    private static void registerListener(final Object target, final Method method, final Method real, EventBus bus) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException(
                    "Method " + method + " has @SubscribeEvent annotation. " +
                            "It has " + parameterTypes.length + " arguments, " +
                            "but event handler methods require a single argument only."
            );
        }

        Class<?> eventType = parameterTypes[0];

        if (!Event.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(
                    "Method " + method + " has @SubscribeEvent annotation, " +
                            "but takes an argument that is not an Event subtype : " + eventType);
        }

        register(eventType, target, real, bus);
    }

    private static void register(Class<?> eventType, Object target, Method method, EventBus bus) {
        try {
            ASMEventHandler asm = new ASMEventHandler(new PluginClassLoaderFactory(), target, method, IGenericEvent.class.isAssignableFrom(eventType));
            bus.addToListeners(target, eventType, asm, asm.getPriority());
        } catch (Throwable e) {
            MohistMC.LOGGER.error("Error registering event handler: {} {}", eventType, method, e);
        }
    }
}
