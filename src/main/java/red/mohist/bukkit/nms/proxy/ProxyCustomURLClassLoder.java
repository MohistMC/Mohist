package red.mohist.bukkit.nms.proxy;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import red.mohist.Mohist;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.remappers.RemapUtils;
import red.mohist.bukkit.nms.remappers.RemapperProcessor;
import red.mohist.bukkit.nms.remappers.MohistJarRemapper;

/**
 *
 * @author pyz
 * @date 2019/7/1 8:41 PM
 */
public class ProxyCustomURLClassLoder extends URLClassLoader {

    private final JarMapping jarMapping;
    private final MohistJarRemapper remapper;
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final LaunchClassLoader launchClassLoader;

    {
        this.jarMapping = RemapUtils.loadJarMapping(this, true);
        this.remapper = RemapUtils.loadRemapper(this , true);
        this.launchClassLoader = (LaunchClassLoader) Mohist.class.getClassLoader();
    }

    public ProxyCustomURLClassLoder(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
    }

    public ProxyCustomURLClassLoder(final URL[] urls) {
        super(urls);
    }

    public ProxyCustomURLClassLoder(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (ClassUtils.isNeedRemap(name)) {
            return launchClassLoader.findClass(RemapUtils.remappedClass(jarMapping, name));
        }

        Class<?> result = this.classes.get(name);
        synchronized (name.intern()) {
            if (result == null) {
                result = this.remappedFindClass(name);

                if (result == null) {
                    try {
                        result = super.findClass(name);
                    } catch (ClassNotFoundException ignored) { }
                }

                if (result == null) {
                    try {
                        result = launchClassLoader.findClass(name);
                    } catch (ClassNotFoundException ignored) { }
                }

                if (result == null) {
                    try {
                        result = launchClassLoader.getClass().getClassLoader().loadClass(name);
                    } catch (Throwable throwable) {
                        throw new ClassNotFoundException(name, throwable);
                    }
                }

                if (result == null) throw new ClassNotFoundException(name);
                this.classes.put(name, result);
            }
        }
        return result;
    }

    private Class<?> remappedFindClass(final String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            final String path = ClassUtils.getInternalName(name).concat(".class");
            final URL url = this.findResource(path);
            if (url != null) {
                final InputStream stream = url.openStream();
                if (stream != null) {
                    byte[] bytecode;
                    bytecode = remapper.remapClassFile(stream, RuntimeRepo.getInstance());
                    bytecode = RemapperProcessor.transform(bytecode);
                    final JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                    final URL jarURL = jarURLConnection.getJarFileURL();
                    final CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);
                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        this.resolveClass(result);
                    }
                }
            }
        }
        catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }
        return result;
    }
}