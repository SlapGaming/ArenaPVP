package me.naithantu.ArenaPVP.Arena.Settings;

public class Setting<T> {
    private SettingGroup settingGroup;
    private String configKey;
    private String name;
    private String description;

    private T setting;

    public Setting( T setting, SettingGroup settingGroup, String configKey, String name, String description){
        this.settingGroup = settingGroup;
        this.setting = setting;
        this.configKey = configKey;
        this.name = name;
        this.description = description;
    }

    public Object getSettingGroup(){
        return settingGroup;
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getConfigKey(){
        return configKey;
    }

    public T getSetting(){
        return setting;
    }
}
