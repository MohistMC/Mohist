package red.mohist.api;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatComponentAPI {

    public static void sendHoverChat(Player player, String message, String hover) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        player.spigot().sendMessage(component);
    }

    public static TextComponent sendHoverChat(String message, String hover) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        return component;
    }

    public static void sendClickChat(Player player, String message, String hover, String command) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        player.spigot().sendMessage(component);
    }

    public static TextComponent sendClickChat(String message, String hover, String command) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return component;
    }

    public static void sendClickOpenURLChat(Player player, String message, String hover, String url) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        player.spigot().sendMessage(component);
    }

    public static TextComponent sendClickOpenURLChat(String message, String hover, String url) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return component;
    }

    public static void sendSuggestCommand(Player player, String message, String hover, String command) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        player.spigot().sendMessage(component);
    }

    public static TextComponent sendSuggestCommand(String message, String hover, String command) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return component;
    }
}
