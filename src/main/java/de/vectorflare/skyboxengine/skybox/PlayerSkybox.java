package de.vectorflare.skyboxengine.skybox;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.tintcolor.TintProvider;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class PlayerSkybox {

    private static final Display.Brightness FULL_BRIGHT = new Display.Brightness(15, 15);

    private final Player player;
    @Getter
    private final Settings.SkyboxSettings settings;

    private ItemDisplay skyboxEntity;

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
        if (SkyboxEngine.getData().disabledSkyboxes.contains(player.getUniqueId())) {
            return;
        }
        Location spawn = player.getLocation();
        spawn.setPitch(0);
        spawn.setYaw(0);
        skyboxEntity = spawn.getWorld().spawn(spawn, ItemDisplay.class, entity -> {
            entity.setVisibleByDefault(false);
            entity.setPersistent(false);
            entity.setItemStack(createSkyboxItem(getColor()));
            entity.setViewRange(1000);
            entity.setBrightness(FULL_BRIGHT);
            entity.setTransformation(scaleOf(getSize()));
            entity.setTeleportDuration(getInterpolationDuration());
            entity.setInterpolationDuration(getInterpolationDuration());
        });
        player.showEntity(SkyboxEngine.getInstance(), skyboxEntity);
        if (settings.isUseMountMovementSync()) {
            player.addPassenger(skyboxEntity);
        }
    }

    public void tickSkybox() {
        Location spawn = player.getLocation();
        spawn.setPitch(0);
        spawn.setYaw(0);
        skyboxEntity.setTransformation(scaleOf(getSize()));
        skyboxEntity.setInterpolationDelay(0);
        skyboxEntity.setInterpolationDuration(getInterpolationDuration());
        if (!skyboxEntity.getWorld().equals(player.getWorld())
                || player.getLocation().distanceSquared(skyboxEntity.getLocation()) > Math.pow(getBaseSize() * 0.5,2)) {
            removeSkybox();
            createSkybox();
            return;
        }

        if (tintProvider != null) {
            skyboxEntity.setItemStack(createSkyboxItem(tintProvider.getTintColor(player,settings)));
        } else {
            skyboxEntity.setItemStack(createSkyboxItem(getColor()));
        }
        skyboxEntity.teleport(spawn);
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

    private ItemStack createSkyboxItem(Color color) {
        ItemStack item = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setItemModel(getSkyboxModel());
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    private static Transformation scaleOf(float size) {
        return new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(size,size,size), new AxisAngle4f());
    }

}
