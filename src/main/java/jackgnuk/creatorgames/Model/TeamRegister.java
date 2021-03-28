package jackgnuk.creatorgames.Model;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TeamRegister {
    public List<PlayerStats> Players;
    public String TeamName;
    public String ID;
    public ChatColor Color;

    public int CoinCount = 0;

    public TeamRegister() {
        this.Players = new ArrayList<>();
    }

    public TeamRegister(String name, String ID, ChatColor color) {
        this.Players = new ArrayList<>();
        this.TeamName = name;
        this.ID = ID;
        this.Color = color;
    }

    public List<String> GetPlayerUUIDs() {
        List<String> tmp = new ArrayList<>();

        for (PlayerStats ps : Players) {
            tmp.add(ps.UUID);
        }
        return tmp;
    }
}
