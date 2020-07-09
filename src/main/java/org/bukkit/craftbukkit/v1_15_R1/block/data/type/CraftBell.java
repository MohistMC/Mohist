package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.type.Bell;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftBell extends CraftBlockData implements Bell {

    private static final EnumProperty<?> ATTACHMENT = getEnum("attachment");

    @Override
    public Attachment getAttachment() {
        return get(ATTACHMENT, Attachment.class);
    }

    @Override
    public void setAttachment(Attachment leaves) {
        set(ATTACHMENT, leaves);
    }
}
