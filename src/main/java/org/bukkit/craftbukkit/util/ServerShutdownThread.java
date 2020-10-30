package org.bukkit.craftbukkit.util;

import net.minecraft.world.MinecraftException;


public class ServerShutdownThread extends Thread {
    private final net.minecraft.server.MinecraftServer server;

    public ServerShutdownThread(net.minecraft.server.MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.stopServer();
        } catch (MinecraftException ex) {
            ex.printStackTrace();
        } finally {
            try {
                server.reader.getTerminal().restore();
            } catch (Exception e) {
            }
        }
    }
}
