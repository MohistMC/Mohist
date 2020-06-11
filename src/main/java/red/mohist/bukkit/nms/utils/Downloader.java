package red.mohist.bukkit.nms.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import red.mohist.bukkit.nms.utils.srgutils.IMappingFile;
import red.mohist.util.i18n.Message;

/**
 * @Author Phobetor
 *
 * @create 2020/6/7 3:29
 */

public class Downloader {

    public void execute(String temppath) throws IOException {

        System.out.println(Message.getString("mcp.download"));
        File mcptool = new File(temppath + "/mcp.zip");
        String url = "https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_config/1.12.2-20200226.224830/mcp_config-1.12.2-20200226.224830.zip";
        ReadableByteChannel readChannel = Channels.newChannel(new URL(url).openStream());
        FileOutputStream fileOS = new FileOutputStream(mcptool);
        System.out.println(Message.getString("mcp.extract"));
        FileChannel writeChannel = fileOS.getChannel();
        writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
        ZipFile mcp = new ZipFile(mcptool);
        ZipEntry tsrg = mcp.getEntry("config/joined.tsrg");
        InputStream zip = mcp.getInputStream(tsrg);
        List<String> lines = new BufferedReader(new InputStreamReader(zip, StandardCharsets.UTF_8)).lines()
                .filter(l -> !l.isEmpty())
                .collect(Collectors.toList());
        FileOutputStream fos = new FileOutputStream(temppath + "/joined.tsrg");
        BufferedWriter copy = new BufferedWriter(new OutputStreamWriter(fos));
        for (String line : lines) {
            copy.write(line);
            copy.newLine();
        }
        copy.close();
        fos.close();
        zip.close();
        mcp.close();
        fileOS.close();
        writeChannel.close();
        mcptool.delete();
        File partz = new File(temppath + "/joined.tsrg");
        IMappingFile.load(partz).write(Paths.get(temppath + "/joined.srg"),IMappingFile.Format.SRG, false);
        partz.delete();
    }
}
