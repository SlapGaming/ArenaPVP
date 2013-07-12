package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;

import org.bukkit.command.CommandSender;

public class CommandPVP extends AbstractCommand {

	protected CommandPVP(CommandSender sender, String[] args, ArenaPVP plugin) {
		super(sender, args, plugin);
	}

	@Override
	public boolean handle() {
		//TODO Create main pvp help command.
		return true;
	}
}
