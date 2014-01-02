package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.IconMenu;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 AbstractArenaCommand is an extension of AbstractCommand.
 It adds methods to select an arena via arenastate/arena name and then use an iconmenu if there are several applicable arenas.
 */
public abstract class AbstractArenaCommand extends AbstractCommand {

    protected AbstractArenaCommand(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
        super(sender, args, plugin, arenaManager);
    }

    protected abstract void runCommand(Arena arena);

    protected abstract Collection<Arena> getArenas();

    protected List<Arena> selectArena(String[] args){
        return selectArena(args, ArenaState.BEFORE_JOIN, ArenaState.LOBBY, ArenaState.WARMUP, ArenaState.STARTING, ArenaState.PLAYING, ArenaState.FINISHED);
    }

    protected List<Arena> selectArena(String[] args, ArenaState... arenaStates) {
        List<Arena> arenas = new ArrayList<>();
        //If player is in a game, only add that arena
        ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(sender.getName());
        if (arenaPlayer != null) {
            arenas.add(arenaPlayer.getArena());
        } else if (args.length > 0) {
            //If player defined arena name in command, get it with that
            Arena arena = arenaManager.getArena(args[0]);
            if (arena != null) {
                if (checkState(arena, arenaStates)) {
                    arenas.add(arena);
                } else {
                    msg(sender, "That arena is not in the correct state!");
                }
            } else {
                msg(sender, "No arena with that name is currently loaded!");
            }
        } else {
            //Else get all arenas that are in the correct arenastate
            for (Arena arena : arenaManager.getArenas().values()) {
                if (checkState(arena, arenaStates)) {
                    arenas.add(arena);
                }
            }
            if (arenas.size() == 0) {
                msg(sender, "No arena is in the correct state for that command!");
            }
        }
        return arenas;
    }

    private boolean checkState(Arena arena, ArenaState... arenaStates) {
        for (ArenaState state : arenaStates) {
            if (state == arena.getArenaState()) {
                return true;
            }
        }
        return false;
    }

    protected void executeCommand(Collection<Arena> arenas) {
        switch (arenas.size()) {
            case 0:
                break;
            case 1:
                runCommand(arenas.iterator().next());
                break;
            default:
                IconMenu iconMenu = new IconMenu("Select arena", (arenas.size() + 8) / 9 * 9, new IconMenu.OptionClickEventHandler() {
                    @Override
                    public void onOptionClick(IconMenu.OptionClickEvent event) {
                        event.setWillDestroy(true);
                        Arena arena = arenaManager.getArena(event.getName());
                        if(getArenas().contains(arena)){
                            runCommand(arena);
                        } else {
                            badMsg(sender, "You can't use that command on that arena anymore!");
                        }
                    }
                }, true, sender.getName(), plugin);
                int i = 0;
                for (Arena arena : arenas) {
                    iconMenu.setOption(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3), arena.getArenaName(), arena.getNickName());
                    i++;
                }
                iconMenu.open((Player) sender);
                break;
        }
    }
}
