package red.mohist.down;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import red.mohist.Mohist;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.util.IOUtil;
import red.mohist.util.JarTool;
import red.mohist.util.i18n.Message;

public class Update {

    // Why use a hard core to split a String? Because I didn't actually start Mohist before this, there is no lib load, we can't use lib.
    public static void hasLatestVersion() {
        String str = "https://api.github.com/repos/Mohist-Community/Mohist/commits/1.7.10";
        String ver = "https://raw.githubusercontent.com/Mohist-Community/Mohist/1.7.10/mohist.ver";
        String dl = "https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.7.10/";
        try {
            System.out.println(Message.getString("update.check"));
            System.out.println(Message.getString("update.stopcheck"));
            URL url = new URL(str);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            String commits = IOUtil.readContent(is, "UTF-8");
            String sha = "\"sha\":\"";
            String date = "\"date\":\"";

            String s0 = commits.substring(commits.indexOf(sha));
            String s1 = s0.substring(s0.indexOf(sha) + 7);
            String s2 = s1.substring(0, 7);

            String oldver = Update.class.getPackage().getImplementationVersion();
            String time = commits.substring(commits.indexOf(date));
            String time1 = time.substring(time.indexOf(date) + 8);
            String time2 = time1.substring(0, 20);

            String newversion = MohistConfigUtil.getUrlString(ver, Mohist.VERSION);
            String oldversion = Mohist.VERSION;
            if (oldver.equals(s2)) {
                System.out.println(Message.getFormatString("update.latest", new Object[]{oldversion, s2, oldver}));
            } else {
                System.out.println(Message.getFormatString("update.old", new Object[]{oldversion, newversion, s2, time2, oldver, dl}));
            }
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCheckVersion() {
        File f = new File("mohist-config", "mohist.yml");
        return MohistConfigUtil.getBoolean(f, "check_update:");
    }

    public static boolean getLibrariesVersion() {
        String s = Mohist.LIB_VERSION;
        File lib = new File(JarTool.getJarDir() + "/libraries/libraries.ver");
        if (!lib.exists()) {
            return true;
        }
        // Get the data in lib
        String i = MohistConfigUtil.getString(lib, "version:", Mohist.LIB_VERSION);
        return !i.equals(s);
    }
}
