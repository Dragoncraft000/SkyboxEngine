package me.dragoncraft.skyboxengine.skybox;

import me.dragoncraft.skyboxengine.SkyboxEngine;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public abstract class WorldPlayerEffect implements Listener {
    private final HashMap<UUID, BukkitTask> currentPlayerTasks = new HashMap<>();
    public abstract World getWorld();
    public abstract int getTickInterval();

    public void createEffect(Player player) {
        if (currentPlayerTasks.containsKey(player.getUniqueId())) {
            return;
        }
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(SkyboxEngine.getInstance(),() ->onEffectTick(player),0,getTickInterval());
        currentPlayerTasks.put(player.getUniqueId(),task);
        onEffectCreate(player);
    }

    public void removeEffect(Player player) {
        if (!currentPlayerTasks.containsKey(player.getUniqueId())) {
            return;
        }
        onEffectRemove(player);
        currentPlayerTasks.get(player.getUniqueId()).cancel();
        currentPlayerTasks.remove(player.getUniqueId());
    }

    public abstract void onEffectCreate(Player player);
    public abstract void onEffectTick(Player player);

    public abstract void onEffectRemove(Player player);
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld() != getWorld() && event.getFrom() == getWorld()) {
            removeEffect(event.getPlayer());
        }
        if (event.getPlayer().getWorld() == getWorld() && event.getFrom() != getWorld()) {
            createEffect(event.getPlayer());
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (event.getPlayer().getWorld() == getWorld()) {
            removeEffect(event.getPlayer());
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld() == getWorld()) {
            createEffect(event.getPlayer());
        }
    }

}
