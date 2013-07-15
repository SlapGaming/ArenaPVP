package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCreate extends AbstractCommand {

	protected CommandCreate(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}
		
		if(args.length == 0){
			this.msg(sender, "Usage: /pvp create [arenaname]");
			return true;
		}

		Player player = (Player) sender;
		
		Arena arena = new Arena(plugin, arenaManager, args[0], "tdm");
		arenaManager.getArenas().add(arena);
		return true;
	}
}
