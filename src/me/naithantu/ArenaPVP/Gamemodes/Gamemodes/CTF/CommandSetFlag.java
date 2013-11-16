package me.naithantu.ArenaPVP.Gamemodes.Gamemodes.CTF;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Commands.AbstractCommand;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandSetFlag extends AbstractCommand {
    Arena arena;

    protected CommandSetFlag(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager, Arena arena) {
        super(sender, args, plugin, arenaManager);
        this.arena = arena;
    }

    @Override
    public boolean handle() {
        if (!testPermission(sender, "setflag") && !testPermission(sender, "admin")) {
            this.noPermission(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            this.msg(sender, "That command can only be used in-game.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            this.msg(sender, "Choose the team you want to change the flag for.");
            this.msg(sender, "Usage: /pvp setflag <teamname>");
            return true;
        }

        ArenaTeam teamToChange = arena.getTeam(args[0]);

        if(teamToChange == null){
            this.msg(sender, "No team with given name was found, type /pvp teams to see available teams.");
            return true;
        }

        ItemStack flagBlock = player.getItemInHand();

        YamlStorage arenaStorage = arena.getArenaStorage();
        Configuration arenaConfig = arenaStorage.getConfig();
        int teamNumber = teamToChange.getTeamNumber();
        arenaConfig.set("gamemodes.ctf.flags." + teamNumber, Util.getStringFromLocation(player.getLocation()));
        arenaStorage.saveConfig();
        this.msg(sender, "Changed flag block for team " + teamToChange.getTeamName() + ".");
        return true;
    }
}
