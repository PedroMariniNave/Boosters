package com.zpedroo.voltzboosters.api;

import com.zpedroo.voltzboosters.managers.DataManager;
import com.zpedroo.voltzboosters.objects.Booster;
import com.zpedroo.voltzboosters.objects.PlayerBooster;
import com.zpedroo.voltzboosters.objects.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BoosterAPI {

    public static PlayerBooster getPlayerBooster(@NotNull Player player, @NotNull String boosterName) {
        return getPlayerBooster(player.getUniqueId(), boosterName);
    }

    public static PlayerBooster getPlayerBooster(@NotNull UUID uuid, @NotNull String boosterName) {
        return getPlayerBooster(uuid, getBoosterByName(boosterName));
    }

    public static PlayerBooster getPlayerBooster(@NotNull Player player, @NotNull Booster booster) {
        return getPlayerBooster(player.getUniqueId(), booster);
    }

    public static PlayerBooster getPlayerBooster(@NotNull UUID uuid, @NotNull Booster booster) {
        List<PlayerBooster> playerBoosters = getPlayerBoosters(uuid);
        for (PlayerBooster playerBooster : playerBoosters) {
            if (playerBooster.getBooster().equals(booster)) return playerBooster;
        }

        return null;
    }

    public static @NotNull List<PlayerBooster> getPlayerBoosters(@NotNull Player player) {
        return getPlayerBoosters(player.getUniqueId());
    }

    public static @NotNull List<PlayerBooster> getPlayerBoosters(@NotNull UUID uuid) {
        if (!DataManager.getInstance().getCache().getPlayerData().containsKey(uuid)) return new ArrayList<>(0);

        return DataManager.getInstance().getCache().getPlayerData().get(uuid).getBoosters();
    }

    public static boolean hasActiveBooster(@NotNull Player player, @NotNull String boosterName) {
        return hasActiveBooster(player.getUniqueId(), boosterName);
    }

    public static boolean hasActiveBooster(@NotNull UUID uuid, @NotNull String boosterName) {
        return hasActiveBooster(uuid, getBoosterByName(boosterName));
    }

    public static boolean hasActiveBooster(@NotNull Player player, @NotNull Booster booster) {
        return hasActiveBooster(player.getUniqueId(), booster);
    }

    public static boolean hasActiveBooster(@NotNull UUID uuid, @NotNull Booster booster) {
        return getPlayerBoosters(uuid).stream().anyMatch(
                playerBooster -> playerBooster.getBooster().equals(booster) && System.currentTimeMillis() < playerBooster.getExpirationDateInMillis()
        );
    }

    public static Booster getBoosterByName(@NotNull String boosterName) {
        return DataManager.getInstance().getCache().getBoosters().get(boosterName.toUpperCase());
    }

    public static void giveBooster(Player player, Booster booster, double multiplier, int durationInMinutes) {
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        if (data == null) return;

        data.addBooster(new PlayerBooster(booster, multiplier, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(durationInMinutes)));
    }
}