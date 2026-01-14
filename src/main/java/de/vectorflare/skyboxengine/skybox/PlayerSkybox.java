package de.vectorflare.skyboxengine.skybox;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.tintcolor.TintProvider;
import de.vectorflare.skyboxengine.util.ConversionUtils;
import de.vectorflare.skyboxengine.util.ItemDisplays;
import lombok.Getter;
import lombok.Setter;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerSkybox {

    private final Player player;
    @Getter
    private final Settings.SkyboxSettings settings;

    private WrapperEntity skyboxEntity;

    @Setter
    private TintProvider tintProvider;


    private int getBaseSize() {
        return Math.min(player.getClientViewDistance(),player.getViewDistance()) * 16;
    }
    private int getSize() {
        return getBaseSize() * -4;
    }
    public static int getInterpolationDuration() {
        return 0;
    }

    private NamespacedKey getSkyboxModel() {
        return NamespacedKey.fromString(settings.getSkyboxId());
    }

    public Color getColor() {
        return Color.fromRGB(255,255,255);
    }


    public PlayerSkybox(Player player, Settings.SkyboxSettings settings) {
        this.player = player;
        this.settings = settings;

        String tintProviderKey = settings.getTintProvider();
        this.tintProvider =SkyboxEngine.getTintProviders().getTintProvider(tintProviderKey);
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
        if (player.getLocation().distanceSquared(ConversionUtils.toBukkitLocation(skyboxEntity.getLocation(), player.getWorld())) > Math.pow(getBaseSize() * 0.5,2)) {
            removeSkybox();
            createSkybox();
            return;
        }

        if (tintProvider != null) {
            ItemDisplays.setDisplayColor(skyboxEntity, tintProvider.getTintColor(player,settings));
        }
        ItemDisplays.teleportDisplay(skyboxEntity,spawn);
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
