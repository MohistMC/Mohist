package red.mohist.reampper.remappers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import red.mohist.reampper.proxy.DelegateURLClassLoder;

/**
 *
 * @author pyz
 * @date 2019/7/2 9:16 PM
 */
public class MohistClassRemapper extends ClassRemapper {

    public MohistClassRemapper(ClassVisitor cv, Remapper remapper) {
        super(cv, remapper);
    }

    public MohistClassRemapper(int api, ClassVisitor cv, Remapper remapper) {
        super(api, cv, remapper);
    }

    /**
     *
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    /**
     *
     * @param version
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ("java/net/URLClassLoader".equals(superName)) {
            superName = DelegateURLClassLoder.desc;
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     *
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param value
     * @return
     */
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return super.visitField(access, name, desc, signature, value);
    }

    /**
     *
     * @param mv
     * @return
     */
    @Override
    protected MethodVisitor createMethodRemapper(MethodVisitor mv) {
        return new ReflectMethodRemapper(mv, remapper);
    }
}
