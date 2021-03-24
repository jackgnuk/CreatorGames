package jackgnuk.creatorgames;

import me.wazup.partygames.PartyGames;
import me.wazup.partygames.PartyGamesAPI;
import me.wazup.partygames.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;

public final class main extends JavaPlugin {
    private FileConfiguration customConfig;

    public main(FileConfiguration customConfig) {
        this.customConfig = customConfig;
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        File customConfigFile = new File(getDataFolder(), "custom.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("custom.yml", false);
        }
        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new listener(), this);
        createCustomConfig();
               //Fired when the server enables the plugin
        }

    @Override
    public void onDisable() {
        //Fired when the server stops and disables all plugins
    }





    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PartyGamesAPI api = PartyGames.api;
        Player player = (Player) sender;
        Player p = Bukkit.getPlayer(player.getName());
        PlayerData data = api.getPlayerData(p);
        // If run /test or Alias /top to show leaderboard.
        if (cmd.getName().equalsIgnoreCase("test") && sender instanceof Player) {
            if (sender instanceof Player) {
                //Player who issues Coins
                Integer coins = data.getCoins(p);
               // Team One (Player A + Player B + Player C)
               Player a = Bukkit.getPlayer("Twitch_TV");
               Player b = Bukkit.getPlayer("Twitch_TV");
               Player c = Bukkit.getPlayer("Twitch_TV");
               PlayerData usera = api.getPlayerData(a);
               PlayerData userb = api.getPlayerData(b);
               PlayerData userc = api.getPlayerData(c);
               Integer coinA = usera.getCoins(a);
               Integer coinB = userb.getCoins(b);
               Integer coinC = userc.getCoins(c);
               Integer sumTEAMONE = coinA + coinB + coinC;
                // Team Two (Player D + Player E + Player F)
                Player d = Bukkit.getPlayer("Twitch_TV");
                Player e = Bukkit.getPlayer("Twitch_TV");
                Player f = Bukkit.getPlayer("Twitch_TV");
                PlayerData userd = api.getPlayerData(d);
                PlayerData usere = api.getPlayerData(e);
                PlayerData userf = api.getPlayerData(f);
                Integer coinD = usera.getCoins(d);
                Integer coinE = userb.getCoins(e);
                Integer coinF = userc.getCoins(f);
                Integer sumTEAMTWO = coinD + coinE + coinF;
                // Team Three (Player g + Player h + Player i)
                Player g = Bukkit.getPlayer("Twitch_TV");
                Player h  = Bukkit.getPlayer("Twitch_TV");
                Player i = Bukkit.getPlayer("Twitch_TV");
                PlayerData userg = api.getPlayerData(g);
                PlayerData userh = api.getPlayerData(h);
                PlayerData useri = api.getPlayerData(i);
                Integer coinG = usera.getCoins(g);
                Integer coinH = userb.getCoins(h);
                Integer coinI = userc.getCoins(i);
                Integer sumTEAMTHREE = coinG + coinH + coinI;
                // Team Four (Player J + Player K + Player L)
                Player j = Bukkit.getPlayer("Twitch_TV");
                Player k  = Bukkit.getPlayer("Twitch_TV");
                Player l = Bukkit.getPlayer("Twitch_TV");
                PlayerData userj = api.getPlayerData(l);
                PlayerData userk = api.getPlayerData(k);
                PlayerData userl = api.getPlayerData(j);
                Integer coinJ = usera.getCoins(j);
                Integer coinK = userb.getCoins(k);
                Integer coinL = userc.getCoins(l);
                Integer sumTEAMFOUR = coinJ + coinK + coinL;
                // Team Five (Player m + Player n + Player o)
                Player m = Bukkit.getPlayer("Twitch_TV");
                Player n  = Bukkit.getPlayer("Twitch_TV");
                Player o = Bukkit.getPlayer("Twitch_TV");
                PlayerData userm = api.getPlayerData(m);
                PlayerData usern = api.getPlayerData(n);
                PlayerData usero = api.getPlayerData(o);
                Integer coinM = usera.getCoins(m);
                Integer coinN = userb.getCoins(n);
                Integer coinO = userc.getCoins(o);
                Integer sumTEAMFIVE = coinM + coinN + coinO;
               // Final Message Sent to user
               player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                  " + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + " Creator Games " + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                  ");
               player.sendMessage( "");
               player.sendMessage(ChatColor.GRAY + "Personal " + ChatColor.YELLOW + "⭐ " + coins);
               player.sendMessage( "");
               player.sendMessage(ChatColor.WHITE + "Incredible Irons" + ChatColor.YELLOW + " ⭐ " + sumTEAMONE);
               player.sendMessage( "");
               player.sendMessage(ChatColor.AQUA + "Dazzling Diamonds" + ChatColor.YELLOW + " ⭐ " + sumTEAMTWO);
               player.sendMessage( "");
               player.sendMessage(ChatColor.DARK_RED + "Rowdy Restones" + ChatColor.YELLOW + " ⭐ " + sumTEAMTHREE);
               player.sendMessage( "");
               player.sendMessage(ChatColor.BLUE + "Luxurious Lapis" + ChatColor.YELLOW + " ⭐ " + sumTEAMFOUR);
               player.sendMessage( "");
               player.sendMessage(ChatColor.DARK_GRAY + "Nimble Netherites" + ChatColor.YELLOW + " ⭐ " + sumTEAMFIVE);
               player.sendMessage( "");
               player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "                                                               ");
            } else {
               sender.sendMessage("You must be a player!");
            }
            //doSomething
            return true;
        }
        return false;
    }
}
