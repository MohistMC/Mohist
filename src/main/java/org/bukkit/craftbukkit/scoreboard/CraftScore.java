package org.bukkit.craftbukkit.scoreboard;

import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

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
        net.minecraft.scoreboard.Scoreboard board = objective.checkState().board;

        if (board.getObjectiveNames().contains(entry)) { // Lazy
            Map<String, net.minecraft.scoreboard.Score> scores = board.func_96510_d(entry);
            net.minecraft.scoreboard.Score score = scores.get(objective.getHandle());
            if (score != null) { // Lazy
                return score.getScorePoints();
            }
        }

        return 0; // Lazy
    }

    public void setScore(int score) throws IllegalStateException {
        objective.checkState().board.func_96529_a(entry, objective.getHandle()).setScorePoints(score);
    }

    public CraftScoreboard getScoreboard() {
        return objective.getScoreboard();
    }
}
