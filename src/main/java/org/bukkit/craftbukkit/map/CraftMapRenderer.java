package org.bukkit.craftbukkit.map;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import net.minecraftforge.cauldron.command.*;

public class CraftMapRenderer extends MapRenderer {

    private final net.minecraft.world.storage.MapData worldMap;

    public CraftMapRenderer(CraftMapView mapView, net.minecraft.world.storage.MapData worldMap) {
        super(false);
        this.worldMap = worldMap;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if(CauldronCommand.debug) {
        System.out.println("Default Map Render called!");
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            System.out.println(ste);
        } }


        // Map
        for (int x = 0; x < 128; ++x) {
            for (int y = 0; y < 128; ++y) {
                canvas.setPixel(x, y, worldMap.colors[y * 128 + x]);
            }
        }

        // Cursors
        MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }

        for (UUID key : worldMap.playersVisibleOnMap.keySet()) { // Spigot string -> uuid
            // If this cursor is for a player check visibility with vanish system
            Player other = Bukkit.getPlayer(key); // Spigot
            if (other != null && !player.canSee(other)) {
                continue;
            }

            net.minecraft.world.storage.MapData.MapCoord decoration = (net.minecraft.world.storage.MapData.MapCoord) worldMap.playersVisibleOnMap.get(key);
            cursors.addCursor(decoration.centerX, decoration.centerZ, (byte) (decoration.iconRotation & 15), decoration.iconSize);
        }
    }

}
