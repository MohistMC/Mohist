package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class AsyncCatcher {

    public static boolean enabled = true;

    public static void catchOp(String reason) {
        if (AsyncCatcher.enabled && Thread.currentThread() != MinecraftServer.getServer().serverThread) {
            throw new IllegalStateException("Asynchronous " + reason + "!");
        }
    }

    public static boolean catchAsync() {
        if (enabled && Thread.currentThread() != MinecraftServer.getServer().serverThread) {
            return true;
        }
        return false;
    }
}
