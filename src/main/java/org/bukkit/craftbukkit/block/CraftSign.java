package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntitySign;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftSign extends CraftBlockState implements Sign {
    private final TileEntitySign sign;
    private final String[] lines;

    public CraftSign(final Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        sign = (net.minecraft.tileentity.TileEntitySign) world.getTileEntityAt(getX(), getY(), getZ());
        // Spigot start
        if (sign == null) {
            lines = new String[]{"", "", "", ""};
            return;
        }
        // Spigot end
        lines = new String[sign.signText.length];
        System.arraycopy(sign.signText, 0, lines, 0, lines.length);
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int index) throws IndexOutOfBoundsException {
        return lines[index];
    }

    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        lines[index] = line;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && sign != null) {
            sign.signText = sanitizeLines(lines);
            sign.markDirty();
        }

        return result;
    }

    public static String[] sanitizeLines(String[] lines) {
        String[] astring = new String[4];

            for(int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                astring[i] = lines[i];
                } else {
                astring[i] = "";
            }
        }

        return TileEntitySign.sanitizeLines(astring);
    }
}
