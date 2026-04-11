package de.vectorflare.skyboxengine;

import de.vectorflare.skyboxengine.config.Settings;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;

public class bStats {

    public static void onEnable(SkyboxEngine instance) {
        int pluginId = 30364;
        Metrics metrics = new Metrics(instance, pluginId);

        metrics.addCustomChart(
                new SimplePie("use_mount_movement_sync", () -> {
                    boolean enabled = false;
                    for (Settings.SkyboxSettings settings : SkyboxEngine.getConfigInstance().getSkyboxRegistry().values()) {
                        if (settings.isUseMountMovementSync()) {
                            enabled = true;
                        }
                    }
                    return "" + enabled;
                }));

        new SingleLineChart("total_skyboxes", () -> SkyboxEngine.getConfigInstance().getSkyboxRegistry().size());
    }

}
