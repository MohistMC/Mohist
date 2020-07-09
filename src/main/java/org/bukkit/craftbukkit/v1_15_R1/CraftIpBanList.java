package org.bukkit.craftbukkit.v1_15_R1;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.IPBanList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

public class CraftIpBanList implements org.bukkit.BanList {
    private final IPBanList list;

    public CraftIpBanList(IPBanList list) {
        this.list = list;
    }

    @Override
    public org.bukkit.BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        IPBanEntry entry = (IPBanEntry) list.getEntry(target);
        if (entry == null) {
            return null;
        }

        return new CraftIpBanEntry(target, entry, list);
    }

    @Override
    public org.bukkit.BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");

        IPBanEntry entry = new IPBanEntry(target, new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);

        list.addEntry(entry);

        try {
            list.writeChanges();
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-ips.json, {0}", ex.getMessage());
        }

        return new CraftIpBanEntry(target, entry, list);
    }

    @Override
    public Set<org.bukkit.BanEntry> getBanEntries() {
        ImmutableSet.Builder<org.bukkit.BanEntry> builder = ImmutableSet.builder();
        for (String target : list.getKeys()) {
            builder.add(new CraftIpBanEntry(target, (IPBanEntry) list.getEntry(target), list));
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
