package com.mohistmc.forge;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class TransformerUtils {

    public static ClassNode accept(byte[] originClass) {
        ClassReader originReader = new ClassReader(originClass);
        ClassNode originNode = new ClassNode();
        originReader.accept(originNode, 0);
        return originNode;
    }
}
