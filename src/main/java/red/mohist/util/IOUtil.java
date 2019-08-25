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
     * 关闭一个流
     * 
     * @param pSteams
     *            流
     * @return 是否无报错的关闭了
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
     * 关闭一个连接
     * 
     * @param pConns
     *            连接
     * @return 连接是否无报错的关闭了
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
     * 复制流中的数据
     * <p>
     * 数据复制完毕后,函数不会主动关闭输入输出流
     * </p>
     * 
     * @param pIPStream
     *            输入流
     * @param pOPStream
     *            输出流
     * @return 复制的字节数
     * @throws IOException
     *             读入或写入数据时发生IO异常
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
     * 将流中的内容全部读取出来,并使用指定编码转换为String
     * 
     * @param pIPStream
     *            输入流
     * @param pEncoding
     *            转换编码
     * @return 读取到的内容
     * @throws IOException
     *             读取数据时发生错误
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
     * 将流中的内容全部读取出来
     * 
     * @param pIPSReader
     *            输入流
     * @return 读取到的内容
     * @throws IOException
     *             读取数据时发生错误
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
     * 将流中的内容全部读取出来
     * 
     * @param pIStream
     *            输入流
     * @return 读取到的内容
     * @throws IOException
     *             读取数据时发生错误
     */
    public static byte[] readData(InputStream pIStream) throws IOException{
        ByteArrayOutputStream tBAOStream=new ByteArrayOutputStream();
        IOUtil.copy(pIStream,tBAOStream);
        return tBAOStream.toByteArray();
    }

}
