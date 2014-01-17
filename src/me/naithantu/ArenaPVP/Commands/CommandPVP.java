package me.naithantu.ArenaPVP.Commands;

import org.bukkit.command.CommandSender;

public class CommandPVP extends AbstractCommand {

	protected CommandPVP(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if(!testPermission(sender, "pvp") && !testPermission(sender, "player")){
			this.noPermission(sender);
			return true;
		}

        this.msg(sender, "Unsupported command, this will be added once the plugin is no longer in such an early alpha/beta state.");
		//TODO Create main pvp help command.
		return true;
	}
}
