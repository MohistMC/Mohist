package org.bukkit.craftbukkit.v1_16_R3.scoreboard;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Collection;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

public final class CraftScoreboard implements org.bukkit.scoreboard.Scoreboard {
    final Scoreboard board;

    CraftScoreboard(Scoreboard board) {
        this.board = board;
    }

    @Override
    public CraftObjective registerNewObjective(String name, String criteria) throws IllegalArgumentException {
        return registerNewObjective(name, criteria, name);
    }

    // Paper start
    @Override
    public CraftObjective registerNewObjective(String name, String criteria, net.kyori.adventure.text.Component displayName) {
        return registerNewObjective(name, criteria, displayName, org.bukkit.scoreboard.RenderType.INTEGER);
    }

    @Override
    public CraftObjective registerNewObjective(String name, String criteria, net.kyori.adventure.text.Component displayName, RenderType renderType) {
        if (displayName == null) {
            displayName = net.kyori.adventure.text.Component.empty();
        }
        Validate.notNull(name, "Objective name cannot be null");
        Validate.notNull(criteria, "Criteria cannot be null");
        Validate.notNull(displayName, "Display name cannot be null");
        Validate.notNull(renderType, "RenderType cannot be null");
        Validate.isTrue(name.length() <= 16, "The name '" + name + "' is longer than the limit of 16 characters");
        Validate.isTrue(board.getObjective(name) == null, "An objective of name '" + name + "' already exists");
        CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
        ScoreObjective objective = board.addObjective(name, craftCriteria.criteria, io.papermc.paper.adventure.PaperAdventure.asVanilla(displayName), CraftScoreboardTranslations.fromBukkitRender(renderType));
        return new CraftObjective(this, objective);
    }
    // Paper end

    @Override
    public CraftObjective registerNewObjective(String name, String criteria, String displayName) throws IllegalArgumentException {
        return registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    @Override
    public CraftObjective registerNewObjective(String name, String criteria, String displayName, RenderType renderType) throws IllegalArgumentException {
        /*Validate.notNull(name, "Objective name cannot be null"); // Paper
        Validate.notNull(criteria, "Criteria cannot be null");
        Validate.notNull(displayName, "Display name cannot be null");
        Validate.notNull(renderType, "RenderType cannot be null");
        Validate.isTrue(name.length() <= 16, "The name '" + name + "' is longer than the limit of 16 characters");
        Validate.isTrue(displayName.length() <= 128, "The display name '" + displayName + "' is longer than the limit of 128 characters");
        Validate.isTrue(board.getObjective(name) == null, "An objective of name '" + name + "' already exists");

        CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
        ScoreObjective objective = board.addObjective(name, craftCriteria.criteria, CraftChatMessage.fromStringOrNull(displayName), CraftScoreboardTranslations.fromBukkitRender(renderType));
        return new CraftObjective(this, objective);*/ // Paper
        return registerNewObjective(name, criteria, io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(displayName), renderType); // Paper


    }

    @Override
    public Objective getObjective(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Name cannot be null");
        ScoreObjective nms = board.getObjective(name);
        return nms == null ? null : new CraftObjective(this, nms);
    }

    @Override
    public ImmutableSet<Objective> getObjectivesByCriteria(String criteria) throws IllegalArgumentException {
        Validate.notNull(criteria, "Criteria cannot be null");

        ImmutableSet.Builder<Objective> objectives = ImmutableSet.builder();
        for (ScoreObjective netObjective : (Collection<ScoreObjective>) this.board.getObjectives()) {
            CraftObjective objective = new CraftObjective(this, netObjective);
            if (objective.getCriteria().equals(criteria)) {
                objectives.add(objective);
            }
        }
        return objectives.build();
    }

    @Override
    public ImmutableSet<Objective> getObjectives() {
        return ImmutableSet.copyOf(Iterables.transform((Collection<ScoreObjective>) this.board.getObjectives(), new Function<ScoreObjective, Objective>() {

            @Override
            public Objective apply(ScoreObjective input) {
                return new CraftObjective(CraftScoreboard.this, input);
            }
        }));
    }

    @Override
    public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Display slot cannot be null");
        ScoreObjective objective = board.getDisplayObjective(CraftScoreboardTranslations.fromBukkitSlot(slot));
        if (objective == null) {
            return null;
        }
        return new CraftObjective(this, objective);
    }

    @Override
    public ImmutableSet<Score> getScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");

        return getScores(player.getName());
    }

    @Override
    public ImmutableSet<Score> getScores(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");

        ImmutableSet.Builder<Score> scores = ImmutableSet.builder();
        for (ScoreObjective objective : (Collection<ScoreObjective>) this.board.getObjectives()) {
            scores.add(new CraftScore(new CraftObjective(this, objective), entry));
        }
        return scores.build();
    }

    @Override
    public void resetScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");

        resetScores(player.getName());
    }

    @Override
    public void resetScores(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");

        for (ScoreObjective objective : (Collection<ScoreObjective>) this.board.getObjectives()) {
            board.resetPlayerScore(entry, objective);
        }
    }

    @Override
    public Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");

        ScorePlayerTeam team = board.getPlayersTeam(player.getName());
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public Team getEntryTeam(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");

        ScorePlayerTeam team = board.getPlayersTeam(entry);
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public Team getTeam(String teamName) throws IllegalArgumentException {
        Validate.notNull(teamName, "Team name cannot be null");

        ScorePlayerTeam team = board.getPlayerTeam(teamName);
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public ImmutableSet<Team> getTeams() {
        return ImmutableSet.copyOf(Iterables.transform((Collection<ScorePlayerTeam>) this.board.getPlayerTeams(), new Function<ScorePlayerTeam, Team>() {

            @Override
            public Team apply(ScorePlayerTeam input) {
                return new CraftTeam(CraftScoreboard.this, input);
            }
        }));
    }

    @Override
    public Team registerNewTeam(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Team name cannot be null");
        Validate.isTrue(name.length() <= 16, "Team name '" + name + "' is longer than the limit of 16 characters");
        Validate.isTrue(board.getPlayerTeam(name) == null, "Team name '" + name + "' is already in use");

        return new CraftTeam(this, board.addPlayerTeam(name));
    }

    @Override
    public ImmutableSet<OfflinePlayer> getPlayers() {
        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (Object playerName : board.getObjectiveNames()) {
            players.add(Bukkit.getOfflinePlayer(playerName.toString()));
        }
        return players.build();
    }

    @Override
    public ImmutableSet<String> getEntries() {
        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (Object entry : board.getObjectiveNames()) {
            entries.add(entry.toString());
        }
        return entries.build();
    }

    @Override
    public void clearSlot(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Slot cannot be null");
        board.setDisplayObjective(CraftScoreboardTranslations.fromBukkitSlot(slot), null);
    }

    // CraftBukkit method
    public Scoreboard getHandle() {
        return board;
    }
}
