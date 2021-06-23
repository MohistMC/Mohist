package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

public class CraftSign extends CraftBlockEntityState<SignTileEntity> implements Sign {

    // Lazily initialized only if requested:
    // Paper start
    private java.util.ArrayList<net.kyori.adventure.text.Component> originalLines = null; // ArrayList for RandomAccess
    private java.util.ArrayList<net.kyori.adventure.text.Component> lines = null; // ArrayList for RandomAccess
    // Paper end

    public CraftSign(final Block block) {
        super(block, SignTileEntity.class);
    }

    public CraftSign(final Material material, final SignTileEntity te) {
        super(material, te);
    }

    // Paper start
    @Override
    public java.util.List<net.kyori.adventure.text.Component> lines() {
        this.loadLines();
        return lines;
    }

    @Override
    public net.kyori.adventure.text.Component line(int index) {
        this.loadLines();
        return this.lines.get(index);
    }

    @Override
    public void line(int index, net.kyori.adventure.text.Component line) {
        this.loadLines();
        this.lines.set(index, line);
    }

    private void loadLines() {
        if (lines != null) {
            return;
        }
        // Lazy initialization:
        SignTileEntity sign = this.getSnapshot();
        lines = io.papermc.paper.adventure.PaperAdventure.asAdventure(com.google.common.collect.Lists.newArrayList(sign.messages));
        originalLines = new java.util.ArrayList<>(lines);
    }

    // Paper end
    @Override
    public String[] getLines() {
        this.loadLines();
        return this.lines.stream().map(io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC::serialize).toArray(String[]::new); // Paper
    }

    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        this.loadLines();
        return io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.serialize(this.lines.get(index)); // Paper
    }

    @Override
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        this.loadLines();
        this.lines.set(index, line != null ? io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(line) : net.kyori.adventure.text.Component.empty()); // Paper
    }

    @Override
    public boolean isEditable() {
        return getSnapshot().isEditable;
    }

    @Override
    public void setEditable(boolean editable) {
        getSnapshot().isEditable = editable;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getSnapshot().getColor().getId());
    }

    @Override
    public void setColor(DyeColor color) {
        getSnapshot().setColor(net.minecraft.item.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public void applyTo(SignTileEntity sign) {
        super.applyTo(sign);

        if (lines != null) {
            // Paper start
            for (int i = 0; i < this.lines.size(); ++i) {
                net.kyori.adventure.text.Component component = this.lines.get(i);
                net.kyori.adventure.text.Component origComp = this.originalLines.get(i);
                if (component.equals(origComp)) {
                    continue; // The line contents are still the same, skip.
                }
                sign.messages[i] = io.papermc.paper.adventure.PaperAdventure.asVanilla(component);
            }
            // Paper end
        }
    }

    // Paper start
    public static ITextComponent[] sanitizeLines(java.util.List<net.kyori.adventure.text.Component> lines) {
        ITextComponent[] components = new ITextComponent[4];
        for (int i = 0; i < 4; i++) {
            if (i < lines.size() && lines.get(i) != null) {
                components[i] = io.papermc.paper.adventure.PaperAdventure.asVanilla(lines.get(i));
            } else {
                components[i] = new StringTextComponent("");
            }
        }
        return components;
    }
    // Paper end

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
