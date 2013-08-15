package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class CommandSpectators extends AbstractCommand {

	protected CommandSpectators(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!testPermission(sender, "spectators") && !testPermission(sender, "player")) {
			this.noPermission(sender);
			return true;
		}

		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

		Player player = (Player) sender;
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if (arenaPlayer == null) {
			//Check how many arenas the player can join.
			int availableArenas = 0;
			for (Arena arena : arenaManager.getArenas().values()) {
				if (arena.getArenaState() != ArenaState.BEFORE_JOIN) {
					availableArenas++;
				}
			}

			if (availableArenas == 0) {
				this.msg(sender, "There are currently no arenas to show the spectators of.");
				return true;
			} else if (availableArenas == 1) {
				showSpectators(arenaManager.getFirstArena()); //TODO This is broken, see commandteams.
			} else {
				if (args.length == 0) {
					this.msg(sender, "There are currently several arenas available, please specify the arena you want to see the spectators of.");
					this.msg(sender, "/pvp spectators <arenaname>");
					return true;
				}

				if (arenaManager.getArenas().containsKey(args[0])) {
					Arena arena = arenaManager.getArenas().get(args[0]);
					this.msg(sender, "Showing spectators of arena " + arena.getNickName());
					showSpectators(arena);
				} else {
					this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
				}
			}
		} else {
			showSpectators(arenaPlayer.getArena());
		}
		return true;
	}

	private void showSpectators(Arena arena) {
			this.msg(sender, ChatColor.GRAY + "Spectators: " + ChatColor.WHITE + Joiner.on(", ").join(arena.getArenaSpectators().getSpectators().keySet()));		
	}
}
