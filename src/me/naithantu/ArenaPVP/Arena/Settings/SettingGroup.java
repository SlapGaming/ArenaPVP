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
}
