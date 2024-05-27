package com.mohistmc.network.download;

import com.mohistmc.util.JarTool;
import com.mohistmc.util.i18n.Message;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.mohistmc.network.download.NetworkUtil.getConn;

public class UpdateUtils {

    private static int percentage = 0;

    public static void downloadFile(String URL, File f) throws Exception {
        URLConnection conn = getConn(URL);
        System.out.println(Message.getFormatString("file.download.start", new Object[]{f.getName(), getSize(conn.getContentLength())}));
        ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
        FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        int fS = conn.getContentLength();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (rbc.isOpen()) {
                    if (percentage != Math.round((float) f.length() / fS * 100) && percentage < 100)
                        System.out.println(Message.getFormatString("file.download.percentage", new Object[]{f.getName(), percentage}));
                    percentage = Math.round((float) f.length() / fS * 100);
                } else t.cancel();
            }
        }, 3000, 1000);
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
        fc.close();
        rbc.close();
        System.out.println(Message.getFormatString("file.download.ok", new Object[]{f.getName()}));
    }

    public static File getMohistJar() {
        try {
            String path = UpdateUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
            if (path.contains("!/")) path = path.split("!/")[0].split("jar:file:/")[1];
            return new File(path);
        } catch (Exception e) {
            System.out.println("Can't found the Mohist jar !");
        }
        return null;
    }

    public static void restartServer(ArrayList<String> cmd) throws Exception {
        if (cmd.stream().anyMatch(s -> s.contains("-Xms")))
            System.out.println("[WARNING] We detected that you're using the -Xms argument and it will add the specified ram to the current Java process and the Java process which will be created by the ProcessBuilder, and this could lead to double RAM consumption.\nIf the server does not restart, please try remove the -Xms jvm argument.");
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(JarTool.getJarDir());
        pb.inheritIO().start().waitFor();
        Thread.sleep(2000);
        System.exit(0);
    }

    public static String getSize(long size) {
        return (size >= 1048576L) ? (float) size / 1048576.0F + "MB" : ((size >= 1024) ? (float) size / 1024.0F + " KB" : size + " B");
    }

    public static long getSizeOfDirectory(File path) throws IOException {
        return Files.walk(path.toPath()).parallel().filter(p -> !p.toFile().isDirectory()).count();
    }
}
