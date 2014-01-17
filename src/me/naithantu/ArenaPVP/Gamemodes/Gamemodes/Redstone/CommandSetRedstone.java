package me.naithantu.ArenaPVP.Gamemodes.Gamemodes.Redstone;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Commands.AbstractCommand;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetRedstone extends AbstractCommand {
    Arena arena;

    protected CommandSetRedstone(CommandSender sender, String[] args, Arena arena) {
        super(sender, args);
        this.arena = arena;
    }

    @Override
    public boolean handle() {
        if (!testPermission(sender, "setredstone") && !testPermission(sender, "mod")) {
            this.noPermission(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            this.msg(sender, "That command can only be used in-game.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            this.msg(sender, "Choose the team you want to change the redstone location for.");
            this.msg(sender, "Usage: /pvp setredstone <teamname>");
            return true;
        }

        ArenaTeam teamToChange = arena.getTeam(args[0]);

        if(teamToChange == null){
            this.msg(sender, "No team with given name was found, type /pvp teams to see available teams.");
            return true;
        }

        int teamNumber = teamToChange.getTeamNumber();
        Util.saveLocation(player.getLocation(), arena.getArenaStorage(), "gamemodes.redstone.locations." + teamNumber);
        this.msg(sender, "Changed redstone location for team " + teamToChange.getTeamName() + ".");
        return true;
    }
}
