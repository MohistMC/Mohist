package red.mohist.reampper.utils;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @Author Phobetor
 *
 * @create 2020/6/7 3:29
 */

public class SrgWriter {

    public void WriteCL(BufferedWriter out, String org, String mapped) throws IOException {
        out.write("CL:" + " " + org + " " + mapped);
        out.newLine();
    }

    public void WriteFD(BufferedWriter out, String org, String mapped) throws IOException {
        out.write("FD:" + " " + org + " " + mapped);
        out.newLine();
    }

    public void WriteMD(BufferedWriter out, String org1, String org2, String mapped1, String mapped2) throws IOException {
        out.write("MD:" + " " + org1 + " " + org2 + " " + mapped1 + " " + mapped2);
        out.newLine();
    }
}
