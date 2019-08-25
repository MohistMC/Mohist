package red.mohist.down;

import red.mohist.i18n.Message;

import java.io.File;
import java.util.Locale;

public class DownloadServer implements Runnable {

    @Override
    public void run() {
        String url = "https://launcher.mojang.com/v1/objects/886945bfb2b978778c3a0288fd7fab09d315b25f/server.jar";
        Locale locale = Locale.getDefault();
        if (locale.getCountry().equals("CN")) {
            url = "https://pfcraft.gitee.io/mohistdown/minecraft_server.1.12.2.jar";
        }
        String fileName = "minecraft_server.1.12.2.jar";
        File Jar = new File(fileName);
        if (!Jar.exists() && !Jar.isDirectory() && !Jar.isFile() && Jar.length() != new Long(30222121)) {
            new Download(url, fileName);
            System.out.println(Message.getFormatString(Message.Dw_Ok, new Object[]{"minecraft_server.1.12.2.jar"}));
        }
    }
}
