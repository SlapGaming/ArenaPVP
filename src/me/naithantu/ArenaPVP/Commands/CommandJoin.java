package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJoin extends AbstractCommand {

	protected CommandJoin(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

		Player player = (Player) sender;

		//Check how many arenas the player can join.
		int availableArenas = 0;
		for (Arena arena : arenaManager.getArenas()) {
			if (arena.getArenaState() == ArenaState.LOBBY || arena.getArenaState() == ArenaState.WARMUP) {
				availableArenas++;
			}
		}
		
		if (availableArenas == 0) {
			this.msg(sender, "There are currently no arenas that you can join.");
			return true;
		}

		if (availableArenas > 1) {
			// If there are several arenas, find out what arena players want to join.
			if (args.length == 0) {
				this.msg(sender, "There are currently several arenas to join, please specify the arena you want to join.");
				this.msg(sender, "/pvp join <arenaname> <teamname>");
				return true;
			}

			for (Arena arena : arenaManager.getArenas()) {
				if (arena.getArenaName().equalsIgnoreCase(args[0])) {
					if (args.length > 2) {
						arena.joinGame(player, arena.getTeam(args[1]));
					} else {
						arena.joinGame(player);
					}
					return true;
				}
			}
			
			this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			return true;
		} else {
			//If there is only arena
			Arena arena = arenaManager.getArenas().get(0);
			if (args.length > 0) {
				arena.joinGame(player, arena.getTeam(args[0]));
			} else {
				arena.joinGame(player);
			}
		}
		return true;
	}
}
