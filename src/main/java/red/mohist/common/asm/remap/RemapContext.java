package red.mohist.common.asm.remap;

import java.util.LinkedList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.objectweb.asm.tree.ClassNode;

/**
 *
 * @author pyz
 * @date 2019/7/21 9:48 PM
 */
public class RemapContext {
    private static final LinkedList<RemapContext> remapStack = new LinkedList<>();

    private ClassNode classNode;
    private PluginDescriptionFile description;

    public static LinkedList<RemapContext> getRemapStack() {
        return remapStack;
    }

    public PluginDescriptionFile getDescription() {
        return description;
    }

    public RemapContext setDescription(PluginDescriptionFile description) {
        this.description = description;
        return this;
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public RemapContext setClassNode(ClassNode classNode) {
        this.classNode = classNode;
        return this;
    }

    public static void push(RemapContext context) {
        remapStack.push(context);
    }

    public static RemapContext peek() {
        return remapStack.peek();
    }

    public static RemapContext pop() {
        return remapStack.pop();
    }
}
