package red.mohist.down;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import red.mohist.util.i18n.Message;

public class Download {

    public Download(String url, String fileName) {
        try {
            URL website = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();
            if (code == 200) {
                long startTime =  System.currentTimeMillis();

                System.out.println(Message.getFormatString("file.download.size", new Object[]{fileName, getSize(connection.getContentLengthLong())}));
                System.out.println(Message.getFormatString("file.download", new Object[]{fileName}));
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                rbc.close();
                fos.close();

                long endTime =  System.currentTimeMillis();
                long usedTime = (endTime-startTime)/1000;
                System.out.println(Message.getFormatString("file.download.ok", new Object[]{ fileName, (int)usedTime}));
            } else {
                System.out.println(Message.getFormatString("file.download.nook", new Object[]{url}));
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSize(long size) {
        if (size >= 1099511627776L) {
            return (float) size / 1099511627776.0F + " TiB";
        }
        if (size >= 1073741824L) {
            return (float) size / 1073741824.0F + " GiB";
        }
        if (size >= 1048576L) {
            return (float) size / 1048576.0F + " MiB";
        }
        if (size >= 1024) {
            return (float) size / 1024.0F + " KiB";
        }
        return size + " B";
    }
}
