package com.mohistmc.util;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class MD5Util {

    public static String getMd5(File path) {
        try {
            return String.format("%032x", new BigInteger(1, MessageDigest.getInstance("MD5").digest(Files.readAllBytes(path.toPath())))).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMd5(InputStream is) {
        try {
            return String.format("%032x", new BigInteger(1, new DigestInputStream(is, MessageDigest.getInstance("MD5")).getMessageDigest().digest())).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }
}
