package red.mohist.common.async;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ThreadFactory;

/**
 * @Author Mgazul
 * @create 2019/9/11 20:57
 */
public class NamedThreadFactory implements ThreadFactory {
    private static int id = 0;
    private String name;

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new MohistThreadBox.AssignableThread(r);
        thread.setName(name + " - " + (++id));
        thread.setPriority(7);
        return thread;
    }

    public static ThreadFactory CustomName(String name){
        return new ThreadFactoryBuilder().setNameFormat(name + " - %1$d").build();
    }
}
