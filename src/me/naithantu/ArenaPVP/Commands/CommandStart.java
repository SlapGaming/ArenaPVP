package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;

import org.bukkit.command.CommandSender;

public class CommandStart extends AbstractCommand {

	protected CommandStart(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!testPermission(sender, "start") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

		// Check how many arenas the player can start.
		int availableArenas = 0;
		for (Arena arena : arenaManager.getArenas().values()) {
			if (arena.getArenaState() == ArenaState.LOBBY) {
				availableArenas++;
			}
		}

		if (availableArenas == 0) {
			this.msg(sender, "There are currently no arenas that you can start.");
			return true;
		}

		if (availableArenas > 1) {
			// If there are several arenas, find out what arena players want to
			// join.
			if (args.length == 0) {
				this.msg(sender, "There are currently several arenas to start, please specify the arena you want to join.");
				this.msg(sender, "/pvp start <arenaname> <teamname>");
				return true;
			}

			String arenaName = args[0].toLowerCase();
			;

			if (arenaManager.getArenas().containsKey(arenaName)) {
				startArena(arenaManager.getArenas().get(arenaName));
				return true;
			}

			this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			return true;
		} else {
			// If there is only one arena
			Arena arena = arenaManager.getFirstArena();
			startArena(arena);
		}
		return true;
	}

	public void startArena(Arena arena) {
		arena.startGame();
		this.msg(sender, "You have started arena " + arena.getArenaName() + "!");
	}
}
