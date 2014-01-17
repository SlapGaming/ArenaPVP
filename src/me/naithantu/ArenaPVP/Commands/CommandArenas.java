package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.command.CommandSender;

public class CommandArenas extends AbstractCommand {

	protected CommandArenas(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if(!testPermission(sender, "arenas") && !testPermission(sender, "player")){
			this.noPermission(sender);
			return true;
		}

		if(ArenaManager.getArenas().isEmpty()){
			this.msg(sender, "There are currently no loaded arenas.");
			return true;
		}
		
		this.msg(sender, "Available arenas:");
		for(Arena arena: ArenaManager.getArenas().values()){
			this.msg(sender, arena.getArenaName() + ": " + arena.getArenaState().toString().toLowerCase().replaceAll("_", " "));
		}
		return true;
	}
}
