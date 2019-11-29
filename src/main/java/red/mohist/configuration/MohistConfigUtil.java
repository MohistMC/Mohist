package red.mohist.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import red.mohist.Mohist;
import red.mohist.util.FileUtil;
import red.mohist.util.IOUtil;
import red.mohist.util.JarTool;
import red.mohist.util.Number;

public class MohistConfigUtil {

    public static String getString(String s, String key, String defaultreturn) {
        if (s.contains(key)) {
            String string = s.substring(s.indexOf(key));
            String s1 = (string.substring(string.indexOf(": ") + 2));
            String[] ss = s1.split("\n");
            return ss[0].trim().replace("'", "").replace("\"", "");
        }
        return defaultreturn;
    }

    public static String getString(File f, String key, String defaultreturn) {
        try {
            String s = FileUtil.readContent(f, "UTF-8");
            if (s.contains(key)) {
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(": ") + 2));
                String[] ss = s1.split("\n");
                return ss[0].trim().replace("'", "").replace("\"", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public static String getUrlString(String urlkey, String defaultreturn) {
        try {
            URL url = new URL(urlkey);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10*1000);
            conn.setReadTimeout(10*1000);
            InputStream is = conn.getInputStream();

            String s = IOUtil.readContent(is, "UTF-8");
            is.close();
            return s;

        } catch (IOException e) {
            System.out.println("");
        }
        return defaultreturn;
    }

    public static boolean getBoolean(File f, String key) {
        String s = getString(f, key, "true");
        if (s.equals("false")) {
            return false;
        }
        return true;
    }

    public static int getInt(File f, String key, String defaultreturn) {
        String s = getString(f, key, defaultreturn);
        if (Number.isInteger(s)) {
            return Integer.parseInt(s);
        }
        return Integer.parseInt(defaultreturn);
    }

    public static void copyMohistConfig() {
        try {
            File configfile = new File("mohist-config");
            if (!configfile.exists()) {
                configfile.mkdirs();

                InputStream jarin = Mohist.class.getClassLoader().getResourceAsStream("configurations/mohist.yml");
                Path currentDir = Paths.get("mohist-config", "mohist.yml");
                Files.copy(jarin, currentDir, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("File copy exception!");
        }
    }

    public static String getMohistJarPath() {
        String f = JarTool.getJarDir();
        return f + "/";
    }
}
