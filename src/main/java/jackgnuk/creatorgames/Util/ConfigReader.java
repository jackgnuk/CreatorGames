package jackgnuk.creatorgames.Util;

import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Model.CGConfig;
import jackgnuk.creatorgames.Model.TeamRegister;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    final CreatorGames instance = CreatorGames.getPlugin(CreatorGames.class);

    public CGConfig GetConfig() throws Exception {
        File config = new File(instance.getDataFolder(), File.separator + "config.yml");

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(config);

        //If the config is yet to be generated, populate with defaults
        if (!config.exists()) {
            try {
                CGConfig tmp = new CGConfig(instance);

                configuration.createSection("MySQL");
                configuration.set("MySQL.enabled", tmp.isMySQLEnabled());
                configuration.set("MySQL.host", tmp.getHost());
                configuration.set("MySQL.port", tmp.getPort());
                configuration.set("MySQL.database", tmp.getDatabase());
                configuration.set("MySQL.username", tmp.getUsername());
                configuration.set("MySQL.password", tmp.getPassword());

                configuration.createSection("Coins");
                configuration.set("Coins.values", tmp.getCoinsAsStringMap());

                configuration.createSection("Teams");

                for (TeamRegister r : tmp.getTeams()) {
                    configuration.createSection("Teams." + r.TeamName);
                    configuration.set("Teams." + r.TeamName + ".ID", r.ID);
                    configuration.set("Teams." + r.TeamName + ".Color", r.Color.name());
                }

                configuration.save(config);
            } catch (IOException ex) {
                instance.getServer().getConsoleSender().sendMessage("Error generating config.yml");
                ex.printStackTrace();
            }
        }

        return mapFileToCGConfig(configuration);
    }

    public void SaveConfig(CGConfig config) {
        File file = new File(instance.getDataFolder(), File.separator + "config.yml");

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        try {
            CGConfig tmp = new CGConfig(instance);

            configuration.createSection("MySQL");
            configuration.set("MySQL.enabled", tmp.isMySQLEnabled());
            configuration.set("MySQL.host", tmp.getHost());
            configuration.set("MySQL.port", tmp.getPort());
            configuration.set("MySQL.database", tmp.getDatabase());
            configuration.set("MySQL.username", tmp.getUsername());
            configuration.set("MySQL.password", tmp.getPassword());

            configuration.createSection("Coins");
            configuration.set("Coins.values", tmp.getCoinsAsStringMap());

            configuration.createSection("Teams");

            for (TeamRegister r : tmp.getTeams()) {
                configuration.createSection("Teams." + r.TeamName);
                configuration.set("Teams." + r.TeamName + ".ID", r.ID);
                configuration.set("Teams." + r.TeamName + ".Color", r.Color.name());
            }

            configuration.save(file);
        } catch (IOException ex) {
            instance.getServer().getConsoleSender().sendMessage("Error writing config.yml");
            ex.printStackTrace();
        }

    }

    private CGConfig mapFileToCGConfig(FileConfiguration config) throws Exception {
        CGConfig tmp = new CGConfig(instance);

        tmp.setMySQLEnabled(config.getBoolean("MySQL.enabled"));
        tmp.setHost(config.getString("MySQL.host"));
        tmp.setPort(config.getInt("MySQL.port"));
        tmp.setDatabase(config.getString("MySQL.database"));
        tmp.setUsername(config.getString("MySQL.username"));
        tmp.setPassword(config.getString("MySQL.password"));

        tmp.setCoinsFromStrings(config.getStringList("Coins.values"));

        List<TeamRegister> registers = new ArrayList<>();
        ConfigurationSection cs = config.getConfigurationSection("Teams");
        if (cs != null) {
            for (String teamName : cs.getKeys(false)) {
                TeamRegister r = new TeamRegister();
                r.TeamName = teamName;
                r.ID = cs.getString(teamName + ".ID");
                r.Color = ChatColor.valueOf(cs.getString(teamName + ".Color"));
                registers.add(r);
            }
        } else {
            throw new Exception("Teams missing from configuration");
        }
        tmp.setTeams(registers);

        return tmp;
    }
}
