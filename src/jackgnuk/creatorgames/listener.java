package jackgnuk.creatorgames;

import me.wazup.partygames.PartyGames;
import me.wazup.partygames.PartyGamesAPI;
import me.wazup.partygames.PlayerData;
import me.wazup.partygames.minigames.AnimalSlaughter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class listener implements Listener {
    // On PlayerJoin set Blank
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
    }
    // On PlayerLeave set Blank
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }
    //On Player kill Animal event, give Coin
    @EventHandler
    public void getCoin(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        PartyGamesAPI api = PartyGames.api;
        Player player = event.getEntity().getKiller();
        Player p = Bukkit.getPlayer(player.getName());
        PlayerData data = api.getPlayerData(p);

        if (entity.getType() == EntityType.BAT) {
            if (player != null) {
                entity.getKiller().sendMessage(ChatColor.YELLOW + "+1  Point");
                entity.getKiller().playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1F, 1F);
                //Gives killer 10 Coins from the API of PartyGames
                data.addCoins(p, 1);
                data.saveStats = true;
            } else {
                return;
            }
        }
    }

//    // On DeathEvent Handler
//    @EventHandler
//    public void onDeath(EntityDeathEvent e) {
//        PartyGamesAPI api = PartyGames.api;
//        Player player = (Player) e.getEntity();
//        Player killer = player.getKiller();
//        // killer to be a player name
//        Player p = Bukkit.getPlayer(killer.getName());
//        PlayerData data = api.getPlayerData(p);
//
//        // Checks player was killed by Player
//        if (player != null && killer != null) {
//
//            //Gives killer 10 Coins from the API of PartyGames
//            data.addCoins(killer, 10);
//            data.saveStats = true;
//        }
//    }

    // Player onDeath set Messages
    @EventHandler
    public  void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        // Check if player was killed by a player not entity.
        if (player != null && killer != null) {
            //Sets DeathScreen/Chat message to Nothing
            event.setDeathMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "");

            //Broadcast Message of Death in Chat
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "-------------------------------");
            Bukkit.broadcastMessage(ChatColor.GRAY + "");
            Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + " was killed by " + killer.getName());
            Bukkit.broadcastMessage(ChatColor.GRAY + "");
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "-------------------------------");
        }
    }
}
