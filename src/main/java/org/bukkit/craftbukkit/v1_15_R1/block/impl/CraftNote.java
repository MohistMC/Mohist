/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;

public final class CraftNote extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.NoteBlock, org.bukkit.block.data.Powerable {

    public CraftNote() {
        super();
    }

    public CraftNote(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.type.CraftNoteBlock

    private static final EnumProperty<?> INSTRUMENT = getEnum(net.minecraft.block.NoteBlock.class, "instrument");
    private static final IntegerProperty NOTE = getInteger(net.minecraft.block.NoteBlock.class, "note");

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

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftPowerable

    private static final BooleanProperty POWERED = getBoolean(net.minecraft.block.NoteBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }
}
