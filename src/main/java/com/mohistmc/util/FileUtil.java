package com.mohistmc.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static void deleteFile(File pFile) {
        if (pFile == null || !pFile.exists()) {
            return;
        }
        if (pFile.isDirectory()) {
            FileUtil.clearDir(pFile);
        }
        if (!pFile.delete()) {
            System.gc();
            pFile.delete();
        }
    }

    public static void clearDir(File pDir) {
        if (!pDir.isDirectory()) {
            return;
        }

        File[] tSubFiles = pDir.listFiles();
        if (tSubFiles != null) {
            for (File sChildFile : tSubFiles) {
                FileUtil.deleteFile(sChildFile);
            }
        }
    }

    public static void createNewFile(File pFile, boolean pReplace) throws IOException {
        if (pFile == null) {
            return;
        }
        if (pFile.isFile()) {
            if (!pReplace) {
                return;
            } else {
                FileUtil.deleteFile(pFile);
            }
        }

        pFile = pFile.getAbsoluteFile();
        File tParent = pFile.getParentFile();
        if (!tParent.isDirectory()) {
            if (!tParent.mkdirs()) {
                throw new IOException("File '" + pFile + "' could not be created");
            }
        }
        pFile.createNewFile();
    }

    public static FileOutputStream openOutputStream(File pFile) throws IOException {
        return FileUtil.openOutputStream(pFile, true);
    }

    public static FileOutputStream openOutputStream(File pFile, boolean pAppend) throws IOException {
        if (!pFile.isFile()) {
            FileUtil.createNewFile(pFile, !pAppend);
        }
        return new FileOutputStream(pFile, pAppend);
    }

    public static FileInputStream openInputStream(File pFile) throws IOException {
        if (!pFile.isFile()) {
            FileUtil.createNewFile(pFile, false);
        }
        return new FileInputStream(pFile);
    }

    public static String readContent(File pFile) throws IOException {
        InputStream tIPStream = null;
        try {
            tIPStream = openInputStream(pFile);
            return IOUtil.readContent(tIPStream);
        } finally {
            IOUtil.closeStream(tIPStream);
        }
    }

    public static byte[] readData(File pFile) throws IOException {
        InputStream tIPStream = null;
        try {
            tIPStream = openInputStream(pFile);
            return IOUtil.readData(tIPStream);
        } finally {
            IOUtil.closeStream(tIPStream);
        }
    }

    public static void writeData(File pFile, byte[] pData) throws IOException {
        FileUtil.writeData(pFile, pData, 0, pData.length);
    }

    public static void writeData(File pFile, byte[] pData, int pOffest, int pLength) throws IOException {
        OutputStream tOStream = null;
        try {
            tOStream = openOutputStream(pFile, false);
            tOStream.write(pData, pOffest, pLength);
        } finally {
            IOUtil.closeStream(tOStream);
        }
    }
}