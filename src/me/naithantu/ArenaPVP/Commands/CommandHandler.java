package me.naithantu.ArenaPVP.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.naithantu.ArenaPVP.ArenaPVP;

public class CommandHandler {
	ArenaPVP plugin;
	
	public CommandHandler(ArenaPVP plugin){
		this.plugin = plugin;
	}
	
	public boolean handle(CommandSender sender, Command cmd, String[] args){
		AbstractCommand commandObj = null;
		if(args.length == 0){
			commandObj = new CommandPVP(sender, args, plugin);
		}
		
		String command = args[0];
		if(command.equals("join")){		
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
