package jackgnuk.creatorgames.Data;

import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Model.CGConfig;
import jackgnuk.creatorgames.Model.PlayerStats;
import jackgnuk.creatorgames.Model.TeamRegister;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorageConnector implements Connector {
    final CreatorGames instance;

    public StorageConnector(CreatorGames instance) {
        this.instance = instance;
    }

    @Override
    public PlayerStats GetPlayer(String id) {
        boolean isUUID = true;
        try {
            UUID uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            isUUID = false;
        }

        if (!isUUID) throw new IllegalArgumentException("Provided ID does not match requirement for flat file storage");

        File userFolder = new File(instance.getDataFolder(), File.separator + "players");
        File f = new File(userFolder, File.separator + id + ".yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {
            //Player has not been queried before
            try {
                PlayerStats tmp = new PlayerStats();

                conf.createSection("Player");
                conf.set("Player.uuid", id);
                conf.set("Player.name", "TBD");

                conf.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mapFileToPlayerStats(conf);
    }

    @Override
    public void UpdatePlayer(String uuid, PlayerStats stats) {
        File userFolder = new File(instance.getDataFolder(), File.separator + "players");
        File f = new File(userFolder, File.separator + uuid + ".yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        try {
            PlayerStats tmp = new PlayerStats();

            conf.createSection("Player");

            conf.set("Player.uuid", uuid);
            conf.set("Player.name", stats.Name);

            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AddPlayer(PlayerStats stats) {
        File userFolder = new File(instance.getDataFolder(), File.separator + "players");
        File f = new File(userFolder, File.separator + stats.UUID + ".yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {
            //Player has not been queried before
            try {
                PlayerStats tmp = new PlayerStats();

                conf.createSection("Player");

                conf.set("Player.uuid", stats.UUID);
                conf.set("Player.name", stats.Name);

                conf.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public TeamRegister GetTeam(String teamID) {
        File f = new File(instance.getDataFolder(), File.separator + "teams.yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {
            //Team file has not been queried before
            try {
                List<String> teams = instance.Config.getTeamStrings();

                for (String team : teams) {
                    conf.createSection(team);

                    conf.set(team + ".uuids", new ArrayList<String>());

                }

                conf.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                TeamRegister register = instance.Config.getTeamFromID(teamID);

                List<String> uuids = conf.getStringList(register.TeamName + ".uuids");

                for (String uuid : uuids) {
                    PlayerStats stats = GetPlayer(uuid);
                    if (register.Players.stream().noneMatch(p -> p.UUID.equals(stats.UUID))) register.Players.add(stats);
                }

                return register;
            } catch (Exception e) {
                e.printStackTrace();
                TeamRegister r = instance.Config.getTeamFromID(teamID);
                return r;
            }
        }
        return null;
    }

    @Override
    public void AddTeammate(PlayerStats stats, String teamName) {
        TeamRegister team = GetTeam(teamName);

        boolean existsAlready = false;
        for (PlayerStats ps : team.Players) {
            if (ps.UUID.equals(stats.UUID)) existsAlready = true;
        }

        if (!existsAlready) team.Players.add(stats);
        else return;

        File f = new File(instance.getDataFolder(), File.separator + "teams.yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {
            //Team file has not been queried before
            try {
                List<String> teams = instance.Config.getTeamStrings();

                for (String t : teams) {
                    conf.createSection(t);

                    if (t.equals(teamName)) conf.set(t + ".uuids", team.GetPlayerUUIDs());
                    else conf.set(t + ".uuids", new ArrayList<String>());

                }

                conf.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                conf.set(teamName + ".uuids", team.GetPlayerUUIDs());

                conf.save(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void RemoveTeammate(PlayerStats stats, String teamName) {
        TeamRegister team = GetTeam(teamName);

        for (int i = 0; i < team.Players.size(); i++) {
            if (team.Players.get(i).UUID.equals(stats.UUID)) team.Players.remove(i);
        }

        File f = new File(instance.getDataFolder(), File.separator + "teams.yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {
            //Team file has not been queried before
            try {
                List<String> teams = instance.Config.getTeamStrings();

                for (String t : teams) {
                    conf.createSection(t);

                    if (t.equals(teamName)) conf.set(t + ".uuids", team.GetPlayerUUIDs());
                    else conf.set(t + ".uuids", new ArrayList<String>());

                }

                conf.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                conf.set(teamName + ".uuids", team.GetPlayerUUIDs());

                conf.save(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private PlayerStats mapFileToPlayerStats(FileConfiguration config) {
        PlayerStats tmp = new PlayerStats();

        tmp.Name = config.getString("Player.name");
        tmp.UUID = config.getString("Player.uuid");

        return tmp;
    }
}
