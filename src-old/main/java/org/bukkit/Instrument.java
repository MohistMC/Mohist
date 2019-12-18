package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum Instrument {

    /**
     * Piano is the standard instrument for a note block.
     */
    PIANO(0x0),
    /**
     * Bass drum is normally played when a note block is on top of a
     * stone-like block.
     */
    BASS_DRUM(0x1),
    /**
     * Snare drum is normally played when a note block is on top of a sandy
     * block.
     */
    SNARE_DRUM(0x2),
    /**
     * Sticks are normally played when a note block is on top of a glass
     * block.
     */
    STICKS(0x3),
    /**
     * Bass guitar is normally played when a note block is on top of a wooden
     * block.
     */
    BASS_GUITAR(0x4),
    /**
     * Flute is normally played when a note block is on top of a clay block.
     */
    FLUTE(0x5),
    /**
     * Bell is normally played when a note block is on top of a gold block.
     */
    BELL(0x6),
    /**
     * Guitar is normally played when a note block is on top of a woolen block.
     */
    GUITAR(0x7),
    /**
     * Chime is normally played when a note block is on top of a packed ice
     * block.
     */
    CHIME(0x8),
    /**
     * Xylophone is normally played when a note block is on top of a bone block.
     */
    XYLOPHONE(0x9);

    private final static Map<Byte, Instrument> BY_DATA = Maps.newHashMap();

    static {
        for (Instrument instrument : Instrument.values()) {
            BY_DATA.put(instrument.getType(), instrument);
        }
    }

    private final byte type;

    private Instrument(final int type) {
        this.type = (byte) type;
    }

    /**
     * Get an instrument by its type ID.
     *
     * @param type The type ID
     * @return The instrument
     * @deprecated Magic value
     */

    public static Instrument getByType(final byte type) {
        return BY_DATA.get(type);
    }

    /**
     * @return The type ID of this instrument.
     * @deprecated Magic value
     */

    public byte getType() {
        return this.type;
    }
}
