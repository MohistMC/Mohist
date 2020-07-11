package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.management.UserListIPBans;
import net.minecraft.server.management.UserListIPBansEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import red.mohist.Mohist;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Set;

public class CraftIpBanList implements org.bukkit.BanList {
    private final UserListIPBans list;

    public CraftIpBanList(UserListIPBans list) {
        this.list = list;
    }

    @Override
    public org.bukkit.BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        UserListIPBansEntry entry = list.getEntry(target);
        if (entry == null) {
            return null;
        }

        return new CraftIpBanEntry(target, entry, list);
    }

    @Override
    public org.bukkit.BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");

        UserListIPBansEntry entry = new UserListIPBansEntry(target, new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);

        list.addEntry(entry);

        try {
            list.writeChanges();
        } catch (IOException ex) {
            Mohist.LOGGER.error("Failed to save banned-ips.json, {0}", ex.getMessage());
        }

        return new CraftIpBanEntry(target, entry, list);
    }

    @Override
    public Set<org.bukkit.BanEntry> getBanEntries() {
        ImmutableSet.Builder<org.bukkit.BanEntry> builder = ImmutableSet.builder();
        for (String target : list.getKeys()) {
            builder.add(new CraftIpBanEntry(target, list.getEntry(target), list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");

        return list.isBanned(InetSocketAddress.createUnresolved(target, 0));
    }

    @Override
    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");

        list.removeEntry(target);
    }
}
