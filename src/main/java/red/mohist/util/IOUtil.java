package red.mohist.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class IOUtil{

    /**
     * 
     * @param pSteams
     * @return
     */
    public static boolean closeStream(Closeable...pSteams){
        boolean pHasError=false;
        for(Closeable sCloseable : pSteams){
            if(sCloseable!=null){
                try{
                    sCloseable.close();
                }catch(Exception exp){
                    pHasError=true;
                }
            }
        }

        return !pHasError;
    }

    /**
     * 
     * @param pConns
     * @return
     */
    public static boolean closeStream(AutoCloseable...pConns){
        boolean pHasError=false;
        for(AutoCloseable sCloseable : pConns){
            if(sCloseable!=null){
                try{
                    sCloseable.close();
                }catch(Exception exp){
                    pHasError=true;
                }
            }
        }

        return !pHasError;
    }

    /**
     * @param pIPStream
     * @param pOPStream
     * @return
     * @throws IOException
     */
    public static long copy(InputStream pIPStream,OutputStream pOPStream) throws IOException{
        int copyedCount=0,readCount=0;
        byte[] tBuff=new byte[4096];
        while((readCount=pIPStream.read(tBuff))!=-1){
            pOPStream.write(tBuff,0,readCount);
            copyedCount+=readCount;
        }
        return copyedCount;
    }

    /**
     * 
     * @param pIPStream
     * @param pEncoding
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static String readContent(InputStream pIPStream,String pEncoding) throws IOException{
        if(StringUtil.isEmpty(pEncoding)){
            return IOUtil.readContent(new InputStreamReader(pIPStream));
        }else{
            return IOUtil.readContent(new InputStreamReader(pIPStream,pEncoding));
        }
    }

    /**
     * 
     * @param pIPSReader
     * @return
     * @throws IOException
     */
    public static String readContent(InputStreamReader pIPSReader) throws IOException{
        int readCount=0;
        char[] tBuff=new char[4096];
        StringBuilder tSB=new StringBuilder();
        while((readCount=pIPSReader.read(tBuff))!=-1){
            tSB.append(tBuff,0,readCount);
        }
        return tSB.toString();
    }

    /**
     * 
     * @param pIStream
     * @return
     * @throws IOException
     */
    public static byte[] readData(InputStream pIStream) throws IOException{
        ByteArrayOutputStream tBAOStream=new ByteArrayOutputStream();
        IOUtil.copy(pIStream,tBAOStream);
        return tBAOStream.toByteArray();
    }

}
