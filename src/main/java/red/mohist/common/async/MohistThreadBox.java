package red.mohist.common.async;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MohistThreadBox {

    public static final ExecutorService DL = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<>(), new NamedThreadFactory("Mohist DL"));

    public static final ScheduledExecutorService METRICS = new ScheduledThreadPoolExecutor(1, NamedThreadFactory.CustomName("Metrics"));

    public static final ExecutorService ASYNCCHAT = Executors.newCachedThreadPool( NamedThreadFactory.CustomName("Async Chat Thread"));

    public static final ExecutorService FILEIO = Executors.newFixedThreadPool(2, NamedThreadFactory.CustomName("Mohist File IO Thread"));

    public static final ExecutorService ASYNCEXECUTOR = Executors.newSingleThreadExecutor(NamedThreadFactory.CustomName("Mohist Async Task Handler Thread"));

    public static final ExecutorService TCW = Executors.newSingleThreadExecutor(NamedThreadFactory.CustomName("TerminalConsoleWriter"));

    /**
     * Registry order: SoundEvent -> Block
     */
    public static final ExecutorService STAGE_BLOCK = Executors.newSingleThreadExecutor(NamedThreadFactory.CustomName("Mohist Parallel Registry Thread"));
    /**
     * Registry order: Item -> PotionBrewer & orderless: BlockFire, Biome (After STAGE_BLOCK)
     */
    public static final ExecutorService STAGE_BLOCK_BASE  = Executors.newFixedThreadPool(3, NamedThreadFactory.CustomName("Mohist Parallel Registry Thread"));

    /**
     * Registry order: MobEffectList -> PotionRegistry & orderless: Enchantment, EntityTypes
     */
    public static final ExecutorService STAGE_STANDALONE = Executors.newFixedThreadPool(3, NamedThreadFactory.CustomName("Mohist Parallel Registry Thread"));

    public static class AssignableThread extends Thread {
        public AssignableThread(Runnable run) {
            super(run);
        }
        public AssignableThread() {
            super();
        }
    }
}
