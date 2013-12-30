package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandChange extends AbstractCommand {

	protected CommandChange(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!testPermission(sender, "change") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

        if(!(sender instanceof Player)){
            this.badMsg(sender, "That command can only be used in-game!");
            return true;
        }

        Player player = (Player) sender;

		// Check how many arenas the player can enable.
		int availableArenas = 0;
		for (Arena arena : arenaManager.getArenas().values()) {
			if (arena.getArenaState() == ArenaState.BEFORE_JOIN) {
				availableArenas++;
			}
		}

		if (availableArenas == 0) {
			this.msg(sender, "There are currently no arenas that you can change.");
			return true;
		}

		if (availableArenas > 1) {
			// If there are several arenas, find out what arena players want to enable.
			if (args.length == 0) {
				this.msg(sender, "There are currently several arenas to change, please specify the arena you want to change.");
				this.msg(sender, "/pvp change <arenaname>");
				return true;
			}

			if (arenaManager.getArenas().containsKey(args[0])) {
				Arena arena = arenaManager.getArenas().get(args[0]);
                changeArena(player, arena);
			} else {
				this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			}
			return true;
		} else {
			// If there is only arena
			Arena arena = arenaManager.getFirstArena(); //TODO When several arenas exist in different states, this doesn't work! Retrieve arena via arenastate (preferably via abstractcommand!)
            changeArena(player, arena);
		}
		return true;
	}

	public void changeArena(Player player, Arena arena) {
        arena.getSettings().getSettingMenu().openMenu(player);
        this.msg(player, "Opened main setting menu!");
	}
}
