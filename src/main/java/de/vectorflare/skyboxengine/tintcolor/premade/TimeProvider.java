package de.vectorflare.skyboxengine.tintcolor.premade;

import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.tintcolor.TintProvider;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class TimeProvider implements TintProvider {


    @Override
    public String getName() {
        return "daytime";
    }

    @Override
    public Color getTintColor(Player player, Settings.SkyboxSettings skyboxSettings) {
        long time = player.getWorld().getTime();
        int compressed = (int) ((time % 24000));
        int red = compressed / 255;
        int green = compressed % 255;
        Color color = Color.fromRGB(red,green,0);
        color.setRed(red);
        color.setGreen(green);
        return color;
    }
}
