package com.mohistmc.util;

import com.mohistmc.util.i18n.i18n;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EulaUtil {
    private static File eula = new File("eula.txt");

    public static void writeInfos() throws IOException {
        eula.createNewFile();
        BufferedWriter b = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("eula.txt"), "UTF-8"));
        b.write(i18n.get("eula.text", "https://account.mojang.com/documents/minecraft_eula") + "\n" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\neula=true");
        b.close();
    }

    public static boolean hasAcceptedEULA() throws IOException {
        return eula.exists() && Files.readAllLines(eula.toPath()).contains("eula=true");
    }
}
