package me.dragoncraft.skyboxengine.skybox;

import lombok.Getter;
import me.dragoncraft.skyboxengine.SkyboxEngine;
import me.dragoncraft.skyboxengine.config.Settings;
import me.dragoncraft.skyboxengine.util.ItemDisplays;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerSkybox {

    @Getter
    private final World world;
    @Getter
    private Biome biome = null;
    private final Player player;
    @Getter
    private final Settings.SkyboxSettings settings;

    private WrapperEntity skyboxEntity;

    private int getBaseSize() {
        return Math.min(player.getClientViewDistance(),player.getViewDistance()) * 16;
    }
    private int getSize() {
        return getBaseSize() * -4;
    }
    public static int getInterpolationDuration() {
        return 10;
    }

    private NamespacedKey getSkyboxModel() {
        return NamespacedKey.fromString(settings.getSkyboxId());
    }

    public Color getColor() {
        return Color.fromRGB(255,255,255);
    }


    public PlayerSkybox(Player player, World world, Settings.SkyboxSettings settings) {
        this.player = player;
        this.world = world;
        this.settings = settings;
    }
    public PlayerSkybox(Player player, World world,Biome biome, Settings.SkyboxSettings settings) {
        this.player = player;
        this.world = world;
        this.biome = biome;
        this.settings = settings;
    }

    public void createSkybox() {
        Location spawn = player.getLocation();
        spawn.setPitch(0);
        spawn.setYaw(0);
        skyboxEntity = ItemDisplays.spawnVFX(new ItemStack(Material.LEATHER_HORSE_ARMOR),spawn,player);
        ItemDisplays.setDisplayModel(skyboxEntity,getSkyboxModel());
        ItemDisplays.setDisplaySize(skyboxEntity,getSize());
        ItemDisplays.setDisplayRenderDistance(skyboxEntity,1000);
        ItemDisplays.setDisplayColor(skyboxEntity,getColor());
    }

    public void tickSkybox() {
        Location spawn = player.getLocation();
        spawn.setPitch(0);
        spawn.setYaw(0);
        ItemDisplays.setDisplayModel(skyboxEntity,getSkyboxModel());
        ItemDisplays.setDisplaySize(skyboxEntity,getSize());
        ItemDisplays.setDisplayRenderDistance(skyboxEntity,1000);
        ItemDisplays.setDisplayTransformationInterpolation(skyboxEntity,getInterpolationDuration());
        ItemDisplays.setDisplayTeleportInterpolation(skyboxEntity,getInterpolationDuration());
        ItemDisplays.setDisplayColor(skyboxEntity,getColor());
        if (player.getLocation().distance(me.dragoncraft.skyboxengine.util.ConversionUtils.toBukkitLocation(skyboxEntity.getLocation(), player.getWorld())) > 100) {
            removeSkybox();
            createSkybox();
            return;
        }
        if (settings.isRedChannel_time()) writeTime(settings.isDoublePrecision_time());
        Location tp = player.getLocation();
        tp.setPitch(0);
        tp.setYaw(0);
        ItemDisplays.teleportDisplay(skyboxEntity,tp);
    }

    public void writeTime(boolean precise) {
        if (!precise) {
            long time = world.getTime();
            if (time % 20 != 0) {
                return;
            }
            int compressed = (int) (((time % 24000f) / 24000f) * 255f);
            Color color = Color.fromRGB(compressed,0,0);
            if (SkyboxEngine.getConfigInstance().getDebugLogLevel() > 1) {
                SkyboxEngine.info("Writing Time Color Data r: " + color.getRed() + " g: " + color.getGreen() + " b: " + color.getBlue() + " for player " + player.getName());
            }
            ItemDisplays.setDisplayColor(skyboxEntity, color);
        } else {
            long time = world.getTime();
            int compressed = (int) ((time % 24000));
            int red = compressed / 255;
            int green = compressed % 255;
            Color color = Color.fromRGB(red,green,0);
            color.setRed(red);
            color.setGreen(green);
            if (SkyboxEngine.getConfigInstance().getDebugLogLevel() > 1) {
                SkyboxEngine.info("Writing Time Color Data r: " + color.getRed() + " g: " + color.getGreen() + " b: " + color.getBlue() + " for player " + player.getName());
            }

            ItemDisplays.setDisplayColor(skyboxEntity, color);
        }
    }

    public void removeSkybox() {
        removeSkybox(0);
    }
    public void removeSkybox(int delay) {
        if (delay > 0)  {
            Bukkit.getScheduler().runTaskLater(SkyboxEngine.getInstance(),() -> skyboxEntity.remove(),delay);
        }
        else {
            skyboxEntity.remove();
        }
    }

}
