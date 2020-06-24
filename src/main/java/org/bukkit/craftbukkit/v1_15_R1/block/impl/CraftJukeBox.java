/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public final class CraftJukeBox extends CraftBlockData implements org.bukkit.block.data.type.Jukebox {

    public CraftJukeBox() {
        super();
    }

    public CraftJukeBox(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftJukebox

    private static final BooleanProperty HAS_RECORD = getBoolean(net.minecraft.block.JukeboxBlock.class, "has_record");

    @Override
    public boolean hasRecord() {
        return get(HAS_RECORD);
    }
}
