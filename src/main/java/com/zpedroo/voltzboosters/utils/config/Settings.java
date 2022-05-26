package com.zpedroo.voltzboosters.utils.config;

import com.zpedroo.voltzboosters.utils.FileUtils;

import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final long CHECK_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.check-interval");
}