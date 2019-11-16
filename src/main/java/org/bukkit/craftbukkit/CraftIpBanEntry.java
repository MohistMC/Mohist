package org.bukkit.craftbukkit;

import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.BanList;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.util.Date;

public final class CraftIpBanEntry implements org.bukkit.BanEntry {
    private final BanList list;
    private final String target;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    public CraftIpBanEntry(String target, IPBanEntry entry, BanList list) {
        this.list = list;
        this.target = target;
        this.created = entry.getCreated() != null ? new Date(entry.getCreated().getTime()) : null;
        this.source = entry.getSource();
        this.expiration = entry.getBanEndDate() != null ? new Date(entry.getBanEndDate().getTime()) : null;
        this.reason = entry.getBanReason();
    }

    @Override
    public String getTarget() {
        return this.target;
    }

    @Override
    public Date getCreated() {
        return this.created == null ? null : (Date) this.created.clone();
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public Date getExpiration() {
        return this.expiration == null ? null : (Date) this.expiration.clone();
    }

    @Override
    public void setExpiration(Date expiration) {
        if (expiration != null && expiration.getTime() == new Date(0, 0, 0, 0, 0, 0).getTime()) {
            expiration = null; // Forces "forever"
        }

        this.expiration = expiration;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void save() {
        IPBanEntry entry = new IPBanEntry(target, this.created, this.source, this.expiration, this.reason);
        this.list.func_152687_a(entry);
        try {
            this.list.func_152678_f();
        } catch (IOException ex) {
            MinecraftServer.getLogger().error("Failed to save banned-ips.json, " + ex.getMessage());
        }
    }
}
