package com.example.Economy.Listener;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.example.Economy.EconomyCore;

import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener{
    private final EconomyCore plugin;
    public JoinListener(EconomyCore economyCore){
        this.plugin = economyCore;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        player.setInvulnerable(true);
        Bukkit.getScheduler().runTaskLater(plugin,() -> {
            if (player.isOnline()){
                player.setInvulnerable(false);
                player.sendMessage(ChatColor.RED + "Đã hết bất tử!");
            }
        }, 30 * 20L);




    }
}
