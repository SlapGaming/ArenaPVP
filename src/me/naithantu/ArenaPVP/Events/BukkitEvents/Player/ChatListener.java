package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.Settings.SettingMenu;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    public ChatListener() {
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        for (Arena arena : ArenaManager.getArenas().values()) {
            SettingMenu settingMenu = arena.getSettings().getSettingMenu();
            if(settingMenu.getChangingPlayer() != null && settingMenu.getChangingPlayer().equals(player.getName())){
                if(settingMenu.getChangeStatus() == SettingMenu.ChangeStatus.STRING){
                    settingMenu.handleChatInput(player, event.getMessage());
                    event.setCancelled(true);
                } else if (settingMenu.getChangeStatus() == SettingMenu.ChangeStatus.INTEGER && settingMenu.getChangingPlayer().equals(player.getName())) {
                    try {
                        Integer integer = Integer.parseInt(event.getMessage());
                        settingMenu.handleChatInput(player, integer);
                        event.setCancelled(true);
                    } catch (NumberFormatException e) {
                        Util.msg(player, "That is not a valid number!");
                    }
                }
            }
        }

        if(!event.isCancelled()){
            ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
            if (arenaPlayer != null) {
                arenaPlayer.getArena().getGamemode().onPlayerChat(event, arenaPlayer);
            }
        }
    }
}
