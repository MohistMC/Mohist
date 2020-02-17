package red.mohist.down;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Download {

    public Download(String url, File fileName) {
        try {
            URL website = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setConnectTimeout(10*1000);
            connection.setReadTimeout(10*1000);
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();
            if (code == 200) {
                long size = connection.getContentLengthLong();

                System.out.println("The library file is missing and is being completed: " + url + " Size " + getSize(size));

                ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                rbc.close();
                fos.close();
                connection.disconnect();
            } else {
                //System.out.println(Message.getFormatString("file.download.nook", new Object[]{url}));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSize(long size) {
        if (size >= 1099511627776L) {
            return (float) size / 1099511627776.0F + " TB";
        }
        if (size >= 1073741824L) {
            return (float) size / 1073741824.0F + " GB";
        }
        if (size >= 1048576L) {
            return (float) size / 1048576.0F + " MB";
        }
        if (size >= 1024) {
            return (float) size / 1024.0F + " KB";
        }
        return size + " B";
    }
}
