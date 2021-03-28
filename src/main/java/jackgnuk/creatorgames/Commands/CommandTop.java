package jackgnuk.creatorgames.Commands;

import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Data.Connector;
import jackgnuk.creatorgames.Data.DatabaseConnector;
import jackgnuk.creatorgames.Data.StorageConnector;
import jackgnuk.creatorgames.Model.PlayerStats;
import jackgnuk.creatorgames.Model.TeamRegister;
import jackgnuk.creatorgames.Util.Sorter;
import me.wazup.partygames.Enums;
import me.wazup.partygames.PartyGames;
import me.wazup.partygames.PartyGamesAPI;
import me.wazup.partygames.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandTop implements CommandExecutor {
    private final CreatorGames instance;
    private final int MAX_NAME_LENGTH = 20;

    public CommandTop(CreatorGames instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PartyGamesAPI api = PartyGames.api;
        int personalCoins = 0;

        if (sender instanceof Player) {
            PlayerData pd = api.getPlayerData((Player) sender);
            personalCoins = pd.getCoins((Player) sender);
        }


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

        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                  " + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + " Creator Games " + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                  ");
        sender.sendMessage( "");

        int nameLen = "Personal".length();
        int spaces = MAX_NAME_LENGTH - nameLen;

        StringBuilder s;

        Map<String, Integer> m  = Sorter.sortByValue(coinsToTeam, false);
        int count = 0;
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            TeamRegister r = teams.stream().filter(reg -> reg.ID.equals(e.getKey())).findFirst().orElse(null);
            assert r != null;

            nameLen = r.TeamName.length();
            spaces = MAX_NAME_LENGTH - nameLen;
            if (spaces < 0) spaces = 0;

            s = new StringBuilder(r.Color + r.TeamName);
            //if (count % 2 == 0) s.append("明");
            for (int i = 0; i < spaces; i++) s.append("ꈁ");
            s.append(ChatColor.YELLOW + "ꈁ⭐ꈁ").append(String.format("%04d", coinsToTeam.getOrDefault(r.ID, 0)));

            sender.sendMessage(s.toString());
        }

        sender.sendMessage("");
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                                                               ");
        return true;
    }
}
