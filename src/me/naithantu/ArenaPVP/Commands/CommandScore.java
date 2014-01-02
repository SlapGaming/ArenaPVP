package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class CommandScore extends AbstractArenaCommand {

	protected CommandScore(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
    protected boolean handle() {
		if(!testPermission(sender, "score") && !testPermission(sender, "player")){
			this.noPermission(sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

        this.executeCommand(getArenas());
		return true;
	}

    @Override
    protected Collection<Arena> getArenas() {
        return this.selectArena(args);
    }

    @Override
    protected void runCommand(Arena arena){
        arena.getGamemode().sendScore(sender);
    }
}
