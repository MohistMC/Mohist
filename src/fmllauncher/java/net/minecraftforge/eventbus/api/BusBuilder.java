package net.minecraftforge.eventbus.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.minecraftforge.eventbus.EventBus;

/**
 * Build a bus
 */
public final class BusBuilder {
    private IEventExceptionHandler exceptionHandler;

    // true by default
    private boolean trackPhases = true;
    private boolean startShutdown = false;
    private Class<?> markerType = Event.class;

    public static BusBuilder builder() {
        return new BusBuilder();
    }

    public BusBuilder setTrackPhases(boolean trackPhases) {
        this.trackPhases = trackPhases;
        return this;
    }

    public BusBuilder setExceptionHandler(IEventExceptionHandler handler) {
        this.exceptionHandler =  handler;
        return this;
    }

    public BusBuilder startShutdown() {
        this.startShutdown = true;
        return this;
    }

    public BusBuilder markerType(Class<?> type) {
        if (!type.isInterface()) throw new IllegalArgumentException("Cannot specify a class marker type");
        this.markerType = type;
        return this;
    }

    public IEventExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public boolean getTrackPhases() {
        return trackPhases;
    }

    boolean debug = false;

    public IEventBus build() {
        if (debug) System.out.println("CustomEventBusBuilder");
        try {
            Class c = Class.forName("com.mohistmc.forge.CustomEventBus");
            Constructor constructor = c.getDeclaredConstructor(BusBuilder.class);
            return (EventBus) constructor.newInstance((BusBuilder)this);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStartingShutdown() {
        return this.startShutdown;
    }

    public Class<?> getMarkerType() {
        return this.markerType;
    }
}
