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
        sender.sendMessage(ChatColor.GRAY + "Personal " + ChatColor.YELLOW + "⭐ " + personalCoins);
        sender.sendMessage( "");

        Map<String, Integer> m  = Sorter.sortByValue(coinsToTeam, false);
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            TeamRegister r = teams.stream().filter(reg -> reg.ID.equals(e.getKey())).findFirst().orElse(null);
            assert r != null;
            sender.sendMessage(r.Color + r.TeamName + ChatColor.YELLOW + " ⭐ " + String.valueOf(coinsToTeam.getOrDefault(r.ID, 0)));
        }

        sender.sendMessage( "");
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                                                               ");
        return true;
    }
}
