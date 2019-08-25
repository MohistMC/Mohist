package red.mohist.asyn;

import java.util.logging.Logger;

/**
 * 异步tick
 * 在tick开始后,会调用所有异步tickTask的prepar方法
 * 在tick结束前,会在主线程调用所有异步ticktask的apply方法
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
