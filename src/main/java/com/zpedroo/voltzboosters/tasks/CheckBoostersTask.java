package com.zpedroo.voltzboosters.tasks;

import com.zpedroo.voltzboosters.managers.DataManager;
import com.zpedroo.voltzboosters.objects.PlayerBooster;
import com.zpedroo.voltzboosters.utils.config.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.LinkedList;

import static com.zpedroo.voltzboosters.utils.config.Settings.CHECK_INTERVAL;

public class CheckBoostersTask extends BukkitRunnable {

    public CheckBoostersTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 20 * CHECK_INTERVAL, 20 * CHECK_INTERVAL);
    }

    @Override
    public void run() {
        new HashSet<>(DataManager.getInstance().getCache().getPlayerData().values()).forEach(playerData -> {
            for (PlayerBooster playerBooster : new LinkedList<>(playerData.getBoosters())) {
                if (System.currentTimeMillis() < playerBooster.getExpirationDateInMillis()) continue;

                playerData.removeBooster(playerBooster);

                Player player = Bukkit.getOfflinePlayer(playerData.getUniqueId()).getPlayer();
                if (player == null) return;

                player.sendMessage(StringUtils.replaceEach(Messages.EXPIRED_BOOSTER, new String[]{
                        "{booster}"
                }, new String[]{
                        playerBooster.getBooster().getDisplayName()
                }));
            }
        });
    }
}