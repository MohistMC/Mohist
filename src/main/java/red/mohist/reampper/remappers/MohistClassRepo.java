package red.mohist.reampper.remappers;

import java.io.IOException;
import java.io.InputStream;
import net.md_5.specialsource.repo.CachingRepo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import red.mohist.reampper.ClassLoaderContext;

/**
 *
 * @author pyz
 * @date 2019/7/4 1:21 AM
 */
public class MohistClassRepo extends CachingRepo {
    private static final MohistClassRepo INSTANCE = new MohistClassRepo();

    public static MohistClassRepo getInstance() {
        return INSTANCE;
    }

    @Override
    protected ClassNode findClass0(String internalName) {
        InputStream in = getClassLoder().getResourceAsStream(internalName + ".class");
        if (in == null || internalName.equals("jdk/nashorn/api/scripting/NashornScriptEngineFactory")) {
            return null;
        }
        ClassNode classNode = new ClassNode();
        try {
            ClassReader reader = new ClassReader(in);
            reader.accept(classNode, 0);
        } catch (IOException e) {
            return null;
        }
        return classNode;
    }

    protected ClassLoader getClassLoder() {
        ClassLoader cl = ClassLoaderContext.peek();
        if (cl == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        if (cl == null) {
            cl = this.getClass().getClassLoader();
        }
        return cl;
    }
}
