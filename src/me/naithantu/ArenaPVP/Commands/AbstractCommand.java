package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand {
    protected ArenaPVP plugin = ArenaPVP.getInstance();
    protected CommandSender sender;
    protected String[] args;

    protected AbstractCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    protected abstract boolean handle();

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
        return !(sender instanceof Player) || sender.hasPermission(permission);
    }
}
