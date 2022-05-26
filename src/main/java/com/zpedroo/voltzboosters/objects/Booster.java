package com.zpedroo.voltzboosters.objects;

import com.zpedroo.voltzboosters.utils.formatter.NumberFormatter;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Booster {

    private final String name;
    private final ItemStack item;
    private final String displayName;

    public Booster(String name, ItemStack item, String displayName) {
        this.name = name;
        this.item = item;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem(double multiplier, int durationInMinutes, int amount) {
        NBTItem nbt = new NBTItem(item.clone());
        nbt.setString("Booster", name);
        nbt.setDouble("BoosterMultiplier", multiplier);
        nbt.setInteger("BoosterDurationInMinutes", durationInMinutes);

        ItemStack item = nbt.getItem();
        if (item.getItemMeta() != null) {
            String displayName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;
            List<String> lore = item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : null;
            ItemMeta meta = item.getItemMeta();

            String[] placeholders = new String[]{
                    "{multiplier}",
                    "{duration}"
            };
            String[] replacers = new String[]{
                    NumberFormatter.formatDecimal(multiplier),
                    String.valueOf(durationInMinutes)
            };

            if (displayName != null) meta.setDisplayName(StringUtils.replaceEach(displayName, placeholders, replacers));

            if (lore != null) {
                List<String> newLore = new ArrayList<>(lore.size());

                for (String str : lore) {
                    newLore.add(StringUtils.replaceEach(str, placeholders, replacers));
                }

                meta.setLore(newLore);
            }

            item.setItemMeta(meta);
        }

        item.setAmount(amount);
        return item;
    }

    public String getDisplayName() {
        return displayName;
    }
}