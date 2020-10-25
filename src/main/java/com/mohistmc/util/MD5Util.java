package com.mohistmc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mgazul
 * @date 2020/4/11 0:53
 */
public class MD5Util {

    /**
     * Get the MD5 value of this input stream
     *
     * @param is
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getMD5(InputStream is) throws NoSuchAlgorithmException, IOException {
        StringBuffer md5 = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();

        // convert the byte to hex format
        for (int i = 0; i < mdbytes.length; i++) {
            md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return md5.toString();
    }

    /**
     * Get the MD5 value of the file
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getMD5(File file) throws NoSuchAlgorithmException, IOException {
        FileInputStream fis = new FileInputStream(file);
        return getMD5(fis);
    }

    /**
     * Get the MD5 value of the specified path file
     *
     * @param path
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getMD5(String path) throws NoSuchAlgorithmException, IOException {
        FileInputStream fis = new FileInputStream(path);
        return getMD5(fis);
    }

    /**
     * Check the MD5 value of this input stream
     *
     * @param is
     * @param toBeCheckSum
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean md5CheckSum(InputStream is, String toBeCheckSum) throws NoSuchAlgorithmException, IOException {
        return getMD5(is).equals(toBeCheckSum);
    }

    /**
     * Check the MD5 value of the file
     *
     * @param file
     * @param toBeCheckSum
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean md5CheckSum(File file, String toBeCheckSum) throws NoSuchAlgorithmException, IOException {
        return getMD5(file).equals(toBeCheckSum);
    }

    /**
     * Check the MD5 value of the specified path file
     *
     * @param path
     * @param toBeCheckSum
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean md5CheckSum(String path, String toBeCheckSum) throws NoSuchAlgorithmException, IOException {
        return getMD5(path).equals(toBeCheckSum);
    }
}
