package me.naithantu.ArenaPVP.Gamemodes.Gamemodes.Redstone;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.Settings.SettingMenu;
import me.naithantu.ArenaPVP.IconMenu;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.entity.Player;

public class RedstoneMenu {
    private Arena arena;
    private YamlStorage arenaStorage;
    private Redstone redstone;
    private SettingMenu settingMenu;

    public RedstoneMenu(Arena arena, YamlStorage arenaStorage, Redstone redstone) {
        this.arena = arena;
        this.arenaStorage = arenaStorage;
        this.redstone = redstone;
        settingMenu = arena.getSettings().getSettingMenu();
    }

    public void setupIconMenu(IconMenu iconMenu) {
        iconMenu.clearMenu();
        settingMenu.setupChooseTeamMenu(false);
    }

    public void handleMenuClick(IconMenu.OptionClickEvent event) {
        Player player = event.getPlayer();
        String name = event.getName();
        IconMenu iconMenu = event.getIconMenu();

        int teamNumber = Integer.parseInt(name);

        Util.saveLocation(player.getLocation(), arena.getArenaStorage(), "gamemodes.redstone.locations." + teamNumber);
        redstone.initialize();
        Util.msg(player, "Changed redstone location for team " + arena.getTeam(teamNumber).getColoredName() + ".");

        settingMenu.stopChanging();
        iconMenu.update(player);
    }
}
