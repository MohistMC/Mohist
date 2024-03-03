package com.mohistmc.api.item;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class MohistMap {

    private MapMeta mm;
    private ItemStack item;
    private MapView mapView;

    public static MohistMap create(ItemStack map) {
        return new MohistMap(map);
    }

    public MohistMap(ItemStack map) {
        this.item = map;
        this.mm = (MapMeta) item.getItemMeta();
    }

    public MohistMap mapView(MapView mapView) {
        this.mapView = mapView;
        return this;
    }

    public MohistMap setImageUrl(String imageUrl) {
        try {
            BufferedImage read = ImageIO.read(new URL(imageUrl));
            BufferedImage bufferedImage = new BufferedImage(128, 128, 2);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(read, 0, 0, 128, 128, null);
            graphics.dispose();
            mapView.getRenderers().clear();
            mapView.addRenderer(new ImageRenderer(bufferedImage));
            mapView.setScale(MapView.Scale.FARTHEST);
            mapView.setLocked(true);
            mm.setMapView(mapView);
            return this;
        } catch (IOException e) {
            return this;
        }
    }

    public int getMapID() {
        return mm.getMapView().getId();
    }

    public MohistMap clone(ItemStack map) {
        this.item = map;
        this.mm = (MapMeta) item.getItemMeta();
        return this;
    }

    public MohistMap buildItemMeta() {
        this.item.setItemMeta(this.mm);
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.mm);
        return this.item;
    }

    public static boolean reload(MapView mapView, String imageUrl) {
        try {
            BufferedImage read = ImageIO.read(new URL(imageUrl));
            BufferedImage bufferedImage = new BufferedImage(128, 128, 2);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(read, 0, 0, 128, 128, null);
            graphics.dispose();
            mapView.setScale(MapView.Scale.FARTHEST);
            mapView.setLocked(true);
            mapView.getRenderers().clear();
            mapView.addRenderer(new ImageRenderer(bufferedImage));
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    private static class ImageRenderer extends MapRenderer
    {
        private final BufferedImage image;

        public ImageRenderer(final BufferedImage image) {
            this.image = image;
        }

        public void render(@NotNull MapView mapView, MapCanvas mapCanvas, Player player) {
            mapCanvas.drawImage(0, 0, this.image);
        }
    }
}
