package de.vectorflare.skyboxengine.util;

import com.github.retrooper.packetevents.util.Vector3f;
import org.bukkit.Location;


@SuppressWarnings("unused")
public class Vector3fUtil {

    public static Vector3f fromBukkitVector(org.bukkit.util.Vector bukkitVector) {
        return new Vector3f((float) bukkitVector.getX(), (float) bukkitVector.getY(), (float) bukkitVector.getZ());
    }

    public static Vector3f fromBukkitLocation(Location location) {
        return new Vector3f((float) location.getX(), (float) location.getY(), (float) location.getZ());
    }

    public static Vector3f fromPacketEventsLocation(com.github.retrooper.packetevents.protocol.world.Location location) {
        return new Vector3f((float) location.getX(), (float) location.getY(), (float) location.getZ());
    }

    public static Vector3f normalize(Vector3f vector) {
        float length = (float) Math.sqrt(vector.getX() * vector.getX() +
                vector.getY() * vector.getY() +
                vector.getZ() * vector.getZ());
        return new Vector3f(vector.getX() / length, vector.getY() / length, vector.getZ() / length);
    }

    public static Vector3f subtract(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    public static Vector3f add(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vector3f multiplyFloat(Vector3f v1, float factor) {
        return new Vector3f(v1.getX() * factor, v1.getY() * factor, v1.getZ() * factor);
    }

    public static Vector3f multiplyInt(Vector3f v1, int factor) {
        return new Vector3f(v1.getX() * factor, v1.getY() * factor, v1.getZ() * factor);
    }

    public static double dot(Vector3f v1, Vector3f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public static double length(Vector3f vector) {
        return Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
    }

    public static Vector3f lerp(Vector3f start, Vector3f end, float t) {
        float x = start.getX() + (end.getX() - start.getX()) * t;
        float y = start.getY() + (end.getY() - start.getY()) * t;
        float z = start.getZ() + (end.getZ() - start.getZ()) * t;
        return new Vector3f(x, y, z);
    }

}
