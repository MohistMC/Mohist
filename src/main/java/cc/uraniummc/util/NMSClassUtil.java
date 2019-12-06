package cc.uraniummc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import org.bukkit.plugin.java.PluginClassLoader;
import sun.reflect.CallerSensitive;

public class NMSClassUtil{

    private final Class<?> mClass;
    private final URLClassLoader mCL;

    public NMSClassUtil(Class<?> pClass,Class<?> pPClass){
        this.mClass=pClass;
        mCL=(URLClassLoader)pPClass.getClassLoader();
    }

    @CallerSensitive
    public static Class<?> forName(String pClassName,Class<?> pClass) throws ClassNotFoundException{
        return forName0(pClass,pClassName,true,pClass.getClassLoader());
    }

    @CallerSensitive
    public static Class<?> forName(String pClassName,boolean pInit,ClassLoader pLoader,Class<?> pClass) throws ClassNotFoundException{
        return forName0(pClass,pClassName,pInit,pLoader);
    }

    @CallerSensitive
    private static Class<?> forName0(Class<?> pClass,String pClassName,boolean pInit,ClassLoader pLoader) throws ClassNotFoundException{
        URLClassLoader mCL=(URLClassLoader)pClass.getClassLoader();
        if(mCL instanceof PluginClassLoader){
            pClassName=((PluginClassLoader)mCL).umcl.remapClass(pClassName).replace("/",".");
        }
        return Class.forName(pClassName,pInit,pLoader==null?pClass.getClassLoader():pLoader);
    }

    @CallerSensitive
    public static Method getMethod(Class<?> pOwnClazz,String pMethodName,Class<?>[] pParamTypes,Class<?> pCaller) throws NoSuchMethodException,SecurityException{
        if(pOwnClazz.getClassLoader()==NMSClassUtil.class.getClassLoader()){
            pMethodName=((PluginClassLoader)pCaller.getClassLoader()).umcl.remapMethod(pOwnClazz,pMethodName,pParamTypes,false);
        }
        return pOwnClazz.getMethod(pMethodName,pParamTypes);
    }

    @CallerSensitive
    public static Method getDeclaredMethod(Class<?> pOwnClazz,String pMethodName,Class<?>[] pParamTypes,Class<?> pCaller) throws NoSuchMethodException,SecurityException{
        if(pOwnClazz.getClassLoader()==NMSClassUtil.class.getClassLoader()){
            pMethodName=((PluginClassLoader)pCaller.getClassLoader()).umcl.remapMethod(pOwnClazz,pMethodName,pParamTypes,true);
        }
        return pOwnClazz.getDeclaredMethod(pMethodName,pParamTypes);
    }

    @CallerSensitive
    public static Field getField(Class<?> pOwnClazz,String pFieldName,Class<?> pCaller) throws NoSuchFieldException,SecurityException{
        if(pOwnClazz.getClassLoader()==NMSClassUtil.class.getClassLoader()){
            pFieldName=((PluginClassLoader)pCaller.getClassLoader()).umcl.remapField(pOwnClazz,pFieldName,false);
        }
        return pOwnClazz.getField(pFieldName);
    }

    @CallerSensitive
    public static Field getDeclaredField(Class<?> pOwnClazz,String pFieldName,Class<?> pCaller) throws NoSuchFieldException,SecurityException{
        if(pOwnClazz.getClassLoader()==NMSClassUtil.class.getClassLoader()){
            pFieldName=((PluginClassLoader)pCaller.getClassLoader()).umcl.remapField(pOwnClazz,pFieldName,true);
        }
        return pOwnClazz.getDeclaredField(pFieldName);
    }

}
