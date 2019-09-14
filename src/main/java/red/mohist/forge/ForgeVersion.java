package red.mohist.forge;

import red.mohist.configuration.MohistConfig;

public class ForgeVersion {

    public static final int major = MohistConfig.instance.major.getValue();
    public static final int minor = MohistConfig.instance.minor.getValue();
    public static final int revision = MohistConfig.instance.revision.getValue();
    public static final int build = MohistConfig.instance.build.getValue();
}
