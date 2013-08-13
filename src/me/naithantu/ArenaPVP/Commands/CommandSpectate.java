package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpectate extends AbstractCommand {

	protected CommandSpectate(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if(!testPermission(sender, "spectate") && !testPermission(sender, "player")){
			this.noPermission(sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}
		
		if(arenaManager.getPlayerByName(sender.getName()) != null){
			this.msg(sender, "You are already in a game, you can leave with /pvp leave!");
			return true;
		}

		Player player = (Player) sender;

		//Check how many arenas the player can join.
		int availableArenas = 0;
		for (Arena arena : arenaManager.getArenas().values()) {
			if (arena.getArenaState() != ArenaState.BEFORE_JOIN) {
				availableArenas++;
			}
		}
		
		if (availableArenas == 0) {
			this.msg(sender, "There are currently no arenas that you can spectate.");
			return true;
		}

		if (availableArenas > 1) {
			// If there are several arenas, find out what arena players want to join.
			if (args.length == 0) {
				this.msg(sender, "There are currently several arenas to spectate, please specify the arena you want to spectate.");
				this.msg(sender, "/pvp spectate <arenaname> <teamname>");
				return true;
			}

			if(arenaManager.getArenas().containsKey(args[0])){
				Arena arena = arenaManager.getArenas().get(args[0]);
				joinSpectate(arena, player);
			} else {
				this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			}
			return true;
		} else {
			//If there is only arena
			Arena arena = arenaManager.getFirstArena();
			joinSpectate(arena, player);
		}
		return true;
	}
	
	public void joinSpectate(Arena arena, Player player){
		if(arena.getArenaStorage().getConfig().getBoolean("allowspectate")){
			arena.joinSpectate(player);
		} else {
			this.msg(sender, "You may not spectate that arena!");
		}
	}
}
