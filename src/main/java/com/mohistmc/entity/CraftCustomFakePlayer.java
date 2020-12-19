package com.mohistmc.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public class CraftCustomFakePlayer extends CraftPlayer {

    public CraftCustomFakePlayer(CraftServer server, ServerPlayerEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isOp() {
        GameProfile profile = this.getHandle().getGameProfile();
        return profile != null && profile.getId() != null && super.isOp();
    }

    @Override
    public void setOp(boolean value) {
    }
}
