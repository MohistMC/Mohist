package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Event;
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
import org.bukkit.plugin.UnknownDependencyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spigotmc.CustomTimingsHandler;
import org.yaml.snakeyaml.error.YAMLException;
import red.mohist.api.Unsafe;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jar
 */
public final class JavaPluginLoader implements PluginLoader {
    final Server server;
    private final Pattern[] fileFilters = new Pattern[]{Pattern.compile("\\.jar$")};
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private final List<PluginClassLoader> loaders = new CopyOnWriteArrayList<PluginClassLoader>();
    public static final CustomTimingsHandler pluginParentTimer = new CustomTimingsHandler("** Plugins"); // Spigot
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static final Cache<Method, Class<? extends EventExecutor>> EXECUTOR_CACHE = CacheBuilder.newBuilder().build();

    /**
     * This class was not meant to be constructed explicitly
     *
     * @param instance the server instance
     */
    @Deprecated
    public JavaPluginLoader(@NotNull Server instance) {
        Validate.notNull(instance, "Server cannot be null");
        server = instance;
    }

    @Override
    @NotNull
    public Plugin loadPlugin(@NotNull File file) throws InvalidPluginException {
        Validate.notNull(file, "File cannot be null");

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        PluginDescriptionFile description;
        try {
            description = getPluginDescription(file);
        } catch (InvalidDescriptionException ex) {
            throw new InvalidPluginException(ex);
        }

        File dataFolder = new File(file.getParentFile(), description.getName());
        File oldDataFolder = getDataFolder(file);

        // Found old data folder
        if (dataFolder.equals(oldDataFolder)) {
            // They are equal -- nothing needs to be done!
        } else if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
            server.getLogger().log(Level.INFO, String.format(
                    "While loading %s (%s) found old-data folder: %s next to the new one: %s",
                    description.getName(),
                description.getFullName(),
                file,
                oldDataFolder,
                dataFolder
            ));
        } else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
            if (!oldDataFolder.renameTo(dataFolder)) {
                throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "' to: `" + dataFolder + "'");
            }
            server.getLogger().log(Level.INFO, String.format(
                    "While loading %s (%s) renamed data folder: '%s' to '%s'",
                    description.getName(),
                description.getFullName(),
                file,
                oldDataFolder,
                dataFolder
            ));
        }

        if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new InvalidPluginException(String.format(
                    "Projected datafolder: '%s' for %s (%s) exists and is not a directory",
                dataFolder,
                description.getName(),
                file
            ));
        }

            List<String> depend = description.getDepend();
            if (depend == null) {
                depend = ImmutableList.<String>of();
            }

            for (String pluginName : depend) {
            Plugin current = server.getPluginManager().getPlugin(pluginName);

            if (current == null) {
                throw new UnknownDependencyException(pluginName);
            }
        }

        server.getUnsafe().checkSupported(description);

        PluginClassLoader loader;
        try {
            loader = new PluginClassLoader(this, getClass().getClassLoader(), description, dataFolder, file);
        } catch (InvalidPluginException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }

        loaders.add(loader);

        return loader.plugin;
    }

    private File getDataFolder(File file) {
        File dataFolder = null;

        String filename = file.getName();
        int index = file.getName().lastIndexOf(".");

        if (index != -1) {
            String name = filename.substring(0, index);

            dataFolder = new File(file.getParentFile(), name);
        } else {
            // This is if there is no extension, which should not happen
            // Using _ to prevent name collision

            dataFolder = new File(file.getParentFile(), filename + "_");
        }
        return dataFolder;
    }
    @Override
    @NotNull
    public PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException {
        Validate.notNull(file, "File cannot be null");

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
    Class<?> getClassByName(final String name) {
        Class<?> cachedClass = classes.get(name);

        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (PluginClassLoader loader : loaders) {
                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException cnfe) {}
                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

    void setClass(@NotNull final String name, @NotNull final Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);

            if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            }
        }
    }

    private void removeClass(@NotNull String name) {
        Class<?> clazz = classes.remove(name);

        try {
            if ((clazz != null) && (ConfigurationSerializable.class.isAssignableFrom(clazz))) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.unregisterClass(serializable);
            }
        } catch (NullPointerException ex) {
            // Boggle!
            // (Native methods throwing NPEs is not fun when you can't stop it before-hand)
        }
    }

    @Override
    @NotNull
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener, @NotNull Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");

        boolean useTimings = server.getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            for (Method method : publicMethods) {
                methods.add(method);
            }
            for (Method method : privateMethods) {
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) {
                continue;
            }
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.get(eventClass);
            if (eventSet == null) {
                eventSet = new HashSet<>();
                ret.put(eventClass, eventSet);
            }

            for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    Warning warning = clazz.getAnnotation(Warning.class);
                    Warning.WarningState warningState = server.getWarningState();
                    if (!warningState.printFor(warning)) {
                        break;
                    }
                    plugin.getLogger().log(
                            Level.WARNING,
                            String.format(
                                    "\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated. \"%s\"; please notify the authors %s.",
                                    plugin.getDescription().getFullName(),
                                    clazz.getName(),
                                    method.toGenericString(),
                                    (warning != null && warning.reason().length() != 0) ? warning.reason() : "Server performance will be affected",
                                    Arrays.toString(plugin.getDescription().getAuthors().toArray())),
                            warningState == Warning.WarningState.ON ? new AuthorNagException(null) : null);
                    break;
                }
            }

            // final CustomTimingsHandler timings = new CustomTimingsHandler("Plugin: " + plugin.getDescription().getFullName() + " Event: " + listener.getClass().getName() + "::" + method.getName() + "(" + eventClass.getSimpleName() + ")", pluginParentTimer); // Spigot

            try {
                Class<? extends EventExecutor> executorClass = createExecutor(method, eventClass);
                Constructor<? extends EventExecutor> constructor = executorClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                EventExecutor executor = constructor.newInstance();
                eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends EventExecutor> createExecutor(Method method, Class<? extends Event> eventClass) throws ExecutionException {
        return EXECUTOR_CACHE.get(method, () -> {
            ClassWriter cv = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cv.visit(Opcodes.V1_8,
                    Opcodes.ACC_SUPER + Opcodes.ACC_SYNTHETIC + Opcodes.ACC_FINAL,
                    Type.getInternalName(method.getDeclaringClass()) + "$$arclight$" + COUNTER.getAndIncrement(),
                    null,
                    Type.getInternalName(Object.class),
                    new String[]{Type.getInternalName(EventExecutor.class)}
            );
            cv.visitOuterClass(Type.getInternalName(method.getDeclaringClass()), null, null);
            createConstructor(cv);
            createImpl(method, eventClass, cv);
            cv.visitEnd();
            return (Class<? extends EventExecutor>) Unsafe.defineAnonymousClass(method.getDeclaringClass(), cv.toByteArray(), null);
        });
    }

    private void createImpl(Method method, Class<? extends Event> eventClass, ClassVisitor cv) {
        String ownerType = Type.getInternalName(method.getDeclaringClass());
        MethodVisitor mv = cv.visitMethod(
                Opcodes.ACC_PUBLIC,
                "execute",
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Listener.class), Type.getType(Event.class)),
                null, null
        );
        mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);

        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        mv.visitTryCatchBlock(label0, label1, label2, "java/lang/Throwable");
        Label label3 = new Label();
        Label label4 = new Label();
        // try {
        mv.visitTryCatchBlock(label3, label4, label2, "java/lang/Throwable");
        //   if (!(event instanceof TYPE))
        mv.visitLabel(label0);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitTypeInsn(Opcodes.INSTANCEOF, Type.getInternalName(eventClass));
        mv.visitJumpInsn(Opcodes.IFNE, label3);
        //      return;
        mv.visitLabel(label1);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitLabel(label3);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        //   ((TYPE) listener).<method>(event);
        //   TYPE.<method>(event);
        int invokeCode;
        if (Modifier.isStatic(method.getModifiers())) {
            invokeCode = Opcodes.INVOKESTATIC;
        } else if (method.getDeclaringClass().isInterface()) {
            invokeCode = Opcodes.INVOKEINTERFACE;
        } else if (Modifier.isPrivate(method.getModifiers())) {
            invokeCode = Opcodes.INVOKESPECIAL;
        } else {
            invokeCode = Opcodes.INVOKEVIRTUAL;
        }
        if (invokeCode != Opcodes.INVOKESTATIC) {
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitTypeInsn(Opcodes.CHECKCAST, ownerType);
        }
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getInternalName(eventClass));
        mv.visitMethodInsn(invokeCode, ownerType, method.getName(), Type.getMethodDescriptor(method), invokeCode == Opcodes.INVOKEINTERFACE);
        int retSize = Type.getType(method.getReturnType()).getSize();
        if (retSize > 0) {
            mv.visitInsn(Opcodes.POP + retSize - 1);
        }
        mv.visitLabel(label4);
        // } catch (Throwable t) {
        Label label5 = new Label();
        mv.visitJumpInsn(Opcodes.GOTO, label5);
        mv.visitLabel(label2);
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/Throwable"});
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        // throw new EventException(t);
        Label label6 = new Label();
        mv.visitLabel(label6);
        mv.visitTypeInsn(Opcodes.NEW, "org/bukkit/event/EventException");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/bukkit/event/EventException", "<init>", "(Ljava/lang/Throwable;)V", false);
        mv.visitInsn(Opcodes.ATHROW);
        mv.visitLabel(label5);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitInsn(Opcodes.RETURN);
        // }
        Label label7 = new Label();
        mv.visitLabel(label7);
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    private void createConstructor(ClassVisitor cv) {
        MethodVisitor mv = cv.visitMethod(
                Opcodes.ACC_PRIVATE,
                "<init>",
                "()V",
                null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    @Override
    public void enablePlugin(@NotNull final Plugin plugin) {
        Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (!plugin.isEnabled()) {
            plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());

            JavaPlugin jPlugin = (JavaPlugin) plugin;

            PluginClassLoader pluginLoader = (PluginClassLoader) jPlugin.getClassLoader();

            if (!loaders.contains(pluginLoader)) {
                loaders.add(pluginLoader);
                server.getLogger().log(Level.WARNING, "Enabled plugin with unregistered PluginClassLoader " + plugin.getDescription().getFullName());
            }

            try {
                jPlugin.setEnabled(true);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
            }

            // Perhaps abort here, rather than continue going, but as it stands,
            // an abort is not possible the way it's currently written
            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (plugin.isEnabled()) {
            String message = String.format("Disabling %s", plugin.getDescription().getFullName());
            plugin.getLogger().info(message);

            server.getPluginManager().callEvent(new PluginDisableEvent(plugin));

            JavaPlugin jPlugin = (JavaPlugin) plugin;
            ClassLoader cloader = jPlugin.getClassLoader();

            try {
                jPlugin.setEnabled(false);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
            }

            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader) cloader;
                loaders.remove(loader);

                Set<String> names = loader.getClasses();

                for (String name : names) {
                    removeClass(name);
                }
            }
        }
    }
}
