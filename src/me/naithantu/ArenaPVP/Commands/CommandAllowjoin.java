package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandAllowjoin extends AbstractCommand {

	protected CommandAllowjoin(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!testPermission(sender, "allowjoin") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

		// Check how many arenas the player can enable.
		int availableArenas = 0;
		for (Arena arena : arenaManager.getArenas().values()) {
			if (arena.getArenaState() == ArenaState.BEFORE_JOIN) {
				availableArenas++;
			}
		}

		if (availableArenas == 0) {
			this.msg(sender, "There are currently no arenas that you can enable.");
			return true;
		}

		if (availableArenas > 1) {
			// If there are several arenas, find out what arena players want to enable.
			if (args.length == 0) {
				this.msg(sender, "There are currently several arenas to enable, please specify the arena you want to enable.");
				this.msg(sender, "/pvp allowjoin <arenaname> <teamname>");
				return true;
			}

			if (arenaManager.getArenas().containsKey(args[0])) {
				Arena arena = arenaManager.getArenas().get(args[0]);
				enableArena(arena);
			} else {
				this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			}
			return true;
		} else {
			// If there is only arena
			Arena arena = arenaManager.getFirstArena();
			enableArena(arena);
		}
		return true;
	}

	public void enableArena(Arena arena) {
		arena.setArenaState(ArenaState.LOBBY);
		this.msg(sender, "Players can now join arena " + arena.getArenaName() + "!");
		Util.broadcast("Type " + ChatColor.AQUA + "/pvp join " + ChatColor.WHITE + "to join arena " + arena.getNickName() + ChatColor.WHITE + " (Gamemode: " + arena.getGamemode().getName() + ")!");
	}
}
