/*
 * MohistMC
 * Copyright (C) 2019-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.api;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatComponentAPI {

    public static void sendHoverChat(Player player, String message, String hover) {
        player.spigot().sendMessage(getHoverChat(message, hover));
    }

    public static TextComponent getHoverChat(String message, String hover) {
        return new ChatComponent(message).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create())).create();
    }

    public static void sendClickChat(Player player, String message, String hover, String command) {
        player.spigot().sendMessage(getClickChat(message, hover, command));
    }

    public static TextComponent getClickChat(String message, String hover, String command) {
        return new ChatComponent(getHoverChat(message, hover)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)).create();
    }

    public static void sendClickOpenURLChat(Player player, String message, String hover, String url) {
        player.spigot().sendMessage(getClickOpenURLChat(message, hover, url));
    }

    public static TextComponent getClickOpenURLChat(String message, String hover, String url) {
        return new ChatComponent(getHoverChat(message, hover)).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)).create();
    }

    public static void sendSuggestCommand(Player player, String message, String hover, String command) {
        player.spigot().sendMessage(getSuggestCommand(message, hover, command));
    }

    public static TextComponent getSuggestCommand(String message, String hover, String command) {
        return new ChatComponent(getHoverChat(message, hover)).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)).create();
    }

    private static class ChatComponent{

        private TextComponent component;

        public ChatComponent(TextComponent component){
            this.component = component;
        }

        public ChatComponent(String message){
            this(new TextComponent(message));
        }

        public ChatComponent setHoverEvent(HoverEvent event){
            this.component.setHoverEvent(event);
            return this;
        }

        public ChatComponent setClickEvent(ClickEvent event){
            this.component.setClickEvent(event);
            return this;
        }

        public TextComponent create(){
            return component;
        }
    }
}


