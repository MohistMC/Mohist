package io.papermc.paper.chat;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.NotNull;

/**
 * A chat renderer is responsible for rendering chat messages sent by {@link Player}s to the server.
 */
@FunctionalInterface
public interface ChatRenderer {
    /**
     * Renders a chat message. This will be called once for each receiving {@link Audience}.
     *
     * @param source            the message source
     * @param sourceDisplayName the display name of the source player
     * @param message           the chat message
     * @param viewer            the receiving {@link Audience}
     * @return a rendered chat message
     */
    @NotNull
    Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer);

    /**
     * Create a new instance of the default {@link ChatRenderer}.
     *
     * @return a new {@link ChatRenderer}
     */
    @NotNull
    static ChatRenderer defaultRenderer() {
        return viewerUnaware((source, sourceDisplayName, message) -> Component.translatable("chat.type.text", sourceDisplayName, message));
    }

    /**
     * Creates a new viewer-unaware {@link ChatRenderer}, which will render the chat message a single time,
     * displaying the same rendered message to every viewing {@link Audience}.
     *
     * @param renderer the viewer unaware renderer
     * @return a new {@link ChatRenderer}
     */
    @NotNull
    static ChatRenderer viewerUnaware(final @NotNull ViewerUnaware renderer) {
        return new ChatRenderer() {
            private @MonotonicNonNull Component message;

            @Override
            public @NotNull Component render(final @NotNull Player source, final @NotNull Component sourceDisplayName, final @NotNull Component message, final @NotNull Audience viewer) {
                if (this.message == null) {
                    this.message = renderer.render(source, sourceDisplayName, message);
                }
                return this.message;
            }
        };
    }

    /**
     * Similar to {@link ChatRenderer}, but without knowledge of the message viewer.
     *
     * @see ChatRenderer#viewerUnaware(ViewerUnaware)
     */
    interface ViewerUnaware {
        /**
         * Renders a chat message.
         *
         * @param source            the message source
         * @param sourceDisplayName the display name of the source player
         * @param message           the chat message
         * @return a rendered chat message
         */
        @NotNull
        Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message);
    }
}