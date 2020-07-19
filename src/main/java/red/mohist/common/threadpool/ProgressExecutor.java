package red.mohist.common.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 测试类
 *
 * @author Carierx_MAX(fakeTrotsky)
 * @date   2020-7-13
 */
public class ProgressExecutor {

    public static void main(String[] args) {

        ThreadExecutor pool = ThreadExecutor.getInstance();

        for (int i = 0; i < 200; i++) {
            Future<?> future = pool.submit(new ExcuteTask1(i+""));
        //TODO  thread return value
        }

        for (int i = 0; i < 200; i++) {
            pool.execute((Runnable) new ExcuteTask1(i+""));
        }
        //close pool
        pool.shutdown();
    }

    /**
     * 执行任务1，实现Callable方式
     *
     * @author Carierx_MAX(fakeTrotsky)
     * @date   2020-7-13
     */
    static class ExcuteTask1 implements Callable<String> {
        private String taskName;

        public ExcuteTask1(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public String call() throws Exception {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "114514";
        }
    }

    /**
     * 执行任务2，实现Runable方式
     *
     * @author Carierx_MAX(fakeTrotsky)
     * @date   2020-7-13
     */

        public void run() {
            try {
                //TODO some code
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO Tasks
        }
}
