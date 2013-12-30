package me.naithantu.ArenaPVP;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class ArenaPVP extends JavaPlugin {
	private CommandHandler commandHandler;
    private ArenaManager arenaManager;

    private PluginManager pm;
    private Logger logger;

    private TabController tabController;
			
	@Override
	public void onEnable(){
		logger = getLogger();
			
		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			logger.log(Level.INFO, "No config found, generating default config.");
			saveDefaultConfig();
		}

		arenaManager = new ArenaManager(this);
		commandHandler = new CommandHandler(this, arenaManager);
		
		tabController = new TabController(this);

        pm = Bukkit.getPluginManager();

		//Create required directories
		new File(getDataFolder() + File.separator + "maps").mkdirs();
		new File(getDataFolder() + File.separator + "players").mkdirs();
		new File(getDataFolder() + File.separator + "classes" + File.separator + "inventory").mkdirs();
		new File(getDataFolder() + File.separator + "classes" + File.separator + "armor").mkdirs();
		}
	
	@Override
	public void onDisable(){
		for(Arena arena: arenaManager.getArenas().values()){
			arena.stopGame();
		}
	}
	
	public void registerListeners(){
		//Player events.
		pm.registerEvents(new BlockListener(arenaManager), this);
		pm.registerEvents(new ChatListener(arenaManager), this);
		pm.registerEvents(new DamageListener(arenaManager), this);
		pm.registerEvents(new DeathListener(arenaManager), this);
		pm.registerEvents(new DropItemListener(arenaManager), this);
        pm.registerEvents(new FoodLevelChangeListener(arenaManager), this);
		pm.registerEvents(new PickupItemListener(arenaManager), this);
		pm.registerEvents(new InventoryClickListener(arenaManager), this);
		pm.registerEvents(new MoveListener(arenaManager), this);
		pm.registerEvents(new QuitListener(arenaManager), this);
		pm.registerEvents(new RespawnListener(this, arenaManager), this);
        //Other events
        pm.registerEvents(new BlockRedstoneListener(arenaManager), this);
        pm.registerEvents(new ProjectileHitListener(arenaManager), this);

	}

    public void unRegisterListeners(){
        HandlerList.unregisterAll(this);
    }

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return commandHandler.handle(sender, cmd, args);
	}
	
	public TabController getTabController() {
		return tabController;
	}
}
