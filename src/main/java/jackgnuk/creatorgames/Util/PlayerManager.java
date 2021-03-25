package jackgnuk.creatorgames.Util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final List<PlayerWrapper> players = new ArrayList<>();

    public PlayerWrapper getPlayer(String id) {
        if (players.size() <= 0) return null;
        for (PlayerWrapper playerWrapper : players) {
            String tmpUUID = playerWrapper.player.getUniqueId().toString();
            String tmpName = playerWrapper.player.getDisplayName();

            if (tmpUUID.equals(id) || tmpName.equals(id)) {
                return playerWrapper;
            }
        }

        return null;
    }

    public PlayerWrapper GetPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (players.size() > 0) {
            return getPlayer(uuid);
        }
        return null;
        // TODO Grab player from db

//        //If not in players list
//        PlayerData playerData;
//        try {
//            playerData = playerYML.GetPlayerFile(player);
//            AddPlayer(playerData);
//        } catch (Exception ex) {
//            player.sendMessage(ex.getMessage());
//            return null;
//        }
//        return playerData;
    }
}
