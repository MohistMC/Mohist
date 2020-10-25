package com.mohistmc.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.plugin.PluginLoadOrder;


public class BukkitStateForgeEvent extends Event {

    private final CraftServer server;

    public BukkitStateForgeEvent(CraftServer server) {
        this.server = server;
    }

    public CraftServer getServer() {
        return server;
    }

    public static abstract class PluginsLoad extends BukkitStateForgeEvent {
        public PluginsLoad(CraftServer server) {
            super(server);
        }

        public static class Pre extends PluginsLoad {
            public Pre(CraftServer server) {
                super(server);
            }
        }

        public static class Post extends PluginsLoad {
            public Post(CraftServer server) {
                super(server);
            }
        }
    }

    public static abstract class PluginsEnable extends BukkitStateForgeEvent {
        private final PluginLoadOrder type;

        public PluginsEnable(CraftServer server, PluginLoadOrder type) {
            super(server);
            this.type = type;
        }

        public PluginLoadOrder getType() {
            return type;
        }

        public static class Pre extends PluginsEnable {
            public Pre(CraftServer server, PluginLoadOrder type) {
                super(server, type);
            }
        }

        public static class Post extends PluginsEnable {
            public Post(CraftServer server, PluginLoadOrder type) {
                super(server, type);
            }
        }
    }
}