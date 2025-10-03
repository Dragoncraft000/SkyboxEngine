package de.vectorflare.skyboxengine.manager;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.listener.BiomeSkyboxListener;
import de.vectorflare.skyboxengine.listener.WorldSkyboxListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSkyboxManager {

    private final HashMap<UUID, PlayerSkyboxData> playerSkyboxes = new HashMap<>();

    private final BukkitTask updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(SkyboxEngine.getInstance(),this::tickHandler,0,1);


    public PlayerSkyboxData getSkyboxData(Player player) {
        return playerSkyboxes.get(player.getUniqueId());
    }

    public void tickHandler() {
        playerSkyboxes.values().forEach(PlayerSkyboxData::tick);
    }

    public void addPlayer(Player player) {
        playerSkyboxes.put(player.getUniqueId(),new PlayerSkyboxData(player));
    }

    public void removePlayer(Player player) {
        playerSkyboxes.get(player.getUniqueId()).remove();
        playerSkyboxes.remove(player.getUniqueId());
    }

    public void recalculateSkyboxes() {
        BiomeSkyboxListener.retrackBiomes();
        BiomeSkyboxListener.checkBiomeChanges();
        WorldSkyboxListener.checkWorldChanges();
        for (PlayerSkyboxData data : playerSkyboxes.values()) {
            data.addDefaultSkybox();
        }

    }


}
