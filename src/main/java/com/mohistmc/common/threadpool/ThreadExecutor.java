package com.mohistmc.common.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 线程处理类
 *
 * @author Carierx_MAX(fakeTrotsky)
 * @date   2020-7-13
 */
public class ThreadExecutor {

    private ExecutorService executor;
    private static ThreadExecutor pool = new ThreadExecutor();
    private final int threadMax = 10;

    private ThreadExecutor() {
        System.out.println("threadMax>>>>>>>" + threadMax);
        executor = ThreadFactory.getInstance().createFixedThreadPool(threadMax);
    }

    public static ThreadExecutor getInstance() {
        return pool;
    }

    /**
     * 关闭线程池，这里要说明的是：调用关闭线程池方法后，线程池会执行完队列中的所有任务才退出
     *
     * @author Carierx_MAX(fakeTrotsky)
     * @date   2020-7-13
     */
    public void shutdown(){
        executor.shutdown();
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     *
     * @param task
     * @return
     * @author Carierx_MAX(fakeTrotsky)
     * @date   2020-7-13
     */
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     *
     * @param task
     * @return
     * @author Carierx_MAX(fakeTrotsky)
     * @date   2020-7-13
     */
    public Future<?> submit(Callable<?> task) {
        return executor.submit(task);
    }

    /**
     * 直接提交任务到线程池，无返回值
     *
     * @param task
     * @author Carierx_MAX(fakeTrotsky)
     * @date   2020-7-13
     */
    public void execute(Runnable task){
        executor.execute(task);
    }

}