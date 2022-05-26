package com.zpedroo.voltzboosters.objects;

public class PlayerBooster {

    private final Booster booster;
    private final double multiplier;
    private final long expirationDateInMillis;

    public PlayerBooster(Booster booster, double multiplier, long expirationDateInMillis) {
        this.booster = booster;
        this.multiplier = multiplier;
        this.expirationDateInMillis = expirationDateInMillis;
    }

    public Booster getBooster() {
        return booster;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public long getExpirationDateInMillis() {
        return expirationDateInMillis;
    }
}