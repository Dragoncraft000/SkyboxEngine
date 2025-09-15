package de.vectorflare.skyboxengine.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class ConversionUtils {

    public static Location toPacketEventsLocation(org.bukkit.Location bukkitLocation) {
        return new Location(
                bukkitLocation.getX(),
                bukkitLocation.getY(),
                bukkitLocation.getZ(),
                bukkitLocation.getYaw(),
                bukkitLocation.getPitch()
        );
    }
    public static WrappedBlockState toPacketEventsBlockState(BlockData data) {
        return WrappedBlockState.getByString(data.getAsString());
    }

    public static Location toPacketEventsLocation(Vector3f packetEventsLocation, float yaw, float pitch) {
        return new Location(
                packetEventsLocation.getX(),
                packetEventsLocation.getY(),
                packetEventsLocation.getZ(),
                yaw,
                pitch
        );
    }

    public static Location toPacketEventsLocation(Vector3d packetEventsLocation, float yaw, float pitch) {
        return new Location(
                packetEventsLocation.getX(),
                packetEventsLocation.getY(),
                packetEventsLocation.getZ(),
                yaw,
                pitch
        );
    }

    public static org.bukkit.Location toBukkitLocation(Location packetEventsLocation, World world) {
        return new org.bukkit.Location(
                world,
                packetEventsLocation.getX(),
                packetEventsLocation.getY(),
                packetEventsLocation.getZ(),
                packetEventsLocation.getYaw(),
                packetEventsLocation.getPitch()
        );
    }

    public static Vector toBukkitVector(Vector3f packetEventsVector) {
        return new Vector(
                packetEventsVector.getX(),
                packetEventsVector.getY(),
                packetEventsVector.getZ()
        );
    }
    public static Vector toBukkitVector(Vector3d packetEventsVector) {
        return new Vector(
                packetEventsVector.getX(),
                packetEventsVector.getY(),
                packetEventsVector.getZ()
        );
    }

    public static User toUser(Player player) {
        return PacketEvents.getAPI().getPlayerManager().getUser(player);
    }

    public static Player toBukkitPlayer(User user) {
        try {
            return Bukkit.getPlayer(user.getProfile().getUUID());
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    public static ItemStack toPacketEventsItemStack(org.bukkit.inventory.ItemStack bukkitItemStack) {
        if (bukkitItemStack == null) {
            return ItemStack.EMPTY;
        }

        return SpigotConversionUtil.fromBukkitItemStack(bukkitItemStack);
    }

    public static org.bukkit.inventory.ItemStack toBukkitItemStack(ItemStack packetEventsItemStack) {
        if (packetEventsItemStack == null || packetEventsItemStack.equals(ItemStack.EMPTY)) {
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }

        return SpigotConversionUtil.toBukkitItemStack(packetEventsItemStack);
    }

}
