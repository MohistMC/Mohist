package red.mohist.down;

import static it.unimi.dsi.fastutil.io.TextIO.BUFFER_SIZE;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import red.mohist.util.i18n.Message;
import red.mohist.util.FileUtil;

public class DownloadLibraries implements Runnable {

    @Override
    public void run() {
        String url = "https://github.com/Mohist-Community/Mohist/releases/download/libraries/libraries.zip";
        String fileName = "libraries.zip";
        Locale locale = Locale.getDefault();
        if (locale.getCountry().equals("CN") || Message.getLanguage(2).equals("CN")) {
            url = "https://mohist-community.gitee.io/mohistdown/libraries.zip";
        }
        new Download(url, fileName);
        File file = new File(fileName);
        unZip(file);
    }


    public static void unZip(File srcFile) throws RuntimeException {
        long start = System.currentTimeMillis();
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.entries();
            System.out.println(Message.getFormatString("file.unzip.start", new Object[]{ srcFile}));
            System.out.println(Message.getFormatString("file.unzip.now", new Object[]{ srcFile, Download.getSize(srcFile.length())}));
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    File targetFile = new File(entry.getName());
                    if(!targetFile.getParentFile().exists()){
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(Message.getFormatString("file.unzip.ok", new Object[]{(end - start)}));
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if(zipFile != null){
                try {
                    zipFile.close();
                    FileUtil.deleteFile(srcFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
