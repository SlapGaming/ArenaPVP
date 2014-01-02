package me.naithantu.ArenaPVP.Gamemodes.Gamemodes.CTF;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.Settings.SettingMenu;
import me.naithantu.ArenaPVP.IconMenu;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CTFMenu {
    private Arena arena;
    private YamlStorage arenaStorage;
    private CTF ctf;
    private SettingMenu settingMenu;

    private ChangeStatus changeStatus = ChangeStatus.NONE;

    public CTFMenu(Arena arena, YamlStorage arenaStorage, CTF ctf) {
        this.arena = arena;
        this.arenaStorage = arenaStorage;
        this.ctf = ctf;
        settingMenu = arena.getSettings().getSettingMenu();
    }

    public void setupIconMenu(IconMenu iconMenu) {
        iconMenu.clearMenu();

        iconMenu.setOption(9, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Flags", "The locations of the flags");
        iconMenu.setOption(10, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Flag blocks", "The block that represents the flag");
    }

    public void handleMenuClick(IconMenu.OptionClickEvent event) {
        Player player = event.getPlayer();
        String name = event.getName();
        IconMenu iconMenu = event.getIconMenu();

        if (changeStatus == ChangeStatus.NONE) {
            switch (name) {
                case "Flags":
                    settingMenu.setupChooseTeamMenu(false);
                    iconMenu.update(player);
                    changeStatus = ChangeStatus.FLAGS;
                    break;
                case "Flag blocks":
                    settingMenu.setupChooseTeamMenu(false);
                    iconMenu.update(player);
                    changeStatus = ChangeStatus.FLAGBLOCKS;
                    break;
            }
        } else if (changeStatus == ChangeStatus.FLAGS) {
            int teamNumber = Integer.parseInt(name);

            Util.saveLocation(player.getLocation(), arena.getArenaStorage(), "gamemodes.ctf.flags." + teamNumber);
            ctf.initialize();
            Util.msg(player, "Changed flag location for team " + arena.getTeam(teamNumber).getColoredName() + ".");

            settingMenu.stopChanging();
            iconMenu.update(player);
        } else if (changeStatus == ChangeStatus.FLAGBLOCKS) {
            Configuration arenaConfig = arenaStorage.getConfig();
            int teamNumber = Integer.parseInt(name);

            arenaConfig.set("gamemodes.ctf.blocks." + teamNumber, player.getItemInHand());
            arenaStorage.saveConfig();
            ctf.initialize();
            Util.msg(player, "Changed flag block for team " + arena.getTeam(teamNumber).getColoredName() + ".");

            settingMenu.stopChanging();
            iconMenu.update(player);
        }

    }

    public void stopChanging() {
        changeStatus = ChangeStatus.NONE;
    }

    private enum ChangeStatus {
        NONE, FLAGS, FLAGBLOCKS
    }
}
