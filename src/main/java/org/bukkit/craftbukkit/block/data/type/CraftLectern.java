package org.bukkit.craftbukkit.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.Lectern;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftLectern extends CraftBlockData implements Lectern {

    private static final BooleanProperty HAS_BOOK = getBoolean("has_book");

    @Override
    public boolean hasBook() {
        return get(HAS_BOOK);
    }
}
