package me.dragoncraft.skyboxengine.manager;

import me.dragoncraft.skyboxengine.SkyboxEngine;
import me.dragoncraft.skyboxengine.config.Settings;
import me.dragoncraft.skyboxengine.skybox.WorldSkybox;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSkyboxManager {

    private final HashMap<UUID, WorldSkybox> playerSkyboxes = new HashMap<>();
    private BukkitTask updateTask;

    public PlayerSkyboxManager() {
        updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(SkyboxEngine.getInstance(),this::tickSkyboxes,0,1);
    }


    private void tickSkyboxes() {
        playerSkyboxes.forEach(((uuid, worldSkybox) -> {
            if (worldSkybox == null) {
                return;
            }
            worldSkybox.tickSkybox();
        }));
    }


    public WorldSkybox getPlayerSkybox(Player player) {
        return playerSkyboxes.getOrDefault(player.getUniqueId(),null);
    }

    public void setPlayerSkybox(Player player, WorldSkybox settings) {
        playerSkyboxes.put(player.getUniqueId(),settings);
    }

    public void addPlayer(Player player) {
        setPlayerSkybox(player,null);
        checkWorldSkyboxChange(player,player.getWorld(),null);
    }

    public void removePlayer(Player player) {
        if (playerSkyboxes.getOrDefault(player.getUniqueId(),null) != null) {
            playerSkyboxes.get(player.getUniqueId()).removeSkybox();
        }
        playerSkyboxes.remove(player.getUniqueId());
    }


    public void checkWorldSkyboxChange(Player player, World world,World last) {
        if (last != null && playerSkyboxes.containsKey(player.getUniqueId()) && playerSkyboxes.get(player.getUniqueId()) != null) {
            if (playerSkyboxes.get(player.getUniqueId()).getWorld() == last) {
                if (SkyboxEngine.getConfigInstance().getDebugLogLevel() > 0) {
                    SkyboxEngine.info("Removing skybox " + playerSkyboxes.get(player.getUniqueId()).getSettings().getSkyboxId() + " for player " + player.getName());
                }
                playerSkyboxes.get(player.getUniqueId()).removeSkybox();
                removePlayer(player);
            }
        }
        if (world != null && (!playerSkyboxes.containsKey(player.getUniqueId()) || playerSkyboxes.get(player.getUniqueId()) == null)) {
            if (SkyboxEngine.getConfigInstance().getDimensionSkyboxes().containsKey(world.getKey().toString())) {
                Settings.SkyboxSettings settings = SkyboxEngine.getConfigInstance().getDimensionSkyboxes().get(world.getKey().toString());
                WorldSkybox skybox = new WorldSkybox(player,world,settings);
                skybox.createSkybox();
                playerSkyboxes.put(player.getUniqueId(),skybox);
                if (SkyboxEngine.getConfigInstance().getDebugLogLevel() > 0) {
                    SkyboxEngine.info("Creating skybox " + skybox.getSettings().getSkyboxId() + " for player " + player.getName());
                }
            }
        }
    }


}
