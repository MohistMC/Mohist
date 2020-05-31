package red.mohist;

import com.sun.javafx.font.Metrics;

/**
 * @author Mgazul
 * @date 2020/5/31 14:38
 */
public class Mohist {

    public static String getVersion() {
        return Mohist.class.getPackage().getImplementationVersion() != null ? Metrics.class.getPackage().getImplementationVersion() : "0.1";
    }
}
