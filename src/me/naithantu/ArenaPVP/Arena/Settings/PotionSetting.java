package me.naithantu.ArenaPVP.Arena.Settings;

import org.bukkit.potion.PotionEffectType;

/**
 * Created by Stoux on 02/03/2015.
 */
public class PotionSetting implements Comparable<PotionSetting> {

    /** The type of the potion */
    private PotionEffectType type;
    /** The power of the potion, can be negative */
    private int power;

    public PotionSetting(PotionEffectType type, int power) {
        this.type = type;
        this.power = power;
    }

    public PotionSetting(PotionEffectType type) {
        this.type = type;
        power = Integer.MIN_VALUE;
    }

    public PotionEffectType getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public int compareTo(PotionSetting o) {
        return type.getName().compareTo(o.getType().getName());
    }
}
