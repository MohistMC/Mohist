package red.mohist.forge;

import red.mohist.configuration.MohistConfigUtil;

public class ForgeVersion {

    public static final int major = MohistConfigUtil.getInt("major:", "14");
    public static final int minor = MohistConfigUtil.getInt("minor:", "23");
    public static final int revision = MohistConfigUtil.getInt("revision:", "5");
    public static final int build = MohistConfigUtil.getInt("build:", "2847");
}
