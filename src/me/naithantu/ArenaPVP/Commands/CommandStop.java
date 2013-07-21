package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

import org.bukkit.command.CommandSender;

public class CommandStop extends AbstractCommand {

	protected CommandStop(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!testPermission(sender, "stop") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

		// Check how many arenas the player can start.
		int availableArenas = 0;


		if (arenaManager.getArenas().isEmpty()) {
			this.msg(sender, "There are currently no arenas that you can stop.");
			return true;
		}

		if (availableArenas > 1) {
			// If there are several arenas, find out what arena players want to
			// join.
			if (args.length == 0) {
				this.msg(sender, "There are currently several arenas to stop, please specify the arena you want to stop.");
				this.msg(sender, "/pvp stop <arenaname>");
				return true;
			}

			String arenaName = args[0].toLowerCase();
			;

			if (arenaManager.getArenas().containsKey(arenaName)) {
				stopArena(arenaManager.getArenas().get(arenaName));
				return true;
			}

			this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			return true;
		} else {
			// If there is only one arena
			Arena arena = arenaManager.getFirstArena();
			stopArena(arena);
		}
		return true;
	}

	public void stopArena(Arena arena) {
		arena.stopGame(null);
		this.msg(sender, "You have stopped arena " + arena.getArenaName() + "!");
	}
}
