package jackgnuk.creatorgames.Commands;

import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Data.Connector;
import jackgnuk.creatorgames.Model.PlayerStats;
import jackgnuk.creatorgames.Model.TeamRegister;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemoveTeammate implements CommandExecutor {
    private final CreatorGames instance;

    public CommandRemoveTeammate(CreatorGames instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("You have provided too many arguments. /removeteammate <teamID> <player>");
        } else {
            Connector conn = instance.Config.CreateConnector();
            String team = args[0];
            String player = args[1];

            Player p;
            try {
                p = Bukkit.getPlayer(player);
            } catch (Exception e) {
                sender.sendMessage("Player is not online! Player must be online to be remove from a team");
                return true;
            }

            PlayerStats s = new PlayerStats();
            assert p != null;
            s.UUID = p.getUniqueId().toString();
            s.Name = p.getName();

            TeamRegister teamName = instance.Config.getTeamFromID(team);

            try {
                conn.RemoveTeammate(s, teamName.TeamName);
                sender.sendMessage(player + " has been removed from " + teamName.TeamName);
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage("Failed to remove " + player + " from " + team);
            }
        }
        return true;
    }
}
