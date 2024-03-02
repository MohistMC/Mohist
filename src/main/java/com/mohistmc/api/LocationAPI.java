package com.mohistmc.api;

import java.util.List;
import org.bukkit.Location;

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
}
