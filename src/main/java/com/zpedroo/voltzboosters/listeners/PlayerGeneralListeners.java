package com.zpedroo.voltzboosters.listeners;

import com.zpedroo.voltzboosters.api.BoosterAPI;
import com.zpedroo.voltzboosters.objects.Booster;
import com.zpedroo.voltzboosters.utils.config.Messages;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) return;

        ItemStack item = event.getItem().clone();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("Booster")) return;

        Booster booster = BoosterAPI.getBoosterByName(nbt.getString("Booster"));
        if (booster == null) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (BoosterAPI.hasActiveBooster(player, booster)) {
            player.sendMessage(Messages.ACTIVE_BOOSTER);
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            player.updateInventory();
            return;
        }

        item.setAmount(1);
        player.getInventory().removeItem(item);
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 10f);

        double multiplier = nbt.getDouble("BoosterMultiplier");
        int durationInMinutes = nbt.getInteger("BoosterDurationInMinutes");

        BoosterAPI.giveBooster(player, booster, multiplier, durationInMinutes);
    }
}