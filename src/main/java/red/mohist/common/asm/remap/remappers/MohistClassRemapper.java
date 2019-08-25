package red.mohist.common.asm.remap.remappers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import red.mohist.common.asm.remap.proxy.DelegateURLClassLoder;

/**
 * 负责反射remap,
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
     * 把所有方法改成public
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
        int modifyAccess = toPublic(access);
        return super.visitMethod(modifyAccess, name, desc, signature, exceptions);
    }

    /**
     * 把所有类改成public
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
        int modifyAccess = toPublic(access);
        if ("java/net/URLClassLoader".equals(superName)) {
            superName = DelegateURLClassLoder.desc;
        }
        super.visit(version, modifyAccess, name, signature, superName, interfaces);
    }

    /**
     * 把所有字段改成public
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
        int modifyAccess = toPublic(access);
        return super.visitField(modifyAccess, name, desc, signature, value);
    }

    /**
     * 做反射proxy
     *
     * @param mv
     * @return
     */
    @Override
    protected MethodVisitor createMethodRemapper(MethodVisitor mv) {
        return new ReflectMethodRemapper(mv, remapper);
    }

    private int toPublic(int access) {
        access |= Opcodes.ACC_PUBLIC;
        access &= ~Opcodes.ACC_PROTECTED;
        access &= ~Opcodes.ACC_PRIVATE;
        return access;
    }
}
