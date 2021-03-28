package jackgnuk.creatorgames.Scoreboard;

import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Data.Connector;
import jackgnuk.creatorgames.Model.PlayerStats;
import jackgnuk.creatorgames.Model.PlayerWrapper;
import jackgnuk.creatorgames.Model.TeamRegister;
import jackgnuk.creatorgames.Util.Sorter;
import me.wazup.partygames.Enums;
import me.wazup.partygames.PartyGames;
import me.wazup.partygames.PartyGamesAPI;
import me.wazup.partygames.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {
    private final CreatorGames instance;

    private final int MAX_NAME_LENGTH = 20;

    int cycle = 10;

    public Leaderboard(CreatorGames instance) {
        this.instance = instance;
    }

    public void ScoreboardUpdater() {
        BukkitTask counter = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                count += 1;

                if (count >= cycle) {
                    for(PlayerWrapper pw : instance.PlayerManager.GetPlayers()) {
                        GiveScoreboard(pw.player);
                    }
                    count = 0;
                }
            }
        }.runTaskTimerAsynchronously(instance, 0, 20);
    }

    public void GiveScoreboard(Player player) {
        TitleManagerAPI titleApi = instance.TitleManagerAPI;
        PartyGamesAPI api = PartyGames.api;
        int personalCoins = 0;

        PlayerData pd = api.getPlayerData(player);
        personalCoins = pd.getCoins(player);

        Connector conn = instance.Config.CreateConnector();

        HashMap<String, String> data = api.getAllPlayersData();
        List<TeamRegister> teams = new ArrayList<>();
        HashMap<String, Integer> coinsToTeam = new HashMap<>();

        for (TeamRegister team : instance.Config.getTeams()) {
            TeamRegister register = conn.GetTeam(team.ID);
            coinsToTeam.put(register.ID, 0);

            for (PlayerStats ps: register.Players) {
                Map.Entry<String, String> datum = data.entrySet().stream().filter(d -> d.getKey().equals(ps.Name)).findFirst().orElse(null);

                if (datum != null) {
                    String[] values = datum.getValue().split(":");
                    int coins = Integer.parseInt(values[Enums.Stat.COINS.ordinal()]);
                    if (coinsToTeam.containsKey(register.ID)) {
                        Integer c = coinsToTeam.get(register.ID);
                        c += coins;
                        coinsToTeam.put(register.ID, c);
                    } else {
                        coinsToTeam.put(register.ID, coins);
                    }
                }
            }
            teams.add(register);
        }


        titleApi.giveScoreboard(player);
        titleApi.setScoreboardTitle(player, "汉");

        Map<String, Integer> m  = Sorter.sortByValue(coinsToTeam, false);
        int count = 1;
        int teamCount = 0;

        int nameLen = "LEADERBOARD".length();
        int spaces = MAX_NAME_LENGTH - nameLen;
        StringBuilder s = new StringBuilder();
        s.append("LEADERBOARD");

        titleApi.setScoreboardValue(player, count++, "");
        titleApi.setScoreboardValue(player, count++, "" + ChatColor.GOLD + ChatColor.BOLD + s);
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            TeamRegister r = teams.stream().filter(reg -> reg.ID.equals(e.getKey())).findFirst().orElse(null);
            assert r != null;

            teamCount++;
            if (teamCount <= 4) {
                nameLen = r.TeamName.length();
                spaces = MAX_NAME_LENGTH - nameLen;
                if (spaces < 0) spaces = 0;

                s = new StringBuilder(" " + teamCount + ". " + r.Color + r.TeamName + " ");
                for (int i = 0; i < spaces; i++) s.append("ꈁ");
                s.append(ChatColor.YELLOW).append(String.format("%04d", coinsToTeam.getOrDefault(r.ID, 0))).append(" 沉");
                titleApi.setScoreboardValue(player, count++, s.toString());
            }
        }

        titleApi.setScoreboardValue(player, count++, "");
        titleApi.setScoreboardValue(player, count, "ꈁꈁꈁꈁꈁꈁ明明书");
    }
}
