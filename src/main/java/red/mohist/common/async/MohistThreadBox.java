package red.mohist.common.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MohistThreadBox {

    public static final ScheduledExecutorService METRICS = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Metrics"));

    public static final ExecutorService ASYNCCHAT = Executors.newCachedThreadPool(new NamedThreadFactory("Async Chat Thread"));

    public static final ExecutorService FILEIO = Executors.newFixedThreadPool(2, new NamedThreadFactory("Mohist File IO Thread"));

    public static final ExecutorService ASYNCEXECUTOR = Executors.newSingleThreadExecutor(new NamedThreadFactory("Mohist Async Task Handler Thread"));

    public static final ExecutorService TCW = Executors.newSingleThreadExecutor(new NamedThreadFactory("TerminalConsoleWriter"));

    public static class AssignableThread extends Thread {
        public AssignableThread(Runnable run) {
            super(run);
        }
        public AssignableThread() {
            super();
        }
    }
}
