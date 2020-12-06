package com.mohistmc.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
  public static String getMd5(File path) {
    try {
      return DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(Files.readAllBytes(path.toPath()))).toLowerCase();
    } catch (NoSuchAlgorithmException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
