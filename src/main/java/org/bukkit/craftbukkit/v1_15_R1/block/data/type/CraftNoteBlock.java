package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftNoteBlock extends CraftBlockData implements NoteBlock {

    private static final EnumProperty<?> INSTRUMENT = getEnum("instrument");
    private static final IntegerProperty NOTE = getInteger("note");

    @Override
    public org.bukkit.Instrument getInstrument() {
        return get(INSTRUMENT, org.bukkit.Instrument.class);
    }

    @Override
    public void setInstrument(org.bukkit.Instrument instrument) {
        set(INSTRUMENT, instrument);
    }

    @Override
    public org.bukkit.Note getNote() {
       return new org.bukkit.Note(get(NOTE));
    }

    @Override
    public void setNote(org.bukkit.Note note) {
        set(NOTE, (int) note.getId());
    }
}
