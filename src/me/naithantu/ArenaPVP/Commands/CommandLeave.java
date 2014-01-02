package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLeave extends AbstractCommand {

	protected CommandLeave(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "leave") && !testPermission(sender, "player")) {
			this.noPermission(sender);
			return true;
		}

		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

		ArenaPlayer arenaPlayer;
		if ((arenaPlayer = arenaManager.getPlayerByName(sender.getName())) == null) {
			this.msg(sender, "You are not in a game!");
			return true;
		}

		if (arenaPlayer.getTeam() == null && arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING) {
			arenaPlayer.getArena().leaveSpectate((Player) sender, arenaPlayer);
			this.msg(sender, "You are no longer spectating!");
		} else {
			arenaPlayer.getArena().leaveGame(arenaPlayer);
			this.msg(sender, "You left the game!");
		}
		return true;
	}
}
