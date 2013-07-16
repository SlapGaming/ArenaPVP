package me.naithantu.ArenaPVP.Commands;

import java.util.ArrayList;
import java.util.List;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;
import me.naithantu.ArenaPVP.Objects.ArenaTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class CommandTeams extends AbstractCommand {

	protected CommandTeams(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if(!testPermission(sender, "teams") && !testPermission(sender, "player")){
			this.noPermission(sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

		Player player = (Player) sender;
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if(arenaPlayer == null){
			this.msg(sender, "You are currently not in a game!");
			return true;
		}
		
		for(ArenaTeam team: arenaPlayer.getArena().getTeams()){
			List<String> teamPlayers = new ArrayList<String>();
			for(ArenaPlayer teamPlayer: team.getPlayers()){
				teamPlayers.add(teamPlayer.getPlayerName());
			}
			this.msg(sender, Joiner.on(",").join(teamPlayers));
		}
		return true;
	}
}
