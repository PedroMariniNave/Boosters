package com.zpedroo.voltzboosters.objects;

import java.util.List;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final List<PlayerBooster> playerBoosters;
    private boolean update;

    public PlayerData(UUID uuid, List<PlayerBooster> playerBoosters) {
        this.uuid = uuid;
        this.playerBoosters = playerBoosters;
        this.update = false;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public List<PlayerBooster> getBoosters() {
        return playerBoosters;
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public void removeBooster(PlayerBooster playerBooster) {
        this.playerBoosters.remove(playerBooster);
        this.update = true;
    }

    public void addBooster(PlayerBooster playerBooster) {
        this.playerBoosters.add(playerBooster);
        this.update = true;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}