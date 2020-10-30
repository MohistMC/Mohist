package org.bukkit.craftbukkit.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CraftScoreboardManager implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final net.minecraft.server.MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<CraftScoreboard>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap<CraftPlayer, CraftScoreboard>();

    public CraftScoreboardManager(net.minecraft.server.MinecraftServer minecraftserver, net.minecraft.scoreboard.Scoreboard scoreboardServer) {
        mainScoreboard = new CraftScoreboard(scoreboardServer);
        server = minecraftserver;
        scoreboards.add(mainScoreboard);
    }

    public CraftScoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    public CraftScoreboard getNewScoreboard() {
        if (Thread.currentThread() != net.minecraft.server.MinecraftServer.getServer().primaryThread) throw new IllegalStateException("Asynchronous scoreboard creation"); // Spigot
        CraftScoreboard scoreboard = new CraftScoreboard(new net.minecraft.scoreboard.ServerScoreboard(server));
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
        net.minecraft.entity.player.EntityPlayerMP entityplayer = player.getHandle();

        if (oldboard == newboard) {
            return;
        }

        if (scoreboard == mainScoreboard) {
            playerBoards.remove(player);
        } else {
            playerBoards.put(player, (CraftScoreboard) scoreboard);
        }

        // Old objective tracking
        HashSet<net.minecraft.scoreboard.ScoreObjective> removed = new HashSet<net.minecraft.scoreboard.ScoreObjective>();
        for (int i = 0; i < 3; ++i) {
            net.minecraft.scoreboard.ScoreObjective scoreboardobjective = oldboard.func_96539_a(i);
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
                entityplayer.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S3BPacketScoreboardObjective(scoreboardobjective, 1));
                removed.add(scoreboardobjective);
            }
        }

        // Old team tracking
        Iterator<?> iterator = oldboard.getTeams().iterator();
        while (iterator.hasNext()) {
            net.minecraft.scoreboard.ScorePlayerTeam scoreboardteam = (net.minecraft.scoreboard.ScorePlayerTeam) iterator.next();
            entityplayer.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S3EPacketTeams(scoreboardteam, 1));
        }

        // The above is the reverse of the below method.
        server.getConfigurationManager().func_96456_a((net.minecraft.scoreboard.ServerScoreboard) newboard, player.getHandle());
    }

    // CraftBukkit method
    public void removePlayer(Player player) {
        playerBoards.remove(player);
    }

    // CraftBukkit method
    public Collection<net.minecraft.scoreboard.Score> getScoreboardScores(net.minecraft.scoreboard.IScoreObjectiveCriteria criteria, String name, Collection<net.minecraft.scoreboard.Score> collection) {
        for (CraftScoreboard scoreboard : scoreboards) {
            net.minecraft.scoreboard.Scoreboard board = scoreboard.board;
            for (net.minecraft.scoreboard.ScoreObjective objective : (Iterable<net.minecraft.scoreboard.ScoreObjective>) board.func_96520_a(criteria)) {
                collection.add(board.func_96529_a(name, objective));
            }
        }
        return collection;
    }

    // CraftBukkit method
    public void updateAllScoresForList(net.minecraft.scoreboard.IScoreObjectiveCriteria criteria, String name, List<net.minecraft.entity.player.EntityPlayerMP> of) {
        for (net.minecraft.scoreboard.Score score : getScoreboardScores(criteria, name, new ArrayList<net.minecraft.scoreboard.Score>())) {
            score.func_96651_a(of);
        }
    }
}
