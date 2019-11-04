package red.mohist.common.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MohistThreadBox {

    public static final ExecutorService DL = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new NamedThreadFactory("Mohist DL"));

    public static final ScheduledExecutorService METRICS = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Metrics"));

    public static final ExecutorService ASYNCCHAT = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new NamedThreadFactory("Async Chat Thread"));

    public static final ExecutorService FILEIO = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory("Mohist File IO Thread"));

    public static class AssignableThread extends Thread {
        public AssignableThread(Runnable run) {
            super(run);
        }
        public AssignableThread() {
            super();
        }
    }
}
