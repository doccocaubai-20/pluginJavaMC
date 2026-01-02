package com.example.Economy;

import org.bukkit.plugin.java.JavaPlugin;

import com.example.Economy.Command.MoneyCommand;
import com.example.Economy.Database.DatabaseManager;
import com.example.Economy.Listener.JoinListener;
import com.example.Economy.Listener.PlayerListener;
import com.example.Economy.Manager.EconomyManager;

public class EconomyCore extends JavaPlugin{
    private DatabaseManager databaseManager;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        databaseManager = new DatabaseManager();
        try {
            databaseManager.connect(this);
        } catch (Exception e) {
            getLogger().severe("Can't connect Database");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        economyManager = new EconomyManager(this,databaseManager);

        if (getCommand("money") != null){
            getCommand("money").setExecutor(new MoneyCommand(this));
        }
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    };

    @Override
    public void onDisable() {
        if (databaseManager != null){
            databaseManager.disconnect();
        }
    };
    public EconomyManager getEconomyManager(){
        return economyManager;
    }
}
