package jackgnuk.creatorgames;

import jackgnuk.creatorgames.Commands.CommandTop;
import jackgnuk.creatorgames.Events.Listeners.EntityEvents;
import jackgnuk.creatorgames.Events.Listeners.PlayerEvents;
import jackgnuk.creatorgames.Util.ConfigReader;
import jackgnuk.creatorgames.Util.PlayerManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CreatorGames extends JavaPlugin {
    public PlayerManager PlayerManager;
    public ConfigReader Config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PlayerManager = new PlayerManager();
        Config = new ConfigReader();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        this.getCommand("top").setExecutor(new CommandTop());
    }

    private void registerListeners() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerEvents(this), this);
        manager.registerEvents(new EntityEvents(this), this);
    }
}
