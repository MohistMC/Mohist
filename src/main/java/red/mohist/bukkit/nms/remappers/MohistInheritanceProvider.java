package red.mohist.bukkit.nms.remappers;

import java.util.Collection;
import java.util.HashSet;
import net.md_5.specialsource.provider.InheritanceProvider;
import org.objectweb.asm.Type;
import red.mohist.bukkit.nms.ClassUtils;
import red.mohist.bukkit.nms.MappingLoader;

/**
 * @author Mgazul
 * @date 2020/4/16 1:08
 */
public class MohistInheritanceProvider implements InheritanceProvider {

    @Override
    public Collection<String> getParents(String className) {
        className = MappingLoader.remapper.map(className);

        try {
            Collection<String> parents = new HashSet<String>();
            Class<?> reference = Class.forName(ClassUtils.toClassName(className).replace('$', '.'), false, this.getClass().getClassLoader()/*RemappedMethods.loader*/);
            Class<?> extend = reference.getSuperclass();
            if (extend != null) {
                parents.add(RemapUtils.reverseMap(Type.getInternalName(extend)));
            }

            for (Class<?> inter : reference.getInterfaces()) {
                if (inter != null) {
                    parents.add(RemapUtils.reverseMap(Type.getInternalName(inter)));
                }
            }

            return parents;
        } catch (Exception e) {
            // Empty catch block
        }
        return null;
    }
}
