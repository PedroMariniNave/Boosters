package com.zpedroo.voltzboosters.utils.config;

import com.zpedroo.voltzboosters.utils.FileUtils;
import org.bukkit.ChatColor;

public class Messages {

    public static final String EXPIRED_BOOSTER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.expired-booster"));

    public static final String ACTIVE_BOOSTER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.active-booster"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}