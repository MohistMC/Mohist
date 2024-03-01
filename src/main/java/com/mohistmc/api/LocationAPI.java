package com.mohistmc.api;

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
}
