package org.bukkit.plugin.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;

import com.mohistmc.bukkit.pluginfix.PluginFixManager;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mohistmc.bukkit.nms.ClassLoaderContext;
import com.mohistmc.bukkit.nms.utils.RemapUtils;

import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.server.MinecraftServer;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public final class PluginClassLoader extends URLClassLoader {
    public JavaPlugin getPlugin() { return plugin; } // Spigot
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    private final ClassLoader libraryLoader;
    final JavaPlugin plugin;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    private final Set<String> seenIllegalAccess = Collections.newSetFromMap(new ConcurrentHashMap<>());

    static {
        ClassLoader.registerAsParallelCapable();
    }

    PluginClassLoader(@NotNull final JavaPluginLoader loader, @Nullable final ClassLoader parent, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file, @Nullable ClassLoader libraryLoader) throws IOException, InvalidPluginException, MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();
        this.libraryLoader = libraryLoader;

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
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass0(name, resolve, true, true);
    }

    Class<?> loadClass0(@NotNull String name, boolean resolve, boolean checkGlobal, boolean checkLibraries) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException ex) {
        }

        if (checkLibraries && libraryLoader != null) {
            try {
                return libraryLoader.loadClass(name);
            } catch (ClassNotFoundException ex) {
            }
        }

        if (checkGlobal) {
            // This ignores the libraries of other plugins, unless they are transitive dependencies.
            Class<?> result = loader.getClassByName(name, resolve, description);

            // If the class was loaded from a library instead of a PluginClassLoader, we can assume that its associated plugin is a transitive dependency and can therefore skip this check.
            if (result != null && result.getClassLoader() instanceof PluginClassLoader) {
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

                return result;
            }
        }

        throw new ClassNotFoundException(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassLoaderContext.put(this);
        Class<?> result;
        try {
            if (name.replace("/", ".").startsWith("net.minecraft.server.v1_16_R3.")) {
                String remappedClass = RemapUtils.jarMapping.byNMSName.get(name).getMcpName();
                return Class.forName(remappedClass);
            }
            if (name.startsWith("org.bukkit.")) {
                throw new ClassNotFoundException(name);
            }
            result = classes.get(name);
            synchronized (name.intern()) {
                if (result == null) {

                    if (result == null) {
                        result = remappedFindClass(name);
                    }

                    if (result != null) {
                        loader.setClass(name, result);
                    }

                    if (result == null) {
                        try {
                            result = MinecraftServer.getServer().getClass().getClassLoader().loadClass(name);
                        } catch (Throwable throwable) {
                            throw new ClassNotFoundException(name, throwable);
                        }
                    }

                    loader.setClass(name, result);
                    classes.put(name, result);
                }
            }
        } finally {
            ClassLoaderContext.pop();
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
    Collection<Class<?>> getClasses() {
        return classes.values();
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

    private Class<?> remappedFindClass(String name) {
        Class<?> result = null;

        try {
            // Load the resource to the name
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null) {
                InputStream stream = url.openStream();
                if (stream != null) {
                    byte[] bytecode = RemapUtils.jarRemapper.remapClassFile(stream, RuntimeRepo.getInstance());
                    bytecode = loader.server.getUnsafe().processClass(description, path, bytecode);
                    bytecode = RemapUtils.remapFindClass(bytecode);

                    bytecode = PluginFixManager.injectPluginFix(name, bytecode); // Mohist - Inject plugin fix

                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    URL jarURL = jarURLConnection.getJarFileURL();

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
                    CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);

                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        // Resolve it - sets the class loader of the class
                        this.resolveClass(result);
                    }
                }
            }
        } catch (Exception t) {
            t.printStackTrace();
        }

        return result;
    }
}
