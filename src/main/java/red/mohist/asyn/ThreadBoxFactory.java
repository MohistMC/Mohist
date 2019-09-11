package red.mohist.asyn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author Mgazul
 * @create 2019/9/11 20:51
 */
public class ThreadBoxFactory {

    public static final ExecutorService asyncWorldSaver = Executors.newSingleThreadExecutor(new NamedThreadFactory("AsyncWorldSaver"));
}
