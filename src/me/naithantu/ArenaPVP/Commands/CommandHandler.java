package me.naithantu.ArenaPVP.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class CommandHandler {
	ArenaPVP plugin;
	ArenaManager arenaManager;

	public CommandHandler(ArenaPVP plugin, ArenaManager arenaManager) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
	}

	public boolean handle(CommandSender sender, Command cmd, String[] args) {
		AbstractCommand commandObj = null;
		if (args.length == 0) {
			commandObj = new CommandPVP(sender, args, plugin, arenaManager);
		} else {
			String command = args[0];

			// Remove first argument to make it easier for sub-commands to use
			// args.
			String cmdArgs[] = new String[args.length - 1];
			for (int i = 0; i < args.length - 1; i++) {
				cmdArgs[i] = args[i + 1];
			}

			if (command.equals("allowjoin")) {
				commandObj = new CommandAllowjoin(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("arenas")) {
				commandObj = new CommandArenas(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("create")) {
				commandObj = new CommandCreate(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("join")) {
				commandObj = new CommandJoin(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("leave")) {
				commandObj = new CommandLeave(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("maps")) {
				commandObj = new CommandMaps(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("saveinventory")) {
				commandObj = new CommandSaveInventory(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("score")) {
				commandObj = new CommandScore(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("select")) {
				commandObj = new CommandSelect(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("start")) {
				commandObj = new CommandStart(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("stop")) {
				commandObj = new CommandStop(sender, cmdArgs, plugin, arenaManager);
			} else if (command.equals("teams")) {
				commandObj = new CommandTeams(sender, cmdArgs, plugin, arenaManager);
			} else {
				ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(sender.getName());
				if (arenaPlayer != null) {
					commandObj = arenaPlayer.getArena().getGamemode().executeCommand(command);
				}
			}
		}

		if (commandObj != null) {
			boolean handled = commandObj.handle();
			if (!handled) {
				commandObj.badMsg(sender, cmd.getUsage());
			}
		}
		return true;
	}
}
