package me.naithantu.ArenaPVP.Arena.Settings;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArenaSettings {
    private YamlStorage arenaStorage;
    private FileConfiguration config;

    private SettingMenu settingMenu;

    private List<Setting> settings = new ArrayList<Setting>();

    private Setting<Integer> maxPlayers;
    private Setting<Integer> respawnTime;
    private Setting<Integer> scoreLimit;
    private Setting<Integer> outOfBoundsTime;
    private Setting<Integer> spawnProtection;

    private Setting<Boolean> enabled;
    private Setting<Boolean> friendlyFire;
    private Setting<Boolean> outOfBoundsArea;
    private Setting<Boolean> spectatorOutOfBoundsArea;
    private Setting<Boolean> autoBalance;
    private Setting<Boolean> allowItemDrop;
    private Setting<Boolean> allowBlockChange;
    private Setting<Boolean> allowSpectate;
    private Setting<Boolean> allowSpectateFly;
    private Setting<Boolean> allowSpectateInArea;
    private Setting<Boolean> noHungerLoss;

    /** List of potions that will be given when a player spawns */
    private List<PotionSetting> potions;

    private Setting<String> nickname;
    private Setting<String> gamemode;
    private Setting<String> outOfBoundsRegion;
    private Setting<String> spectatorOutOfBoundsRegion;

    public ArenaSettings(YamlStorage arenaStorage, Arena arena, boolean createMenu) {
        this.arenaStorage = arenaStorage;
        config = arenaStorage.getConfig();
        initializeSettings();
        if (createMenu) {
            settingMenu = new SettingMenu(arenaStorage, arena, this);
        }
    }

    public void reloadSettings() {
        arenaStorage.reloadConfig();
        initializeSettings();
    }

    public void initializeSettings() {
        //Make sure settings list is clear.
        settings.clear();

        //Unassigned
        nickname = new Setting<>(config.getString("nickname", ""), null, "nickname", "Nickname", "The arena's nickname.");
        gamemode = new Setting<>(config.getString("gamemode"), null, "gamemode", "Gamemode", "The gamemode of this arena.");

        //General settings
        enabled = new Setting<>(config.getBoolean("enabled", false), SettingGroup.GENERAL, "enabled", "Enabled", "Arena is enabled & playable.");
        maxPlayers = new Setting<>(config.getInt("maxplayers"), SettingGroup.GENERAL, "maxplayers", "Max players", "Number of players that can join.");
        scoreLimit = new Setting<>(config.getInt("scorelimit"), SettingGroup.GENERAL, "scorelimit", "Score limit", "Score needed to win.");
        autoBalance = new Setting<>(config.getBoolean("autobalance"), SettingGroup.GENERAL, "autobalance", "Auto balance", "Balance teams before game start.");
        friendlyFire = new Setting<>(config.getBoolean("friendlyfire"), SettingGroup.GENERAL, "friendlyfire", "Friendly fire", "Allow teammates to hit eachother.");
        settings.add(enabled);
        settings.add(maxPlayers);
        settings.add(scoreLimit);
        settings.add(autoBalance);
        settings.add(friendlyFire);

        //Other? settings
        allowItemDrop = new Setting<>(config.getBoolean("allowitemdrop"), SettingGroup.OTHER, "allowitemdrop", "Allow item drop", "Allow players to drop items and armour.");
        allowBlockChange = new Setting<>(config.getBoolean("allowblockchange"), SettingGroup.OTHER, "allowblockchange", "Allow block change", "Allow players to change blocks");
        noHungerLoss = new Setting<>(config.getBoolean("nohungerloss"), SettingGroup.OTHER, "nohungerloss", "No hunger loss", "Always keep players at full hunger");
        settings.add(allowItemDrop);
        settings.add(allowBlockChange);
        settings.add(noHungerLoss);

        //=> List with potions
        potions = new ArrayList<>();
        if (config.contains("potions")) {
            ConfigurationSection section = config.getConfigurationSection("potions");
            for (String key : section.getKeys(false)) {
                //Get data from section
                String typeName = section.getString(key + ".type");
                int power = section.getInt(key + ".power");

                //Parse into PotionEffectType
                PotionEffectType type = PotionEffectType.getByName(typeName);
                if (type == null) continue;

                //Add to list of potions
                potions.add(new PotionSetting(type, power));
            }
        }


        //Respawn settings
        respawnTime = new Setting<>(config.getInt("respawntime"), SettingGroup.RESPAWN, "respawntime", "Respawn time", "Number of seconds before a player respawns");
        spawnProtection = new Setting<>(config.getInt("spawnprotection"), SettingGroup.RESPAWN, "spawnprotection", "Spawn protection", "Number of seconds a player is invulnerable after respawning");
        settings.add(respawnTime);
        settings.add(spawnProtection);

        //Out of bounds settings
        outOfBoundsArea = new Setting<>(config.getBoolean("outofboundsarea"), SettingGroup.OUT_OF_BOUNDS, "outofboundsarea", "Out of bounds area", "Have an out of bounds area.");
        outOfBoundsRegion = new Setting<>(config.getString("outofboundsregion", ""), SettingGroup.OUT_OF_BOUNDS, "outofboundsregion", "Out of bounds region", "The regionname for the out of bounds area");
        outOfBoundsTime = new Setting<>(config.getInt("outofboundstime"), SettingGroup.OUT_OF_BOUNDS, "outofboundstime", "Out of bounds time", "Number of seconds before an out of bounds player is killed");
        settings.add(outOfBoundsArea);
        settings.add(outOfBoundsRegion);
        settings.add(outOfBoundsTime);

        //Spectate settings
        allowSpectate = new Setting<>(config.getBoolean("allowspectate"), SettingGroup.SPECTATOR, "allowspectate", "Allow spectate", "Allow players to spectate.");
        allowSpectateInArea = new Setting<>(config.getBoolean("allowspectateinarea"), SettingGroup.SPECTATOR, "allowspectateinarea", "Allow spectate in area", "Allow spectators to get into the pvp out of bounds area.");
        allowSpectateFly = new Setting<>(config.getBoolean("allowspectatefly"), SettingGroup.SPECTATOR, "allowspectatefly", "Allow spectate fly", "Allow spectators to fly around.");
        spectatorOutOfBoundsArea = new Setting<>(config.getBoolean("spectatoroutofboundsarea"), SettingGroup.SPECTATOR, "spectatoroutofboundsarea", "Spectator out of bounds area", "Have a seperate out of bounds area for spectators.");
        spectatorOutOfBoundsRegion = new Setting<>(config.getString("spectatoroutofboundsregion", ""), SettingGroup.SPECTATOR, "spectatoroutofboundsregion", "Spectator out of bounds region", "The regionname for the specator's out of bounds area");
        settings.add(allowSpectate);
        settings.add(allowSpectateInArea);
        settings.add(allowSpectateFly);
        settings.add(spectatorOutOfBoundsArea);
        settings.add(spectatorOutOfBoundsRegion);
    }

    public SettingMenu getSettingMenu() {
        return settingMenu;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public String getNickname() {
        return nickname.getSetting();
    }

    public String getGamemode() {
        return gamemode.getSetting();
    }

    public boolean isEnabled() {
        return enabled.getSetting();
    }

    public int getMaxPlayers() {
        return maxPlayers.getSetting();
    }

    public int getRespawnTime() {
        return respawnTime.getSetting();
    }

    public int getScoreLimit() {
        return scoreLimit.getSetting();
    }

    public int getOutOfBoundsTime() {
        return outOfBoundsTime.getSetting();
    }

    public int getSpawnProtection() {
        return spawnProtection.getSetting();
    }

    public boolean isFriendlyFire() {
        return friendlyFire.getSetting();
    }

    public boolean isOutOfBoundsArea() {
        return outOfBoundsArea.getSetting();
    }

    public String getOutOfBoundsRegion() {
        return outOfBoundsRegion.getSetting();
    }

    public boolean isSpectatorOutOfBoundsArea() {
        return spectatorOutOfBoundsArea.getSetting();
    }

    public String getSpectatorOutOfBoundsRegion() {
        return spectatorOutOfBoundsRegion.getSetting();
    }

    public boolean isAutoBalance() {
        return autoBalance.getSetting();
    }

    public boolean isAllowItemDrop() {
        return allowItemDrop.getSetting();
    }

    public boolean isAllowBlockChange() {
        return allowBlockChange.getSetting();
    }

    public boolean isAllowSpectate() {
        return allowSpectate.getSetting();
    }

    public boolean isAllowSpectateFly() {
        return allowSpectateFly.getSetting();
    }

    public boolean isAllowSpectateInArea() {
        return allowSpectateInArea.getSetting();
    }

    public boolean isNoHungerLoss() {
        return noHungerLoss.getSetting();
    }

    /**
     * Get a list of potions on this map
     * @return the list of potions
     */
    public List<PotionSetting> getPotions() {
        return potions;
    }

    /**
     * Set a new list of potions
     * This also sorts them & saves them in the config file
     * @param potions the list of potions
     */
    public void setPotions(List<PotionSetting> potions) {
        this.potions = potions;
        Collections.sort(this.potions);

        //Save the potions in the config
        config.set("potions", null);
        for (int i = 0; i < this.potions.size(); i++) {
            PotionSetting setting = this.potions.get(i);
            String key = "potions." + i + ".";
            config.set(key + "type", setting.getType().getName());
            config.set(key + "power", setting.getPower());
        }
        arenaStorage.saveConfig();
    }
}
