package org.bukkit.craftbukkit.v1_20_R1.scoreboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mohistmc.paper.adventure.PaperAdventure;
import java.util.Collection;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

final class CraftTeam extends CraftScoreboardComponent implements Team {
    private final PlayerTeam team;

    CraftTeam(CraftScoreboard scoreboard, PlayerTeam team) {
        super(scoreboard);
        this.team = team;
    }

    @Override
    public String getName() {
        checkState();

        return team.getName();
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component displayName() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        return PaperAdventure.asAdventure(team.getDisplayName());
    }
    @Override
    public void displayName(net.kyori.adventure.text.Component displayName) throws IllegalStateException, IllegalArgumentException {
        if (displayName == null) displayName = net.kyori.adventure.text.Component.empty();
        CraftScoreboard scoreboard = checkState();
        team.setDisplayName(PaperAdventure.asVanilla(displayName));
    }
    @Override
    public net.kyori.adventure.text.Component prefix() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        return PaperAdventure.asAdventure(team.getPlayerPrefix());
    }
    @Override
    public void prefix(net.kyori.adventure.text.Component prefix) throws IllegalStateException, IllegalArgumentException {
        if (prefix == null) prefix = net.kyori.adventure.text.Component.empty();
        CraftScoreboard scoreboard = checkState();
        team.setPlayerPrefix(PaperAdventure.asVanilla(prefix));
    }
    @Override
    public net.kyori.adventure.text.Component suffix() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        return PaperAdventure.asAdventure(team.getPlayerSuffix());
    }
    @Override
    public void suffix(net.kyori.adventure.text.Component suffix) throws IllegalStateException, IllegalArgumentException {
        if (suffix == null) suffix = net.kyori.adventure.text.Component.empty();
        CraftScoreboard scoreboard = checkState();
        team.setPlayerSuffix(PaperAdventure.asVanilla(suffix));
    }
    @Override
    public boolean hasColor() {
        CraftScoreboard scoreboard = checkState();
        return this.team.getColor().getColor() != null;
    }
    @Override
    public net.kyori.adventure.text.format.TextColor color() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        if (team.getColor().getColor() == null) throw new IllegalStateException("Team colors must have hex values");
        net.kyori.adventure.text.format.TextColor color = net.kyori.adventure.text.format.TextColor.color(team.getColor().getColor());
        if (!(color instanceof net.kyori.adventure.text.format.NamedTextColor)) throw new IllegalStateException("Team doesn't have a NamedTextColor");
        return (net.kyori.adventure.text.format.NamedTextColor) color;
    }
    @Override
    public void color(net.kyori.adventure.text.format.NamedTextColor color) {
        CraftScoreboard scoreboard = checkState();
        if (color == null) {
            this.team.setColor(net.minecraft.ChatFormatting.RESET);
        } else {
            this.team.setColor(PaperAdventure.asVanilla(color));
        }
    }
    // Paper end


    @Override
    public String getDisplayName() {
        checkState();

        return CraftChatMessage.fromComponent(team.getDisplayName());
    }

    @Override
    public void setDisplayName(String displayName) {
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        checkState();

        team.setDisplayName(CraftChatMessage.fromString(displayName)[0]); // SPIGOT-4112: not nullable
    }

    @Override
    public String getPrefix() {
        checkState();

        return CraftChatMessage.fromComponent(team.getPlayerPrefix());
    }

    @Override
    public void setPrefix(String prefix) {
        Preconditions.checkArgument(prefix != null, "Prefix cannot be null");
        checkState();

        team.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix));
    }

    @Override
    public String getSuffix() {
        checkState();

        return CraftChatMessage.fromComponent(team.getPlayerSuffix());
    }

    @Override
    public void setSuffix(String suffix) {
        Preconditions.checkArgument(suffix != null, "Suffix cannot be null");
        checkState();

        team.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));
    }

    @Override
    public ChatColor getColor() {
        checkState();

        return CraftChatMessage.getColor(team.getColor());
    }

    @Override
    public void setColor(ChatColor color) {
        Preconditions.checkArgument(color != null, "Color cannot be null");
        checkState();

        team.setColor(CraftChatMessage.getColor(color));
    }

    @Override
    public boolean allowFriendlyFire() {
        checkState();

        return team.isAllowFriendlyFire();
    }

    @Override
    public void setAllowFriendlyFire(boolean enabled) {
        checkState();

        team.setAllowFriendlyFire(enabled);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        checkState();

        return team.canSeeFriendlyInvisibles();
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean enabled) {
        checkState();

        team.setSeeFriendlyInvisibles(enabled);
    }

    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        checkState();

        return notchToBukkit(team.getNameTagVisibility());
    }

    @Override
    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
        checkState();

        team.setNameTagVisibility(bukkitToNotch(visibility));
    }

    @Override
    public Set<OfflinePlayer> getPlayers() {
        checkState();

        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (String playerName : team.getPlayers()) {
            players.add(Bukkit.getOfflinePlayer(playerName));
        }
        return players.build();
    }

    @Override
    public Set<String> getEntries() {
        checkState();

        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (String playerName : team.getPlayers()) {
            entries.add(playerName);
        }
        return entries.build();
    }

    @Override
    public int getSize() {
        checkState();

        return team.getPlayers().size();
    }

    @Override
    public void addPlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        addEntry(player.getName());
    }

    @Override
    public void addEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        CraftScoreboard scoreboard = checkState();

        scoreboard.board.addPlayerToTeam(entry, team);
    }

    // Paper start
    @Override
    public void addEntities(java.util.Collection<org.bukkit.entity.Entity> entities) throws IllegalStateException, IllegalArgumentException {
        this.addEntries(entities.stream().map(entity -> ((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity) entity).getHandle().getScoreboardName()).toList());
    }

    @Override
    public void addEntries(java.util.Collection<String> entries) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entries != null, "Entries cannot be null");
        CraftScoreboard scoreboard = this.checkState();

        ((net.minecraft.server.ServerScoreboard) scoreboard.board).addPlayersToTeam(entries, this.team);
    }
    // Paper end

    @Override
    public boolean removePlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return removeEntry(player.getName());
    }

    @Override
    public boolean removeEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        CraftScoreboard scoreboard = checkState();

        if (!team.getPlayers().contains(entry)) {
            return false;
        }

        scoreboard.board.removePlayerFromTeam(entry, team);
        return true;
    }

    // Paper start
    @Override
    public boolean removeEntities(java.util.Collection<org.bukkit.entity.Entity> entities) throws IllegalStateException, IllegalArgumentException {
        return this.removeEntries(entities.stream().map(entity -> ((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity) entity).getHandle().getScoreboardName()).toList());
    }

    @Override
    public boolean removeEntries(java.util.Collection<String> entries) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entries != null, "Entry cannot be null");
        CraftScoreboard scoreboard = this.checkState();

        for (String entry : entries) {
            if (this.team.getPlayers().contains(entry)) {
                ((net.minecraft.server.ServerScoreboard) scoreboard.board).removePlayersFromTeam(entries, this.team);
                return true;
            }
        }

        return false;
    }
    // Paper end

    @Override
    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return hasEntry(player.getName());
    }

    @Override
    public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        checkState();

        return team.getPlayers().contains(entry);
    }

    @Override
    public void unregister() {
        CraftScoreboard scoreboard = checkState();

        scoreboard.board.removePlayerTeam(team);
    }

    @Override
    public OptionStatus getOption(Option option) {
        checkState();

        switch (option) {
            case NAME_TAG_VISIBILITY:
                return OptionStatus.values()[team.getNameTagVisibility().ordinal()];
            case DEATH_MESSAGE_VISIBILITY:
                return OptionStatus.values()[team.getDeathMessageVisibility().ordinal()];
            case COLLISION_RULE:
                return OptionStatus.values()[team.getCollisionRule().ordinal()];
            default:
                throw new IllegalArgumentException("Unrecognised option " + option);
        }
    }

    @Override
    public void setOption(Option option, OptionStatus status) {
        checkState();

        switch (option) {
            case NAME_TAG_VISIBILITY:
                team.setNameTagVisibility(net.minecraft.world.scores.Team.Visibility.values()[status.ordinal()]);
                break;
            case DEATH_MESSAGE_VISIBILITY:
                team.setDeathMessageVisibility(net.minecraft.world.scores.Team.Visibility.values()[status.ordinal()]);
                break;
            case COLLISION_RULE:
                team.setCollisionRule(net.minecraft.world.scores.Team.CollisionRule.values()[status.ordinal()]);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised option " + option);
        }
    }

    // Paper start
    @Override
    public void addEntity(org.bukkit.entity.Entity entity) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        this.addEntry(((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity) entity).getHandle().getScoreboardName());
    }

    @Override
    public boolean removeEntity(org.bukkit.entity.Entity entity) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        return this.removeEntry(((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity) entity).getHandle().getScoreboardName());
    }

    @Override
    public boolean hasEntity(org.bukkit.entity.Entity entity) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        return this.hasEntry(((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity) entity).getHandle().getScoreboardName());
    }
    // Paper end

    public static net.minecraft.world.scores.Team.Visibility bukkitToNotch(NameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return net.minecraft.world.scores.Team.Visibility.ALWAYS;
            case NEVER:
                return net.minecraft.world.scores.Team.Visibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    public static NameTagVisibility notchToBukkit(net.minecraft.world.scores.Team.Visibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return NameTagVisibility.ALWAYS;
            case NEVER:
                return NameTagVisibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return NameTagVisibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    @Override
    CraftScoreboard checkState() {
        Preconditions.checkState(getScoreboard().board.getPlayerTeam(team.getName()) != null, "Unregistered scoreboard component");

        return getScoreboard();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.team != null ? this.team.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftTeam other = (CraftTeam) obj;
        return !(this.team != other.team && (this.team == null || !this.team.equals(other.team)));
    }

}
