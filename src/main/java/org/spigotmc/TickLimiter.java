package org.spigotmc;

public class TickLimiter {
    private final int maxTime;
    private long startTime;
    private int tick;
    private boolean shouldContinue;
    public TickLimiter(int maxTime) {
        this.maxTime = maxTime;
    }
    
    public void initTick() {
        startTime = System.currentTimeMillis();
        tick = 0;
        shouldContinue = true;
    }
    
    public boolean shouldContinue() {
        if (++tick >= 300 && shouldContinue) {
            tick = 0;
            shouldContinue = System.currentTimeMillis() - startTime < maxTime; 
        }
        return shouldContinue;
    }
}
