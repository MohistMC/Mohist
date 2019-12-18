package red.mohist.common.remap.remappers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 *
 * @author pyz
 * @date 2019/7/21 9:56 PM
 */
public class BaseClassRemapper extends ClassRemapper {
    public BaseClassRemapper(ClassVisitor cv, Remapper remapper) {
        super(cv, remapper);
    }

    public BaseClassRemapper(int api, ClassVisitor cv, Remapper remapper) {
        super(api, cv, remapper);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
