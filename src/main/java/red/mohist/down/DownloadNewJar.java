package red.mohist.down;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.FileUtils;
import red.mohist.Mohist;

/**
 * @author Mgazul
 * @date 2020/3/28 0:42
 */
public class DownloadNewJar {

    public static void run() throws IOException {
        File serverjarold = new File("./Mohist-"+Update.jar_sha+"-server.jar");
        String filename;

        if(!new File("./Mohist-" + Update.jar_sha + "-server.jar").exists()) { //DETECT IF JAR HAVE A CUSTOM NAME (Can be useful for host which requires server.jar name for example)
            filename = new File(Mohist.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName();
        } else {
            filename = "./Mohist-" + Update.jar_sha + "-server.jar";
        }

        URLConnection conn = new URL("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/artifact/build/distributions/Mohist-" + Update.ci_sha + "-server.jar").openConnection();
        conn.setRequestProperty("User-Agent", "Mohist");
        conn.connect();
        System.out.println("Downloading new server jar... (~" + ((conn.getContentLength()/1024)/1024) + " Mo)");
        FileUtils.copyInputStreamToFile(conn.getInputStream(), new File(filename)); //Download

        if(serverjarold.exists())
            serverjarold.renameTo(new File("./Mohist-" + Update.ci_sha + "-server.jar"));

        System.out.println("Download Finished ! Please restart the server.");
        System.exit(0);

    }
}
