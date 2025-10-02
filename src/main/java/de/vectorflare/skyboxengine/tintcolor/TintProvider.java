package de.vectorflare.skyboxengine.tintcolor;

import de.vectorflare.skyboxengine.config.Settings;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public interface TintProvider {

    String getName();

    Color getTintColor(Player player, Settings.SkyboxSettings skyboxSettings);


}
