/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.player;

import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.jetbrains.annotations.NotNull;

public class PlayerContainerEvent extends PlayerEvent
{
    private final AbstractContainerMenu container;
    public PlayerContainerEvent(Player player, AbstractContainerMenu container)
    {
        super(player);
        this.container = container;
    }

    public static class Open extends PlayerContainerEvent
    {
        public Open(Player player, AbstractContainerMenu container)
        {
            super(player, container);
        }
    }
    public static class Close extends PlayerContainerEvent
    {
        private final AtomicReference<Reason> reason = new AtomicReference<>(Reason.UNKNOWN);
        @NotNull
        public Reason getReason() {
            return reason.getAndSet(Reason.UNKNOWN);
        }

        public Close reason(Reason reason) {
            this.reason.set(reason);
            return this;
        }

        public Close(Player player, AbstractContainerMenu container)
        {
            super(player, container);
        }
    }

    public AbstractContainerMenu getContainer()
    {
        return container;
    }
}
