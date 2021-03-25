package jackgnuk.creatorgames.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (((Player) sender));

            player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                  " + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + " Creator Games " + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                  ");
            player.sendMessage( "");
            player.sendMessage(ChatColor.WHITE + " TO BE IMPLEMENTED");
        }
        else {
            sender.sendMessage("You must be a player!");
        }
        return true;
    }
}
