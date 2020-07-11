package org.bukkit.map;

import java.awt.*;

/**
 * Represents a canvas for drawing to a map. Each canvas is associated with a
 * specific {@link MapRenderer} and represents that renderer's layer on the
 * map.
 */
public interface MapCanvas {

    /**
     * Get the map this canvas is attached to.
     *
     * @return The MapView this canvas is attached to.
     */
    MapView getMapView();

    /**
     * Get the cursor collection associated with this canvas.
     *
     * @return The MapCursorCollection associated with this canvas.
     */
    MapCursorCollection getCursors();

    /**
     * Set the cursor collection associated with this canvas. This does not
     * usually need to be called since a MapCursorCollection is already
     * provided.
     *
     * @param cursors The MapCursorCollection to associate with this canvas.
     */
    void setCursors(MapCursorCollection cursors);

    /**
     * Draw a pixel to the canvas.
     *
     * @param x     The x coordinate, from 0 to 127.
     * @param y     The y coordinate, from 0 to 127.
     * @param color The color. See {@link MapPalette}.
     */
    void setPixel(int x, int y, byte color);

    /**
     * Get a pixel from the canvas.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @return The color. See {@link MapPalette}.
     */
    byte getPixel(int x, int y);

    /**
     * Get a pixel from the layers below this canvas.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @return The color. See {@link MapPalette}.
     */
    byte getBasePixel(int x, int y);

    /**
     * Draw an image to the map. The image will be clipped if necessary.
     *
     * @param x     The x coordinate of the image.
     * @param y     The y coordinate of the image.
     * @param image The Image to draw.
     */
    void drawImage(int x, int y, Image image);

    /**
     * Render text to the map using fancy formatting. Newline (\n) characters
     * will move down one line and return to the original column, and the text
     * color can be changed using sequences such as "§12;", replacing 12 with
     * the palette index of the color (see {@link MapPalette}).
     *
     * @param x    The column to start rendering on.
     * @param y    The row to start rendering on.
     * @param font The font to use.
     * @param text The formatted text to render.
     */
    void drawText(int x, int y, MapFont font, String text);

}
