/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
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

package net.minecraftforge.fml.common;

import net.minecraftforge.fml.common.event.*;

/**
 * The state enum used to help track state progression for the loader
 *
 * @author cpw
 */
public enum LoaderState {
    NOINIT("Uninitialized", null),
    LOADING("Loading", null),
    CONSTRUCTING("Constructing mods", FMLConstructionEvent.class),
    PREINITIALIZATION("Pre-initializing mods", FMLPreInitializationEvent.class),
    INITIALIZATION("Initializing mods", FMLInitializationEvent.class),
    POSTINITIALIZATION("Post-initializing mods", FMLPostInitializationEvent.class),
    AVAILABLE("Mod loading complete", FMLLoadCompleteEvent.class),
    SERVER_ABOUT_TO_START("Server about to start", FMLServerAboutToStartEvent.class),
    SERVER_STARTING("Server starting", FMLServerStartingEvent.class),
    SERVER_STARTED("Server started", FMLServerStartedEvent.class),
    SERVER_STOPPING("Server stopping", FMLServerStoppingEvent.class),
    SERVER_STOPPED("Server stopped", FMLServerStoppedEvent.class),
    ERRORED("Mod Loading errored", null);


    private final Class<? extends FMLStateEvent> eventClass;
    private final String name;

    LoaderState(String name, Class<? extends FMLStateEvent> event) {
        this.name = name;
        this.eventClass = event;
    }

    public LoaderState transition(boolean errored) {
        if (errored) {
            return ERRORED;
        }
        // stopping -> available
        if (this == SERVER_STOPPED) {
            return AVAILABLE;
        }
        return values()[ordinal() < values().length ? ordinal() + 1 : ordinal()];
    }

    public boolean hasEvent() {
        return eventClass != null;
    }

    public FMLStateEvent getEvent(Object... eventData) {
        try {
            return eventClass.getConstructor(Object[].class).newInstance((Object) eventData);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public LoaderState requiredState() {
        if (this == NOINIT) return NOINIT;
        return LoaderState.values()[this.ordinal() - 1];
    }

    public String getPrettyName() {
        return name;
    }

    public enum ModState {
        UNLOADED("Unloaded", "U"),
        LOADED("Loaded", "L"),
        CONSTRUCTED("Constructed", "C"),
        PREINITIALIZED("Pre-initialized", "H"),
        INITIALIZED("Initialized", "I"),
        POSTINITIALIZED("Post-initialized", "J"),
        AVAILABLE("Available", "A"),
        DISABLED("Disabled", "D"),
        ERRORED("Errored", "E");

        private final String label;
        private final String marker;

        ModState(String label, String marker) {
            this.label = label;
            this.marker = marker;
        }

        @Override
        public String toString() {
            return this.label;
        }

        public String getMarker() {
            return this.marker;
        }
    }
}
