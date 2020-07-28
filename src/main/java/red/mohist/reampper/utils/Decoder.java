package red.mohist.reampper.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Phobetor
 *
 * @create 2020/6/7 3:29
 */

public class Decoder {

    public void Decode(File key, File nms) throws IOException {

        InputStream map = Decoder.class.getClassLoader().getResourceAsStream("mappings/map.srg");
        InputStream joined = new FileInputStream(key);
        int j = 0;
        List<String> lines = new BufferedReader(new InputStreamReader(map, StandardCharsets.UTF_8)).lines()
                .filter(l -> !l.isEmpty())
                .collect(Collectors.toList());
        List<String> controls = new BufferedReader(new InputStreamReader(joined, StandardCharsets.UTF_8)).lines()
                .filter(l -> !l.isEmpty())
                .collect(Collectors.toList());
        FileOutputStream fos = new FileOutputStream(nms);
        BufferedWriter fwriter = new BufferedWriter(new OutputStreamWriter(fos));
        SrgWriter out = new SrgWriter();

        for (String line : lines) {
            String[] pts = line.split(" ");
            switch (pts[0]) {
                case "CL:":
                    for (String MCP : controls) {
                        String[] cnt = MCP.split(" ");
                        if (cnt[0].equals("CL:")) {
                            if (cnt[1].equals(pts[2])) {
                                out.WriteCL(fwriter, pts[1], cnt[2]);
                                break;
                            }
                        }
                    }
                    break;

                case "FD:":
                    for (String MCP : controls) {
                        String[] cnt = MCP.split(" ");
                        if (cnt[0].equals("FD:")) {
                            if (cnt[1].equals(pts[2])) {
                                out.WriteFD(fwriter, pts[1], cnt[2]);
                                j = 1;
                                break;
                            }
                        }
                    }
                    //catching Extra cases
                    if (j != 1) {
                        out.WriteFD(fwriter, pts[1], pts[2]);
                    }
                    j = 0;
                    break;

                case "MD:":
                    for (String MCP : controls) {
                        String[] cnt = MCP.split(" ");
                        if (cnt[0].equals("MD:")) {
                            if (cnt[1].equals(pts[3]) && cnt[2].equals(pts[4])) {
                                out.WriteMD(fwriter, pts[1], pts[2], cnt[3], cnt[4]);
                                j = 1;
                                break;
                            }
                        }
                    }
                    //catching Extra cases
                    if (j != 1) {
                        out.WriteMD(fwriter, pts[1], pts[2], pts[3], pts[4]);
                    }
                    j = 0;
                    break;
            }
        }
        fwriter.close();
    }
}
