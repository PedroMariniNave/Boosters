package com.zpedroo.voltzboosters.managers.cache;

import com.zpedroo.voltzboosters.mysql.DBConnection;
import com.zpedroo.voltzboosters.objects.Booster;
import com.zpedroo.voltzboosters.objects.PlayerData;
import com.zpedroo.voltzboosters.utils.FileUtils;
import com.zpedroo.voltzboosters.utils.builder.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DataCache {

    private final Map<UUID, PlayerData> playerData = getPlayerDataFromDatabase();
    private final Map<String, Booster> boosters = getBoostersFromConfig();

    public Map<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public Map<String, Booster> getBoosters() {
        return boosters;
    }

    private Map<String, Booster> getBoostersFromConfig() {
        FileUtils.Files file = FileUtils.Files.CONFIG;

        Set<String> boosters = FileUtils.get().getSection(file, "Boosters");
        Map<String, Booster> ret = new HashMap<>(boosters.size());

        for (String boosterName : boosters) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Boosters." + boosterName + ".item").build();
            String displayName = FileUtils.get().getString(file, "Boosters." + boosterName + ".display-name");

            ret.put(boosterName.toUpperCase(), new Booster(boosterName.toUpperCase(), item, displayName));
        }

        return ret;
    }

    private Map<UUID, PlayerData> getPlayerDataFromDatabase() {
        return DBConnection.getInstance().getDBManager().getPlayerBoostersFromDatabase();
    }
}