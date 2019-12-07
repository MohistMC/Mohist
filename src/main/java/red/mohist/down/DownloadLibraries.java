package red.mohist.down;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.util.FileUtil;
import red.mohist.util.HttpUtil;
import red.mohist.util.i18n.Message;

public class DownloadLibraries{

    public static final String FIND_LOCATE = "https://passport.lazercloud.com/api/v1/options/GetLocate";

    public static void run() {
        String url = null;
        String fileName = "libraries.zip";
        try {
            String locateInfo = HttpUtil.doGet(FIND_LOCATE);

            if (locateInfo !=null && locateInfo.equals("CN")) {
                System.out.println("Detected China IP, Using Gitee Mirror.");
                url = "https://mohist-community.gitee.io/mohistdown/libraries-1.7.10.zip"; //Gitee Mirror
            } else {
                url = "https://github.com/Mohist-Community/Mohist/releases/download/1.7.10bate/libraries.zip"; //Github Mirror
            }
        } catch (Exception e) {
            url = "https://github.com/Mohist-Community/Mohist/releases/download/1.7.10bate/libraries.zip"; //Github Mirror
        }
        new Download(url, fileName);
        File file = new File(fileName);
        unZip(file);
    }


    public static void unZip(File srcFile) throws RuntimeException {
        long start = System.currentTimeMillis();
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "The file indicated does not exist.");
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
                    File dir = new File(MohistConfigUtil.getMohistJarPath() + dirPath);
                    dir.mkdirs();
                } else {
                    File targetFile = new File(MohistConfigUtil.getMohistJarPath() + entry.getName());
                    if(!targetFile.getParentFile().exists()){
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[8 * 1024];
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
