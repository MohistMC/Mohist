package org.bukkit.plugin.java;

import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final ConcurrentMap<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>(); // Cauldron - Threadsafe classloading
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    JavaPlugin plugin; // Cauldron - remove final
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    private final Set<String> seenIllegalAccess = Collections.newSetFromMap(new ConcurrentHashMap<>());

    static {
        ClassLoader.registerAsParallelCapable();
    }

    PluginClassLoader(@NotNull final JavaPluginLoader loader, @Nullable final ClassLoader parent, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file) throws IOException, InvalidPluginException, MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    @Override
    public URL getResource(String name) {
        return findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return findResources(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(@NotNull String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);

                if (result != null) {
                    PluginDescriptionFile provider = ((PluginClassLoader) result.getClassLoader()).description;

                    if (provider != description
                            && !seenIllegalAccess.contains(provider.getName())
                            && !((SimplePluginManager) loader.server.getPluginManager()).isTransitiveDepend(description, provider)) {

                        seenIllegalAccess.add(provider.getName());
                        if (plugin != null) {
                            plugin.getLogger().log(Level.WARNING, "Loaded class {0} from {1} which is not a depend, softdepend or loadbefore of this plugin.", new Object[]{name, provider.getFullName()});
                        } else {
                            // In case the bad access occurs on construction
                            loader.server.getLogger().log(Level.WARNING, "[{0}] Loaded class {1} from {2} which is not a depend, softdepend or loadbefore of this plugin.", new Object[]{description.getName(), name, provider.getFullName()});
                        }
                    }
                }
            }

            if (result == null) {
                String path = name.replace('.', '/').concat(".class");
                JarEntry entry = jar.getJarEntry(path);

                if (entry != null) {
                    byte[] classBytes;

                    try (InputStream is = jar.getInputStream(entry)) {
                        classBytes = ByteStreams.toByteArray(is);
                    } catch (IOException ex) {
                        throw new ClassNotFoundException(name, ex);
                    }

                    classBytes = loader.server.getUnsafe().processClass(description, path, classBytes);

                    int dot = name.lastIndexOf('.');
                    if (dot != -1) {
                        String pkgName = name.substring(0, dot);
                        if (getPackage(pkgName) == null) {
                            try {
                                if (manifest != null) {
                                    definePackage(pkgName, manifest, url);
                                } else {
                                    definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (IllegalArgumentException ex) {
                                if (getPackage(pkgName) == null) {
                                    throw new IllegalStateException("Cannot find package " + pkgName);
                                }
                            }
                        }
                    }

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(url, signers);

                    result = defineClass(name, classBytes, 0, classBytes.length, source);
                }

                if (result == null) {
                    result = super.findClass(name);
                }

                if (result != null) {
                    loader.setClass(name, result);
                }

                classes.put(name, result);
            }
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            jar.close();
        }
    }

    @NotNull
    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(@NotNull JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }
}
