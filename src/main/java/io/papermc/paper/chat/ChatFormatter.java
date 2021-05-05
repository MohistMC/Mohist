package io.papermc.paper.chat;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A chat formatter is responsible for the formatting of chat messages sent by {@link Player}s to the server.
 *
 * @deprecated in favour of {@link ChatComposer}
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
     */
    @Deprecated
    @NotNull
    Component chat(final @NotNull Component displayName, final @NotNull Component message);
}
