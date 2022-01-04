package com.mohistmc.forge;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Optional;

public class LibrarianLibFix implements IClassTransformer {
    private final IClassTransformer originTransformer;

    public LibrarianLibFix(IClassTransformer originTransformer) {
        this.originTransformer = originTransformer;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;
        byte[] patchedClass = originTransformer.transform(name, transformedName, basicClass);
        if ("net.minecraft.network.NetHandlerPlayServer".equals(transformedName)) {
            return patchupdate(basicClass, patchedClass);
        }
        return patchedClass;
    }

    public byte[] patchupdate(byte[] originClass, byte[] patchedClass) {
        ClassNode patchedNode = TransformerUtils.accept(patchedClass);
        ClassNode originNode = TransformerUtils.accept(originClass);

        Optional<MethodNode> optional = patchedNode.methods.stream().filter(methodNode -> "func_73660_a".equals(methodNode.name) && "()V".equals(methodNode.desc)).findFirst();
        if (optional.isPresent()) {
            originNode.methods.removeIf(methodNode -> "func_73660_a".equals(methodNode.name) && "()V".equals(methodNode.desc));
            originNode.methods.add(optional.get());
        }

        ClassWriter writer = new ClassWriter(0);
        originNode.accept(writer);
        return writer.toByteArray();
    }
}
