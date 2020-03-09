package red.mohist.down;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import red.mohist.util.i18n.Message;

public class Download {

    public Download(String url, File savePath, String jaranme) {
        ReadableByteChannel rbc = null;
        FileChannel fileChannel = null;
        try {
            URL website = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            if (connection.getResponseCode() < 400) {
                long size = connection.getContentLengthLong();

                System.out.println(Message.getFormatString("file.download.start", new Object[]{url, getSize(size)}));

                rbc = Channels.newChannel(website.openStream());
                fileChannel = FileChannel.open(savePath.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                fileChannel.transferFrom(rbc, 0L, Long.MAX_VALUE);
                try {
                    if (rbc != null) {
                        rbc.close();
                    }
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Message.getFormatString("file.download.ok", new Object[]{jaranme}));
            } else {
                System.out.println(Message.getFormatString("file.download.nook", new Object[]{url}));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSize(long size) {
        if (size >= 1048576L) {
            return (float) size / 1048576.0F + " MB";
        }
        if (size >= 1024) {
            return (float) size / 1024.0F + " KB";
        }
        return size + " B";
    }
}
