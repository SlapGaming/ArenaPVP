package me.naithantu.ArenaPVP;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.naithantu.ArenaPVP.Commands.CommandHandler;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.DamageListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.DeathListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.MoveListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.QuitListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.RespawnListener;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaPVP extends JavaPlugin {
	CommandHandler commandHandler;
	ArenaManager arenaManager;
	
	Logger logger;
		
	@Override
	public void onEnable(){
		logger = getLogger();
		
		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			logger.log(Level.INFO, "No config found, generating default config.");
			saveDefaultConfig();
		}
		generateConfig();
		
		arenaManager = new ArenaManager();
		commandHandler = new CommandHandler(this, arenaManager);
		
		registerListeners();
		
		//Create required directories
		new File(getDataFolder() + File.separator + "maps").mkdirs();
		new File(getDataFolder() + File.separator + "players").mkdirs();
		new File(getDataFolder() + File.separator + "classes" + File.separator + "inventory").mkdirs();
		new File(getDataFolder() + File.separator + "classes" + File.separator + "armor").mkdirs();
	}
	
	@Override
	public void onDisable(){
		for(Arena arena: arenaManager.getArenas().values()){
			arena.stopGame(null);
		}
	}
	
	public void registerListeners(){
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DamageListener(arenaManager), this);
		pm.registerEvents(new DeathListener(arenaManager), this);
		pm.registerEvents(new MoveListener(arenaManager), this);
		pm.registerEvents(new QuitListener(arenaManager), this);
		pm.registerEvents(new RespawnListener(this, arenaManager), this);
	}
	
	public void generateConfig(){
		Configuration config = getConfig();
		if(config.getString("spawnlocation") == null){
			config.set("spawnlocation", Util.getStringFromLocation(Bukkit.getWorld("world").getSpawnLocation()));
			saveConfig();
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return commandHandler.handle(sender, cmd, args);
	}
}
