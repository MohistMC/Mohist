package org.bukkit.plugin.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import red.mohist.common.remap.ClassLoaderContext;
import red.mohist.common.remap.RemapUtils;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public final class PluginClassLoader extends URLClassLoader {
    final JavaPlugin plugin;
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;

    static {
        try {
            Class.forName("org.bukkit.PackageDefine");
            Class.forName("org.bukkit.craftbukkit.PackageDefine");
            Class.forName("org.spigotmc.PackageDefine");
            Class.forName("com.destroystokyo.paper.PackageDefine");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    PluginClassLoader(final JavaPluginLoader loader, final ClassLoader parent, final PluginDescriptionFile description, final File dataFolder, final File file) throws IOException, InvalidPluginException {
        super(new URL[]{file.toURI().toURL()}, parent);
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
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        ClassLoaderContext.put(this);
        Class<?> result;
        try {
            if (name.startsWith("net.minecraft.server.v1_12_R1")) {
                String remappedClass = RemapUtils.jarMapping.byNMSName.get(name).getMcpName();
                return Class.forName(remappedClass);
            }

            if (name.startsWith("org.bukkit.")) {
                throw new ClassNotFoundException(name);
            }
            result = classes.get(name);
            synchronized (name.intern()) {
                if (result == null) {
                    if (checkGlobal) {
                        result = loader.getClassByName(name);
                    }

                    if (result == null) {
                        result = remappedFindClass(name);

                        if (result != null) {
                            loader.setClass(name, result);
                        }
                    }

                    if (result == null) {
                        throw new ClassNotFoundException(name);
                    }

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

    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }

    private Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;

        try {
            // Load the resource to the name
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null) {
                InputStream stream = url.openStream();
                if (stream != null) {
//                  remap
                    byte[] bytecode = IOUtils.toByteArray(stream);
                    bytecode = RemapUtils.remapFindClass(description, name, bytecode);
                    // Define (create) the class using the modified byte code
                    // The top-child class loader is used for this to prevent access violations
                    // Set the codesource to the jar, not within the jar, for compatibility with
                    // plugins that do new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()))
                    // instead of using getResourceAsStream - see https://github.com/MinecraftPortCentral/Cauldron-Plus/issues/75
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection(); // parses only
                    URL jarURL = jarURLConnection.getJarFileURL();
                    CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);

                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        // Resolve it - sets the class loader of the class
                        this.resolveClass(result);
                    }
                }
            }
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }

        return result;
    }

    public PluginDescriptionFile getDescription() {
        return description;
    }
}
