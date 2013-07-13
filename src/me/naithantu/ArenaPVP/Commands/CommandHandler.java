package me.naithantu.ArenaPVP.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

public class CommandHandler {
	ArenaPVP plugin;
	ArenaManager arenaManager;
	
	public CommandHandler(ArenaPVP plugin, ArenaManager arenaManager){
		this.plugin = plugin;
		this.arenaManager = arenaManager;
	}
	
	public boolean handle(CommandSender sender, Command cmd, String[] args){
		AbstractCommand commandObj = null;
		if(args.length == 0){
			commandObj = new CommandPVP(sender, args, plugin, arenaManager);
		}
		
		//Remove first argument to make it easier for sub-commands to use args.
		String newArgs[] = new String[args.length - 1];
		for(int i = 0; i < args.length - 1; i++){
			newArgs[i] = args[i + 1];
		}
		
		String command = args[0];
		if(command.equals("join")){
			commandObj = new CommandPVP(sender, args, plugin, arenaManager);
			//TODO Join command etc. here, copy/paste things from simplepvp
		}
		
		if (commandObj != null) {
			boolean handled = commandObj.handle();
			if (!handled) {
				commandObj.badMsg(sender, cmd.getUsage());
			}
		}
		return true;
	}
}
