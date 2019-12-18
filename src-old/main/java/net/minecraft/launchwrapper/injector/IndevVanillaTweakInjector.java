package net.minecraft.launchwrapper.injector;

import java.io.File;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.TABLESWITCH;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class IndevVanillaTweakInjector implements IClassTransformer {
    public IndevVanillaTweakInjector() {
    }

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        if (!classNode.interfaces.contains("java/lang/Runnable")) {
            return bytes;
        }

        MethodNode runMethod = null;
        for (final MethodNode methodNode : classNode.methods) {
            if ("run".equals(methodNode.name)) {
                runMethod = methodNode;
                break;
            }
        }
        if (runMethod == null) {
            // WTF? We got no main method
            return bytes;
        }

        System.out.println("Probably the Minecraft class (it has run && is applet!): " + name);

        final ListIterator<AbstractInsnNode> iterator = runMethod.instructions.iterator();
        int firstSwitchJump = -1;

        while (iterator.hasNext()) {
            AbstractInsnNode instruction = iterator.next();

            if (instruction.getOpcode() == TABLESWITCH) {
                TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) instruction;

                firstSwitchJump = runMethod.instructions.indexOf(tableSwitchInsnNode.labels.get(0));
            } else if (firstSwitchJump >= 0 && runMethod.instructions.indexOf(instruction) == firstSwitchJump) {
                int endOfSwitch = -1;
                while (iterator.hasNext()) {
                    instruction = iterator.next();
                    if (instruction.getOpcode() == GOTO) {
                        endOfSwitch = runMethod.instructions.indexOf(((JumpInsnNode) instruction).label);
                        break;
                    }
                }

                if (endOfSwitch >= 0) {
                    while (runMethod.instructions.indexOf(instruction) != endOfSwitch && iterator.hasNext()) {
                        instruction = iterator.next();
                    }

                    instruction = iterator.next();
                    runMethod.instructions.insertBefore(instruction, new MethodInsnNode(INVOKESTATIC, "net/minecraft/launchwrapper/injector/IndevVanillaTweakInjector", "inject", "()Ljava/io/File;"));
                    runMethod.instructions.insertBefore(instruction, new VarInsnNode(ASTORE, 2));
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static File inject() {
        // Speed up imageloading
        System.out.println("Turning off ImageIO disk-caching");
        ImageIO.setUseCache(false);

        // Set the workdir, return value will get assigned
        System.out.println("Setting gameDir to: " + Launch.minecraftHome);
        return Launch.minecraftHome;
    }
}
