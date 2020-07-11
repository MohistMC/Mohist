package org.bukkit.block;

import org.bukkit.Instrument;
import org.bukkit.Note;

/**
 * Represents a captured state of a note block.
 */
public interface NoteBlock extends BlockState {

    /**
     * Gets the note.
     *
     * @return The note.
     */
    Note getNote();

    /**
     * Set the note.
     *
     * @param note The note.
     */
    void setNote(Note note);

    /**
     * Gets the note.
     *
     * @return The note ID.
     * @deprecated Magic value
     */
    @Deprecated
    byte getRawNote();

    /**
     * Set the note.
     *
     * @param note The note ID.
     * @deprecated Magic value
     */
    @Deprecated
    void setRawNote(byte note);

    /**
     * Attempts to play the note at the block.
     * <p>
     * If the block represented by this block state is no longer a note block,
     * this will return false.
     *
     * @return true if successful, otherwise false
     * @throws IllegalStateException if this block state is not placed
     */
    boolean play();

    /**
     * Plays an arbitrary note with an arbitrary instrument at the block.
     * <p>
     * If the block represented by this block state is no longer a note block,
     * this will return false.
     *
     * @param instrument Instrument ID
     * @param note       Note ID
     * @return true if successful, otherwise false
     * @throws IllegalStateException if this block state is not placed
     * @deprecated Magic value
     */
    @Deprecated
    boolean play(byte instrument, byte note);

    /**
     * Plays an arbitrary note with an arbitrary instrument at the block.
     * <p>
     * If the block represented by this block state is no longer a note block,
     * this will return false.
     *
     * @param instrument The instrument
     * @param note       The note
     * @return true if successful, otherwise false
     * @throws IllegalStateException if this block state is not placed
     * @see Instrument Note
     */
    boolean play(Instrument instrument, Note note);
}
