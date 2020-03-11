package red.mohist.down;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import red.mohist.Mohist;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.util.IOUtil;
import red.mohist.util.i18n.Message;

public class Update {

    // Why use a hard core to split a String? Because I didn't actually start Mohist before this, there is no lib load, we can't use lib.
    public static void hasLatestVersion() {
        String str = "https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastBuild/";
        String ver = "https://raw.githubusercontent.com/Mohist-Community/Mohist/1.12.2/mohist.ver";
        String dl = "https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/";
        try {
            System.out.println(Message.getString("update.check"));
            System.out.println(Message.getString("update.stopcheck"));
            URL url = new URL(str);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mohist");
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                String commits = IOUtil.readContent(is);
                String sha = "/build/distributions/Mohist-";
                String date = "(Wed Mar ";

                String s0 = commits.substring(commits.indexOf(sha));
                String s1 = s0.substring(s0.indexOf(sha) + sha.length());
                String s2 = s1.substring(0, 7);

                String oldver = Update.class.getPackage().getImplementationVersion();
                String time = commits.substring(commits.indexOf(date));
                String time1 = time.substring(time.indexOf(date) + date.length());
                String time2 = time1.substring(0, 20);

                String newversion = MohistConfigUtil.getUrlString(ver, Mohist.VERSION);
                String oldversion = Mohist.VERSION;
                if (oldver.equals(s2)) {
                    System.out.println(Message.getFormatString("update.latest", new Object[]{oldversion, s2, oldver}));
                } else {
                    System.out.println(Message.getFormatString("update.old", new Object[]{oldversion, newversion, s2, time2, oldver, dl}));
                }
                is.close();
            } else {
                System.out.println("Link access failed");
            }
        } catch (IOException e) {
        }
    }

    public static boolean isCheckVersion() {
        File f = new File("mohist-config", "mohist.yml");
        return MohistConfigUtil.getBoolean(f, "check_update:");
    }
}
