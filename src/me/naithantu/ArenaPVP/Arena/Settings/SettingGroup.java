package me.naithantu.ArenaPVP.Arena.Settings;

public enum SettingGroup {
    SPECTATOR("Spectators"), GENERAL("General"), RESPAWN("Respawning"), OUT_OF_BOUNDS("Out of bounds"), OTHER("Other");

    private String title;

    SettingGroup(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Get a SettingGroup based on it's title
     * @param title The title
     * @return The group or null
     */
    public static SettingGroup parseTitle(String title) {
        for (SettingGroup settingGroup : values()) {
            if (settingGroup.getTitle().equalsIgnoreCase(title)) {
                return settingGroup;
            }
        }
        return null;
    }

}
