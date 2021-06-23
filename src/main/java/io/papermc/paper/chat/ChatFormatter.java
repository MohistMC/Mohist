package io.papermc.paper.chat;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A chat formatter is responsible for the formatting of chat messages sent by {@link Player}s to the server.
 *
 * @deprecated for removal with 1.17, in favour of {@link ChatRenderer}
 */
@Deprecated
@FunctionalInterface
public interface ChatFormatter {
    @Deprecated
    ChatFormatter DEFAULT = (displayName, message) -> Component.translatable("chat.type.text", displayName, message);

    /**
     * Formats a chat message.
     *
     * @param displayName the display name of the {@link Player} sending the message
     * @param message     the chat message
     * @return a formatted chat message
     * @deprecated for removal with 1.17
     */
    @Deprecated
    @NotNull
    Component chat(final @NotNull Component displayName, final @NotNull Component message);
}
