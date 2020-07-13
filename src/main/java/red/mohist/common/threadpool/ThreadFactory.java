package red.mohist.common.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 线程池构造工厂
 *
 * @author Carierx_Max(fakeTrotsky)
 * @date   2020-7-13
 */
public class ThreadFactory {
    private static ThreadFactory executorFactory = new ThreadFactory();
    /**
     * 定时任务线程池
     */
    private ExecutorService executors;

    private ThreadFactory() {
    }

    /**
     * 获取ExecutorServiceFactory
     *
     * @return
     */
    public static ThreadFactory getInstance() {
        return executorFactory;
    }

    /**
     * 创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。
     *
     * @return
     */
    public ExecutorService createScheduledThreadPool() {
        // CPU个数
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        // 创建
        executors = Executors.newScheduledThreadPool(availableProcessors * 10, getThreadFactory());
        return executors;
    }

    /**
     * 创建一个使用单个 worker 线程的
     * Executor，以无界队列方式来运行该线程。（注意，如果因为在关闭前的执行期间出现失败而终止了此单个线程，
     * 那么如果需要，一个新线程将代替它执行后续的任务）。可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。与其他等效的
     * newFixedThreadPool(1) 不同，可保证无需重新配置此方法所返回的执行程序即可使用其他的线程。
     *
     * @return
     */
    public ExecutorService createSingleThreadExecutor() {
        // 创建
        executors = Executors.newSingleThreadExecutor(getThreadFactory());
        return executors;
    }

    /**
     * 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。对于执行很多短期异步任务的程序而言，这些线程池通常可提高程序性能。调用
     * execute 将重用以前构造的线程（如果线程可用）。如果现有线程没有可用的，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60
     * 秒钟未被使用的线程。因此，长时间保持空闲的线程池不会使用任何资源。注意，可以使用 ThreadPoolExecutor
     * 构造方法创建具有类似属性但细节不同（例如超时参数）的线程池。
     *
     * @return
     */
    public ExecutorService createCachedThreadPool() {
        // 创建
        executors = Executors.newCachedThreadPool(getThreadFactory());
        return executors;
    }

    /**
     * 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。在任意点，在大多数 nThreads
     * 线程会处于处理任务的活动状态。如果在所有线程处于活动状态时提交附加任务
     * ，则在有可用线程之前，附加任务将在队列中等待。如果在关闭前的执行期间由于失败而导致任何线程终止
     * ，那么一个新线程将代替它执行后续的任务（如果需要）。在某个线程被显式地关闭之前，池中的线程将一直存在。
     *
     * @return
     */
    public ExecutorService createFixedThreadPool(int count) {
        // 创建
        executors = Executors.newFixedThreadPool(count, getThreadFactory());
        return executors;
    }


    /**
     * 获取线程池工厂
     *
     * @return
     */
    private java.util.concurrent.ThreadFactory getThreadFactory() {
        return new java.util.concurrent.ThreadFactory() {
            AtomicInteger sn = new AtomicInteger();
            public Thread newThread(Runnable r) {
                SecurityManager s = System.getSecurityManager();
                ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                Thread t = new Thread(group, r);
                t.setName("Task Thread - " + sn.incrementAndGet());
                return t;
            }
        };
    }
}