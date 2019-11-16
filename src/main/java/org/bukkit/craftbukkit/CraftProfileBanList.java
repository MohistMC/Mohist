package org.bukkit.craftbukkit;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListEntry;

import com.mojang.authlib.GameProfile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;

public class CraftProfileBanList implements org.bukkit.BanList {
    private final UserListBans list;

    public CraftProfileBanList(UserListBans list){
        this.list = list;
    }

    @Override
    public org.bukkit.BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(target);
        if (profile == null) {
            return null;
        }

        UserListBansEntry entry = (UserListBansEntry) list.func_152683_b(profile);
        if (entry == null) {
            return null;
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public org.bukkit.BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");

        GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(target);
        if (profile == null) {
            return null;
        }

        UserListBansEntry entry = new UserListBansEntry(profile, new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);

        list.func_152687_a(entry);

        try {
            list.func_152678_f();
        } catch (IOException ex) {
            MinecraftServer.getLogger().error("Failed to save banned-players.json, " + ex.getMessage());
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public Set<org.bukkit.BanEntry> getBanEntries() {
        ImmutableSet.Builder<org.bukkit.BanEntry> builder = ImmutableSet.builder();
        for (UserListEntry entry : list.getValues()) {
            GameProfile profile = (GameProfile) entry.func_152640_f(); // Should be getKey
            builder.add(new CraftProfileBanEntry(profile, (UserListBansEntry) entry, list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(target);
        if (profile == null) {
            return false;
        }

        return list.func_152702_a(profile);
    }

    @Override
    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(target);
        list.func_152684_c(profile);
    }
}
