package red.mohist.bukkit.nms.proxy;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;

import java.security.ProtectionDomain;
import red.mohist.bukkit.nms.MappingLoader;
import red.mohist.bukkit.nms.remappers.MohistInheritanceProvider;
import red.mohist.bukkit.nms.remappers.MohistJarRemapper;
import red.mohist.bukkit.nms.remappers.RemapperProcessor;

public class ProxyCustomClassLoder extends ClassLoader {
    private JarMapping jarMapping;
    private MohistJarRemapper remapper;

    {
        this.jarMapping = MappingLoader.loadMapping();
        final JointProvider provider = new JointProvider();
        provider.add(new MohistInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        this.jarMapping.setFallbackInheritanceProvider(provider);
        this.remapper = new MohistJarRemapper(this.jarMapping);
    }

    protected ProxyCustomClassLoder() {
        super();
    }

    protected ProxyCustomClassLoder(ClassLoader parent) {
        super(parent);
    }

    public final Class<?> defineClassMohist(byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMohist(null, b, off, len, null);
    }

    public final Class<?> defineClassMohist(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMohist(name, b, off, len, null);
    }

    public final Class<?> defineClassMohist(String name, java.nio.ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (!b.isDirect() && b.hasArray()) {
            return remappedFindClass(name, b.array(), protectionDomain);
        }
        return defineClass(name, b, protectionDomain);
    }

    public final Class<?> defineClassMohist(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (off == 0) {
            return remappedFindClass(name, b, protectionDomain);
        }

        return defineClass(name, b, off, len, protectionDomain);
    }

    private Class<?> remappedFindClass(String name, byte[] stream, ProtectionDomain protectionDomain) throws ClassFormatError {
        Class<?> result = null;

        try {
            byte[] bytecode = remapper.remapClassFile(stream, RuntimeRepo.getInstance());
            bytecode = RemapperProcessor.transform(bytecode);

            result = this.defineClass(name, bytecode, 0, bytecode.length, protectionDomain);
        } catch (Throwable t) {
            throw new ClassFormatError("Failed to remap class " + name);
        }

        return result;
    }
}
