package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.spigotmc.CustomTimingsHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventExecutor1 implements EventExecutor {
    private final CustomTimingsHandler timings;
    private final Method method;
    private final Class<? extends Event> eventClass;

    public EventExecutor1(Method method, Class<? extends Event> eventClass, CustomTimingsHandler timings) {
        this.method = method;
        this.eventClass = eventClass;
        this.timings = timings;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            if (!eventClass.isAssignableFrom(event.getClass())) {
                return;
            }
            // Spigot start
            boolean isAsync = event.isAsynchronous();
            if (!isAsync) timings.startTiming();
            method.invoke(listener, event);
            if (!isAsync) timings.stopTiming();
            // Spigot end
        } catch (InvocationTargetException ex) {
            throw new EventException(ex.getCause());
        } catch (Throwable t) {
            throw new EventException(t);
        }
    }
}
