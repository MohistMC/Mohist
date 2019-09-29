package red.mohist.forge;

import java.io.File;
import red.mohist.configuration.MohistConfigUtil;

public class ForgeVersion {
    private static final File f = new File("mohist-config", "mohist.yml");
    public static final int major = MohistConfigUtil.getInt(f, "major:", "14");
    public static final int minor = MohistConfigUtil.getInt(f, "minor:", "23");
    public static final int revision = MohistConfigUtil.getInt(f, "revision:", "5");
    public static final int build = MohistConfigUtil.getInt(f, "build:", "2847");
}
