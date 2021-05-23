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
