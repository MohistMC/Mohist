package red.mohist.reampper.proxy;

import red.mohist.reampper.utils.RemapUtils;

import java.security.ProtectionDomain;

public class DelegateClassLoder extends ClassLoader{

    public static final String desc = DelegateClassLoder.class.getName().replace('.', '/');

    protected DelegateClassLoder() {
        super();
    }

    protected DelegateClassLoder(ClassLoader parent) {
        super(parent);
    }

    public final Class<?> defineClassMohsit(byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMohsit(null, b, off, len, null);
    }

    public final Class<?> defineClassMohsit(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMohsit(name, b, off, len, null);
    }

    public final Class<?> defineClassMohsit(String name, java.nio.ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (!b.isDirect() && b.hasArray()) {
            return remappedFindClass(name, b.array(), protectionDomain);
        }
        return defineClass(name, b, protectionDomain);
    }

    public final Class<?> defineClassMohsit(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (off == 0) {
            return remappedFindClass(name, b, protectionDomain);
        }

        return defineClass(name, b, off, len, protectionDomain);
    }

    private Class<?> remappedFindClass(String name, byte[] stream, ProtectionDomain protectionDomain) throws ClassFormatError {
        Class<?> result = null;
        try {
            byte[] bytecode = RemapUtils.remapFindClass(stream);
            result = this.defineClass(name, bytecode, 0, bytecode.length, protectionDomain);
        } catch (Throwable t) {
            throw new ClassFormatError("Failed to remap class " + name);
        }
        return result;
    }
}
