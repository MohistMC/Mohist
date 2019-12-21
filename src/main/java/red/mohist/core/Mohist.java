package red.mohist.core;

import net.minecraftforge.fml.common.Mod;

/**
 * @author Mgazul
 * @date 2019/12/21 9:05
 */
@Mod("mohist")
public class Mohist {

    public static final String NAME = "Mohist";
    public static final double VERSION = 0.1;
    public static final String GITVERSION =
            Mohist.class.getPackage().getImplementationVersion() != null ? Mohist.class.getPackage()
                    .getImplementationVersion() : "α";
    public static final String BUKKIT_VERSION = "v1_15_R1";
    public static final String NMS_PREFIX = "net/minecraft/server/";
}
