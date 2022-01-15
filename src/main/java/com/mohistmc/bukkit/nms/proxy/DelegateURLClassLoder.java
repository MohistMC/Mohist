package com.mohistmc.bukkit.nms.proxy;

import com.mohistmc.bukkit.nms.utils.RemapUtils;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * @author pyz
 * @date 2019/7/1 8:41 PM
 */
public class DelegateURLClassLoder extends URLClassLoader {

    public static final String desc = DelegateURLClassLoder.class.getName().replace('.', '/');
    private static LaunchClassLoader launchClassLoader;
    private final Set<Package> packageCache = Collections.newSetFromMap(new ConcurrentHashMap<>());

    static {
        launchClassLoader = (LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader();
    }

    private final Map<String, Class<?>> classeCache = new HashMap<>();

    public DelegateURLClassLoder(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
    }

    public DelegateURLClassLoder(final URL[] urls) {
        super(urls);
    }

    public DelegateURLClassLoder(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (RemapUtils.isNMSClass(name)) {
            String mapName = RemapUtils.map(name.replace('.', '/')).replace('/', '.');
            return launchClassLoader.findClass(mapName);
        }
        Class<?> result = this.classeCache.get(name);
        if (result != null) {
            return result;
        }
        synchronized (name.intern()) {
            result = this.remappedFindClass(name);
            if (result != null) {
                return result;
            }
            result = this.remappedFindClass(name);
            if (result == null) {
                try {
                    result = super.findClass(name);
                } catch (ClassNotFoundException e) {
                    result = launchClassLoader.getClass().getClassLoader().loadClass(name);
                }
            }
            if (result == null) {
                throw new ClassNotFoundException(name);
            }
            this.cacheClass(name, result);
        }
        return result;
    }

    protected Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            final String path = name.replace('.', '/').concat(".class");
            final URL url = this.findResource(path);
            if (url != null) {
                final InputStream stream = url.openStream();
                if (stream != null) {
                    byte[] bytecode = RemapUtils.jarRemapper.remapClassFile(stream, RuntimeRepo.getInstance());
                    bytecode = RemapUtils.remapFindClass(bytecode);
                    final JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    final URL jarURL = jarURLConnection.getJarFileURL();

                    final Manifest manifest = jarURLConnection.getManifest();
                    fixPackage(manifest, url, name);

                    final CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);
                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        this.resolveClass(result);
                    }
                }
            }
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }
        return result;
    }

    protected void cacheClass(final String name, final Class<?> clazz) {
        this.classeCache.put(name, clazz);
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            ConfigurationSerialization.registerClass((Class<? extends ConfigurationSerializable>) clazz);
        }
    }

    private void fixPackage(Manifest manifest, URL url, String name) {
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
            if (pkg != null && manifest != null) {
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
                            } catch (Exception ignored) {
                            }
                        } finally {
                            packageCache.add(pkg);
                        }
                    }
                }
            }
        }
    }
}