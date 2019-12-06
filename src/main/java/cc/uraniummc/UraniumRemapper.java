package cc.uraniummc;

import cc.uraniummc.util.NMSClassUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import jdk.nashorn.internal.objects.annotations.Setter;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.repo.ClassRepo;
import org.bukkit.plugin.java.PluginClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class UraniumRemapper extends JarRemapper implements Opcodes{

    protected final PluginClassLoader mLoader;
    private int writerFlags=COMPUTE_MAXS;
    private int readerFlags=0;
    private boolean debug=false;
    private UraniumMapping fUmap;
    private boolean remapReflection =false;
    public UraniumRemapper(UraniumMapping jarMapping,PluginClassLoader pPluginClassLoader){
        super(jarMapping);
        this.fUmap=jarMapping;
        this.mLoader=pPluginClassLoader;
    }

    public UraniumRemapper(UraniumMapping jarMapping,PluginClassLoader pPluginClassLoader,boolean reflection){
        this(jarMapping,pPluginClassLoader);
        this.remapReflection =reflection;
    }

    @Override
    public String mapSignature(String signature,boolean typeSignature){
        try{
            return super.mapSignature(signature,typeSignature);
        }catch(Exception e){
            return signature;
        }
    }

    @Override
    public String map(String typeName){
        // not remap class that exist in jar file
        // this mean plugin use it's owner lib 
        return this.mLoader.findResource(typeName.replace('.','/').concat(".class"))!=null?typeName:super.map(typeName);
    }

    /**
     * Remap an individual class given an InputStream to its bytecode
     */
    public byte[] remapClassFile(InputStream is,ClassRepo repo) throws IOException{
        byte[] remapped = super.remapClassFile(is,repo);
        return remapClassFile2(new ClassReader(remapped));
    }

    public byte[] remapClassFile(byte[] in,ClassRepo repo){
        byte[] remapped = super.remapClassFile(in,repo);
        return remapClassFile2(new ClassReader(remapped));
    }

    private void logR(String message){
        if(debug){
            System.out.println("[ReflectionRemapper] "+message);
        }
    }
    @SuppressWarnings("unchecked")
    private byte[] remapClassFile2(ClassReader pCReader){
        ClassNode tCNode=new ClassNode();
        pCReader.accept(tCNode,ClassReader.EXPAND_FRAMES);
        if(tCNode.superName.startsWith("net/minecraft")) {
            for (int i = 0; i < tCNode.fields.size(); i++) {
                FieldNode fieldNode = tCNode.fields.get(i);
                if (!Modifier.isStatic(fieldNode.access)&&Modifier.isPrivate(fieldNode.access)){
                    fieldNode.name = fUmap.remapField(tCNode.name, fieldNode.name, true);
                }
            }
        }
        /*
        for (int i = 0; i < tCNode.methods.size(); i++) {
            MethodNode tMNode = tCNode.methods.get(i);
            for (int j = 0; j < tMNode.instructions.size(); j++) {
                AbstractInsnNode tInsnNode = tMNode.instructions.get(j);
                if (tInsnNode instanceof MethodInsnNode && (tInsnNode.getOpcode() >= INVOKEVIRTUAL && tInsnNode.getOpcode() < INVOKEDYNAMIC)) {
                    val node=(MethodInsnNode)tInsnNode;
                    if(!node.name.startsWith("func_"))
                    node.name=fUmap.remapExtendsMethod(node.owner,node.name,node.name+" "+node.desc,true);
                }
                else if(tInsnNode instanceof FieldInsnNode && tInsnNode.getOpcode() == GETFIELD){
                    val node=(FieldInsnNode)tInsnNode;
                    if(!node.name.startsWith("field_"))
                    node.name=fUmap.remapField(node.owner,node.name,false);
                }
            }
        }
        */
        if(remapReflection) {
            for (int i = 0; i < tCNode.methods.size(); i++) {
                MethodNode tMNode = tCNode.methods.get(i);
                for (int j = 0; j < tMNode.instructions.size(); j++) {
                    AbstractInsnNode tInsnNode = tMNode.instructions.get(j);
                    if (tInsnNode instanceof MethodInsnNode && (tInsnNode.getOpcode() >= INVOKEVIRTUAL && tInsnNode.getOpcode() < INVOKEDYNAMIC)) {

                        MethodInsnNode mi = (MethodInsnNode) tInsnNode;
                        if (mi.owner.equals("java/lang/Class") && (mi.name.equals("getDeclaredMethod") ||
                                mi.name.equals("getMethod") ||
                                mi.name.equals("getField") ||
                                mi.name.equals("getDeclaredField"))) {
                            mi.setOpcode(INVOKESTATIC);
                            mi.owner = Type.getType(NMSClassUtil.class).getInternalName();
                            tMNode.instructions.insertBefore(mi, new LdcInsnNode(Type.getObjectType(mLoader.getDescription().getMain().replace(".", "/"))));
                            int tIndex = mi.desc.indexOf(')');
                            mi.desc = "(Ljava/lang/Class;" + (mi.desc.substring(1, tIndex)) + "Ljava/lang/Class;" + (mi.desc.substring(tIndex));
                        } else if (mi.getOpcode() == INVOKESTATIC && mi.owner.equals("java/lang/Class") && mi.name.equals("forName")) {
                            tMNode.instructions.insertBefore(mi, new LdcInsnNode(Type.getObjectType(mLoader.getDescription().getMain().replace(".", "/"))));
                            mi.owner = Type.getType(NMSClassUtil.class).getInternalName();
                            int tIndex = mi.desc.indexOf(')');
                            mi.desc = mi.desc.substring(0, tIndex) + "Ljava/lang/Class;" + (mi.desc.substring(tIndex));
                        }
                    }
                }
            }
        }
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        tCNode.accept(cw);
        return cw.toByteArray();
    }

    public void setDebug(boolean b) {
        this.debug = b;
    }
}
