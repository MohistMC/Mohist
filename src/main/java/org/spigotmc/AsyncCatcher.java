package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class AsyncCatcher
{

    public static boolean enabled = false;

    public static void catchOp(String reason)
    {
        if ( enabled && Thread.currentThread() != MinecraftServer.getServer().serverThread )
        {
            throw new IllegalStateException( "Asynchronous " + reason + "!" );
        }
    }
}
