package red.mohist.util;

import java.util.Collection;

public class ValidData{

    private static void error(String pMsg,Object...pParams){
        if(pParams.length!=0){
            throw new IllegalArgumentException(String.format(pMsg,pParams));
        }else{
            throw new IllegalArgumentException(pMsg);
        }
    }

    public static void notNull(Object pObj,String pMsg,Object...pParams){
        if(pObj==null){
            ValidData.error(pMsg,pParams);
        }
    }

    public static void notEmpty(String pStr,String pMsg,Object...pParams){
        if(pStr==null||pStr.isEmpty()){
            ValidData.error(pMsg,pParams);
        }
    }

    public static void notEmpty(Collection<?> pColl,String pMsg,Object...pParams){
        if(pColl==null||pColl.isEmpty()){
            ValidData.error(pMsg,pParams);
        }
    }

    public static void valid(boolean pResult,String pMsg,Object...pParams){
        if(!pResult){
            ValidData.error(pMsg,pParams);
        }
    }

}
