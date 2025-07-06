package me.dragoncraft.skyboxengine.util;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta;
import me.tofaa.entitylib.meta.display.ItemDisplayMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class ItemDisplays {
    public static WrapperEntity spawnVFX(Collection<Player> users, ItemStack item, Location location) {
        WrapperEntity entity = new WrapperEntity(EntityTypes.ITEM_DISPLAY);
        ItemDisplayMeta meta = (ItemDisplayMeta) entity.getEntityMeta();
        meta.setItem(ConversionUtils.toPacketEventsItemStack(item));
        meta.setTransformationInterpolationDuration(5);
        meta.setPositionRotationInterpolationDuration(5);
        meta.setViewRange(1000);
        meta.setBrightnessOverride(255);
        for (Player user : users) {
            entity.addViewer(ConversionUtils.toUser(user));
        }
        entity.spawn(ConversionUtils.toPacketEventsLocation(location));
        entity.refresh();
        return entity;
    }
    public static WrapperEntity spawnVFX(Player user, ItemStack item, Location location) {
        return spawnVFX(List.of(user),item,location);
    }
    public static WrapperEntity spawnVFX(ItemStack item, Location location,Collection<Player> users) {
        return spawnVFX(users,item,location);
    }
    public static WrapperEntity spawnVFX(ItemStack item, Location location,Player... users) {
        return spawnVFX(Arrays.stream(users).toList(),item,location);
    }
    public static WrapperEntity spawnVFX(ItemStack item, Location location) {
        Collection<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        return spawnVFX(players,item,location);
    }
    public static WrapperEntity spawnVFX(int modelId, Location location) {
        Collection<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        WrapperEntity vfx = spawnVFX(players,location);
        setDisplayModel(vfx,modelId);
        return vfx;
    }
    public static WrapperEntity spawnVFX(Collection<Player> users, Location location) {
        return spawnVFX(users,new ItemStack(Material.ARMOR_STAND),location);
    }

    public static WrapperEntity spawnVFX(Player user, Location location) {
        return spawnVFX(List.of(user),new ItemStack(Material.ARMOR_STAND),location);
    }


    public static WrapperEntity spawnVFX(Location location) {
        return spawnVFX(new ItemStack(Material.AIR),location);
    }

    public static void setDisplayRenderDistance(WrapperEntity entity,float distance) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setViewRange(distance);
    }

    public static void setDisplayModel(WrapperEntity entity,int model) {
        ItemDisplayMeta meta = (ItemDisplayMeta) entity.getEntityMeta();
        ItemStack item = ConversionUtils.toBukkitItemStack(meta.getItem());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(model);
        item.setItemMeta(itemMeta);
        meta.setItem(ConversionUtils.toPacketEventsItemStack(item));
    }
    public static void setDisplayModel(WrapperEntity entity, NamespacedKey model) {
        ItemDisplayMeta meta = (ItemDisplayMeta) entity.getEntityMeta();
        ItemStack item = ConversionUtils.toBukkitItemStack(meta.getItem());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setItemModel(model);
        item.setItemMeta(itemMeta);
        meta.setItem(ConversionUtils.toPacketEventsItemStack(item));
    }
    public static void setDisplayModel(WrapperEntity entity, String model) {
        String[] elements = model.split(":");
        if (elements.length != 2) {
            return;
        }
        setDisplayModel(entity,new NamespacedKey(elements[0],elements[1]));
    }

    public static void setDisplaySize(WrapperEntity entity, Vector size) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setScale(Vector3fUtil.fromBukkitVector(size));
    }
    public static void setDisplaySize(WrapperEntity entity, float size) {
        setDisplaySize(entity,new Vector(size,size,size));
    }
    public static void setDisplaySize(WrapperEntity entity, float x,float y,float z) {
        setDisplaySize(entity,new Vector(x,y,z));
    }
    public static void setDisplayTranslation(WrapperEntity entity, Vector size) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setTranslation(Vector3fUtil.fromBukkitVector(size));
    }
    public static void setDisplayTranslation(WrapperEntity entity, float size) {
        setDisplayTranslation(entity,new Vector(size,size,size));
    }
    public static void setDisplayTranslation(WrapperEntity entity, float x,float y,float z) {
        setDisplayTranslation(entity,new Vector(x,y,z));
    }
    public static void setDisplayTeleportInterpolation(WrapperEntity entity, int interpolation) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setPositionRotationInterpolationDuration(interpolation);
    }
    public static void teleportDisplay(WrapperEntity entity,Location loc) {
        entity.teleport(ConversionUtils.toPacketEventsLocation(loc));
    }

    public static void setDisplayTransformationInterpolation(WrapperEntity entity, int interpolation) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setInterpolationDelay(0);
        meta.setTransformationInterpolationDuration(interpolation);
    }
    public static void setDisplayBrightness(WrapperEntity entity,int skyLight,int blockLight) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setBrightnessOverride(16 * skyLight + blockLight);
    }
    public static void setDisplayBrightness(WrapperEntity entity,int light) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setBrightnessOverride(16 * light);
    }

    public static void setDisplayGlowing(WrapperEntity entity, boolean glow) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setGlowing(glow);
    }
    public static void setDisplayGlowing(WrapperEntity entity, boolean glow,int color) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setGlowing(glow);
        meta.setGlowColorOverride(color);
    }
    public static void setDisplayGlowing(WrapperEntity entity, boolean glow,int r,int g ,int b) {
        AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();
        meta.setGlowing(glow);
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        meta.setGlowColorOverride(rgb);
    }

    public static void setDisplayColor(WrapperEntity entity,int r, int g,int b) {
        ItemDisplayMeta entityMeta = (ItemDisplayMeta) entity.getEntityMeta();
        ItemStack itemStack = ConversionUtils.toBukkitItemStack(entityMeta.getItem());
        LeatherArmorMeta meta =  (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(r,g,b));
        itemStack.setItemMeta(meta);
        entityMeta.setItem(ConversionUtils.toPacketEventsItemStack(itemStack));
    }
    public static void setDisplayColor(WrapperEntity entity,Color color) {
        ItemDisplayMeta entityMeta = (ItemDisplayMeta) entity.getEntityMeta();
        ItemStack itemStack = ConversionUtils.toBukkitItemStack(entityMeta.getItem());
        LeatherArmorMeta meta =  (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(color);
        itemStack.setItemMeta(meta);
        entityMeta.setItem(ConversionUtils.toPacketEventsItemStack(itemStack));
    }

    public static void spectateDisplay(WrapperEntity entity) {
        WrapperPlayServerCamera camera = new WrapperPlayServerCamera(entity.getEntityId());
        entity.sendPacketToViewers(camera);
    }
}
