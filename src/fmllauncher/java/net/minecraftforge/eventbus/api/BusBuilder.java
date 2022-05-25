/*
 * Minecraft Forge
 * Copyright (c) 2016-2022.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

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
        // Mohist start - Replace and use CustomEventBus here
        if (debug) System.out.println("CustomEventBusBuilder");
        try {
            Class c = Class.forName("com.mohistmc.forge.CustomEventBus");
            Constructor constructor = c.getDeclaredConstructor(BusBuilder.class);
            return (EventBus) constructor.newInstance((BusBuilder)this);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        // Mohist end
    }

    public boolean isStartingShutdown() {
        return this.startShutdown;
    }

    public Class<?> getMarkerType() {
        return this.markerType;
    }
}
