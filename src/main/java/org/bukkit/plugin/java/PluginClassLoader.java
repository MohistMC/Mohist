package org.bukkit.plugin.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.mohistmc.bukkit.nms.model.ClassMapping;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import com.mohistmc.bukkit.nms.ClassLoaderContext;
import com.mohistmc.bukkit.nms.utils.RemapUtils;

import static com.mohistmc.configuration.MohistConfigUtil.bMohist;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public final class PluginClassLoader extends URLClassLoader {
    public JavaPlugin getPlugin() { return plugin; } // Spigot
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
    private final LaunchClassLoader launchClassLoader;

    private final List<Package> packageCache = new ArrayList<>();

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

        this.launchClassLoader = parent instanceof LaunchClassLoader ? (LaunchClassLoader)parent : (LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader();
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
		if (bMohist("forge_can_call_bukkit") && parent != null && parent instanceof LaunchClassLoader) {
			try {
				Method methode = parent.getClass().getDeclaredMethod("addChild", ClassLoader.class);
				methode.invoke(parent, this);
			} catch(Exception ignored) {}
		}
    }

    static {
        // require to remove synchronize with this instance
        registerAsParallelCapable();
    }

    @Override
    protected Object getClassLoadingLock(String className) {
        return launchClassLoader;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        ClassLoaderContext.put(this);
        Class<?> result;
        try {
            if (name.replace("/", ".").startsWith("net.minecraft.server.v1_12_R1")) {
                ClassMapping remappedClassMapping = RemapUtils.jarMapping.byNMSName.get(name);
                if(remappedClassMapping == null){
                    throw new ClassNotFoundException(name.replace('/','.'));
                }
                String remappedClass = remappedClassMapping.getMcpName();
                return launchClassLoader.findClass(remappedClass);
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

                        if (result == null) {
                            result = super.findClass(name);
                        }

                        if (result != null) {
                            loader.setClass(name, result);
                        }
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
            JarEntry entry = this.jar.getJarEntry(path);
            if (entry != null) {
                InputStream stream = this.jar.getInputStream(entry);
                if (stream != null) {
                    byte[] classBytes = RemapUtils.jarRemapper.remapClassFile(stream, RuntimeRepo.getInstance());
                    classBytes = RemapUtils.remapFindClass(classBytes);

                    fixPackage(name);

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(this.url, signers);

                    result = this.defineClass(name, classBytes, 0, classBytes.length, source);
                    if (result != null) {
                        // Resolve it - sets the class loader of the class
                        this.resolveClass(result);
                    }
                }
            } else {
                final URL url = this.findResource(path);
                if (url != null) {
                    final InputStream stream = url.openStream();
                    if (stream != null) {
                        byte[] bytecode = RemapUtils.jarRemapper.remapClassFile(stream, RuntimeRepo.getInstance());
                        bytecode = RemapUtils.remapFindClass(bytecode);
                        final JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        final URL jarURL = jarURLConnection.getJarFileURL();

                        fixPackage(name);

                        final CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);
                        result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                        if (result != null) {
                            this.resolveClass(result);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }

        return result;
    }

    private void fixPackage(String name) {
        int dot = name.lastIndexOf('.');
        if (dot != -1) {
            String pkgName = name.substring(0, dot);
            Package pkg = getPackage(pkgName);
            if (pkg == null) {
                try {
                    if (manifest != null) {
                        pkg = definePackage(pkgName, manifest, url);
                    } else {
                        pkg = definePackage(pkgName, null, null, null, null, null, null, null);
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (!packageCache.contains(pkg)) {
                Attributes attributes = manifest.getMainAttributes();
                if (attributes != null) {
                    try {
                        try {
                            ObfuscationReflectionHelper.setPrivateValue(Package.class, pkg, attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE), "implTitle");
                            ObfuscationReflectionHelper.setPrivateValue(Package.class, pkg, attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION), "implVersion");
                            ObfuscationReflectionHelper.setPrivateValue(Package.class, pkg, attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR), "implVendor");
                            ObfuscationReflectionHelper.setPrivateValue(Package.class, pkg, attributes.getValue(Attributes.Name.SPECIFICATION_TITLE), "specTitle");
                            ObfuscationReflectionHelper.setPrivateValue(Package.class, pkg, attributes.getValue(Attributes.Name.SPECIFICATION_VERSION), "specVersion");
                            ObfuscationReflectionHelper.setPrivateValue(Package.class, pkg, attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR), "specVendor");
                        } catch (Exception ignored) {}
                    } finally {
                        packageCache.add(pkg);
                    }
                }
            }
        }
    }
}
