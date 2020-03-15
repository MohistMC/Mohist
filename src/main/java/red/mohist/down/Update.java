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
        String str = "https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastBuild/api/json";
        String ver = "https://raw.githubusercontent.com/Mohist-Community/Mohist/1.12.2/mohist.ver";
        String dl = "https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/";
        try {
            System.out.println(Message.getString("update.check"));
            System.out.println(Message.getString("update.stopcheck"));
            URL url = new URL(str);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setRequestProperty("User-Agent", "Mohist");
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                String commits = IOUtil.readContent(is);
                String sha = "\"SHA1\"";

                String s0 = commits.substring(commits.indexOf(sha));

                String[] ss = s0.split("\n");
                String b = ss[0].trim().replace(" ", "").replace("\"", "");
                String ci_sha = b.substring(5, 12);

                String jar_sha = Update.class.getPackage().getImplementationVersion();

                String newversion = MohistConfigUtil.getUrlString(ver, Mohist.VERSION);
                String oldversion = Mohist.VERSION;
                if (jar_sha.equals(ci_sha)) {
                    System.out.println(Message.getFormatString("update.latest", new Object[]{oldversion, ci_sha, jar_sha}));
                } else {
                    String time = "\"date\"";
                    String time1 = commits.substring(commits.indexOf(time));
                    String[] ss1 = time1.split("\n");
                    String b1 = ss1[0].trim().replace("\"", "");
                    String time3 = b1.substring(5, 24);
                    System.out.println(Message.getFormatString("update.old", new Object[]{oldversion, newversion, ci_sha, time3, jar_sha, dl}));
                }
                is.close();
            } else {
                System.out.println("Link access failed");
            }
        } catch (IOException e) {
        }
    }

    public static boolean isCheck() {
        File f = new File("mohist-config", "mohist.yml");
        return MohistConfigUtil.getBoolean(f, "check_update:");
    }
}
