package red.mohist.down;

import java.io.File;
import java.util.Locale;
import red.mohist.util.i18n.Message;

public class DownloadServer implements Runnable {

    @Override
    public void run() {
        String url = "https://launcher.mojang.com/v1/objects/886945bfb2b978778c3a0288fd7fab09d315b25f/server.jar";
        Locale locale = Locale.getDefault();
        if (locale.getCountry().equals("CN") || Message.getLanguage(2).equals("CN")) {
            url = "https://pfcraft.gitee.io/mohistdown/minecraft_server.1.12.2.jar";
        }
        String fileName = "minecraft_server.1.12.2.jar";
        File Jar = new File(fileName);
        if (!Jar.exists() && !Jar.isDirectory() && !Jar.isFile() && Jar.length() != 30222121L) {
            new Download(url, fileName);
        }
    }
}
