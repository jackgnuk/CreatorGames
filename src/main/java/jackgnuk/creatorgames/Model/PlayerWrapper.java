package jackgnuk.creatorgames.Model;

import org.bukkit.entity.Player;

public class PlayerWrapper {
    public final Player player;

    public PlayerStats playerStats;

    public PlayerWrapper(Player player) {
        this.player = player;
    }
}
