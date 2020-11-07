package com.mohistmc.util;


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
     * 
     * @param pFile
     *
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
     * 
     * @param pDir
     *
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
     *
     * @param pFile
     * @param pReplace
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
     * 
     * @param pFile
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(File pFile) throws IOException{
        return FileUtil.openOutputStream(pFile,true);
    }

    /**
     * 
     * @param pFile
     * @param pAppend
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(File pFile,boolean pAppend) throws IOException{
        if(!pFile.isFile()){
            FileUtil.createNewFile(pFile,!pAppend);
        }
        return new FileOutputStream(pFile,pAppend);
    }

    /**
     * 
     * @param pFile
     * @return
     * @throws IOException
     */
    public static FileInputStream openInputStream(File pFile) throws IOException{
        if(!pFile.isFile()){
            FileUtil.createNewFile(pFile,false);
        }
        return new FileInputStream(pFile);
    }

    /**
     * 
     * @param pFile
     * @param pEncoding
     * @return
     * @throws IOException
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
     * 
     * @param pFile
     * @throws IOException
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
     * 
     * @param pFile
     * @param pData
     * @throws IOException
     */
    public static void writeData(File pFile,byte[] pData) throws IOException{
        FileUtil.writeData(pFile,pData,0,pData.length);
    }

    /**
     * 
     * @param pFile
     * @param pData
     * @param pOffest
     * @param pLength
     * @throws IOException
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
     * @param pSourceFile
     * @param pDestFile
     * @param pCopyFileInfo
     * @throws IOException
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
     * 
     * @param pZOStream
     * @param pZipDir
     * @param pIncludeDir
     * @throws IOException
     */
    public static void zipFileAndDir(ZipOutputStream pZOStream,File pZipDir,boolean pIncludeDir) throws IOException{
        FileUtil.zipFileAndDir(pZOStream,pZipDir,null,pIncludeDir);
    }

    /**
     * 
     * @param pZOStream
     * @param pZipDir
     * @param pFilter
     * @param pIncludeDir
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
     * 
     * @param pZOStream
     * @param pEntryPrefix
     * @param pZipDir
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
