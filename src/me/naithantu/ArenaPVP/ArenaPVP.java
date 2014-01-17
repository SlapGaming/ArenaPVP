package me.naithantu.ArenaPVP;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Commands.CommandHandler;
import me.naithantu.ArenaPVP.Events.BukkitEvents.BlockRedstoneListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.*;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ProjectileHitListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArenaPVP extends JavaPlugin {

    private static ArenaPVP instance;

    private PluginManager pm;
    private Logger logger;

    private TabController tabController;
			
	@Override
	public void onEnable(){
        //Initialize static instance
        instance = this;

		logger = getLogger();
			
		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			logger.log(Level.INFO, "No config found, generating default config.");
			saveDefaultConfig();
		}

		tabController = new TabController();

        pm = Bukkit.getPluginManager();

		//Create required directories
		new File(getDataFolder() + File.separator + "maps").mkdirs();
		new File(getDataFolder() + File.separator + "players").mkdirs();
		new File(getDataFolder() + File.separator + "classes" + File.separator + "inventory").mkdirs();
		new File(getDataFolder() + File.separator + "classes" + File.separator + "armor").mkdirs();
		}
	
	@Override
	public void onDisable(){
		for(Arena arena: ArenaManager.getArenas().values()){
			arena.stopGame();
		}

        //Remove static instance
        instance = null;
	}

    public static ArenaPVP getInstance(){
        return instance;
    }
	
	public void registerListeners(){
		//Player events.
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new DropItemListener(), this);
        pm.registerEvents(new FoodLevelChangeListener(), this);
		pm.registerEvents(new PickupItemListener(), this);
		pm.registerEvents(new InventoryClickListener(), this);
		pm.registerEvents(new MoveListener(), this);
		pm.registerEvents(new QuitListener(), this);
		pm.registerEvents(new RespawnListener(), this);
        //Other events
        pm.registerEvents(new BlockRedstoneListener(), this);
        pm.registerEvents(new ProjectileHitListener(), this);

	}

    public void unRegisterListeners(){
        HandlerList.unregisterAll(this);
    }

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return CommandHandler.handle(sender, cmd, args);
	}
	
	public TabController getTabController() {
		return tabController;
	}
}
