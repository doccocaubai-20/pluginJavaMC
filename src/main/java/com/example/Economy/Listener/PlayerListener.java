package com.example.Economy.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.example.Economy.EconomyCore;

public class PlayerListener implements Listener{
    private final EconomyCore plugin;

    public PlayerListener(EconomyCore economyCore){
        this.plugin = economyCore;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        plugin.getEconomyManager().loadPlayerMoney(event.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        plugin.getEconomyManager().unloadPlayerData(event.getPlayer().getUniqueId());
    }
}
