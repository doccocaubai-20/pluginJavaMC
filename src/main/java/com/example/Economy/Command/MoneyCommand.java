package com.example.Economy.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.example.Economy.EconomyCore;

import net.md_5.bungee.api.ChatColor;

public class MoneyCommand implements CommandExecutor{
    private final EconomyCore plugin;
    
    public MoneyCommand(EconomyCore economyCore){
        this.plugin = economyCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arg) {
        // /money
        if ( arg.length == 0){
            if ( !(sender instanceof Player)){
                return true;
            }
            Player player = (Player) sender;
            
            double balance = plugin.getEconomyManager().getBalance(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Số dư : " + ChatColor.GOLD + String.format("%,.2f $", balance ));
            return true;
        }
        // /money give  <player> <amount>
        if ( arg.length ==  3 && arg[0].equalsIgnoreCase("give")){
            // OP
            if (!sender.isOp()){
                sender.sendMessage("Not OP!");
                return true;
            }
            String targetName = arg[1];
            Player target = Bukkit.getPlayer(targetName);

            if ( target == null){
                sender.sendMessage("KHÔNG ONL");
                return true;
            }

            try {
                double amount = Double.parseDouble(arg[2]);
                plugin.getEconomyManager().deposit(target, amount);
                sender.sendMessage(ChatColor.GREEN + "Đã chuyển " + amount + "$ cho " + target.getName());
                target.sendMessage(ChatColor.GREEN + "Đã nhận được " + amount + "$ từ Admin");
            }
            catch (NumberFormatException e){
                sender.sendMessage(ChatColor.RED + "Not Valid Money");
            }
            return true;
        }
        return false;
    };
}
