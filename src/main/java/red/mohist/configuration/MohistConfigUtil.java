package red.mohist.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import red.mohist.down.Update;
import red.mohist.util.FileUtil;
import red.mohist.util.IOUtil;
import red.mohist.util.Number;
import red.mohist.util.i18n.Message;

public class MohistConfigUtil {

    public static File f = new File("mohist-config", "mohist.yml");

    public static String getString(String key, String defaultreturn) {
        try {
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains(key)){
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(": ") + 2));
                String[] ss = s1.split("\n");
                return ss[0].trim().replace("'", "").replace("\"" , "");
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
            InputStream is = conn.getInputStream();

            String s = IOUtil.readContent(is, "UTF-8");
            is.close();
            return s;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public static boolean getBoolean(String key, Boolean defaultreturn) {
        String s = getString(key, defaultreturn.toString());
        if (s.equals("false")){
            return false;
        }
        return defaultreturn;
    }

    public static int getInt(String key, String defaultreturn){
        String s = getString(key, defaultreturn);
        if (Number.isInteger(s)) {
            return Integer.parseInt(s);
        }
        return  Integer.parseInt(defaultreturn);
    }
}
