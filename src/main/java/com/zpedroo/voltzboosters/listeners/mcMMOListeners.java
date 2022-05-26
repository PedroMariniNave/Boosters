package com.zpedroo.voltzboosters.listeners;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.zpedroo.voltzboosters.api.BoosterAPI;
import com.zpedroo.voltzboosters.objects.Booster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class mcMMOListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onXpGain(McMMOPlayerXpGainEvent event) {
        Player player = event.getPlayer();
        Booster booster = BoosterAPI.getBoosterByName("mcMMO");
        if (booster == null || !BoosterAPI.hasActiveBooster(player, booster)) return;

        event.setXpGained((int) (event.getXpGained() * BoosterAPI.getPlayerBooster(player, booster).getMultiplier()));
    }
}