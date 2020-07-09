package org.bukkit.craftbukkit.v1_15_R1;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import net.minecraft.server.management.ProfileBanEntry;
import net.minecraft.server.management.BanList;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

public class CraftProfileBanList implements org.bukkit.BanList {
    private final BanList list;

    public CraftProfileBanList(BanList list){
        this.list = list;
    }

    @Override
    public org.bukkit.BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = getProfile(target);
        if (profile == null) {
            return null;
        }

        ProfileBanEntry entry = (ProfileBanEntry) list.getEntry(profile);
        if (entry == null) {
            return null;
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public org.bukkit.BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");

        GameProfile profile = getProfile(target);
        if (profile == null) {
            return null;
        }

        ProfileBanEntry entry = new ProfileBanEntry(profile, new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);

        list.addEntry(entry);

        try {
            list.writeChanges();
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ex.getMessage());
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public Set<org.bukkit.BanEntry> getBanEntries() {
        ImmutableSet.Builder<org.bukkit.BanEntry> builder = ImmutableSet.builder();

        for (UserListEntry entry : list.getEntries()) {
            GameProfile profile = (GameProfile) entry.getValue();
            builder.add(new CraftProfileBanEntry(profile, (ProfileBanEntry) entry, list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = getProfile(target);
        if (profile == null) {
            return false;
        }

        return list.isBanned(profile);
    }

    @Override
    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = getProfile(target);
        list.removeEntry(profile);
    }

    private GameProfile getProfile(String target) {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(target);
        } catch (IllegalArgumentException ex) {
            //
        }

        return (uuid != null) ? MinecraftServer.getServer().getPlayerProfileCache().getProfileByUUID(uuid) : MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(target);
    }
}
