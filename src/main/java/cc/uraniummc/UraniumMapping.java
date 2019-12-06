package cc.uraniummc;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.md_5.specialsource.InheritanceMap;
import net.md_5.specialsource.JarComparer;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.InheritanceProvider;

public class UraniumMapping extends JarMapping{

    public final Map<String,String> mRclasses=new HashMap<String,String>();
    public final Map<String,String> mMethodsNR=new HashMap<String,String>();
    public final Map<String,String> mMethodsExtends=new HashMap<String, String>();
    public final Set<String> mNMSClasses=new HashSet<String>();
    public InheritanceMap inheritanceMap;
    public InheritanceProvider fallbackInheritanceProvider;

    public UraniumMapping(){
        super();
    }

    public UraniumMapping(JarComparer oldJar,JarComparer newJar,File logFile,boolean compact) throws IOException{
        super(oldJar,newJar,logFile,compact);
    }

    public UraniumMapping(JarComparer oldJar,JarComparer newJar,File logfile,boolean compact,boolean full) throws IOException{
        super(oldJar,newJar,logfile,compact,full);
    }

    @Override
    public void loadMappings(String filename,boolean reverse,boolean numericSrgNames,String inShadeRelocation,String outShadeRelocation) throws IOException{
        super.loadMappings(filename,reverse,numericSrgNames,inShadeRelocation,outShadeRelocation);
    }

    @Override
    public void loadMappings(File file) throws IOException{
        super.loadMappings(file);
    }

    public void setInheritanceMap(InheritanceMap inheritanceMap){
        this.inheritanceMap=inheritanceMap;
        super.setInheritanceMap(inheritanceMap);
    }

    public void setFallbackInheritanceProvider(InheritanceProvider fallbackInheritanceProvider){
        this.fallbackInheritanceProvider=fallbackInheritanceProvider;
        super.setFallbackInheritanceProvider(fallbackInheritanceProvider);
    }

    public void loadRClasses(){
        for(Map.Entry<String,String> CLASS : classes.entrySet()){
            if(!CLASS.getKey().startsWith("net/minecraft/server/v1_7_R4")){
                continue;
            }
            mRclasses.put(CLASS.getValue(),CLASS.getKey());
        }
    }
    
    public String remapFieldOrMethod(Map<String, String> pRemap,String pOwner,String pDef,String pDescHead,boolean pDeclared){
        if(pOwner==null) return pDef;
        String tDesc=pOwner+"/"+pDescHead;
        String newmethod=pRemap.get(tDesc);
        if(newmethod!=null){
            return newmethod;
        }else{
            Collection<String> parents=null;;
            if(this.inheritanceMap.hasParents(pOwner)){
                parents=this.inheritanceMap.getParents(pOwner);
            }else if(this.fallbackInheritanceProvider!=null){
                parents=this.fallbackInheritanceProvider.getParents(pOwner);
                this.inheritanceMap.setParents(pOwner,parents);
            }

            if(parents!=null){
                for(String parent : parents){
                    newmethod=remapFieldOrMethod(pRemap,parent,pDef,pDescHead,pDeclared);
                    if(newmethod!=null){
                        return newmethod;
                    }
                }
            }
        }
        return pDef;
    }

    public String remapExtendsMethod(String pOwner,String pName,String pParamDescWithName,boolean pDeclared){
        return this.remapFieldOrMethod(this.mMethodsExtends,pOwner,pName,pParamDescWithName,pDeclared);
    }

    /**
     * 
     * @param pOwner
     * @param pName
     * @param pParamDescWithName 方法签名格式为 名字(参数类型)
     * @param pDeclared
     * @return
     */
    public String remapMethod(String pOwner,String pName,String pParamDescWithName,boolean pDeclared){
        return this.remapFieldOrMethod(this.mMethodsNR,pOwner,pName,pParamDescWithName,pDeclared);
    }
    
    public String remapField(String pOwner,String pName,boolean pDeclared){
        return this.remapFieldOrMethod(this.fields,pOwner,pName,pName,pDeclared);
    }

    public void loadMethodsNR(){
        for(Map.Entry<String,String> METHOD : methods.entrySet()){
            if(!METHOD.getKey().startsWith("net/minecraft/server")){
                continue;
            }
            String[] key=METHOD.getKey().split(" ");
            boolean obj=false;
            StringBuilder newkey=new StringBuilder(" ");
            StringBuilder obj_name=new StringBuilder();
            for(char i : key[1].toCharArray()){
                if(i=='L'){
                    obj=true;
                }else if(obj){
                    if(i==';'){
                        String newclass=classes.get(obj_name.toString());
                        newkey.append(newclass==null?obj_name.toString():newclass).append(";");
                        obj_name.delete(0,obj_name.length());
                        obj=false;
                        continue;
                    }
                    obj_name.append(i);
                    continue;
                }
                newkey.append(i);
            }
            String newkeyy=newkey.toString();
            int args_end=newkeyy.lastIndexOf(')')+1;
            String types=newkeyy.substring(0,args_end);
            int class_end=key[0].lastIndexOf('/');
            mMethodsExtends.put(classes.get(key[0].substring(0,class_end))+key[0].substring(class_end)+newkeyy,METHOD.getValue());
            if(METHOD.getKey().startsWith("net/minecraft/server/v1_7_R4")){
                mMethodsNR.put(key[0]+types,METHOD.getValue());
            }
        }
    }
}
