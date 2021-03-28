package jackgnuk.creatorgames.Events.Listeners;

import jackgnuk.creatorgames.CreatorGames;
import me.wazup.partygames.PartyGames;
import me.wazup.partygames.PartyGamesAPI;
import me.wazup.partygames.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityEvents implements Listener {
    final CreatorGames instance;

    public EntityEvents(CreatorGames instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        PartyGamesAPI api = PartyGames.api;

        Player player = entity.getKiller();

        if (player != null) {
            PlayerData playerData = api.getPlayerData(player);

            EntityType entityType = entity.getType();
            if (instance.Config.coinAvailable(entityType)) {
                int coinsToDrop = instance.Config.CoinsToDrop(entityType);
                player.sendMessage(ChatColor.YELLOW + "+" + coinsToDrop + " Point");
                player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1f, 1f);

                playerData.addCoins(player, coinsToDrop);
                playerData.saveStats = true;
            }
        }
    }
}
