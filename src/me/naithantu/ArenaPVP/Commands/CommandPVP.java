package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

import org.bukkit.command.CommandSender;

public class CommandPVP extends AbstractCommand {

	protected CommandPVP(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		//TODO Create main pvp help command.
		return true;
	}
}
