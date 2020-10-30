package red.mohist.down;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            connection.setConnectTimeout(10*1000);
            connection.setReadTimeout(10*1000);
            connection.setRequestMethod("GET");
            long alreadySize = 0;
            int code = connection.getResponseCode();
            if (code == 200) {
                long startTime =  System.currentTimeMillis();
                long unfinishedSize = connection.getContentLength();

                long size = alreadySize + unfinishedSize;
                System.out.println(Message.getFormatString("file.download.size", new Object[]{fileName, getSize(connection.getContentLengthLong())}));
                System.out.println(Message.getFormatString("file.download", new Object[]{fileName}));

                InputStream in = connection.getInputStream();

                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                byte[] buff = new byte[2048];
                int len;
                while ((len = in.read(buff)) != -1) {
                    fos.write(buff, 0, len);
                    alreadySize += len;
                    Progress cpb = new Progress(50, '#');
                    if (cpb.isEnable()) {
                        cpb.show(fileName, (int) (alreadySize * 1.0 / size * 100));
                    }
                }
                in.close();
                rbc.close();
                fos.close();
                connection.disconnect();

                long endTime =  System.currentTimeMillis();
                long usedTime = (endTime-startTime)/1000;
                System.out.println(Message.getFormatString("file.download.ok", new Object[]{ fileName, String.valueOf(usedTime)}));
            } else {
                System.out.println(Message.getFormatString("file.download.nook", new Object[]{url}));
            }
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
