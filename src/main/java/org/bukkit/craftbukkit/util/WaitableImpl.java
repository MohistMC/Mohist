package org.bukkit.craftbukkit.util;

/**
 * Java 8 Waitable
 */
public class WaitableImpl extends Waitable<Object> {

    private Runnable runnable;
    public WaitableImpl(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    protected Object evaluate() {
        runnable.run();
        return null;
    }

}