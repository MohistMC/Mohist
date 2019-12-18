package red.mohist.common.remap.remappers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import red.mohist.common.remap.ASMUtils;

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
        if (ASMUtils.isValidSingnature(signature)) {
            return super.mapSignature(signature, typeSignature);
        } else {
            return signature;
        }
    }
}
