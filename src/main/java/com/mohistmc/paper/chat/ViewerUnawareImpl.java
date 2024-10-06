package com.mohistmc.paper.chat;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

sealed class ViewerUnawareImpl implements ChatRenderer, ChatRenderer.ViewerUnaware permits ViewerUnawareImpl.Default {
    private final ViewerUnaware unaware;
    private @Nullable Component message;

    ViewerUnawareImpl(final ViewerUnaware unaware) {
        this.unaware = unaware;
    }

    @Override
    public @NotNull Component render(final @NotNull Player source, final @NotNull Component sourceDisplayName, final @NotNull Component message, final @NotNull Audience viewer) {
        return this.render(source, sourceDisplayName, message);
    }

    @Override
    public @NotNull Component render(final @NotNull Player source, final @NotNull Component sourceDisplayName, final @NotNull Component message) {
        if (this.message == null) {
            this.message = this.unaware.render(source, sourceDisplayName, message);
        }
        return this.message;
    }

    static final class Default extends ViewerUnawareImpl implements ChatRenderer.Default {
        Default(final ViewerUnaware unaware) {
            super(unaware);
        }
    }
}
