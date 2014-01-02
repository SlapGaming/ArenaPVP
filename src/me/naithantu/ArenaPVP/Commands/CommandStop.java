package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;

import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CommandStop extends AbstractArenaCommand {

	protected CommandStop(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "stop") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
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
    protected void runCommand(Arena arena) {
		arena.stopGame();
		this.msg(sender, "You have stopped arena " + arena.getArenaName() + "!");
	}
}
