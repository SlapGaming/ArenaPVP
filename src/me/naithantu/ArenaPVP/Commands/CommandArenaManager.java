package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.command.CommandSender;

public class CommandArenaManager extends AbstractCommand {

	protected CommandArenaManager(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if(!testPermission(sender, "arenamanager") && !testPermission(sender, "admin")){
			this.noPermission(sender);
			return true;
		}
		
		this.msg(sender, "Arenas in arenamanager:");
		for(Arena arena: ArenaManager.getArenas().values()){
			this.msg(sender, arena.getArenaName() + ": " + arena.getArenaState().toString().toLowerCase().replaceAll("_", " "));
		}
        this.msg(sender, "Players in arenamanager:");
        for(ArenaPlayer arenaPlayer : ArenaManager.getAllPlayers().values()){
            this.msg(sender, arenaPlayer.getPlayerName() + " in arena " + arenaPlayer.getArena().getArenaName() + " in team " + arenaPlayer.getTeam().getTeamName());
        }
		return true;
	}
}
