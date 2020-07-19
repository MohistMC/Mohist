package org.bukkit.material;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents textured materials like steps and smooth bricks
 */
public abstract class TexturedMaterial extends MaterialData {

    public TexturedMaterial(Material m) {
        super(m);
    }

    /**
     * @param type the raw type id
     * @deprecated Magic value
     */

    public TexturedMaterial(int type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */

    public TexturedMaterial(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */

    public TexturedMaterial(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Retrieve a list of possible textures. The first element of the list
     * will be used as a default.
     *
     * @return a list of possible textures for this block
     */
    public abstract List<Material> getTextures();

    /**
     * Gets the current Material this block is made of
     *
     * @return Material of this block
     */
    public Material getMaterial() {
        int n = getTextureIndex();
        if (n > getTextures().size() - 1) {
            n = 0;
        }

        return getTextures().get(n);
    }

    /**
     * Sets the material this block is made of
     *
     * @param material New material of this block
     */
    public void setMaterial(Material material) {
        if (getTextures().contains(material)) {
            setTextureIndex(getTextures().indexOf(material));
        } else {
            setTextureIndex(0x0);
        }
    }

    /**
     * Get material index from data
     *
     * @return index of data in textures list
     * @deprecated Magic value
     */

    protected int getTextureIndex() {
        return getData(); // Default to using all bits - override for other mappings
    }

    /**
     * Set material index
     *
     * @param idx - index of data in textures list
     * @deprecated Magic value
     */

    protected void setTextureIndex(int idx) {
        setData((byte) idx); // Default to using all bits - override for other mappings
    }

    @Override
    public String toString() {
        return getMaterial() + " " + super.toString();
    }

    @Override
    public TexturedMaterial clone() {
        return (TexturedMaterial) super.clone();
    }
}
