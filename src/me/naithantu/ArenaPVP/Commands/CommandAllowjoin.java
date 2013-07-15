package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;

import org.bukkit.command.CommandSender;

public class CommandAllowjoin extends AbstractCommand {

	protected CommandAllowjoin(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		//Check how many arenas the player can enable.
		int availableArenas = 0;
		for (Arena arena : arenaManager.getArenas()) {
			if (arena.getArenaState() == ArenaState.BEFORE_JOIN) {
				availableArenas++;
			}
		}
		
		if (availableArenas == 0) {
			this.msg(sender, "There are currently no arenas that you can enable.");
			return true;
		}

		if (availableArenas > 1) {
			// If there are several arenas, find out what arena players want to join.
			if (args.length == 0) {
				this.msg(sender, "There are currently several arenas to enable, please specify the arena you want to join.");
				this.msg(sender, "/pvp allowjoin <arenaname> <teamname>");
				return true;
			}

			for (Arena arena : arenaManager.getArenas()) {
				if (arena.getArenaName().equalsIgnoreCase(args[0])) {
					arena.setArenaState(ArenaState.LOBBY);
					return true;
				}
			}
			
			this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			return true;
		} else {
			//If there is only arena
			Arena arena = arenaManager.getArenas().get(0);
			arena.setArenaState(ArenaState.LOBBY);
		}
		return true;
	}
}
