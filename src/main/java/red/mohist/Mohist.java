package red.mohist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Mgazul
 * @date 2020/5/31 14:38
 */
public class Mohist {

    public static final Logger LOGGER = LogManager.getLogger();

    public static String getVersion() {
        return Mohist.class.getPackage().getImplementationVersion() != null ? Metrics.class.getPackage().getImplementationVersion() : "0.1";
    }
}
