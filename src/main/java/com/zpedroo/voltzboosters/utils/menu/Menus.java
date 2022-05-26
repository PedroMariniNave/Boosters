package com.zpedroo.voltzboosters.utils.menu;

import com.zpedroo.voltzboosters.api.BoosterAPI;
import com.zpedroo.voltzboosters.objects.Booster;
import com.zpedroo.voltzboosters.utils.FileUtils;
import com.zpedroo.voltzboosters.utils.builder.InventoryBuilder;
import com.zpedroo.voltzboosters.utils.builder.InventoryUtils;
import com.zpedroo.voltzboosters.utils.builder.ItemBuilder;
import com.zpedroo.voltzboosters.utils.formatter.NumberFormatter;
import com.zpedroo.voltzboosters.utils.formatter.TimeFormatter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    public Menus() {
        instance = this;
    }

    public void openPlayerBoostersMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.PLAYER_BOOSTERS;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String items : FileUtils.get().getSection(file, "Inventory.items")) {
            Booster booster = BoosterAPI.getBoosterByName(items);
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Inventory.items." + items, new String[]{
                    "{multiplier}",
                    "{remaining_time}"
            }, new String[]{
                    NumberFormatter.formatDecimal(booster == null ? 1 : BoosterAPI.getPlayerBooster(player, booster) == null ? 1 :
                            BoosterAPI.getPlayerBooster(player, booster).getMultiplier()),
                    String.valueOf(booster == null ? 0 :
                            TimeFormatter.millisToFormattedTime(BoosterAPI.getPlayerBooster(player, booster) == null ? 0 :
                                    BoosterAPI.getPlayerBooster(player, booster).getExpirationDateInMillis() - System.currentTimeMillis()))
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + items + ".slot");

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }
}