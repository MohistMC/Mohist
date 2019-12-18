package com.destroystokyo.paper.event.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class IllegalPacketEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String type;
    private final String ex;
    private String kickMessage;
    private boolean shouldKick = true;

    public IllegalPacketEvent(Player player, String type, String kickMessage, Exception e) {
        super(player);
        this.type = type;
        this.kickMessage = kickMessage;
        this.ex = e.getMessage();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static void process(Player player, String type, String kickMessage, Exception exception) {
        IllegalPacketEvent event = new IllegalPacketEvent(player, type, kickMessage, exception);
        event.callEvent();
        if (event.shouldKick) {
            player.kickPlayer(kickMessage);
        }
        Bukkit.getLogger().severe(player.getName() + "/" + type + ": " + exception.getMessage());
    }

    public boolean isShouldKick() {
        return shouldKick;
    }

    public void setShouldKick(boolean shouldKick) {
        this.shouldKick = shouldKick;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
    }

    public String getType() {
        return type;
    }

    public String getExceptionMessage() {
        return ex;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
