package thermos;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETFIELD;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;

import pw.prok.imagine.asm.ImagineASM;
import pw.prok.imagine.asm.Transformer;
import cpw.mods.fml.common.FMLLog;

@Transformer.RegisterTransformer
public class ThermosClassTransformer implements Transformer {
    @Override
    public void transform(final ImagineASM asm) {
        if (asm.is("climateControl.utils.ChunkGeneratorExtractor")) {
            boolean undergroundBiomesInstalled = false;
            try {
                Class.forName("exterminatorJeff.undergroundBiomes.worldGen.ChunkProviderWrapper");
                undergroundBiomesInstalled = true;
            } catch (Exception ignored) {
            }
            if (!undergroundBiomesInstalled) {
                FMLLog.log(Level.INFO, "Thermos: Patching " + asm.getActualName() + " for compatibility with Climate Control");
                extractFrom(asm, asm.method("extractFrom",
                        "(Lnet/minecraft/world/WorldServer;)Lnet/minecraft/world/chunk/IChunkProvider;").instructions());
            }
        }
    }

    public void extractFrom(ImagineASM asm, InsnList list) {
        //Pair<String, String> fieldChunkProvider = asm.field("net/minecraft/world/World", "chunkProvider");
        list.clear();
        list.add(new IntInsnNode(ALOAD, 1));
        list.add(new FieldInsnNode(GETFIELD, "ahb", "v", "Lapu;"));
        list.add(new InsnNode(ARETURN));
    }
}
