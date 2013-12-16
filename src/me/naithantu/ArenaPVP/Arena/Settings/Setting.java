package me.naithantu.ArenaPVP.Arena.Settings;

public abstract class Setting {
    SettingGroup settingGroup;
    String name;

    public Setting(SettingGroup settingGroup, String name){
        this.settingGroup = settingGroup;
        this.name = name;
    }

    public Object getSettingGroup(){
        return settingGroup;
    }

    public String getName(){
        return name;
    }
}
