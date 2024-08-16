package org.bukkit.plugin.java;

import com.google.common.base.Preconditions;
import com.mohistmc.MohistMC;
import com.mohistmc.util.I18n;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.Warning.WarningState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.mohistmc.org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jar
 */
public final class JavaPluginLoader implements PluginLoader {
    final Server server;
    private final Pattern[] fileFilters = new Pattern[]{Pattern.compile("\\.jar$")};
    private final List<PluginClassLoader> loaders = new CopyOnWriteArrayList<PluginClassLoader>();
    private final LibraryLoader libraryLoader;

    /**
     * This class was not meant to be constructed explicitly
     *
     * @param instance the server instance
     */
    @Deprecated
    public JavaPluginLoader(@NotNull Server instance) {
        Preconditions.checkArgument(instance != null, "Server cannot be null");
        server = instance;


        LibraryLoader libraryLoader = null;
        try {
            libraryLoader = new LibraryLoader();
        } catch (NoClassDefFoundError ex) {
            // Provided depends were not added back
            server.getLogger().warning("Could not initialize LibraryLoader (missing dependencies?)");
        }
        this.libraryLoader = libraryLoader;
    }

    @Override
    @NotNull
    public Plugin loadPlugin(@NotNull final File file) throws InvalidPluginException {
        Preconditions.checkArgument(file != null, "File cannot be null");

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        final PluginDescriptionFile description;
        try {
            description = getPluginDescription(file);
        } catch (InvalidDescriptionException ex) {
            throw new InvalidPluginException(ex);
        }

        final File parentFile = file.getParentFile();
        final File dataFolder = new File(parentFile, description.getName());
        @SuppressWarnings("deprecation")
        final File oldDataFolder = new File(parentFile, description.getRawName());

        // Found old data folder
        if (dataFolder.equals(oldDataFolder)) {
            // They are equal -- nothing needs to be done!
        } else if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
            server.getLogger().warning(String.format(
                MohistMC.i18n.as("mohist.i18n.16",
                description.getFullName(),
                file,
                oldDataFolder,
                dataFolder
            )));
        } else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
            if (!oldDataFolder.renameTo(dataFolder)) {
                throw new InvalidPluginException(MohistMC.i18n.as("mohist.i18n.17", oldDataFolder, dataFolder));
            }
            server.getLogger().log(Level.INFO, String.format(
                MohistMC.i18n.as( "mohist.i18n.18",
                description.getFullName(),
                file,
                oldDataFolder,
                dataFolder
            )));
        }

        if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new InvalidPluginException(String.format(
                MohistMC.i18n.as("mohist.i18n.19",
                dataFolder,
                description.getFullName(),
                file
            )));
        }

        for (final String pluginName : description.getDepend()) {
            Plugin current = server.getPluginManager().getPlugin(pluginName);

            if (current == null) {
                throw new UnknownDependencyException(MohistMC.i18n.as("mohist.i18n.12", pluginName, pluginName));
            }
        }

        server.getUnsafe().checkSupported(description);

        final PluginClassLoader loader;
        try {
            loader = new PluginClassLoader(this, getClass().getClassLoader(), description, dataFolder, file, (libraryLoader != null) ? libraryLoader.createLoader(description) : null);
        } catch (InvalidPluginException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }

        loaders.add(loader);

        return loader.plugin;
    }

    @Override
    @NotNull
    public PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException {
        Preconditions.checkArgument(file != null, "File cannot be null");

        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(entry);

            return new PluginDescriptionFile(stream);

        } catch (IOException ex) {
            throw new InvalidDescriptionException(ex);
        } catch (YAMLException ex) {
            throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    @NotNull
    public Pattern[] getPluginFileFilters() {
        return fileFilters.clone();
    }

    @Nullable
    Class<?> getClassByName(final String name, boolean resolve, PluginDescriptionFile description) {
        for (PluginClassLoader loader : loaders) {
            try {
                return loader.loadClass0(name, resolve, false, ((SimplePluginManager) server.getPluginManager()).isTransitiveDepend(description, loader.plugin.getDescription()));
            } catch (ClassNotFoundException cnfe) {
            }
        }
        return null;
    }

    void setClass(@NotNull final String name, @NotNull final Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.registerClass(serializable);
        }
    }

    private void removeClass(@NotNull Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.unregisterClass(serializable);
        }
    }

    @Override
    @NotNull
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener, @NotNull final Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Plugin can not be null");
        Preconditions.checkArgument(listener != null, "Listener can not be null");

        boolean useTimings = server.getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<Method>(publicMethods.length + privateMethods.length, 1.0f);
            for (Method method : publicMethods) {
                methods.add(method);
            }
            for (Method method : privateMethods) {
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().severe(MohistMC.i18n.as("mohist.i18n.13", plugin.getDescription().getFullName(), listener.getClass(), e.getMessage()));
            return ret;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger().severe(MohistMC.i18n.as("mohist.i18n.14", plugin.getDescription().getFullName(), method.toGenericString(), listener.getClass()));
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.get(eventClass);
            if (eventSet == null) {
                eventSet = new HashSet<RegisteredListener>();
                ret.put(eventClass, eventSet);
            }

            for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    Warning warning = clazz.getAnnotation(Warning.class);
                    WarningState warningState = server.getWarningState();
                    if (!warningState.printFor(warning)) {
                        break;
                    }
                    plugin.getLogger().log(
                            Level.WARNING,
                            String.format(
                                    MohistMC.i18n.as( "mohist.i18n.15",
                                    plugin.getDescription().getFullName(),
                                    clazz.getName(),
                                    method.toGenericString(),
                                    (warning != null && warning.reason().length() != 0) ? warning.reason() : "Server performance will be affected",
                                    Arrays.toString(plugin.getDescription().getAuthors().toArray())),
                            warningState == WarningState.ON ? new AuthorNagException(null) : null));
                    break;
                }
            }

            EventExecutor executor = new EventExecutor() {
                @Override
                public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
                    try {
                        if (!eventClass.isAssignableFrom(event.getClass())) {
                            return;
                        }
                        method.invoke(listener, event);
                    } catch (InvocationTargetException ex) {
                        throw new EventException(ex.getCause());
                    } catch (Throwable t) {
                        throw new EventException(t);
                    }
                }
            };
            if (false) { // Spigot - RL handles useTimings check now
                eventSet.add(new TimedRegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            } else {
                eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            }
        }
        return ret;
    }

    @Override
    public void enablePlugin(@NotNull final Plugin plugin) {
        Preconditions.checkArgument(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (!plugin.isEnabled()) {
            plugin.getLogger().info(I18n.as("minecraftserver.plugin.load.enabling", plugin.getDescription().getFullName()));

            JavaPlugin jPlugin = (JavaPlugin) plugin;

            PluginClassLoader pluginLoader = (PluginClassLoader) jPlugin.getClassLoader();

            if (!loaders.contains(pluginLoader)) {
                loaders.add(pluginLoader);
                server.getLogger().log(Level.WARNING,MohistMC.i18n.as( "mohist.i18n.20", plugin.getDescription().getFullName()));
            }

            try {
                jPlugin.setEnabled(true);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE,MohistMC.i18n.as("mohist.i18n.21", plugin.getDescription().getFullName()), ex);
                // Mohist start - Disable plugins that fail to load
                this.server.getPluginManager().disablePlugin(jPlugin);
                return;
                // Mohist end
            }

            // Perhaps abort here, rather than continue going, but as it stands,
            // an abort is not possible the way it's currently written
            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        Preconditions.checkArgument(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (plugin.isEnabled()) {
            plugin.getLogger().info(MohistMC.i18n.as("minecraftserver.plugin.load.disabling",plugin.getDescription().getFullName()));

            server.getPluginManager().callEvent(new PluginDisableEvent(plugin));

            JavaPlugin jPlugin = (JavaPlugin) plugin;
            ClassLoader cloader = jPlugin.getClassLoader();

            try {
                jPlugin.setEnabled(false);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, MohistMC.i18n.as( "mohist.i18n.22", plugin.getDescription().getFullName()), ex);
            }

            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader) cloader;
                loaders.remove(loader);

                Collection<Class<?>> classes = loader.getClasses();

                for (Class<?> clazz : classes) {
                    removeClass(clazz);
                }

                try {
                    loader.close();
                } catch (IOException ex) {
                    //
                }
            }
        }
    }
}
