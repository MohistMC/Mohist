package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CraftScoreboardManager implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap<>();

    public CraftScoreboardManager(MinecraftServer minecraftserver, net.minecraft.scoreboard.Scoreboard scoreboardServer) {
        mainScoreboard = new CraftScoreboard(scoreboardServer);
        server = minecraftserver;
        scoreboards.add(mainScoreboard);
    }

    public CraftScoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    public CraftScoreboard getNewScoreboard() {
        CraftScoreboard scoreboard = new CraftScoreboard(new ServerScoreboard(server));
        scoreboards.add(scoreboard);
        return scoreboard;
    }

    // CraftBukkit method
    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = playerBoards.get(player);
        return (CraftScoreboard) (board == null ? getMainScoreboard() : board);
    }

    // CraftBukkit method
    public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) throws IllegalArgumentException {
        Validate.isTrue(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");

        CraftScoreboard scoreboard = (CraftScoreboard) bukkitScoreboard;
        net.minecraft.scoreboard.Scoreboard oldboard = getPlayerBoard(player).getHandle();
        net.minecraft.scoreboard.Scoreboard newboard = scoreboard.getHandle();
        EntityPlayerMP entityplayer = player.getHandle();

        if (oldboard == newboard) {
            return;
        }

        if (scoreboard == mainScoreboard) {
            playerBoards.remove(player);
        } else {
            playerBoards.put(player, scoreboard);
        }

        // Old objective tracking
        HashSet<ScoreObjective> removed = new HashSet<>();
        for (int i = 0; i < 3; ++i) {
            ScoreObjective scoreboardobjective = oldboard.getObjectiveInDisplaySlot(i);
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
                entityplayer.connection.sendPacket(new SPacketScoreboardObjective(scoreboardobjective, 1));
                removed.add(scoreboardobjective);
            }
        }

        // Old team tracking
        for (ScorePlayerTeam scoreboardteam : oldboard.getTeams()) {
            entityplayer.connection.sendPacket(new SPacketTeams(scoreboardteam, 1));
        }

        // The above is the reverse of the below method.
        server.getPlayerList().sendScoreboard((ServerScoreboard) newboard, player.getHandle());
    }

    // CraftBukkit method
    public void removePlayer(Player player) {
        playerBoards.remove(player);
    }

    // CraftBukkit method
    public Collection<Score> getScoreboardScores(IScoreCriteria criteria, String name, Collection<Score> collection) {
        for (CraftScoreboard scoreboard : scoreboards) {
            Scoreboard board = scoreboard.board;
            for (ScoreObjective objective : board.getObjectivesFromCriteria(criteria)) {
                collection.add(board.getOrCreateScore(name, objective));
            }
        }
        return collection;
    }
}
