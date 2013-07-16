package me.naithantu.ArenaPVP.Commands;

import java.io.File;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

import org.bukkit.command.CommandSender;

public class CommandSelect extends AbstractCommand {

	protected CommandSelect(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if(!testPermission(sender, "select") && !testPermission(sender, "mod")){
			this.noPermission(sender);
			return true;
		}
		
		if(args.length == 0){
			this.msg(sender, "Usage: /pvp select [arenaname]");
			return true;
		}
		
		String arenaName = args[0].toLowerCase();
		
		
		File file = new File(plugin.getDataFolder() + File.separator + "maps", arenaName + ".yml");
		if(!file.exists()){
			this.msg(sender, "That map does not exist!");
			return true;
		}
		
		if(arenaManager.getArenas().containsKey(arenaName)){
			this.msg(sender, "That arena is already loaded! You can unload it with /pvp stop [arenaname].");
			return true;
		}
		
		Arena arena = new Arena(plugin, arenaManager, arenaName, "tdm");
		arenaManager.getArenas().put(arenaName, arena);
		
		this.msg(sender, "Loaded arena with name " + arena.getArenaName() + "!");
		return true;
	}
}
