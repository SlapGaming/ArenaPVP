package me.naithantu.ArenaPVP;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Commands.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArenaPVP extends JavaPlugin {

    private static ArenaPVP instance;

    private Logger logger;

    private TabController tabController;
    private ListenerHandler listenerHandler;

    @Override
    public void onEnable() {
        //Initialize static instance
        instance = this;

        logger = getLogger();

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            logger.log(Level.INFO, "No config found, generating default config.");
            saveDefaultConfig();
        }

        tabController = new TabController();

        //Create required directories
        new File(getDataFolder() + File.separator + "maps").mkdirs();
        new File(getDataFolder() + File.separator + "players").mkdirs();
        new File(getDataFolder() + File.separator + "classes" + File.separator + "inventory").mkdirs();
        new File(getDataFolder() + File.separator + "classes" + File.separator + "armor").mkdirs();

        listenerHandler = new ListenerHandler(this, this.getServer().getPluginManager());
    }

    @Override
    public void onDisable() {
        for (Arena arena : ArenaManager.getArenas().values()) {
            arena.stopGame();
        }

        //Remove static instance
        instance = null;
    }

    public static ArenaPVP getInstance() {
        return instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return CommandHandler.handle(sender, cmd, args);
    }

    public TabController getTabController() {
        return tabController;
    }

    public ListenerHandler getListenerHandler() {
        return listenerHandler;
    }
}
