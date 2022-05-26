package com.zpedroo.voltzboosters;

import com.zpedroo.voltzboosters.commands.BoosterCmd;
import com.zpedroo.voltzboosters.listeners.PlayerGeneralListeners;
import com.zpedroo.voltzboosters.listeners.mcMMOListeners;
import com.zpedroo.voltzboosters.managers.DataManager;
import com.zpedroo.voltzboosters.mysql.DBConnection;
import com.zpedroo.voltzboosters.tasks.CheckBoostersTask;
import com.zpedroo.voltzboosters.tasks.SaveTask;
import com.zpedroo.voltzboosters.utils.FileUtils;
import com.zpedroo.voltzboosters.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import static com.zpedroo.voltzboosters.utils.config.Settings.COMMAND;
import static com.zpedroo.voltzboosters.utils.config.Settings.ALIASES;

public class VoltzBoosters extends JavaPlugin {

    private static VoltzBoosters instance;
    public static VoltzBoosters get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new DBConnection(getConfig());
        new DataManager();
        new CheckBoostersTask(this);
        new SaveTask(this);
        new Menus();

        registerListeners();
        registerCommand(COMMAND, ALIASES, new BoosterCmd());
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            DataManager.getInstance().saveAllPlayersData();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
        if (getServer().getPluginManager().getPlugin("mcMMO") != null) {
            getServer().getPluginManager().registerEvents(new mcMMOListeners(), this);
        }
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isMySQLEnabled(FileConfiguration file) {
        if (!file.contains("MySQL.enabled")) return false;

        return file.getBoolean("MySQL.enabled");
    }
}