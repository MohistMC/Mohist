package io.papermc.paper.chat;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A chat composer is responsible for composing chat messages sent by {@link Player}s to the server.
 */
@FunctionalInterface
public interface ChatComposer {
    ChatComposer DEFAULT = (player, displayName, message) -> Component.translatable("chat.type.text", displayName, message);

    /**
     * Composes a chat message.
     *
     * @param source      the message source
     * @param displayName the display name of the {@link Player} sending the message
     * @param message     the chat message
     * @return a composed chat message
     */
    @NotNull
    Component composeChat(final @NotNull Player source, final @NotNull Component displayName, final @NotNull Component message);
}
