package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

public class CommandAllowjoin extends AbstractArenaCommand {

	protected CommandAllowjoin(CommandSender sender, String[] args) {
		super(sender, args);
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
        List<Arena> arenas =  this.selectArena(args, ArenaState.BEFORE_JOIN);
        for (int i = arenas.size() - 1; i >= 0; i--) {
            if (!arenas.get(i).getSettings().isEnabled()) {
                arenas.remove(i);
            }
        }

        //Check if any arenas left
        if (arenas.isEmpty()) {
            msg(sender, "No arena is in the correct state for that command!");
        }
        return arenas;
    }

    @Override
    protected void runCommand(Arena arena) {
        arena.setArenaState(ArenaState.LOBBY);
        this.msg(sender, "Players can now join arena " + arena.getArenaName() + "!");
        Util.broadcast("Type " + ChatColor.AQUA + "/pvp join " + ChatColor.WHITE + "to join arena " + arena.getNickName() + ChatColor.WHITE + " (Gamemode: " + arena.getGamemode().getName() + ")!");
    }
}
