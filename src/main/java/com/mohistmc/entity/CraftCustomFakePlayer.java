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

package com.mohistmc.entity;

import com.mohistmc.MohistMC;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CraftCustomFakePlayer extends CraftPlayer {

    public CraftCustomFakePlayer(CraftServer server, PlayerEntity entity) {
        super(server, FakePlayerFactory.get(server.getServer().getLevel(entity.level.dimension()), entity.getGameProfile()));
    }

    @Override
    public boolean isOp() {
        GameProfile profile = this.getHandle().getGameProfile();
        return profile != null && profile.getId() != null && super.isOp();
    }

    @Override
    public void setOp(boolean value) {
        MohistMC.LOGGER.warn(new Exception("Plugin attempt to setOp on FakePlayer."));
    }

    @Override
    @NotNull
    public GameMode getGameMode() {
        // If handle has gamemode, return it
        GameMode superGameMode = super.getGameMode();
        if (superGameMode != null) {
            return superGameMode;
        }

        // If It has owner, Use owner's gamemode
        Player owner = Bukkit.getPlayer(getUniqueId());
        if (owner != null) {
            superGameMode = owner.getGameMode();
        }

        return superGameMode == null ? Bukkit.getServer().getDefaultGameMode() : superGameMode;
    }

    @Override
    public boolean isFakePlayer(){
        return true;
    }
}
