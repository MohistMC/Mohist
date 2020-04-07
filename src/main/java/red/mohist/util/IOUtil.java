package red.mohist.util;

import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtil {

    public static String readContent(InputStreamReader pIPSReader) throws IOException {
        int readCount;
        char[] tBuff = new char[4096];
        StringBuilder tSB = new StringBuilder();
        while ((readCount = pIPSReader.read(tBuff)) != -1) {
            tSB.append(tBuff, 0, readCount);
        }
        pIPSReader.close();
        return tSB.toString();
    }

}