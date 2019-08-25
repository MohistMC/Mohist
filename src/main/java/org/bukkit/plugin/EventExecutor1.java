package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventExecutor1 implements EventExecutor {
    private Method method;
    private Class<? extends Event> eventClass;

    public EventExecutor1(Method method, Class<? extends Event> eventClass) {
        this.method = method;
        this.eventClass = eventClass;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            if (!this.eventClass.isAssignableFrom(event.getClass())) {
                return;
            }
            this.method.invoke(listener, event);
        } catch (InvocationTargetException ex) {
            throw new EventException(ex.getCause());
        } catch (Throwable t) {
            throw new EventException(t);
        }
    }
}
