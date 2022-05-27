/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
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

package com.mohistmc.forge;

import com.mohistmc.api.event.BukkitHookForgeEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import org.bukkit.Bukkit;

public class CustomEventBus extends EventBus {

    public static final CustomEventBus BUS = new CustomEventBus(BusBuilder.builder());
    public CustomEventBus(BusBuilder busBuilder) {
        super(busBuilder);
    }

    @Override
    public boolean post(Event event)
    {
        if (Bukkit.getServer() != null) {
            BukkitHookForgeEvent bukkitHookForgeEvent = new BukkitHookForgeEvent(event);
            if (bukkitHookForgeEvent.getHandlers().getRegisteredListeners().length > 0) {
                Bukkit.getPluginManager().callEvent(bukkitHookForgeEvent);
            }
        }
        return super.post(event);
    }
}
