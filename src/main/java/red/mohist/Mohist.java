package red.mohist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Mgazul
 * @date 2019/11/16 2:53
 */
public class Mohist {

    public static final String NAME = "Mohist";
    public static Logger LOGGER = LogManager.getLogger("Mohist");

    public static String getVersion() {
        return Mohist.class.getPackage().getImplementationVersion() != null ? Metrics.class.getPackage().getImplementationVersion() : "unknown";
    }
}
