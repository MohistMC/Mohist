package red.mohist.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ToolKit{

    public static final Charset UTF_8= StandardCharsets.UTF_8;

    protected static final String systemLineSeparator=System.getProperty("line.separator","\r\n");

    public static void printStackTrace(){
        StackTraceElement[] tElements=Thread.currentThread().getStackTrace();
        for(int i=2;i<tElements.length;i++){
            System.out.println(tElements[i]);
        }
    }

    public static <T extends Enum<T>> T getElement(T[] pValues,String pEnumName){
        for(T sT : pValues){
            if(sT.name().equalsIgnoreCase(pEnumName)) {
                return sT;
            }
        }
        return null;
    }

    public static byte[] randomByteArray(int pLength){
        if(pLength<=0) {
            throw new IllegalArgumentException("The number must be positive ("+pLength+")");
        }

        byte[] tData=new byte[pLength];
        Random tRandom=new Random(System.nanoTime());
        for(int i=0;i<pLength;i++){
            tData[i]=(byte)tRandom.nextInt(256);
        }
        return tData;
    }

    public static int between(int pMin,int pMax,int pValue){
        return Math.max(pMin,Math.min(pMax,pValue));
    }

    public static int paseIntOrDefault(String pStr,int pDefValue){
        return paseIntOrDefault(pStr,10,pDefValue);
    }

    public static int paseIntOrDefault(String pStr,int pRadix,int pDefValue){
        if(StringUtil.isEmpty(pStr)) {
            return pDefValue;
        }

        try{
            return Integer.parseInt(pStr,pRadix);
        }catch(NumberFormatException exp){
            int tMaxCharValue=pRadix+'0';
            StringBuilder tSB=new StringBuilder();
            pStr=pStr.toLowerCase();
            for(int i=0;i<pStr.length();i++){
                char c=pStr.charAt(i);
                if(c>='0'&&c<tMaxCharValue){
                    tSB.append(c);
                }
            }
            try{
                return Integer.parseInt(tSB.toString(),pRadix);
            }catch(NumberFormatException expc){
                return pDefValue;
            }
        }
    }

    public static int compareVersion(String pV1,String pV2){
        String[] pVs1=pV1.split("\\.");
        String[] pVs2=pV2.split("\\.");
        for(int i=0;i<pVs1.length;i++){
            if(pVs2.length<=i) {
                return 0;
            }
            int t=pVs1[i].length()-pVs2[i].length();
            if(t!=0) {
                return t;
            }
            t=pVs1[i].compareTo(pVs2[i]);
            if(t!=0) {
                return t;
            }
        }
        return pVs1.length-pVs2.length;
    }

    public static boolean isPrintable(char pChar){
        return (pChar>='\u0020'&&pChar<='\u007E')||pChar=='\n'||pChar=='\r'||pChar=='\t'
                ||pChar=='\u0085'||(pChar>='\u00A0'&&pChar<='\uD7FF')
                ||(pChar>='\uE000'&&pChar<='\uFFFD');

    }
}
