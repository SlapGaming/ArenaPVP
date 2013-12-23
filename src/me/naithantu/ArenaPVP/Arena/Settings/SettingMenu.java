package me.naithantu.ArenaPVP.Arena.Settings;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.IconMenu;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettingMenu {
    private ArenaPVP plugin;
    private ArenaSettings arenaSettings;
    private YamlStorage arenaStorage;
    private FileConfiguration arenaConfig;

    private IconMenu iconMenu;

    private ChangeStatus changeStatus;
    private Setting changingSetting;
    private String changingPlayer;

    public SettingMenu(ArenaPVP plugin, YamlStorage arenaStorage, final ArenaSettings arenaSettings) {
        this.plugin = plugin;
        this.arenaSettings = arenaSettings;
        this.arenaStorage = arenaStorage;
        arenaConfig = arenaStorage.getConfig();
        changeStatus = ChangeStatus.NONE;
        iconMenu = new IconMenu("Setting menu", 5 * 9, new MenuClickEventHandler(), plugin);
        setupMenu();
    }

    public String getChangingPlayer(){
        return changingPlayer;
    }

    public ChangeStatus getChangeStatus(){
        return changeStatus;
    }

    public Setting getChangingSetting(){
        return changingSetting;
    }

    private void setupMenu(SettingGroup settingGroup) {
        //Empty menu
        iconMenu.clearMenu();

        setupMenu();

        int i = 9;

        //Add settings
        for (Setting setting : arenaSettings.getSettings()) {
            if (setting.getSettingGroup() == settingGroup) {
                ItemStack itemStack = new ItemStack(Material.STAINED_CLAY, 1, (short) 1);
                String currentSetting;
                if(setting.getSetting() instanceof Boolean){
                    if(setting.getSetting() == true){
                        currentSetting = "Yes";
                    } else {
                        currentSetting = "No";
                    }
                } else {
                    currentSetting = setting.getSetting().toString();
                }
                iconMenu.setOption(i, itemStack, setting.getName() + " (" + currentSetting + ")", setting.getDescription());
                i++;
            }
        }
    }

    private void setupMenu() {
        int i = 0;
        //Add setting groups.
        for (SettingGroup settingGroup : SettingGroup.values()) {
            ItemStack itemStack = new ItemStack(Material.STAINED_GLASS, 1);
            iconMenu.setOption(i, itemStack, settingGroup.name().substring(0, 1) + settingGroup.name().substring(1).toLowerCase());
            i++;
        }

        //Add alternative settings
        iconMenu.setOption(36, new ItemStack(Material.STAINED_GLASS, 1), "Gamemode");
    }

    private void setupBooleanChangeMenu(Setting setting){
        iconMenu.clearMenu();
        setupMenu();
        iconMenu.setOption(9, new ItemStack(Material.STAINED_CLAY, 1, (short) 3), setting.getName() + "?", setting.getDescription());
        iconMenu.setOption(18, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Yes");
        iconMenu.setOption(19, new ItemStack(Material.STAINED_CLAY, 1, (short) 14), "No");
    }

    public void changeIntegerSetting(Setting setting, Integer newSetting){
        arenaConfig.set(changingSetting.getConfigKey(), newSetting);
        arenaStorage.saveConfig();
        arenaSettings.initializeSettings();
        stopChanging();
    }

    public void openMenu(Player player) {
        iconMenu.open(player);
    }

    public void openMenuDelayed(final Player player){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                openMenu(player);
            }
        }, 1);
    }

    private void stopChanging(){
        changeStatus = ChangeStatus.NONE;
        changingSetting = null;
        changingPlayer = null;
    }


    public class MenuClickEventHandler implements IconMenu.OptionClickEventHandler {
        @Override
        public void onOptionClick(final IconMenu.OptionClickEvent event) {
            Player player = event.getPlayer();

            if (event.getPosition() <= 8) {
                //Clicked a setting group.
                stopChanging();

                SettingGroup settingGroup = SettingGroup.valueOf(event.getName().toUpperCase());
                if(settingGroup != null){
                    setupMenu(settingGroup);
                    openMenuDelayed(player);
                }
            } else if (event.getPosition() >= 36) {
                //Clicked an alternative setting (gamemode etc.)
                stopChanging();

                //TODO Allow changing of gamemode from this menu
            }

            if(changeStatus == ChangeStatus.BOOLEAN){
                boolean settingChanged = false;
                if(event.getName().equals("Yes")){
                    arenaConfig.set(changingSetting.getConfigKey(), true);
                    settingChanged = true;
                } else if (event.getName().equals("No")){
                    arenaConfig.set(changingSetting.getConfigKey(), false);
                    settingChanged = true;
                }

                if(settingChanged){
                    stopChanging();
                    arenaStorage.saveConfig();
                    arenaSettings.initializeSettings();
                    iconMenu.clearMenu();
                    setupMenu();
                    openMenuDelayed(player);
                }
            } else {
                for (Setting setting : arenaSettings.getSettings()) {
                    if (event.getName().contains(setting.getName())) {
                        if(setting.getSetting() instanceof Boolean){
                            setupBooleanChangeMenu(setting);
                            changeStatus = ChangeStatus.BOOLEAN;
                            changingSetting = setting;
                            openMenuDelayed(player);
                        } else {
                            changeStatus = ChangeStatus.INTEGER;
                            changingPlayer = player.getName();
                            changingSetting = setting;
                            iconMenu.clearMenu();
                            setupMenu();
                            Util.msg(player, "Type the new setting for " + setting.getName() + "!");
                        }
                        break;
                    }
                }
            }
        }
    }

    public enum ChangeStatus {
        NONE, BOOLEAN, INTEGER
    }
}
