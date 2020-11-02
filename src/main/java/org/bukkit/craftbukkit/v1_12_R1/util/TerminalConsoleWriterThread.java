package org.bukkit.craftbukkit.v1_12_R1.util;

import com.mojang.util.QueueLogAppender;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.console.ConsoleReader;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.v1_12_R1.command.ColouredConsoleSender;

@SuppressWarnings("ALL")
public class TerminalConsoleWriterThread implements Runnable {
    private final static byte[] RESET_LINE = String.valueOf(jline.console.ConsoleReader.RESET_LINE).getBytes();

    private static final Map<String, BlockingQueue<String>> QUEUES;
    private static final ReadWriteLock QUEUE_LOCK;

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

    final private ConsoleReader reader;
    final private OutputStream output;
    private BlockingQueue<String> queue = null;

    public TerminalConsoleWriterThread(OutputStream output, ConsoleReader reader) {
        this.output = output;
        this.reader = reader;
    }

    public void run() {
        String message;
        while (queue == null) {
            QUEUE_LOCK.readLock().lock();
            queue = QUEUES.get("TerminalConsole");
            QUEUE_LOCK.readLock().unlock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Using name from log4j config in vanilla jar
        while (true) {
            try {
                message = queue.poll();
                if (Main.useJline) {
                    this.output.write(RESET_LINE);
                    this.output.write(ColouredConsoleSender.toAnsiStr(message).getBytes());
                    this.output.flush();

                    try {
                        reader.drawLine();
                    } catch (Throwable ex) {
                        reader.getCursorBuffer().clear();
                    }
                    reader.flush();
                } else {
                    output.write(message.getBytes());
                    output.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(TerminalConsoleWriterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
