package red.mohist.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOUtil {

    public static String readContent(InputStreamReader pIPSReader) throws IOException {
        int readCount;
        char[] tBuff = new char[4096];
        StringBuilder tSB = new StringBuilder();
        while ((readCount = pIPSReader.read(tBuff)) != -1) {
            tSB.append(tBuff, 0, readCount);
        }
        return tSB.toString();
    }

}