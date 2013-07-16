package me.naithantu.ArenaPVP;

import java.io.File;

import me.naithantu.ArenaPVP.Commands.CommandHandler;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.DamageListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.DeathListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.MoveListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.QuitListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.RespawnListener;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaPVP extends JavaPlugin {
	CommandHandler commandHandler;
	ArenaManager arenaManager;
	
	@Override
	public void onEnable(){
		arenaManager = new ArenaManager();
		commandHandler = new CommandHandler(this, arenaManager);
		
		
		
		//Create required directories
		new File(getDataFolder() + File.separator + "maps").mkdirs();
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public void registerListeners(){
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DamageListener(arenaManager), this);
		pm.registerEvents(new DeathListener(arenaManager), this);
		pm.registerEvents(new MoveListener(arenaManager), this);
		pm.registerEvents(new QuitListener(arenaManager), this);
		pm.registerEvents(new RespawnListener(arenaManager), this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return commandHandler.handle(sender, cmd, args);
	}
}
