package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {

    private CommandHandler() {
    }

    public static boolean handle(CommandSender sender, Command cmd, String[] args) {
        AbstractCommand commandObj = null;
        if (args.length == 0) {
            commandObj = new CommandPVP(sender, args);
        } else {
            String command = args[0];
            // Remove first argument to make it easier for sub-commands to use args.

            String cmdArgs[] = new String[args.length - 1];
            for (int i = 0; i < args.length - 1; i++) {
                cmdArgs[i] = args[i + 1];
            }
            
            switch (command.toLowerCase()) {
                case "allowjoin":
                    commandObj = new CommandAllowjoin(sender, cmdArgs);
                    break;
                case "arenas":
                    commandObj = new CommandArenas(sender, cmdArgs);
                    break;
                case "arenamanager":
                    commandObj = new CommandArenaManager(sender, cmdArgs);
                    break;
                case "chat":
                    commandObj = new CommandChat(sender, cmdArgs);
                    break;
                case "chatspy":
                    commandObj = new CommandChatspy(sender, cmdArgs);
                    break;
                case "create":
                    commandObj = new CommandCreate(sender, cmdArgs);
                    break;
                case "change":
                    commandObj = new CommandChange(sender, cmdArgs);
                    break;
                case "join":
                    commandObj = new CommandJoin(sender, cmdArgs);
                    break;
                case "leave":
                    commandObj = new CommandLeave(sender, cmdArgs);
                    break;
                case "maps":
                    commandObj = new CommandMaps(sender, cmdArgs);
                    break;
                case "saveschematic":
                    commandObj = new CommandSaveSchematic(sender, cmdArgs);
                    break;
                case "score":
                    commandObj = new CommandScore(sender, cmdArgs);
                    break;
                case "select":
                    commandObj = new CommandSelect(sender, cmdArgs);
                    break;
                case "spectate":
                    commandObj = new CommandSpectate(sender, cmdArgs);
                    break;
                case "spectators":
                    commandObj = new CommandSpectators(sender, cmdArgs);
                    break;
                case "start":
                    commandObj = new CommandStart(sender, cmdArgs);
                    break;
                case "stop":
                    commandObj = new CommandStop(sender, cmdArgs);
                    break;
                case "teams":
                    commandObj = new CommandTeams(sender, cmdArgs);
                    break;
                    
                default:
                    ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(sender.getName());
                    if (arenaPlayer != null) {
                        commandObj = arenaPlayer.getArena().getGamemode().handleGamemodeCommand(sender, command, cmdArgs);
                    }
            }
        }

        if (commandObj != null) {
            boolean handled = commandObj.handle();
            if (!handled) {
                commandObj.badMsg(sender, cmd.getUsage());
            }
        } else {
            Util.msg(sender, "That command does not exist, use /pvp for help!");
        }
        return true;
    }
}
