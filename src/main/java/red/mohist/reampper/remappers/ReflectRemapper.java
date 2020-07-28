package red.mohist.reampper.remappers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 *
 * @author pyz
 * @date 2019/7/2 8:05 PM
 */
public class ReflectRemapper extends Remapper implements ClassRemapperSupplier {

    /**
     *
     * @param classWriter
     * @return
     */
    @Override
    public ClassRemapper getClassRemapper(ClassVisitor classWriter) {
        return new MohistClassRemapper(classWriter, this);
    }

    @Override
    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Exception e) {
            return signature;
        }
    }
}