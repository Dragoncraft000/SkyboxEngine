package de.vectorflare.skyboxengine.listener;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.ConfigManager;
import de.vectorflare.skyboxengine.manager.PlayerSkyboxData;
import de.vectorflare.skyboxengine.skybox.ActiveSkybox;
import de.vectorflare.skyboxengine.skybox.SkyboxReason;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class WorldSkyboxListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        checkWorldSkyboxChange(event.getPlayer(), event.getPlayer().getWorld());
        reconstructCustomSkyboxes(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        checkWorldSkyboxChange(event.getPlayer(), event.getPlayer().getWorld());
    }

    public static void checkWorldChanges() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            checkWorldSkyboxChange(p,p.getWorld());
        });
    }

    private static void reconstructCustomSkyboxes(Player player) {
        PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player);
        List<ActiveSkybox> skyboxes = data.playerSkyboxes.stream().toList();
        for (ActiveSkybox activeSkybox : skyboxes) {
            if (activeSkybox.reason == SkyboxReason.CUSTOM || activeSkybox.reason == SkyboxReason.PLUGIN) {
                data.removeActiveSkybox(activeSkybox);
                data.addActivePlayerSkybox(activeSkybox);
            }
        }
        data.changeRenderedPlayerSkybox(true);
    }


    private static void checkWorldSkyboxChange(Player player, World world) {
        PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player);
        List<ActiveSkybox> skyboxes = data.playerSkyboxes.stream().toList();

        for (ActiveSkybox activeSkybox : skyboxes) {
            if (activeSkybox.reason == SkyboxReason.DIMENSION) {
                data.removeActiveSkybox(activeSkybox);
                break;
            }
        }
        if (SkyboxEngine.getConfigInstance().getDimensionSkyboxes().containsKey(world.getKey().toString())) {
            String name = SkyboxEngine.getConfigInstance().getDimensionSkyboxes().get(world.getKey().toString());
            if (ConfigManager.getSkyboxSettings(name) == null) {
                return;
            }
            data.addActivePlayerSkybox(new ActiveSkybox(ConfigManager.getSkyboxSettings(name),SkyboxReason.DIMENSION,SkyboxEngine.getConfigInstance().getDimensionSkyboxPriority()));
        }
    }


}
