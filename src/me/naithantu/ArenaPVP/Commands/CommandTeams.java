package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CommandTeams extends AbstractArenaCommand {

	protected CommandTeams(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "teams") && !testPermission(sender, "player")) {
			this.noPermission(sender);
			return true;
		}

        this.executeCommand(getArenas());
        return true;
	}

    @Override
    protected Collection<Arena> getArenas() {
        return this.selectArena(args, ArenaState.WARMUP, ArenaState.PLAYING, ArenaState.STARTING, ArenaState.LOBBY, ArenaState.FINISHED);
    }

    @Override
    protected void runCommand(Arena arena) {
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
			
			this.msg(sender, team.getColoredName() + "(" + team.getPlayers().size() + "): " + strBuilder.toString());
		}
	}
}
