package com.mohistmc.console.log4j;

import com.mojang.util.QueueLogAppender;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;

public class Log4jCrashFix {

    private static final Map<String, BlockingQueue<String>> QUEUES;
    private static final ReadWriteLock QUEUE_LOCK;
    private static BlockingQueue<String> queue = null;
    final private OutputStream output;

    static {
        Field f = null;
        try {
            f = QueueLogAppender.class.getDeclaredField("QUEUES");
            f.setAccessible(true);
            QUEUES = (Map<String, BlockingQueue<String>>) f.get(null);
            f = QueueLogAppender.class.getDeclaredField("QUEUE_LOCK");
            f.setAccessible(true);
            QUEUE_LOCK = (ReadWriteLock) f.get(null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Log4jCrashFix(OutputStream output) {
        this.output = output;
    }

    public void run() {
        try {
            String message;
            QUEUE_LOCK.readLock().lock();
            queue = QUEUES.get("TerminalConsole");
            if (queue != null) {
                while ((message = queue.poll()) != null) {
                    output.write(message.getBytes());
                    output.flush();
                }
            }
            QUEUE_LOCK.readLock().unlock();
        } catch (IOException ex) {}
    }
}
