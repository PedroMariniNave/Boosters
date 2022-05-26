package com.zpedroo.voltzboosters.commands;

import com.zpedroo.voltzboosters.api.BoosterAPI;
import com.zpedroo.voltzboosters.objects.Booster;
import com.zpedroo.voltzboosters.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BoosterCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        if (args.length > 0) {
            switch (args[0].toUpperCase()) {
                case "GIVE":
                    if (!sender.hasPermission("boosters.admin")) break;
                    if (args.length < 6) break;

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    Booster booster = BoosterAPI.getBoosterByName(args[2]);
                    if (booster == null) break;

                    double multiplier = 0;
                    int durationInMinutes = 0;
                    int amount = 0;
                    try {
                        multiplier = Double.parseDouble(args[3]);
                        durationInMinutes = Integer.parseInt(args[4]);
                        amount = Integer.parseInt(args[5]);
                    } catch (Exception ex) {
                        // ignore
                    }

                    if (multiplier <= 0 || durationInMinutes <= 0 || amount <= 0) break;

                    ItemStack item = booster.getItem(multiplier, durationInMinutes, amount);
                    if (target.getInventory().firstEmpty() != -1) {
                        target.getInventory().addItem(item);
                    } else {
                        target.getWorld().dropItemNaturally(target.getLocation(), item);
                    }
                    return true;
            }
        }

        if (player == null) return true;

        Menus.getInstance().openPlayerBoostersMenu(player);
        return false;
    }
}