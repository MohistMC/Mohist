package red.mohist.bukkit.nms.remappers;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;

/**
 *
 * @author pyz
 * @date 2019/7/3 10:38 PM
 */
public class MohistJarRemapper extends JarRemapper {

    public MohistJarRemapper(JarMapping jarMapping) {
        super(jarMapping);
    }

    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Exception e) {
            return signature;
        }
    }

    @Override
    public String mapFieldName(String owner, String name, String desc, int access) {
        return super.mapFieldName(owner, name, desc, -1);
    }
}
