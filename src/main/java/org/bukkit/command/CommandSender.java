package org.bukkit.command;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandSender extends net.kyori.adventure.audience.Audience, Permissible { // Paper

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     * @see #sendMessage(net.kyori.adventure.text.Component)
     */
    public void sendMessage(@NotNull String message);

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     * @see #sendMessage(net.kyori.adventure.text.Component)
     */
    public void sendMessage(@NotNull String[] messages);

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     * @param sender  The sender of this message
     * @see #sendMessage(net.kyori.adventure.identity.Identified, net.kyori.adventure.text.Component)
     */
    public void sendMessage(@Nullable UUID sender, @NotNull String message);

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     * @param sender   The sender of this message
     * @see #sendMessage(net.kyori.adventure.identity.Identified, net.kyori.adventure.text.Component)
     */
    public void sendMessage(@Nullable UUID sender, @NotNull String[] messages);

    /**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     */
    @NotNull
    public Server getServer();

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    @NotNull
    public String getName();

    // Spigot start
    public class Spigot {

        /**
         * Sends this sender a chat component.
         *
         * @param component the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the sender.
         *
         * @param components the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends this sender a chat component.
         *
         * @param component the components to send
         * @param sender    the sender of the message
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the sender.
         *
         * @param components the components to send
         * @param sender     the sender of the message
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @NotNull
    Spigot spigot();
    // Spigot end

    // Paper start
    @Override
    default void sendMessage(final @NotNull net.kyori.adventure.identity.Identity identity, final @NotNull net.kyori.adventure.text.Component message, final @NotNull net.kyori.adventure.audience.MessageType type) {
        this.sendMessage(org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().serialize(message));
    }
    // Paper end
}
