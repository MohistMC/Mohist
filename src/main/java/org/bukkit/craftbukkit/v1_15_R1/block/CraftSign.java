package org.bukkit.craftbukkit.v1_15_R1.block;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.tileentity.SignTileEntity;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;

public class CraftSign extends CraftBlockEntityState<SignTileEntity> implements Sign {

    private String[] lines;
    private boolean editable;

    public CraftSign(final Block block) {
        super(block, SignTileEntity.class);
    }

    public CraftSign(final Material material, final SignTileEntity te) {
        super(material, te);
    }

    @Override
    public void load(SignTileEntity sign) {
        super.load(sign);

        lines = new String[sign.signText.length];
        System.arraycopy(revertComponents(sign.signText), 0, lines, 0, lines.length);
        editable = sign.isEditable;
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
        getSnapshot().setTextColor(net.minecraft.item.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public void applyTo(SignTileEntity sign) {
        super.applyTo(sign);

        ITextComponent[] newLines = sanitizeLines(lines);
        System.arraycopy(newLines, 0, sign.signText, 0, 4);
        sign.isEditable = editable;
    }

    public static ITextComponent[] sanitizeLines(String[] lines) {
        ITextComponent[] components = new ITextComponent[4];

        for (int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = new StringTextComponent("");
            }
        }

        return components;
    }

    public static String[] revertComponents(ITextComponent[] components) {
        String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = revertComponent(components[i]);
        }
        return lines;
    }

    private static String revertComponent(ITextComponent component) {
        return CraftChatMessage.fromComponent(component);
    }
}
