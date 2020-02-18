package red.mohist.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    /**
     * @param pFile
     * @throws IOException
     */
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

    /**
     * @param pDir
     */
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

    /**
     * @param pFile
     * @param pReplace
     * @throws IOException
     */
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

    /**
     * @param pFile
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(File pFile) throws IOException {
        return FileUtil.openOutputStream(pFile, true);
    }

    /**
     * @param pFile
     * @param pAppend
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(File pFile, boolean pAppend) throws IOException {
        if (!pFile.isFile()) {
            FileUtil.createNewFile(pFile, !pAppend);
        }
        return new FileOutputStream(pFile, pAppend);
    }

    /**
     * @param pFile
     * @return
     * @throws IOException
     */
    public static FileInputStream openInputStream(File pFile) throws IOException {
        if (!pFile.isFile()) {
            FileUtil.createNewFile(pFile, false);
        }
        return new FileInputStream(pFile);
    }

    /**
     * @param pFile
     * @return
     * @throws IOException
     */
    public static String readContent(File pFile) throws IOException {
        InputStream tIPStream = null;
        try {
            tIPStream = openInputStream(pFile);
            return IOUtil.readContent(tIPStream);
        } finally {
            IOUtil.closeStream(tIPStream);
        }
    }

    /**
     * @param pFile
     * @throws IOException
     */
    public static byte[] readData(File pFile) throws IOException {
        InputStream tIPStream = null;
        try {
            tIPStream = openInputStream(pFile);
            return IOUtil.readData(tIPStream);
        } finally {
            IOUtil.closeStream(tIPStream);
        }
    }

    /**
     * @param pFile
     * @param pData
     * @throws IOException
     */
    public static void writeData(File pFile, byte[] pData) throws IOException {
        FileUtil.writeData(pFile, pData, 0, pData.length);
    }

    /**
     * @param pFile
     * @param pData
     * @param pOffest
     * @param pLength
     * @throws IOException
     */
    public static void writeData(File pFile, byte[] pData, int pOffest, int pLength) throws IOException {
        OutputStream tOStream = null;
        try {
            tOStream = openOutputStream(pFile, false);
            tOStream.write(pData, pOffest, pLength);
        } finally {
            IOUtil.closeStream(tOStream);
        }
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        FileUtil.copyFile(srcFile, destFile, true);
    }

    /**
     * @param pSourceFile
     * @param pDestFile
     * @param pCopyFileInfo
     * @throws IOException
     */
    public static void copyFile(File pSourceFile, File pDestFile, boolean pCopyFileInfo) throws IOException {
        if (pSourceFile.getCanonicalPath().equals(pDestFile.getCanonicalPath())) {
            return;
        }

        FileInputStream tIPStream = null;
        FileOutputStream tOPStream = null;
        try {
            tIPStream = new FileInputStream(pSourceFile);
            tOPStream = FileUtil.openOutputStream(pDestFile, false);
            IOUtil.copy(tIPStream, tOPStream);
        } finally {
            IOUtil.closeStream(tIPStream, tOPStream);
        }
        if (pCopyFileInfo) {
            pDestFile.setLastModified(Math.max(0, pSourceFile.lastModified()));
        }
    }
}
