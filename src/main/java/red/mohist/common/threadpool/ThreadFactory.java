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

    public ExecutorService createSingleThreadExecutor() {
        // 创建
        executors = Executors.newSingleThreadExecutor(getThreadFactory());
        return executors;
    }

    public ExecutorService createCachedThreadPool() {
        // 创建
        executors = Executors.newCachedThreadPool(getThreadFactory());
        return executors;
    }

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
                //TODO => ThreadFactory
                ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                Thread t = new Thread(group, r);
                t.setName("Task Thread - " + sn.incrementAndGet());
                return t;
            }
        };
    }
}