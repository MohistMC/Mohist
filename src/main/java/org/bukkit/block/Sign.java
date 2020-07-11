package org.bukkit.block;

/**
 * Represents a captured state of either a SignPost or a WallSign.
 */
public interface Sign extends BlockState {

    /**
     * Gets all the lines of text currently on this sign.
     *
     * @return Array of Strings containing each line of text
     */
    String[] getLines();

    /**
     * Gets the line of text at the specified index.
     * <p>
     * For example, getLine(0) will return the first line of text.
     *
     * @param index Line number to get the text from, starting at 0
     * @return Text on the given line
     * @throws IndexOutOfBoundsException Thrown when the line does not exist
     */
    String getLine(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the line of text at the specified index.
     * <p>
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line  New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     */
    void setLine(int index, String line) throws IndexOutOfBoundsException;
}
