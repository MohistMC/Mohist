package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import red.mohist.Mohist;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

public class CraftProfileBanList implements org.bukkit.BanList {
    private final UserListBans list;

    public CraftProfileBanList(UserListBans list) {
        this.list = list;
    }

    @Override
    public org.bukkit.BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        if (profile == null) {
            return null;
        }

        UserListBansEntry entry = list.getEntry(profile);
        if (entry == null) {
            return null;
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public org.bukkit.BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");

        GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        if (profile == null) {
            return null;
        }

        UserListBansEntry entry = new UserListBansEntry(profile, new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);

        list.addEntry(entry);

        try {
            list.writeChanges();
        } catch (IOException ex) {
            Mohist.LOGGER.error("Failed to save banned-players.json, {0}", ex.getMessage());
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public Set<org.bukkit.BanEntry> getBanEntries() {
        ImmutableSet.Builder<org.bukkit.BanEntry> builder = ImmutableSet.builder();

        for (UserListEntry entry : list.getValuesCB()) {
            GameProfile profile = (GameProfile) entry.getValue();
            builder.add(new CraftProfileBanEntry(profile, (UserListBansEntry) entry, list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        if (profile == null) {
            return false;
        }

        return list.isBanned(profile);
    }

    @Override
    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        list.removeEntry(profile);
    }
}
