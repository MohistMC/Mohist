package io.papermc.paper.chat;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A chat composer is responsible for composing chat messages sent by {@link Player}s to the server.
 *
 * @deprecated for removal with 1.17, in favor of {@link ChatRenderer}
 */
@Deprecated
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
     * @deprecated for removal with 1.17
     */
    @Deprecated
    @NotNull
    Component composeChat(final @NotNull Player source, final @NotNull Component displayName, final @NotNull Component message);
}