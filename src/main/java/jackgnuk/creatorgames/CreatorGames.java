package jackgnuk.creatorgames;

import jackgnuk.creatorgames.Commands.CommandAddTeammate;
import jackgnuk.creatorgames.Commands.CommandRemoveTeammate;
import jackgnuk.creatorgames.Commands.CommandTop;
import jackgnuk.creatorgames.Events.Listeners.EntityEvents;
import jackgnuk.creatorgames.Events.Listeners.PlayerEvents;
import jackgnuk.creatorgames.Model.CGConfig;
import jackgnuk.creatorgames.Util.ConfigReader;
import jackgnuk.creatorgames.Util.PlayerManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CreatorGames extends JavaPlugin {
    public PlayerManager PlayerManager;
    public CGConfig Config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PlayerManager = new PlayerManager(this);

        ConfigReader configReader = new ConfigReader();
        try {
            Config = configReader.GetConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        this.getCommand("top").setExecutor(new CommandTop(this));
        this.getCommand("addteammate").setExecutor(new CommandAddTeammate(this));
        this.getCommand("removeteammate").setExecutor(new CommandRemoveTeammate(this));
    }

    private void registerListeners() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerEvents(this), this);
        manager.registerEvents(new EntityEvents(this), this);
    }
}
