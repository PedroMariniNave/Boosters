package com.zpedroo.voltzboosters.utils.serialization;

import com.zpedroo.voltzboosters.api.BoosterAPI;
import com.zpedroo.voltzboosters.objects.Booster;
import com.zpedroo.voltzboosters.objects.PlayerBooster;

import java.util.ArrayList;
import java.util.List;

public abstract class BoosterSerialization {

    public String serializeBoosters(List<PlayerBooster> playerBoosters) {
        StringBuilder builder = new StringBuilder(playerBoosters.size() * 6);

        for (PlayerBooster playerBooster : playerBoosters) {
            if (System.currentTimeMillis() >= playerBooster.getExpirationDateInMillis()) continue;
            if (builder.length() > 0) builder.append(",");

            builder.append(playerBooster.getBooster().getName()).append("#");
            builder.append(playerBooster.getMultiplier()).append("#");
            builder.append(playerBooster.getExpirationDateInMillis());
        }

        return builder.toString();
    }

    public List<PlayerBooster> deserializeBoosters(String serialized) {
        List<PlayerBooster> ret = new ArrayList<>(2);

        String[] boostersSplit = serialized.split(",");

        for (String boosterInfo : boostersSplit) {
            String[] boosterInfoSplit = boosterInfo.split("#");

            String boosterName = boosterInfoSplit[0];
            Booster booster = BoosterAPI.getBoosterByName(boosterName);
            if (booster == null) continue;

            double multiplier = Double.parseDouble(boosterInfoSplit[1]);
            long expirationDateInMillis = Long.parseLong(boosterInfoSplit[2]);
            if (System.currentTimeMillis() >= expirationDateInMillis) continue;

            ret.add(new PlayerBooster(booster, multiplier, expirationDateInMillis));
        }

        return ret;
    }
}