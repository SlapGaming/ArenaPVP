package me.naithantu.ArenaPVP.Arena.Settings;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.IconMenu;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SettingMenu {
    private ArenaPVP plugin;
    private Arena arena;
    private ArenaManager arenaManager;
    private ArenaSettings arenaSettings;
    private YamlStorage arenaStorage;
    private FileConfiguration arenaConfig;

    private IconMenu iconMenu;

    private ChangeStatus changeStatus;
    private MenuStatus menuStatus;
    private Change nextChange;
    private Setting changingSetting;
    private String changingPlayer;

    private ArenaTeam changingTeam;

    public SettingMenu(ArenaPVP plugin, YamlStorage arenaStorage, ArenaManager arenaManager, Arena arena, final ArenaSettings arenaSettings) {
        this.plugin = plugin;
        this.arena = arena;
        this.arenaSettings = arenaSettings;
        this.arenaStorage = arenaStorage;
        this.arenaManager = arenaManager;
        arenaConfig = arenaStorage.getConfig();
        menuStatus = MenuStatus.NONE;
        iconMenu = new IconMenu("Setting menu", 5 * 9, new MenuClickEventHandler(), false, null, plugin);
        setupMenu();
    }

    public void destroy() {
        iconMenu.destroy();
    }

    public ChangeStatus getChangeStatus() {
        return changeStatus;
    }

    public String getChangingPlayer() {
        return changingPlayer;
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
                if (setting.getSetting() instanceof Boolean) {
                    if (setting.getSetting() == true) {
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
        iconMenu.setOption(36, new ItemStack(Material.STAINED_GLASS, 1), "Arena");
        iconMenu.setOption(37, new ItemStack(Material.STAINED_GLASS, 1), "Gamemodes", "Choose gamemode");
        iconMenu.setOption(38, new ItemStack(Material.STAINED_GLASS, 1), "Teams");
        iconMenu.setOption(39, new ItemStack(Material.STAINED_GLASS, 1), "Gamemode", "Change gamemode settings");
    }

    private void setupBooleanChangeMenu(Setting setting) {
        iconMenu.clearMenu();
        setupMenu();
        iconMenu.setOption(9, new ItemStack(Material.STAINED_CLAY, 1, (short) 3), setting.getName() + "?", setting.getDescription());
        iconMenu.setOption(18, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Yes");
        iconMenu.setOption(19, new ItemStack(Material.STAINED_CLAY, 1, (short) 14), "No");
    }

    public void setupChooseTeamMenu(boolean includeSpectators) {
        iconMenu.clearMenu();
        setupMenu();

        int i = 9;
        for (ArenaTeam team : arena.getTeams()) {
            iconMenu.setOption(i, new ItemStack(Material.STAINED_CLAY, 1, (short) 3), Integer.toString(team.getTeamNumber()), team.getColoredName());
            i++;
        }
        if (includeSpectators) {
            iconMenu.setOption(i, new ItemStack(Material.STAINED_CLAY, 1, (short) 3), "spectator", "Spectators");
        }
    }

    private void setupTeamMenu(ArenaTeam team) {
        iconMenu.clearMenu();
        setupMenu();
        iconMenu.setOption(9, new ItemStack(Material.STAINED_CLAY, 1, (short) 3), Integer.toString(team.getTeamNumber()), team.getColoredName());
        iconMenu.setOption(18, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Name");
        iconMenu.setOption(19, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Color");
        iconMenu.setOption(20, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Gear", "Change inventory & armor");
        iconMenu.setOption(21, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Spawns");

        menuStatus = MenuStatus.TEAM;
    }

    private void setupSpawnsMenu(Player player, String configKey) {
        iconMenu.clearMenu();
        setupMenu();

        List<Location> spawns = arena.getArenaSpawns().getSpawns(configKey);

        if (spawns.size() >= 27) {
            Util.msg(player, "This team has more spawns than fit in the menu, you'll have to edit them manually in the config!");
            return;
        }

        int i = 9;
        for (int key = 0; key < spawns.size(); key++) {
            Location location = spawns.get(key);
            iconMenu.setOption(i, new ItemStack(Material.STAINED_CLAY, 1, (short) 3), Integer.toString(key), location.getX() + "," + location.getY() + "," + location.getZ());
            i++;
        }
        iconMenu.setOption(i, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Add", "Adds a new spawn location at your position");

        if (configKey.equals("spectator")) {
            menuStatus = MenuStatus.SPECTATORSPAWNS;
        } else {
            menuStatus = MenuStatus.SPAWNS;
        }
    }

    private void setupSpawnMenu(String teamKey, String spawnKey) {
        iconMenu.clearMenu();
        setupMenu();

        Location location = Util.getLocation(arenaStorage, "spawns." + teamKey + "." + spawnKey);

        iconMenu.setOption(9, new ItemStack(Material.STAINED_CLAY, 1, (short) 3), spawnKey, location.getX() + "," + location.getY() + "," + location.getZ());
        iconMenu.setOption(18, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Move", "Moves this spawn location to your position");
        iconMenu.setOption(19, new ItemStack(Material.STAINED_CLAY, 1, (short) 1), "Teleport", "Teleport to this spawn location");
        iconMenu.setOption(20, new ItemStack(Material.STAINED_CLAY, 1, (short) 14), "Remove", "Removes this spawn location");

        if (teamKey.equals("spectator")) {
            menuStatus = MenuStatus.SPECTATORSPAWN;
        } else {
            menuStatus = MenuStatus.SPAWN;
        }
    }

    private void setupArenaMenu() {
        iconMenu.clearMenu();
        setupMenu();

        iconMenu.setOption(9, new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "Nickname", "The in-game name of the arena");

        menuStatus = MenuStatus.ARENA;
    }

    private void setupGamemodeMenu() {
        iconMenu.clearMenu();
        setupMenu();

        int i = 9;
        //Add setting groups.
        for (Gamemode.Gamemodes gamemode : Gamemode.Gamemodes.values()) {
            ItemStack itemStack = new ItemStack(Material.STAINED_GLASS, 1, (short) 13);
            iconMenu.setOption(i, itemStack, gamemode.name().substring(0, 1) + gamemode.name().substring(1).toLowerCase());
            i++;
        }

        menuStatus = MenuStatus.GAMEMODES;
    }

    public void openMenu(Player player) {
        iconMenu.open(player);
    }

    public void openMenuDelayed(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                openMenu(player);
            }
        }, 1);
    }

    public void stopChanging() {
        arena.getGamemode().stopChanging();
        menuStatus = MenuStatus.NONE;
        nextChange = Change.NONE;
        changingSetting = null;
        changingPlayer = null;
        changingTeam = null;
        iconMenu.clearMenu();
        setupMenu();
    }


    public class MenuClickEventHandler implements IconMenu.OptionClickEventHandler {
        @Override
        public void onOptionClick(final IconMenu.OptionClickEvent event) {
            event.setWillClose(false);

            final Player player = event.getPlayer();

            String name = event.getName();

            if (event.getPosition() <= 8) {
                //Clicked a setting group.
                stopChanging();

                SettingGroup settingGroup = SettingGroup.valueOf(name.toUpperCase());
                if (settingGroup != null) {
                    setupMenu(settingGroup);
                    iconMenu.update(player);
                }
            } else if (event.getPosition() >= 36) {
                //Clicked an alternative setting (gamemode etc.)
                stopChanging();
                switch (name) {
                    case "Arena":
                        setupArenaMenu();
                        iconMenu.update(player);
                        break;
                    case "Gamemodes":
                        setupGamemodeMenu();
                        iconMenu.update(player);
                        break;
                    case "Teams":
                        setupChooseTeamMenu(true);
                        menuStatus = MenuStatus.TEAMS;
                        iconMenu.update(player);
                        break;
                    case "Gamemode":
                        if (arena.getGamemode().hasConfigSettings()) {
                            menuStatus = MenuStatus.GAMEMODE;
                            arena.getGamemode().setupIconMenu(iconMenu);
                            setupMenu();
                            iconMenu.update(player);
                        } else {
                            Util.msg(player, "That gamemode has no settings you can change!");
                        }
                        break;
                }
            } else {
                if (menuStatus == MenuStatus.GAMEMODE) {
                    arena.getGamemode().handleMenuClick(event);
                } else if (menuStatus == MenuStatus.BOOLEAN) {
                    boolean settingChanged = false;
                    if (name.equals("Yes")) {
                        arenaConfig.set(changingSetting.getConfigKey(), true);
                        settingChanged = true;
                    } else if (name.equals("No")) {
                        arenaConfig.set(changingSetting.getConfigKey(), false);
                        settingChanged = true;
                    }

                    if (settingChanged) {
                        arenaStorage.saveConfig();
                        arenaSettings.initializeSettings();
                        stopChanging();
                        iconMenu.update(player);
                    }
                } else if (menuStatus == MenuStatus.TEAMS) {
                    if (name.equals("spectator")) {
                        setupSpawnsMenu(player, "spectator");
                    } else {
                        changingTeam = arena.getTeam(Integer.parseInt(name));
                        setupTeamMenu(changingTeam);
                    }
                    iconMenu.update(player);
                } else if (menuStatus == MenuStatus.TEAM) {
                    switch (name) {
                        case "Name":
                            nextChange = Change.TEAMNAME;
                            changeStatus = ChangeStatus.STRING;
                            changingPlayer = player.getName();
                            Util.msg(player, "Type the new name for team " + changingTeam.getColoredName() + "!");
                            event.setWillClose(true);
                            break;
                        case "Color":
                            nextChange = Change.TEAMCOLOR;
                            changeStatus = ChangeStatus.STRING;
                            changingPlayer = player.getName();
                            Util.msg(player, "Type the new color for team " + changingTeam.getColoredName() + "!");
                            event.setWillClose(true);
                            break;
                        case "Gear":
                            arenaConfig.set("classes." + changingTeam.getTeamNumber() + ".inventory", Arrays.asList(player.getInventory().getContents()));
                            arenaConfig.set("classes." + changingTeam.getTeamNumber() + ".armor", Arrays.asList(player.getInventory().getArmorContents()));
                            arenaStorage.saveConfig();
                            changingTeam.setupTeam();

                            Util.msg(player, "Changed inventory & armor for team " + changingTeam.getColoredName());
                            stopChanging();
                            iconMenu.update(player);
                            break;
                        case "Spawns":
                            setupSpawnsMenu(player, Integer.toString(changingTeam.getTeamNumber()));
                            iconMenu.update(player);
                            break;
                    }
                } else if (menuStatus == MenuStatus.SPAWNS || menuStatus == MenuStatus.SPECTATORSPAWNS) {
                    String teamKey;
                    String teamName;

                    if (menuStatus == MenuStatus.SPAWNS) {
                        teamKey = Integer.toString(changingTeam.getTeamNumber());
                        teamName = changingTeam.getColoredName();
                    } else {
                        teamKey = "spectator";
                        teamName = "spectators";
                    }

                    if (name.equals("Add")) {
                        String configKey = "spawns." + teamKey + "." + Integer.toString(arena.getArenaSpawns().getSpawns(teamKey).size());
                        Util.saveLocation(player.getLocation(), arenaStorage, configKey, true);
                        Util.msg(player, "Added spawn location to team " + teamName);
                        stopChanging();
                        iconMenu.update(player);
                    } else {
                        setupSpawnMenu(teamKey, name);
                        iconMenu.update(player);
                    }
                } else if (menuStatus == MenuStatus.SPAWN || menuStatus == MenuStatus.SPECTATORSPAWN) {
                    String teamKey;
                    String teamName;

                    if (menuStatus == MenuStatus.SPAWN) {
                        teamKey = Integer.toString(changingTeam.getTeamNumber());
                        teamName = changingTeam.getColoredName();
                    } else {
                        teamKey = "spectator";
                        teamName = "spectators";
                    }

                    String configKey = "spawns." + teamKey + "." + iconMenu.getName(9);

                    switch (name) {
                        case "Move":
                            Util.saveLocation(player.getLocation(), arenaStorage, configKey, true);
                            Util.msg(player, "Changed spawn location #" + iconMenu.getName(9) + "!");
                            stopChanging();
                            iconMenu.update(player);
                            break;
                        case "Teleport":
                            player.teleport(Util.getLocation(arenaStorage, configKey));
                            Util.msg(player, "Teleported to spawn location #" + iconMenu.getName(9) + "!");
                            stopChanging();
                            event.setWillClose(true);
                            break;
                        case "Remove":
                            int spawnNumber = Integer.parseInt(iconMenu.getName(9));

                            int spawns = arena.getArenaSpawns().getSpawns(teamKey).size();

                            if (spawns == 1) {
                                Util.msg(player, "This is the only spawn the team has, you may not remove it!");
                                stopChanging();
                                iconMenu.update(player);
                            } else {
                                arenaConfig.set("spawns." + teamKey + "." + spawnNumber, null);

                                //If the removed spawn wasn't the last spawn, move last spawn to this number.
                                int lastSpawn = spawns - 1;
                                if (spawnNumber != lastSpawn) {
                                    Location location = Util.getLocation(arenaStorage, "spawns." + teamKey + "." + lastSpawn);
                                    arenaConfig.set("spawns." + teamKey + "." + lastSpawn, null);
                                    Util.saveLocation(location, arenaStorage, "spawns." + teamKey + "." + spawnNumber);
                                }

                                Util.msg(player, "Removed spawn #" + spawnNumber + " from team " + teamName);
                                stopChanging();
                                iconMenu.update(player);
                            }
                            break;
                    }
                } else if (menuStatus == MenuStatus.ARENA) {
                    if (event.getName().equals("Nickname")) {
                        nextChange = Change.ARENANAME;
                        changeStatus = ChangeStatus.STRING;
                        changingPlayer = player.getName();
                        Util.msg(player, "Type the new name for arena " + arena.getNickName() + "!");
                        event.setWillClose(true);
                    }
                } else if (menuStatus == MenuStatus.GAMEMODES) {
                    Gamemode.Gamemodes gamemode = Gamemode.Gamemodes.valueOf(name.toUpperCase());
                    if (arena.getGamemode().getGamemode() != gamemode) {
                        arenaConfig.set("gamemode", gamemode.name());
                        arenaStorage.saveConfig();
                        Util.msg(player, "Restarting arena...");
                        final String arenaName = arena.getArenaName();
                        event.setWillClose(true);
                        arena.stopGame();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                Arena arena = new Arena(plugin, arenaManager, arenaName);
                                arenaManager.addArena(arenaName, arena);
                                Util.msg(player, "Reloaded arena with gamemode " + event.getName());
                            }
                        }, 2);
                    }
                } else {
                    for (Setting setting : arenaSettings.getSettings()) {
                        if (name.contains(setting.getName())) {
                            if (setting.getSetting() instanceof Boolean) {
                                setupBooleanChangeMenu(setting);
                                menuStatus = MenuStatus.BOOLEAN;
                                changingSetting = setting;
                                iconMenu.update(player);
                            } else {
                                changeStatus = ChangeStatus.INTEGER;
                                changingPlayer = player.getName();
                                changingSetting = setting;
                                iconMenu.clearMenu();
                                setupMenu();

                                Util.msg(player, "Type the new setting for " + setting.getName() + "!");
                                event.setWillClose(true);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void handleChatInput(Player player, String string) {
        if (nextChange == Change.ARENANAME) {
            arenaConfig.set("nickname", string);
            arenaStorage.saveConfig();
            arena.setupArena();
            Util.msg(player, "Changed arena name to " + string + "!");
            stopChanging();
            openMenuDelayed(player);
        } else if (nextChange == Change.TEAMNAME) {
            arenaConfig.set("teams." + changingTeam.getTeamNumber(), string);
            arenaStorage.saveConfig();
            changingTeam.setupTeam();
            Util.msg(player, "Changed team name to " + changingTeam.getColoredName() + "!");
            stopChanging();
            openMenuDelayed(player);
        } else if (nextChange == Change.TEAMCOLOR) {
            if (string.length() != 0 && ChatColor.getByChar(string) != null) {
                arenaConfig.set("teamcolors." + changingTeam.getTeamNumber(), string);
                arenaStorage.saveConfig();
                changingTeam.setupTeam();
                Util.msg(player, "Changed team color to " + changingTeam.getColoredName() + "!");
                stopChanging();
                openMenuDelayed(player);
            } else {
                Util.msg(player, "Enter a valid chatcolor!");
            }
        }
    }

    public void handleChatInput(Player player, Integer integer) {
        //Check if a setting is being changed.
        if (changingSetting != null) {
            arenaConfig.set(changingSetting.getConfigKey(), integer);
            arenaStorage.saveConfig();
            arenaSettings.initializeSettings();

            Util.msg(player, "Changed " + changingSetting.getName() + " to " + integer + "!");
            stopChanging();
            openMenuDelayed(player);
        }
    }

    //MenuStatus is how the iconmenu currently looks.
    public enum MenuStatus {
        NONE, BOOLEAN, ARENA, TEAMS, TEAM, SPECTATOR, SPAWNS, SPECTATORSPAWNS, SPAWN, SPECTATORSPAWN, GAMEMODES, GAMEMODE
    }

    //ChangeStatus is the next expected chat input.
    public enum ChangeStatus {
        NONE, STRING, INTEGER
    }

    public enum Change {
        NONE, ARENANAME, TEAMNAME, TEAMCOLOR
    }
}
