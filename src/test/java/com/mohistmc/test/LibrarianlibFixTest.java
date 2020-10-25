package com.mohistmc.test;

public class LibrarianlibFixTest {

    /*

    private static byte[] transformNetHandlerPlayServer(byte[] basicClass) {
        MethodSignature sig = new MethodSignature("update", "func_73660_a", "()V");
        MethodSignature target = new MethodSignature("onUpdateEntity", "func_71127_g", "()V");
        MethodSignature methodSignature;
        InsnList instructions;
        InsnList beforeInstructions;
        InsnList afterInstructions;
        LabelNode notCanceled;
        LabelNode escapeMethod;
        return transform(basicClass, sig, "Update hook", combine(node -> ((AbstractInsnNode)node).getOpcode() == 182 && methodSignature.matches(node), (method, node) -> {
            instructions = method.instructions;
            beforeInstructions = new InsnList();
            afterInstructions = new InsnList();
            notCanceled = new LabelNode();
            escapeMethod = new LabelNode();
            beforeInstructions.add((AbstractInsnNode)new InsnNode(89));
            beforeInstructions.add((AbstractInsnNode)new MethodInsnNode(184, "com/teamwizardry/librarianlib/asm/LibLibAsmHooks", "preUpdateMP", "(Lnet/minecraft/entity/player/EntityPlayerMP;)Z", false));
            beforeInstructions.add((AbstractInsnNode)new JumpInsnNode(153, notCanceled));
            beforeInstructions.add((AbstractInsnNode)new InsnNode(87));
            beforeInstructions.add((AbstractInsnNode)new JumpInsnNode(167, escapeMethod));
            beforeInstructions.add((AbstractInsnNode)notCanceled);
            beforeInstructions.add((AbstractInsnNode)new InsnNode(89));
            afterInstructions.add((AbstractInsnNode)new MethodInsnNode(184, "com/teamwizardry/librarianlib/asm/LibLibAsmHooks", "postUpdateMP", "(Lnet/minecraft/entity/player/EntityPlayerMP;)V", false));
            afterInstructions.add((AbstractInsnNode)escapeMethod);
            instructions.insertBefore(node, beforeInstructions);
            instructions.insert(node, afterInstructions);
            instructions.resetLabels();
            return false;
        }));
    }
     */
}
