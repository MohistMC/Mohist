package com.mohistmc.api.location;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationAPI {

    /**
     * Calculates the Euclidean distance between two points.
     * @param a The coordinates of the first point.
     * @param b The coordinates of the second point.
     * @return The Euclidean distance as an integer (rounded down).
     */
    public static int distanceBetweenLocation(Location a, Location b) {
        return (int) Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    public static ClosestDistance findClosest(List<Location> Locations, Location referenceLocation) {
        ClosestDistance closest = new ClosestDistance(null, Integer.MAX_VALUE);

        for (Location point : Locations) {
            int distance = distanceBetweenLocation(referenceLocation, point);
            if (distance < closest.distance) {
                closest = new ClosestDistance(point, distance);
            }
        }

        return closest;
    }

    public record ClosestDistance(Location location, int distance) {
    }

    /**
     * Get the direction the player is facing.
     *
     * @param player Player object, not null。
     * @return Returns the Direction enumeration type that the player is currently facing, or null if the direction cannot be determined。
     */
    public static Direction getDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return Direction.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return Direction.NORTH_EAST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return Direction.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return Direction.SOUTH_EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return Direction.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return Direction.SOUTH_WEST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return Direction.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return Direction.NORTH_WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return Direction.NORTH;
        } else {
            return null;
        }
    }

}
