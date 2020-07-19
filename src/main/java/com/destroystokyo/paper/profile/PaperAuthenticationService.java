package com.destroystokyo.paper.profile;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import java.net.Proxy;

public class PaperAuthenticationService extends YggdrasilAuthenticationService {
    public PaperAuthenticationService(Proxy proxy, String clientToken) {
        super(proxy, clientToken);
    }

    @Override
    public UserAuthentication createUserAuthentication(Agent agent) {
        return new PaperUserAuthentication(this, agent);
    }

    @Override
    public MinecraftSessionService createMinecraftSessionService() {
        return new PaperMinecraftSessionService(this);
    }

    @Override
    public GameProfileRepository createProfileRepository() {
        return new PaperGameProfileRepository(this);
    }
}
