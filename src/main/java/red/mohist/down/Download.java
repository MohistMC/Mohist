package red.mohist.down;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import red.mohist.util.i18n.Message;

public class Download {

    public Download(String url, String savePath, String jaranme) {
        try {
            String fName;
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                fName = savePath;
            } else {
                fName = "/" + savePath;
            }
            File file=new File(fName);
            if(!file.exists()){
                file.createNewFile();
            }
            URL website = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setConnectTimeout(10*1000);
            connection.setReadTimeout(10*1000);
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                long size = connection.getContentLengthLong();

                System.out.println(Message.getFormatString("file.download.start", new Object[]{jaranme, getSize(size)}));

                DataInputStream in = new DataInputStream(connection.getInputStream());
                DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[2048];
                int count = 0;
                while ((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                }
                try {
                    if(out!=null) {
                        out.close();
                    }
                    if(in!=null) {
                        in.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connection.disconnect();
                System.out.println(Message.getFormatString("file.download.ok", new Object[]{jaranme}));
            } else {
                System.out.println(Message.getFormatString("file.download.nook", new Object[]{jaranme}));
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
