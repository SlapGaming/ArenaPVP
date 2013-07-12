package me.naithantu.ArenaPVP;

import me.naithantu.ArenaPVP.Commands.CommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaPVP extends JavaPlugin {
	CommandHandler commandHandler;
	
	@Override
	public void onEnable(){
		commandHandler = new CommandHandler(this);
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return commandHandler.handle(sender, cmd, args);
	}
}
