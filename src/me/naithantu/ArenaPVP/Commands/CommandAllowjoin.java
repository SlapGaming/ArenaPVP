package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CommandAllowjoin extends AbstractArenaCommand {

	protected CommandAllowjoin(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

    @Override
	protected boolean handle() {
		if (!testPermission(sender, "allowjoin") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

        this.executeCommand(getArenas());
		return true;
	}

    @Override
    protected Collection<Arena> getArenas(){
        return this.selectArena(args, ArenaState.BEFORE_JOIN);
    }

    @Override
    protected void runCommand(Arena arena) {
        arena.setArenaState(ArenaState.LOBBY);
        this.msg(sender, "Players can now join arena " + arena.getArenaName() + "!");
        Util.broadcast("Type " + ChatColor.AQUA + "/pvp join " + ChatColor.WHITE + "to join arena " + arena.getNickName() + ChatColor.WHITE + " (Gamemode: " + arena.getGamemode().getName() + ")!");
    }
}
