package de.vectorflare.skyboxengine.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

@SuppressWarnings("unused")
public class LocationUtil {
    public static Location forwards(Location loc, double distance) {
        return loc.clone().add(loc.clone().getDirection().normalize().multiply(distance));
    }
    public static Location forwardsHorizontal(Location loc, double distance) {
        Location newLoc = loc.clone();
        newLoc.setPitch(0);
        newLoc.add(newLoc.clone().getDirection().normalize().multiply(distance));
        newLoc.setDirection(loc.getDirection());
        return newLoc;
    }
    public static Location backwards(Location loc, double distance) {
        return loc.clone().add(loc.clone().getDirection().normalize().multiply(-1 * distance));
    }
    public static Location backwardsHorizontal(Location loc, double distance) {
        Location newLoc = loc.clone();
        newLoc.setPitch(0);
        newLoc.add(newLoc.clone().getDirection().normalize().multiply(-1 * distance));
        newLoc.setDirection(loc.getDirection());
        return newLoc;
    }
    public static Location left(Location loc, double distance) {
        return loc.clone().add(loc.clone().getDirection().rotateAroundY(90).normalize().multiply(distance));
    }
    public static Location right(Location loc, double distance) {
        return loc.clone().add(loc.clone().getDirection().rotateAroundY(90).normalize().multiply(-1 * distance));
    }

    public static double distance(Location loc1, Location loc2) {
        return loc1.distance(loc2);
    }

    public static Location removeRotation(Location loc) {
        return loc.clone().setDirection(new Vector(1,0,0));
    }
    public static Location removePitch(Location loc) {
        Location newLoc =  loc.clone();
        newLoc.setPitch(0);
        return newLoc;
    }
    public static Location removeYaw(Location loc) {
        Location newLoc =  loc.clone();
        newLoc.setYaw(0);
        return newLoc;
    }

    public static Location getGroundLocation(Location loc) {
        return loc.getWorld().getHighestBlockAt(loc).getLocation().add(0,1.5,0);
    }
    public static Location fitToGroundLocation(Location loc) {
        Location groundLoc = getGroundLocation(loc);
        if (loc.getY() >= groundLoc.getY()) {
            return groundLoc;
        }
        return loc.clone();
    }

    public static Location fitToActualGroundLocation(Location loc,int maxSteps) {
        Location newLoc = loc.clone();
        for (int i = 0 ; i < maxSteps;i++) {
            if (!newLoc.clone().add(0,-1,0).getBlock().isSolid()) {
                newLoc.add(0,-1,0);
            }
            if (newLoc.clone().getBlock().isSolid()) {
                newLoc.add(0,1,0);
            }
        }
        return newLoc;
    }

    public static Location getExtendedLocationList(List<Location> list, int index) {
        if (index < 0) {
            return backwards(list.getFirst(),1);
        }
        if (index >= list.size()) {
            return forwards(list.getLast(),1);
        }
        return list.get(index);
    }

}
