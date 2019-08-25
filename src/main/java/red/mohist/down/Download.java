package red.mohist.down;

import red.mohist.i18n.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Download {

    public Download(String url, String fileName) {
        Object[] o1 = {fileName};
        System.out.println(Message.getFormatString(Message.Dw_File, o1));
        System.out.println(Message.getString(Message.Dw_Start) + "......");
        File Jar = new File(fileName);
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
