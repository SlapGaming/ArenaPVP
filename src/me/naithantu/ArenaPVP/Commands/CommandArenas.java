package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import org.bukkit.command.CommandSender;

public class CommandArenas extends AbstractCommand {

	protected CommandArenas(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if(!testPermission(sender, "arenas") && !testPermission(sender, "player")){
			this.noPermission(sender);
			return true;
		}

		if(arenaManager.getArenas().isEmpty()){
			this.msg(sender, "There are currently no loaded arenas.");
			return true;
		}
		
		this.msg(sender, "Available arenas:");
		for(Arena arena: arenaManager.getArenas().values()){
			this.msg(sender, arena.getArenaName() + ": " + arena.getArenaState().toString().toLowerCase().replaceAll("_", " "));
		}
		return true;
	}
}
