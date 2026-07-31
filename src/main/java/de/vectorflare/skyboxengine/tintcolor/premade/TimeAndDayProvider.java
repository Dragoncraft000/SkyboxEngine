package de.vectorflare.skyboxengine.tintcolor.premade;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.tintcolor.TintProvider;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class TimeAndDayProvider implements TintProvider {


    @Override
    public String getName() {
        return "datetime";
    }

    @Override
    public Color getTintColor(Player player, Settings.SkyboxSettings skyboxSettings) {
        long time = player.getWorld().getTime() +1;
        int compressed = (int) ((time % 24000));
        int red = Math.min(compressed / 255,127);
        int green = compressed % 255;
        int day = Math.toIntExact(time / 24000) % SkyboxEngine.getConfigInstance().getYearLength();
        int blue = day % 255;
        if ( day > 255) {
            red += 128;
        }

        return Color.fromRGB(red,green,blue);
    }
}
