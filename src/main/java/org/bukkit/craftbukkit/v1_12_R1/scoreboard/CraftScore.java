package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Map;

/**
 * TL;DR: This class is special and lazily grabs a handle...
 * ...because a handle is a full fledged (I think permanent) hashMap for the associated name.
 * <p>
 * Also, as an added perk, a CraftScore will (intentionally) stay a valid reference so long as objective is valid.
 */
final class CraftScore implements Score {
    private final String entry;
    private final CraftObjective objective;

    CraftScore(CraftObjective objective, String entry) {
        this.objective = objective;
        this.entry = entry;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(entry);
    }

    public String getEntry() {
        return entry;
    }

    public Objective getObjective() {
        return objective;
    }

    public int getScore() throws IllegalStateException {
        Scoreboard board = objective.checkState().board;

        if (board.getObjectiveNames().contains(entry)) { // Lazy
            Map<ScoreObjective, net.minecraft.scoreboard.Score> scores = board.getObjectivesForEntity(entry);
            net.minecraft.scoreboard.Score score = scores.get(objective.getHandle());
            if (score != null) { // Lazy
                return score.getScorePoints();
            }
        }

        return 0; // Lazy
    }

    public void setScore(int score) throws IllegalStateException {
        objective.checkState().board.getOrCreateScore(entry, objective.getHandle()).setScorePoints(score);
    }

    @Override
    public boolean isScoreSet() throws IllegalStateException {
        Scoreboard board = objective.checkState().board;

        return board.getObjectiveNames().contains(entry) && board.getObjectivesForEntity(entry).containsKey(objective.getHandle());
    }

    public CraftScoreboard getScoreboard() {
        return objective.getScoreboard();
    }
}
