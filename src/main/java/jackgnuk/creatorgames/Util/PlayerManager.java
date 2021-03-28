package jackgnuk.creatorgames.Util;

import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Data.Connector;
import jackgnuk.creatorgames.Data.DatabaseConnector;
import jackgnuk.creatorgames.Data.StorageConnector;
import jackgnuk.creatorgames.Model.PlayerStats;
import jackgnuk.creatorgames.Model.PlayerWrapper;
import jackgnuk.creatorgames.Scoreboard.Leaderboard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final CreatorGames instance;

    public PlayerManager(CreatorGames instance) {
        this.instance = instance;
        this.leaderboardHandler = new Leaderboard(instance);

        leaderboardHandler.ScoreboardUpdater();
    }

    private final List<PlayerWrapper> players = new ArrayList<>();

    private final Leaderboard leaderboardHandler;

    public void OnJoin(Player player) {
        leaderboardHandler.GiveScoreboard(player);
    }

    public List<PlayerWrapper> GetPlayers() {
        return players;
    }

    public PlayerWrapper GetPlayer(String id) {
        if (players.size() <= 0) return null;
        for (PlayerWrapper playerWrapper : players) {
            String tmpUUID = playerWrapper.player.getUniqueId().toString();
            String tmpName = playerWrapper.player.getDisplayName();

            if (tmpUUID.equals(id) || tmpName.equals(id)) {
                return playerWrapper;
            }
        }

        return null;
    }

    public PlayerWrapper GetPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (players.size() > 0) {
            for (PlayerWrapper playerWrapper : players) {
                String tmpUUID = playerWrapper.player.getUniqueId().toString();
                String tmpName = playerWrapper.player.getDisplayName();

                if (tmpUUID.equals(uuid) || tmpName.equals(player.getName())) {
                    return playerWrapper;
                }
            }
        }

        PlayerWrapper wrapper = new PlayerWrapper(player);
        Connector conn = instance.Config.CreateConnector();


        PlayerStats stats = conn.GetPlayer(player.getUniqueId().toString());

        if (stats == null) {
            stats = new PlayerStats();
            stats.UUID = player.getUniqueId().toString();
            stats.Name = player.getName();
            conn.AddPlayer(stats);
        }

        wrapper.playerStats = stats;

        if (!stats.Name.equals(player.getName())) {
            //Player has changed name, reflect this in storage
            wrapper.playerStats.Name = player.getName();
            conn.UpdatePlayer(player.getUniqueId().toString(), wrapper.playerStats);
        }
        AddPlayer(wrapper);

        return wrapper;
    }

    public void AddPlayer(PlayerWrapper playerData) {
        players.add(playerData);
    }

    public void RemovePlayer(String uuid) {
        if (players.size() <= 0) return;
        for (int i = 0; i < players.size(); i++) {
            String tUUID = players.get(i).player.getUniqueId().toString();
            if (tUUID.equals(uuid)) {
                //players.get(i).HandleExit();
                players.remove(i);
                return;
            }
        }
    }
}
