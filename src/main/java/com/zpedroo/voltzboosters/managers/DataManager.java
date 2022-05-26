package com.zpedroo.voltzboosters.managers;

import com.zpedroo.voltzboosters.managers.cache.DataCache;
import com.zpedroo.voltzboosters.mysql.DBConnection;
import com.zpedroo.voltzboosters.objects.PlayerData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache;

    public DataManager() {
        instance = this;
        this.dataCache = new DataCache();
    }

    public PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        PlayerData data = dataCache.getPlayerData().get(uuid);
        if (data == null) {
            data = new PlayerData(uuid, new ArrayList<>(2));
            dataCache.getPlayerData().put(uuid, data);
        }

        return data;
    }

    public void savePlayerData(UUID uuid) {
        PlayerData data = dataCache.getPlayerData().get(uuid);
        if (data == null || !data.isQueueUpdate()) return;

        data.setUpdate(false);

        if (data.getBoosters().isEmpty()) {
            DBConnection.getInstance().getDBManager().deletePlayerData(uuid);
            return;
        }

        DBConnection.getInstance().getDBManager().savePlayerData(data);
    }

    public void saveAllPlayersData() {
        new HashSet<>(dataCache.getPlayerData().keySet()).forEach(this::savePlayerData);
    }

    public DataCache getCache() {
        return dataCache;
    }
}