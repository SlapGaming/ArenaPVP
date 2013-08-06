package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand {

	abstract public boolean handle();

	protected CommandSender sender;
	protected String[] args;
	protected ArenaPVP plugin;
	protected ArenaManager arenaManager;

	protected AbstractCommand(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		this.sender = sender;
		this.args = args;
		this.plugin = plugin;
		this.arenaManager = arenaManager;
	}

	protected void msg(CommandSender sender, String msg) {
		Util.msg(sender, msg);
	}

	protected void badMsg(CommandSender sender, String msg) {
		if (sender instanceof Player) {
			sender.sendMessage(ChatColor.RED + msg);
		} else {
			sender.sendMessage(msg);
		}
	}

	protected void noPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
	}

	protected boolean testPermission(CommandSender sender, String perm) {
		String permission = "arenapvp." + perm;
		if (!(sender instanceof Player) || sender.hasPermission(permission))
			return true;
		return false;
	}
}
