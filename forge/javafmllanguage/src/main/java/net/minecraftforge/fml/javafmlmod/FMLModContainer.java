/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.javafmlmod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import net.minecraftforge.eventbus.EventBusErrorMessage;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FMLModContainer extends ModContainer
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker LOADING = MarkerManager.getMarker("LOADING");
    private final ModFileScanData scanResults;
    private final IEventBus eventBus;
    private Object modInstance;
    private final Class<?> modClass;
    private final FMLJavaModLoadingContext context = new FMLJavaModLoadingContext(this);

    public FMLModContainer(IModInfo info, String className, ModFileScanData modFileScanResults, ModuleLayer gameLayer)
    {
        super(info);
        LOGGER.debug(LOADING,"Creating FMLModContainer instance for {}", className);
        this.scanResults = modFileScanResults;
        activityMap.put(ModLoadingStage.CONSTRUCT, this::constructMod);
        this.eventBus = BusBuilder.builder().setExceptionHandler(this::onEventFailed).setTrackPhases(false).markerType(IModBusEvent.class).build();
        this.configHandler = Optional.of(ce->this.eventBus.post(ce.self()));
        this.contextExtension = () -> context;
        try
        {
            var layer = gameLayer.findModule(info.getOwningFile().moduleName()).orElseThrow();
            modClass = Class.forName(layer, className);
            LOGGER.trace(LOADING,"Loaded modclass {} with {}", modClass.getName(), modClass.getClassLoader());
        }
        catch (Throwable e)
        {
            LOGGER.error(LOADING, "Failed to load class {}", className, e);
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
        }
    }

    private void onEventFailed(IEventBus iEventBus, Event event, IEventListener[] iEventListeners, int i, Throwable throwable)
    {
        LOGGER.error(new EventBusErrorMessage(event, i, iEventListeners, throwable));
    }

    private void constructMod()
    {
        try
        {
            LOGGER.trace(LOADING, "Loading mod instance {} of type {}", getModId(), modClass.getName());
            try {
                // Try noargs constructor first
                Constructor<?> constructor;
                try {
                    constructor = modClass.getDeclaredConstructor(context.getClass());
                } catch (NoSuchMethodException | SecurityException exception) {
                    constructor = modClass.getDeclaredConstructor();
                }
                this.modInstance = constructor.getParameterCount() == 0 ? constructor.newInstance() : constructor.newInstance(context);
            } catch (NoSuchMethodException ignored) {
                // Otherwise look for constructor that can accept more arguments
                Map<Class<?>, Object> allowedConstructorArgs = Map.of(
                        IEventBus.class, eventBus,
                        ModContainer.class, this,
                        FMLModContainer.class, this);

                constructorsLoop: for (var constructor : modClass.getDeclaredConstructors()) {
                    var parameterTypes = constructor.getParameterTypes();
                    Object[] constructorArgs = new Object[parameterTypes.length];
                    Set<Class<?>> foundArgs = new HashSet<>();

                    for (int i = 0; i < parameterTypes.length; i++) {
                        Object argInstance = allowedConstructorArgs.get(parameterTypes[i]);
                        if (argInstance == null) {
                            // Unknown argument, try next constructor method...
                            continue constructorsLoop;
                        }

                        if (foundArgs.contains(parameterTypes[i])) {
                            throw new RuntimeException("Duplicate constructor argument type: " + parameterTypes[i]);
                        }

                        foundArgs.add(parameterTypes[i]);
                        constructorArgs[i] = argInstance;
                    }

                    // All arguments are found
                    this.modInstance = constructor.newInstance(constructorArgs);
                }

                if (this.modInstance == null) {
                    throw new RuntimeException("Could not find mod constructor. Allowed optional argument classes: " +
                            allowedConstructorArgs.keySet().stream().map(Class::getSimpleName).collect(Collectors.joining(", ")));
                }
            }
            LOGGER.trace(LOADING, "Loaded mod instance {} of type {}", getModId(), modClass.getName());
        }
        catch (Throwable e)
        {
            // When a mod constructor throws an exception, it's wrapped in an InvocationTargetException which hides the
            // actual exception from the mod loading error screen.
            if (e instanceof InvocationTargetException wrapped)
                e = Objects.requireNonNullElse(wrapped.getCause(), e); // unwrap the exception
            LOGGER.error(LOADING,"Failed to create mod instance. ModID: {}, class {}", getModId(), modClass.getName(), e);
            throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, modClass);
        }
        try {
            LOGGER.trace(LOADING, "Injecting Automatic event subscribers for {}", getModId());
            AutomaticEventSubscriber.inject(this, this.scanResults, this.modClass.getClassLoader());
            LOGGER.trace(LOADING, "Completed Automatic event subscribers for {}", getModId());
        } catch (Throwable e) {
            LOGGER.error(LOADING,"Failed to register automatic subscribers. ModID: {}, class {}", getModId(), modClass.getName(), e);
            throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, modClass);
        }
    }

    @Override
    public boolean matches(Object mod)
    {
        return mod == modInstance;
    }

    @Override
    public Object getMod()
    {
        return modInstance;
    }

    public IEventBus getEventBus()
    {
        return this.eventBus;
    }

    @Override
    protected <T extends Event & IModBusEvent> void acceptEvent(final T e) {
        try {
            LOGGER.trace(LOADING, "Firing event for modid {} : {}", this.getModId(), e);
            this.eventBus.post(e);
            LOGGER.trace(LOADING, "Fired event for modid {} : {}", this.getModId(), e);
        } catch (Throwable t) {
            LOGGER.error(LOADING,"Caught exception during event {} dispatch for modid {}", e, this.getModId(), t);
            throw new ModLoadingException(modInfo, modLoadingStage, "fml.modloading.errorduringevent", t);
        }
    }
}
