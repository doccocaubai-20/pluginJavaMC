package com.example.Economy.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.example.Economy.EconomyCore;
import com.example.Economy.Database.DatabaseManager;

public class EconomyManager {
    private final EconomyCore plugin;
    private final DatabaseManager dbManager;

    // Money cache 
    private final ConcurrentHashMap<UUID,Double> balanceCache = new ConcurrentHashMap<>(); 


    public EconomyManager(EconomyCore plugin,DatabaseManager databaseManager){
        this.plugin = plugin;
        this.dbManager  = databaseManager;
    }

    public void loadPlayerMoney(Player player){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection conn = dbManager.getConnection()){
                PreparedStatement ps = conn.prepareStatement("SELECT balance FROM player_economy WHERE uuid = ?");
                ps.setString(1, player.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                double balance = 0.0;
                if (rs.next()){
                    balance = rs.getDouble("balance");
                }

                balanceCache.put(player.getUniqueId(), balance);
                
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        });
    }
    public void unloadPlayerData(UUID uuid){
        balanceCache.remove(uuid);
    }
    
    public double getBalance(UUID uuid){
        return balanceCache.getOrDefault(uuid, 0.0);
    }

    // ADD MONEY
    public void deposit(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double currentBalance = getBalance(uuid);
        double newBalance = currentBalance + amount;

        balanceCache.put(uuid, newBalance);

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (Connection conn = dbManager.getConnection()) {
                String sql = "INSERT INTO player_economy (uuid, username, balance) VALUES (?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE balance = ?, username = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, player.getUniqueId().toString());
                ps.setString(2, player.getName());
                ps.setDouble(3, newBalance);
                ps.setDouble(4, newBalance);
                ps.setString(5, player.getName());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


}
