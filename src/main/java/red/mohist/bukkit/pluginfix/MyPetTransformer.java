package red.mohist.bukkit.pluginfix;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

/**
 * @author Mgazul
 * @date 2020/4/14 3:48
 */
public class MyPetTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;
        if (transformedName.equals("net.minecraft.entity.Entity")) return patchUniqueID(basicClass);
        return basicClass;
    }

    private byte[] patchUniqueID(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        // Add main method
        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "getUniqueID", "()Ljava/util/UUID;", null, null);
        // Begin main method code
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/Entity", "func_110124_au", "()Ljava/util/UUID;", false);
        mv.visitInsn(Opcodes.ARETURN);
        // End main method code
        mv.visitEnd();

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
