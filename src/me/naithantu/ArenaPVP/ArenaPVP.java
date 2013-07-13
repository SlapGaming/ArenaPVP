package me.naithantu.ArenaPVP;

import me.naithantu.ArenaPVP.Commands.CommandHandler;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaPVP extends JavaPlugin {
	CommandHandler commandHandler;
	ArenaManager arenaManager;
	
	@Override
	public void onEnable(){
		arenaManager = new ArenaManager();
		commandHandler = new CommandHandler(this, arenaManager);
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return commandHandler.handle(sender, cmd, args);
	}
}
