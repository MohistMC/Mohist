package io.papermc.paper.inventory;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public enum ItemRarity {

    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.YELLOW),
    RARE(NamedTextColor.AQUA),
    EPIC(NamedTextColor.LIGHT_PURPLE);

    TextColor color;

    ItemRarity(TextColor color) {
        this.color = color;
    }

    /**
     * Gets the color formatting associated with the rarity.
     * @return
     */
    @NotNull
    public TextColor getColor() {
        return color;
    }
}
