package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.Settings.SettingMenu;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

public class ChatListener implements Listener {
    ArenaManager arenaManager;

    public ChatListener(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        for (Arena arena : arenaManager.getArenas().values()) {
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
            ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
            if (arenaPlayer != null) {
                arenaPlayer.getArena().getGamemode().onPlayerChat(event, arenaPlayer);
            }
        }
    }
}
