package red.mohist.down;

import red.mohist.i18n.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DownloadLibraries implements Runnable {

    @Override
    public void run() {
        String url = "https://github.com/PFCraft/Mohist/releases/download/libraries-1.2/libraries-1.2.zip";
        String fileName = "libraries.zip";
        Locale locale = Locale.getDefault();
        if (locale.getCountry().equals("CN")) {
            url = "https://pfcraft.gitee.io/mohistdown/libraries-1.2.zip";
        }
        new Download(url, fileName);
        System.out.println(Message.getFormatString(Message.Dw_Ok, new Object[]{fileName}));
        try {
            @SuppressWarnings("resource")
            ZipFile zip = new ZipFile(new File(fileName), Charset.forName("GBK"));
            File filepath = new File(".");
            if (!(filepath.exists())) {
                filepath.mkdirs();
            }
            System.out.println(Message.getFormatString(Message.UnZip_Start, new Object[]{fileName}));
            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream is = zip.getInputStream(entry);
                String outPath = ("." + "/" + zipEntryName).replaceAll("\\*", "/");
                File file1 = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!(file1.exists())) {
                    file1.mkdirs();
                }
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                FileOutputStream fos = new FileOutputStream(outPath);
                byte[] b = new byte[1024];
                int i;

                while ((i = is.read(b)) > 0) {
                    fos.write(b, 0, i);
                }
                is.close();
                fos.close();
            }
            System.out.println(Message.getFormatString(Message.UnZip_Ok, new Object[]{fileName}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
