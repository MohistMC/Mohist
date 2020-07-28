package red.mohist.reampper.remappers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 *
 * @author pyz
 * @date 2019/7/2 11:24 PM
 */
public interface ClassRemapperSupplier {
    default ClassRemapper getClassRemapper( ClassVisitor cv) {
        return new ClassRemapper(cv, (Remapper) this);
    }
}

