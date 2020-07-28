package org.bukkit.craftbukkit.v1_16_R1.block;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;

public class CraftSign extends CraftBlockEntityState<SignBlockEntity> implements Sign {

    private String[] lines;
    private boolean editable;

    public CraftSign(final Block block) {
        super(block, SignBlockEntity.class);
    }

    public CraftSign(final Material material, final SignBlockEntity te) {
        super(material, te);
    }

    @Override
    public void load(SignBlockEntity sign) {
        super.load(sign);

        // FIXME BROKEN!!!!!!!!!!!!!!!!!!!!!!!!!!
        //lines = new String[sign.text.length];
        //System.arraycopy(revertComponents(sign.text), 0, lines, 0, lines.length);
        //editable = sign.isEditable();
    }

    @Override
    public String[] getLines() {
        return lines;
    }

    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        return lines[index];
    }

    @Override
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        lines[index] = line;
    }

    @Override
    public boolean isEditable() {
        return this.editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getSnapshot().getTextColor().getId());
    }

    @Override
    public void setColor(DyeColor color) {
        getSnapshot().setTextColor(net.minecraft.util.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public void applyTo(SignBlockEntity sign) {
        super.applyTo(sign);

        Text[] newLines = sanitizeLines(lines);
        // FIXME System.arraycopy(newLines, 0, sign.text, 0, 4);
        sign.setEditable(editable);
    }

    public static Text[] sanitizeLines(String[] lines) {
        Text[] components = new Text[4];

        for (int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = new LiteralText("");
            }
        }

        return components;
    }

    public static String[] revertComponents(Text[] components) {
        String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++)
            lines[i] = revertComponent(components[i]);
        return lines;
    }

    private static String revertComponent(Text component) {
        return CraftChatMessage.fromComponent(component);
    }

}