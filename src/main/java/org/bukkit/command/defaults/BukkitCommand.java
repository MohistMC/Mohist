package org.bukkit.command.defaults;

import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BukkitCommand extends Command {
    protected BukkitCommand(@NotNull String name) {
        super(name);
    }

    protected BukkitCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}
