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
            if (settingMenu.getChangeStatus() == SettingMenu.ChangeStatus.INTEGER && settingMenu.getChangingPlayer().equals(player.getName())) {
                try {
                    Integer newSetting = Integer.parseInt(event.getMessage());
                    Util.msg(event.getPlayer(), "Changed " + settingMenu.getChangingSetting().getName() + " to " + newSetting + "!");
                    settingMenu.changeIntegerSetting(settingMenu.getChangingSetting(), newSetting);
                    event.setCancelled(true);
                } catch (NumberFormatException e) {
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
