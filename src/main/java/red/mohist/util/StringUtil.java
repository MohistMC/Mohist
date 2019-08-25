package red.mohist.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class StringUtil{

    /**
     * 判断字符串是否为Empty,包括null情况
     * 
     * @param pStr
     *            字符串
     * @return 是否为Empty
     */
    public static boolean isEmpty(String pStr){
        return pStr==null||(pStr.isEmpty());
    }

    /**
     * 判断字符串是否不为Empty,包括null也为empty
     * 
     * @param pStr
     *            字符串
     * @return 是否不为Empty
     */
    public static boolean isNotEmpty(String pStr){
        return !StringUtil.isEmpty(pStr);
    }

    /**
     * 判断字符串是否全部由{@link Character#isWhitespace(char)}定义的空格组成
     * 
     * @param pStr
     *            字符串
     * @return 是否全是空白字符
     */
    public static boolean isBlank(String pStr){
        if(StringUtil.isEmpty(pStr))
            return true;

        char[] tContent=pStr.toCharArray();
        for(int i=0;i<tContent.length;i++){
            if(!Character.isWhitespace(tContent[i]))
                return false;
        }
        return true;
    }

    /**
     * 参见{@link StringUtil#isBlank(String)}
     * 
     * @param pStr
     *            字符串
     * @return 是否不是全部都是空白字符
     */
    public static boolean isNotBlank(String pStr){
        return !StringUtil.isBlank(pStr);
    }

    /**
     * 去掉字符串两边的指定的字符
     * 
     * @param pStr
     *            要操作的字符串
     * @param pChars
     *            要去掉的字符
     * @return 去掉指定字符后的字符串
     */
    public static String trim(String pStr,char...pChars){
        if(StringUtil.isEmpty(pStr)||pChars.length==0)
            return pStr;

        char[] tContent=pStr.toCharArray();
        TreeSet<Character> tExclude=new TreeSet<>();
        for(char sC : pChars)
            tExclude.add(sC);
        int start,end;
        for(start=0;start<pChars.length&&tExclude.contains(tContent[start]);start++);
        for(end=tContent.length-1;end>=start&&tExclude.contains(tContent[end]);end--);
        end++;
        if(end<=start)
            return "";
        else return new String(tContent,start,end-start);
    }

    /**
     * 使用指定字符分割字符串
     * 
     * @param pStr
     *            要分割的字符串
     * @param pSeparator
     *            用于分割的字符
     * @return 被分割后的字符
     */
    public static ArrayList<String> split(String pStr,char pSeparator){
        ArrayList<String> tSubStr=new ArrayList<>();
        if(pStr==null||pStr.isEmpty())
            return tSubStr;
        char[] tContent=pStr.toCharArray();
        int tLastIndex=-1,sIndex=0;
        for(;sIndex<tContent.length;sIndex++){
            if(tContent[sIndex]==pSeparator){
                if(sIndex>tLastIndex){
                    tSubStr.add(new String(tContent,tLastIndex+1,sIndex-tLastIndex-1));
                }
                tLastIndex=sIndex;
            }
        }
        if(tLastIndex<tContent.length-1){
            tSubStr.add(new String(tContent,tLastIndex+1,tContent.length-tLastIndex-1));
        }
        return tSubStr;
    }

    /**
     * 使用指定字符分割字符串,并且分割结果去掉空字符串
     * 
     * @param pStr
     *            要分割的字符串
     * @param pSeparator
     *            用于分割的字符
     * @return 被分割后的不包括空字符的结果
     */
    public static ArrayList<String> splitNoEmpty(String pStr,char pSeparator){
        ArrayList<String> tSubStr=new ArrayList<>();
        if(pStr==null||pStr.isEmpty())
            return tSubStr;
        char[] tContent=pStr.toCharArray();
        int tLastIndex=-1,sIndex=0;
        for(;sIndex<tContent.length;sIndex++){
            if(tContent[sIndex]==pSeparator){
                if(sIndex>tLastIndex+1){
                    tSubStr.add(new String(tContent,tLastIndex+1,sIndex-tLastIndex-1));
                }
                tLastIndex=sIndex;
            }
        }
        if(tLastIndex<tContent.length-1){
            tSubStr.add(new String(tContent,tLastIndex+1,tContent.length-tLastIndex-1));
        }
        return tSubStr;
    }

    /**
     * 将字符串的首字母大写
     * 
     * @param pString
     *            字符串
     * @return 首字母大写的字符串
     */
    public static String upperFirst(String pString){
        if(pString==null||pString.length()==0)
            return pString;

        char[] tContent=pString.toCharArray();
        if(tContent[0]>='a'&&tContent[0]<='z'){
            tContent[0]-=32;
            return String.valueOf(tContent);
        }
        return pString;
    }

    /**
     * 将字符串的首字母小写写
     * 
     * @param pString
     *            字符串
     * @return 首字母大写的字符串
     */
    public static String lowerFirst(String pString){
        if(pString==null||pString.length()==0)
            return pString;

        char[] tContent=pString.toCharArray();
        if(tContent[0]>='A'&&tContent[0]<='Z'){
            tContent[0]+=32;
            return String.valueOf(tContent);
        }
        return pString;
    }

    public static int compareTo(String pStr1,String pStr2){
        if(pStr1==null&&pStr2==null)
            return 0;
        if(pStr1==null){
            return -1;
        }else if(pStr2==null){
            return 1;
        }else{
            return pStr1.compareTo(pStr2);
        }
    }

    /**
     * 获取忽略大小写的指定前缀的字符串
     * <p>
     * 如果指定的前缀为空时将返回全部的目标
     * </p>
     * 
     * @param pStrs
     *            搜索目标,不能为null
     * @param pPrefix
     *            指定的前缀,不能为null
     * @param pIgnoreSame
     *            是否忽略相同的字符串
     * @return 符合添加的字符串
     */
    public static Collection<String> getSamePrefixIgnoreCase(Collection<String> pStrs,String pPrefix,boolean pIgnoreSame){
        return StringUtil.getSamePrefix(pStrs,pPrefix,true,pIgnoreSame);
    }

    /**
     * 获取指定前缀的字符串
     * <p>
     * 如果指定的前缀为空时将返回全部的目标
     * </p>
     * 
     * @param pStrs
     *            搜索目标,不能为null
     * @param pPrefix
     *            指定的前缀,不能为null
     * @param pIgnoreCase
     *            是否忽略大小写
     * @param pIgnoreSame
     *            是否忽略相同的字符串
     * @return 符合添加的字符串
     */
    public static Collection<String> getSamePrefix(Collection<String> pStrs,String pPrefix,boolean pIgnoreCase,boolean pIgnoreSame){
        ArrayList<String> tFound=new ArrayList<>();
        if(pStrs==null||pStrs.isEmpty())
            return tFound;
        if(pPrefix==null||pPrefix.isEmpty())
            return pStrs;
        if(pIgnoreCase){
            pPrefix=pPrefix.toLowerCase();
            for(String sStr : pStrs){
                if(sStr==null)
                    continue;
                String lowsStr=sStr.toLowerCase();
                if(lowsStr.startsWith(pPrefix)&&(!pIgnoreSame||lowsStr.length()!=pPrefix.length()))
                    tFound.add(sStr);
            }
        }else{
            for(String sStr : pStrs){
                if(sStr==null)
                    continue;
                if(sStr.startsWith(pPrefix)&&(!pIgnoreSame||sStr.length()!=pPrefix.length()))
                    tFound.add(sStr);
            }
        }
        return tFound;
    }

    /**
     * 是否存在忽略大小的指定字符串
     * 
     * @param pStrs
     *            目标位置,不能为null
     * @param pTarget
     *            要查找的字符串,可以为null
     * @return 是否存在
     */
    public static boolean containsIgnoreCase(Collection<String> pStrs,String pTarget){
        if(pStrs==null)
            return false;
        if(pTarget==null)
            return pStrs.contains(pTarget);
        return StringUtil.getIgnoreCase(pStrs,pTarget)!=null;
    }

    /**
     * 获取忽略大小的指定字符串
     * 
     * @param pStrs
     *            目标位置,不能为null
     * @param pTarget
     *            要查找的字符串,不能为null
     * @return 查找到的字符串或者null
     */
    public static String getIgnoreCase(Collection<String> pStrs,String pTarget){
        if(pStrs==null||pStrs.isEmpty())
            return null;
        if(pTarget==null)
            return null;
        for(String sStr : pStrs)
            if(sStr!=null&&sStr.equalsIgnoreCase(pTarget))
                return sStr;
        return null;
    }

    /**
     * 为所有字符串添加相同的前缀 ,adcd+null=abcd
     * 
     * @param pList
     *            添加列表
     * @param pPrefix
     *            添加的前缀
     * @return 添加了前缀的字符串
     */
    public static ArrayList<String> addSamePreifx(Collection<String> pList,String pPrefix){
        if(pList==null||pList.isEmpty())
            return new ArrayList<String>(0);
        if(pPrefix==null)
            pPrefix="";
        ArrayList<String> newList=new ArrayList<>(pList.size());
        for(String sStr : pList){
            if(sStr==null)
                newList.add(pPrefix);
            else newList.add(pPrefix+sStr);
        }
        return newList;
    }

    /**
     * 从中间缩短字符串到指定长度
     * 
     * @param pContent
     *            字符串
     * @param pMiddle
     *            省略内容替代符
     * @param pLength
     *            字符串最长长度
     * @return 缩短后的字符串
     */
    public static String abbreviateMiddle(String pContent,String pMiddle,int pLength){
        ValidData.valid(pLength>4,"Length too short");

        if(pContent.length()<=pLength){
            return pContent;
        }

        int tNewContentLen=pLength-3;
        return pContent.substring(0,(tNewContentLen+1)/2)+pMiddle+pContent.substring(pContent.length()-tNewContentLen/2);
    }

    /**
     * 从尾部缩短字符串到指定长度
     * 
     * @param pContent
     *            字符串
     * @param pTail
     *            省略内容替代符
     * @param pLength
     *            字符串最长长度
     * @return 缩短后的字符串
     */
    public static String abbreviateTail(String pContent,String pTail,int pLength){
        ValidData.valid(pLength>4,"Length too short");

        if(pContent.length()<=pLength){
            return pContent;
        }

        return pContent.substring(0,pLength-3)+pTail;
    }

    /**
     * 集合转换成String
     * 
     * @param pCol
     *            集合
     * @param pSeparator
     *            每个元素之间的分隔字符串
     * @return 字符串
     */
    public static <T> String toString(Collection<T> pCol,String pSeparator){
        if(pCol.isEmpty()) return "";

        Iterator<T> tIt=pCol.iterator();
        StringBuilder tSBuilde=new StringBuilder(tIt.next().toString());
        while(tIt.hasNext()){
            tSBuilde.append(pSeparator).append(tIt.next().toString());
        }

        return tSBuilde.toString();
    }

    public static boolean existPrefix(Collection<String> pCol,String pPrefix){
        for(String sStr : pCol){
            if(sStr!=null&&pPrefix.startsWith(sStr)) return true;
        }
        return false;
    }
    
    /**
     * 移除字符串中的不可打印字符
     * @param pStr
     * @return
     */
    public static String fixToPrintable(String pStr){
        if(StringUtil.isEmpty(pStr)) return "";
        StringBuilder tSBuilder=new StringBuilder();
        int tSize=pStr.length();
        for(int i=0;i<tSize;i++){
            char tChar=pStr.charAt(i);
            if(ToolKit.isPrintable(tChar))
                tSBuilder.append(tChar);
        }
        
        return tSBuilder.toString();
    }

}
