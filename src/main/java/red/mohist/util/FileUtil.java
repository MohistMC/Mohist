package red.mohist.util;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil{

    /**
     * 删除一个文件,如果文件是文件夹,那么会删除这个文件夹中的所有内容
     * 
     * @param pFile
     *            文件
     * @throws IOException
     */
    public static void deleteFile(File pFile){
        if(pFile==null||!pFile.exists()) {
            return;
        }
        if(pFile.isDirectory()) {
            FileUtil.clearDir(pFile);
        }
        if(!pFile.delete()){
            System.gc();
            pFile.delete();
        }
    }

    /**
     * 清空一个文件夹
     * 
     * @param pDir
     *            文件夹
     */
    public static void clearDir(File pDir){
        if(!pDir.isDirectory()) {
            return;
        }

        File[] tSubFiles=pDir.listFiles();
        if(tSubFiles!=null){
            for(File sChildFile : tSubFiles){
                FileUtil.deleteFile(sChildFile);
            }
        }
    }

    /**
     * 创建一个新文件
     *
     * @param pFile
     *            要创建的文件
     * @param pReplace
     *            是否替换已经存在的文件
     * @throws IOException
     */
    public static void createNewFile(File pFile,boolean pReplace) throws IOException{
        if(pFile==null) {
            return;
        }
        if(pFile.isFile()){
            if(!pReplace){
                return;
            }else{
                FileUtil.deleteFile(pFile);
            }
        }

        pFile=pFile.getAbsoluteFile();
        File tParent=pFile.getParentFile();
        if(!tParent.isDirectory()){
            if(!tParent.mkdirs()){
                throw new IOException("File '"+pFile+"' could not be created");
            }
        }
        pFile.createNewFile();
    }

    /**
     * 使用给定的文件打开输出流
     * <p>
     * 如果文件不存在,将会自动创建
     * </p>
     * 
     * @param pFile
     *            指定的文件
     * @return 创建的输出流
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(File pFile) throws IOException{
        return FileUtil.openOutputStream(pFile,true);
    }

    /**
     * 从给定的文件打开输出流
     * <p>
     * 如果文件不存在,将会自动创建
     * </p>
     * 
     * @param pFile
     *            指定的文件
     * @param pAppend
     *            是否以追加模式打开
     * @return 创建的输出流
     * @throws IOException
     *             可能发生的异常
     */
    public static FileOutputStream openOutputStream(File pFile,boolean pAppend) throws IOException{
        if(!pFile.isFile()){
            FileUtil.createNewFile(pFile,!pAppend);
        }
        return new FileOutputStream(pFile,pAppend);
    }

    /**
     * 使用指定文件创建一个输入流
     * 
     * @param pFile
     *            要打开的文件
     * @return 创建的输入流
     * @throws IOException
     *             创建输入流时发生错误
     */
    public static FileInputStream openInputStream(File pFile) throws IOException{
        if(!pFile.isFile()){
            FileUtil.createNewFile(pFile,false);
        }
        return new FileInputStream(pFile);
    }

    /**
     * 将文件内容全部读取出来,并使用指定编码转换为String
     * 
     * @param pFile
     *            要读取的文件
     * @param pEncoding
     *            使用的转换编码
     * @return 文件内容
     * @throws IOException
     *             打开文件或读取数据时发生错误
     */
    public static String readContent(File pFile,String pEncoding) throws IOException{
        InputStream tIPStream=null;
        try{
            tIPStream=openInputStream(pFile);
            return IOUtil.readContent(tIPStream,pEncoding);
        }finally{
            IOUtil.closeStream(tIPStream);
        }
    }

    /**
     * 将文件内容全部读取出来
     * 
     * @param pFile
     *            要读取的文件
     * @return 文件内容
     * @throws IOException
     *             打开文件或读取数据时发生错误
     */
    public static byte[] readData(File pFile) throws IOException{
        InputStream tIPStream=null;
        try{
            tIPStream=openInputStream(pFile);
            return IOUtil.readData(tIPStream);
        }finally{
            IOUtil.closeStream(tIPStream);
        }
    }

    /**
     * 将文件内容全部读取出来
     * 
     * @param pFile
     *            要读取的文件
     * @param pData
     *            写入的数据
     * @throws IOException
     *             打开文件或读取数据时发生错误
     */
    public static void writeData(File pFile,byte[] pData) throws IOException{
        FileUtil.writeData(pFile,pData,0,pData.length);
    }

    /**
     * 将文件内容全部读取出来
     * 
     * @param pFile
     *            要读取的文件
     * @param pData
     *            写入的数据
     * @param pOffest
     *            数据偏移量
     * @param pLength
     *            写入的数据长度
     * @throws IOException
     *             打开文件或读取数据时发生错误
     */
    public static void writeData(File pFile,byte[] pData,int pOffest,int pLength) throws IOException{
        OutputStream tOStream=null;
        try{
            tOStream=openOutputStream(pFile,false);
            tOStream.write(pData,pOffest,pLength);
        }finally{
            IOUtil.closeStream(tOStream);
        }
    }

    public static void copyFile(File srcFile,File destFile) throws IOException{
        FileUtil.copyFile(srcFile,destFile,true);
    }

    /**
     * 复制文件到新位置
     * <p>
     * 如果目标文件或文件夹不存在,将会自动创建<br>
     * 如果目标文件存在,将会覆盖
     * </p>
     * 
     * @param pSourceFile
     *            要被复制的文件
     * @param pDestFile
     *            复制到的新文件
     * @param pCopyFileInfo
     *            是否设置复制后的文件的修改日期与源文件相同
     * @throws IOException
     *             源文件不存在,创建新文件时发生错误,读取数据时发生错误
     */
    public static void copyFile(File pSourceFile,File pDestFile,boolean pCopyFileInfo) throws IOException{
        if(pSourceFile.getCanonicalPath().equals(pDestFile.getCanonicalPath())) {
            return;
        }

        FileInputStream tIPStream=null;
        FileOutputStream tOPStream=null;
        try{
            tIPStream=new FileInputStream(pSourceFile);
            tOPStream=FileUtil.openOutputStream(pDestFile,false);
            IOUtil.copy(tIPStream,tOPStream);
        }finally{
            IOUtil.closeStream(tIPStream,tOPStream);
        }
        if(pCopyFileInfo){
            pDestFile.setLastModified(Math.max(0,pSourceFile.lastModified()));
        }
    }

    /**
     * 压缩文件夹内的文件夹和文件
     * 
     * @param pZOStream
     *            压缩输出流
     * @param pZipDir
     *            要压缩的文件夹
     * @param pIncludeDir
     *            是否将压缩目录本身也压缩进流
     * @throws IOException
     */
    public static void zipFileAndDir(ZipOutputStream pZOStream,File pZipDir,boolean pIncludeDir) throws IOException{
        FileUtil.zipFileAndDir(pZOStream,pZipDir,null,pIncludeDir);
    }

    /**
     * 压缩文件夹内的文件夹和文件
     * 
     * @param pZOStream
     *            压缩输出流
     * @param pZipDir
     *            要压缩的文件夹
     * @param pFilter
     *            文件过滤器,哪些文件不进行压缩,可以为null
     * @param pIncludeDir
     *            是否将压缩目录本身也压缩进流
     * @throws IOException
     */
    public static void zipFileAndDir(ZipOutputStream pZOStream,File pZipDir,FileFilter pFilter,boolean pIncludeDir) throws IOException{
        if(pIncludeDir){
            FileUtil.zipFileAndDir(pZOStream,pZipDir.getName(),pFilter,pZipDir);
        }else{
            FileUtil.zipFileAndDir(pZOStream,"",pFilter,pZipDir);
        }
    }

    /**
     * 压缩文件夹的内的文件到Zip流
     * 
     * @param pZOStream
     *            zip流
     * @param pEntryPrefix
     *            ZipEntry前缀,递归时使用,默认请设置为空
     * @param pZipDir
     *            压缩的文件夹
     */
    private static void zipFileAndDir(ZipOutputStream pZOStream,String pEntryPrefix,FileFilter pFilter,File pZipDir) throws IOException{
        File[] tListFiles=pZipDir.listFiles();
        if(tListFiles==null||tListFiles.length==0) {
            return;
        }
        if(StringUtil.isEmpty(pEntryPrefix)){
            pEntryPrefix="";
        }else if(!pEntryPrefix.endsWith(File.separator)){
            pEntryPrefix+=File.separator;
        }

        FileInputStream tFIStream=null;
        for(File sChildFile : tListFiles){
            if(pFilter!=null&&!pFilter.accept(sChildFile)) {
                continue;
            }
            try{
                if(sChildFile.isDirectory()){
                    pZOStream.putNextEntry(new ZipEntry(pEntryPrefix+sChildFile.getName()+File.separator));
                    FileUtil.zipFileAndDir(pZOStream,pEntryPrefix+sChildFile.getName(),pFilter,sChildFile);
                }else{
                    pZOStream.putNextEntry(new ZipEntry(pEntryPrefix+sChildFile.getName()));
                    tFIStream=new FileInputStream(sChildFile);
                    IOUtil.copy(tFIStream,pZOStream);
                    tFIStream.close();
                    tFIStream=null;
                }
            }finally{
                IOUtil.closeStream(tFIStream);
            }
        }
    }

}
