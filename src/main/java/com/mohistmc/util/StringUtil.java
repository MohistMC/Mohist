package com.mohistmc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class StringUtil{

    public static boolean isEmpty(String pStr){
        return pStr==null||(pStr.isEmpty());
    }

    public static boolean isNotEmpty(String pStr){
        return !StringUtil.isEmpty(pStr);
    }

    public static boolean isBlank(String pStr){
        if(StringUtil.isEmpty(pStr)) {
            return true;
        }

        char[] tContent=pStr.toCharArray();
        for (char c : tContent) {
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String pStr){
        return !StringUtil.isBlank(pStr);
    }

    public static String trim(String pStr,char...pChars){
        if(StringUtil.isEmpty(pStr)||pChars.length==0) {
            return pStr;
        }

        char[] tContent=pStr.toCharArray();
        TreeSet<Character> tExclude=new TreeSet<Character>();
        for(char sC : pChars) {
            tExclude.add(sC);
        }
        int start,end;
        for(start=0;start<pChars.length&&tExclude.contains(tContent[start]);start++) {
            ;
        }
        for(end=tContent.length-1;end>=start&&tExclude.contains(tContent[end]);end--) {
            ;
        }
        end++;
        if(end<=start) {
            return "";
        } else {
            return new String(tContent,start,end-start);
        }
    }

    public static ArrayList<String> split(String pStr,char pSeparator){
        ArrayList<String> tSubStr=new ArrayList<String>();
        if(pStr==null||pStr.isEmpty()) {
            return tSubStr;
        }
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

    public static ArrayList<String> splitNoEmpty(String pStr,char pSeparator){
        ArrayList<String> tSubStr=new ArrayList<String>();
        if(pStr==null||pStr.isEmpty()) {
            return tSubStr;
        }
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

    public static String upperFirst(String pString){
        if(pString==null||pString.length()==0) {
            return pString;
        }

        char[] tContent=pString.toCharArray();
        if(tContent[0]>='a'&&tContent[0]<='z'){
            tContent[0]-=32;
            return String.valueOf(tContent);
        }
        return pString;
    }

    public static String lowerFirst(String pString){
        if(pString==null||pString.length()==0) {
            return pString;
        }

        char[] tContent=pString.toCharArray();
        if(tContent[0]>='A'&&tContent[0]<='Z'){
            tContent[0]+=32;
            return String.valueOf(tContent);
        }
        return pString;
    }

    public static int compareTo(String pStr1,String pStr2){
        if(pStr1==null&&pStr2==null) {
            return 0;
        }
        if(pStr1==null){
            return -1;
        }else if(pStr2==null){
            return 1;
        }else{
            return pStr1.compareTo(pStr2);
        }
    }

    public static Collection<String> getSamePrefixIgnoreCase(Collection<String> pStrs,String pPrefix,boolean pIgnoreSame){
        return StringUtil.getSamePrefix(pStrs,pPrefix,true,pIgnoreSame);
    }

    public static Collection<String> getSamePrefix(Collection<String> pStrs,String pPrefix,boolean pIgnoreCase,boolean pIgnoreSame){
        ArrayList<String> tFound=new ArrayList<String>();
        if(pStrs==null||pStrs.isEmpty()) {
            return tFound;
        }
        if(pPrefix==null||pPrefix.isEmpty()) {
            return pStrs;
        }
        if(pIgnoreCase){
            pPrefix=pPrefix.toLowerCase();
            for(String sStr : pStrs){
                if(sStr==null) {
                    continue;
                }
                String lowsStr=sStr.toLowerCase();
                if(lowsStr.startsWith(pPrefix)&&(!pIgnoreSame||lowsStr.length()!=pPrefix.length())) {
                    tFound.add(sStr);
                }
            }
        }else{
            for(String sStr : pStrs){
                if(sStr==null) {
                    continue;
                }
                if(sStr.startsWith(pPrefix)&&(!pIgnoreSame||sStr.length()!=pPrefix.length())) {
                    tFound.add(sStr);
                }
            }
        }
        return tFound;
    }

    public static boolean containsIgnoreCase(Collection<String> pStrs,String pTarget){
        if(pStrs==null) {
            return false;
        }
        if(pTarget==null) {
            return pStrs.contains(pTarget);
        }
        return StringUtil.getIgnoreCase(pStrs,pTarget)!=null;
    }

    public static String getIgnoreCase(Collection<String> pStrs,String pTarget){
        if(pStrs==null||pStrs.isEmpty()) {
            return null;
        }
        if(pTarget==null) {
            return null;
        }
        for(String sStr : pStrs) {
            if(sStr!=null&&sStr.equalsIgnoreCase(pTarget)) {
                return sStr;
            }
        }
        return null;
    }

    public static ArrayList<String> addSamePreifx(Collection<String> pList,String pPrefix){
        if(pList==null||pList.isEmpty()) {
            return new ArrayList<String>(0);
        }
        if(pPrefix==null) {
            pPrefix="";
        }
        ArrayList<String> newList=new ArrayList<String>(pList.size());
        for(String sStr : pList){
            if(sStr==null) {
                newList.add(pPrefix);
            } else {
                newList.add(pPrefix+sStr);
            }
        }
        return newList;
    }

    public static String abbreviateMiddle(String pContent,String pMiddle,int pLength){
        ValidData.valid(pLength>4,"Length too short");

        if(pContent.length()<=pLength){
            return pContent;
        }

        int tNewContentLen=pLength-3;
        return pContent.substring(0,(tNewContentLen+1)/2)+pMiddle+pContent.substring(pContent.length()-tNewContentLen/2);
    }

    public static String abbreviateTail(String pContent,String pTail,int pLength){
        ValidData.valid(pLength>4,"Length too short");

        if(pContent.length()<=pLength){
            return pContent;
        }

        return pContent.substring(0,pLength-3)+pTail;
    }

    public static <T> String toString(Collection<T> pCol,String pSeparator){
        if(pCol.isEmpty()) {
            return "";
        }

        Iterator<T> tIt=pCol.iterator();
        StringBuilder tSBuilde=new StringBuilder(tIt.next().toString());
        while(tIt.hasNext()){
            tSBuilde.append(pSeparator).append(tIt.next().toString());
        }

        return tSBuilde.toString();
    }

    public static boolean existPrefix(Collection<String> pCol,String pPrefix){
        for(String sStr : pCol){
            if(sStr!=null&&pPrefix.startsWith(sStr)) {
                return true;
            }
        }
        return false;
    }

    public static String fixToPrintable(String pStr){
        if(StringUtil.isEmpty(pStr)) {
            return "";
        }
        StringBuilder tSBuilder=new StringBuilder();
        int tSize=pStr.length();
        for(int i=0;i<tSize;i++){
            char tChar=pStr.charAt(i);
            if(ToolKit.isPrintable(tChar)) {
                tSBuilder.append(tChar);
            }
        }
        
        return tSBuilder.toString();
    }

}
