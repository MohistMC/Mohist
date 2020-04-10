package red.mohist.util;

import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import red.mohist.util.i18n.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EulaUtil {
    public static void writeInfos() throws IOException {
        new File("eula.txt").createNewFile();
        BufferedWriter b = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("eula.txt"), "UTF-8"));
        b.write(Message.getFormatString("eula.text", new Object[]{"https://account.mojang.com/documents/minecraft_eula"}) + "\n" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\neula=true");
        b.close();
    }

    public static boolean hasAcceptedEULA() throws IOException {
        if(new File("eula.txt").exists())
            return FileUtils.readFileToString(new File("eula.txt"), StandardCharsets.UTF_8).contains("eula=true");
        return false;
    }
}