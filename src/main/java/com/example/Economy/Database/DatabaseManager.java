package com.example.Economy.Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import org.bukkit.plugin.java.JavaPlugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {
    private HikariDataSource dataSource;

    public void connect(JavaPlugin plugin){
        HikariConfig config = new HikariConfig();
        String host = plugin.getConfig().getString("database.host");
        String port = plugin.getConfig().getString("database.port");
        String dbName = plugin.getConfig().getString("database.database");

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true");
        config.setUsername(plugin.getConfig().getString("database.username"));
        config.setPassword(plugin.getConfig().getString("database.password"));
        config.setMaximumPoolSize(plugin.getConfig().getInt("database.pool-size"));

        dataSource = new HikariDataSource(config);
        plugin.getLogger().info("Connected Database");
        createTable();

    }
    private void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS player_economy (" +
                     "uuid VARCHAR(36) PRIMARY KEY, " +
                     "username VARCHAR(16), "+
                     "balance DOUBLE DEFAULT 0.0" + 
                     ");";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
