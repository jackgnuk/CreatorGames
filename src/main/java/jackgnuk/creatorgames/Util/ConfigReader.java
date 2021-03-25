package jackgnuk.creatorgames.Util;

import org.bukkit.entity.EntityType;

import java.util.Dictionary;

public class ConfigReader {

    private Dictionary<EntityType, Integer> coinsToEntity;
    public int CoinsToDrop(EntityType entityType) {
        Integer coin = coinsToEntity.get(entityType);

        if (coin == null) return 0;
        return coin;
    }

}
