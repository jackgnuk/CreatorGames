package jackgnuk.creatorgames.Events.Listeners;

import jackgnuk.creatorgames.CreatorGames;
import me.wazup.partygames.PartyGames;
import me.wazup.partygames.PartyGamesAPI;
import me.wazup.partygames.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {
    final CreatorGames instance;

    public PlayerEvents(CreatorGames instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        instance.PlayerManager.GetPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        instance.PlayerManager.RemovePlayer(event.getPlayer().getUniqueId().toString());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
        //TODO handle saving player
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        PartyGamesAPI api = PartyGames.api;

        if (killer != null) {
            PlayerData playerData = api.getPlayerData(killer);
            playerData.addCoins(killer, instance.Config.CoinsToDrop(EntityType.PLAYER));
            playerData.saveStats = true;

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
