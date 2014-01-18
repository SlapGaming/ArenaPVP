package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class CommandSpectate extends AbstractArenaCommand {

	protected CommandSpectate(CommandSender sender, String[] args) {
		super(sender, args);
	}

    @Override
    protected boolean handle() {
		if(!testPermission(sender, "spectate") && !testPermission(sender, "player")){
			this.noPermission(sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

        if(ArenaManager.getPlayerByName(sender.getName()) != null){
            this.msg(sender, "You are already in a game, you can leave with /pvp leave!");
            return true;
        }

        this.executeCommand(getArenas());
		return true;
	}

    @Override
    protected Collection<Arena> getArenas() {
        return this.selectArena(args, ArenaState.WARMUP, ArenaState.PLAYING, ArenaState.STARTING, ArenaState.LOBBY, ArenaState.FINISHED);
    }

    @Override
    protected void runCommand(Arena arena) {
        if(arena.getSettings().isAllowSpectate()){
            arena.getArenaSpectatorController().joinSpectate((Player) sender);
        } else {
            this.msg(sender, "You may not spectate that arena!");
        }
    }
}
