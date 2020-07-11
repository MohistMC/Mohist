package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class AsyncCatcher {

    public static boolean enabled = true;

    public static void catchOp(String reason) {
        if (enabled && Thread.currentThread() != MinecraftServer.getServerInst().primaryThread) {
            //throw new IllegalStateException("Asynchronous " + reason + "!");
        }
    }

    public static boolean catchInv() {
        return enabled && Thread.currentThread() != MinecraftServer.getServerInst().primaryThread;
    }
}
