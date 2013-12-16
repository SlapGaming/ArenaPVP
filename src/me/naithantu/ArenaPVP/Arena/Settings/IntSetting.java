package me.naithantu.ArenaPVP.Arena.Settings;

public class IntSetting extends Setting {
    int setting;

    public IntSetting(int setting, SettingGroup settingGroup, String name) {
        super(settingGroup, name);
        this.setting = setting;
    }

    public int getSetting(){
        return setting;
    }
}
