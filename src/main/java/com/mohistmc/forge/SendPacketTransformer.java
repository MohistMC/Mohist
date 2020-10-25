package com.mohistmc.forge;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

/**
 * @author Mgazul
 * @date 2020/3/31 11:05
 */
public class SendPacketTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;
        if (transformedName.equals("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher$1")) return transformClass(basicClass);
        return basicClass;
    }

    /**
     *
     * Fix can not access a member of class net.minecraftforge.fml.common.network.handshake.NetworkDispatcher$1 with modifiers "public"
     *
     * @param basicClass
     * @return
     */
    private byte[] transformClass(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        // Add main method
        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "sendPacket", "(Lnet/minecraft/network/Packet;)V", "(Lnet/minecraft/network/Packet<*>;)V", null);
        // Begin main method code
        mv.visitCode();
        // Create new instance
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/network/NetHandlerPlayServer", "func_147359_a", "(Lnet/minecraft/network/Packet;)V", true);
        // Add void return
        mv.visitInsn(Opcodes.RETURN);
        // End main method code
        mv.visitEnd();

        // The class's access flags
        classNode.access = Opcodes.ACC_SUPER + Opcodes.ACC_PUBLIC;

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
