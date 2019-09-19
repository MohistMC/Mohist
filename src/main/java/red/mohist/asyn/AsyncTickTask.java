package red.mohist.asyn;

import java.util.logging.Logger;

/**
 *
 * @author pyz
 * @date 2019/7/5 8:14 PM
 */
public interface AsyncTickTask extends Runnable {

    Logger LOGGER = Logger.getLogger("AsyncTick");

    void prepare();

    void apply();

    @Override
    default void run() {
        try {
            prepare();
        } catch (Throwable e) {
            LOGGER.severe("error while prepare async tick:" + this.toString());
            e.printStackTrace();
        }
    }

    @Override
    String toString();
}
