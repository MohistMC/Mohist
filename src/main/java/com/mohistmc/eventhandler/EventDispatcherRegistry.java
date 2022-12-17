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

package com.mohistmc.eventhandler;

import com.mohistmc.MohistMC;
import com.mohistmc.eventhandler.dispatcher.*;
import net.minecraftforge.common.MinecraftForge;

public class EventDispatcherRegistry {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new BucketEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new CommandsEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new WorldEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new ItemEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new BlockEventDispatcher());
        MohistMC.LOGGER.info("EventDispatcherRegistry initialized");
    }
}
