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

    /**
     * 对象不为null,如果null抛出异常
     * 
     * @param pObj
     *            对象
     * @param pMsg
     *            异常消息
     * @param pParams
     *            消息参数
     */
    public static void notNull(Object pObj,String pMsg,Object...pParams){
        if(pObj==null){
            ValidData.error(pMsg,pParams);
        }
    }

    /**
     * 字符串不为空,如果为空抛出异常
     * 
     * @param pStr
     *            字符串
     * @param pMsg
     *            异常消息
     * @param pParams
     *            消息参数
     */
    public static void notEmpty(String pStr,String pMsg,Object...pParams){
        if(pStr==null||pStr.isEmpty()){
            ValidData.error(pMsg,pParams);
        }
    }

    /**
     * 集合不为空,如果为空抛出异常
     * 
     * @param pColl
     *            集合
     * @param pMsg
     *            异常消息
     * @param pParams
     *            消息参数
     */
    public static void notEmpty(Collection<?> pColl,String pMsg,Object...pParams){
        if(pColl==null||pColl.isEmpty()){
            ValidData.error(pMsg,pParams);
        }
    }

    /**
     * 集合不为false,如果为false抛出异常
     * 
     * @param pResult
     *            结果
     * @param pMsg
     *            异常消息
     * @param pParams
     *            消息参数
     */
    public static void valid(boolean pResult,String pMsg,Object...pParams){
        if(!pResult){
            ValidData.error(pMsg,pParams);
        }
    }

}
