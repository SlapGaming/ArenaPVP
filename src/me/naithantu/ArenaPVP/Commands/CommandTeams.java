package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeams extends AbstractCommand {

	protected CommandTeams(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!testPermission(sender, "teams") && !testPermission(sender, "player")) {
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
				this.msg(sender, "There are currently no arenas to show the teams off.");
				return true;
			} else if (availableArenas == 1) {
				showTeams(arenaManager.getFirstArena());
			} else {
				if (args.length == 0) {
					this.msg(sender, "There are currently several arenas to spectate, please specify the arena you want to spectate.");
					this.msg(sender, "/pvp spectate <arenaname> <teamname>");
					return true;
				}

				if (arenaManager.getArenas().containsKey(args[0])) {
					Arena arena = arenaManager.getArenas().get(args[0]);
					this.msg(sender, "Showing teams of arena " + arena.getNickName());
					showTeams(arena);
				} else {
					this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
				}
				this.msg(sender, "Showing teams for arena ");
			}
		} else {
			showTeams(arenaPlayer.getArena());
		}
		return true;
	}

	private void showTeams(Arena arena) {
		this.msg(sender, "Teams (" + arena.getArenaSpectators().getSpectators().size() + " spectators):");
		for (ArenaTeam team : arena.getTeams()) {
			StringBuilder strBuilder = new StringBuilder();
			boolean first = true;
			for (ArenaPlayer teamPlayer : team.getPlayers()) {
				if(first){
					first = false;
				} else {
					strBuilder.append(", ");
				}
				
				if(teamPlayer.getPlayerState() != ArenaPlayerState.SPECTATING){
					strBuilder.append(teamPlayer.getPlayerName());
				} else {
					strBuilder.append(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + teamPlayer.getPlayerName() + ChatColor.WHITE);
				}
			}
			
			this.msg(sender, team.getColoredName() + ChatColor.WHITE + "(" + team.getPlayers().size() + "): " + strBuilder.toString());
		}
	}
}
