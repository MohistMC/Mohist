package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a book ({@link Material#WRITABLE_BOOK} or {@link
 * Material#WRITTEN_BOOK}) that can have a title, an author, and pages.
 */
public interface BookMeta extends ItemMeta {

    /**
     * Represents the generation (or level of copying) of a written book
     */
    enum Generation {
        /**
         * Book written into a book-and-quill. Can be copied. (Default value)
         */
        ORIGINAL,
        /**
         * Book that was copied from an original. Can be copied.
         */
        COPY_OF_ORIGINAL,
        /**
         * Book that was copied from a copy of an original. Can't be copied.
         */
        COPY_OF_COPY,
        /**
         * Unused; unobtainable by players. Can't be copied.
         */
        TATTERED;
    }

    /**
     * Checks for the existence of a title in the book.
     *
     * @return true if the book has a title
     */
    boolean hasTitle();

    /**
     * Gets the title of the book.
     * <p>
     * Plugins should check that hasTitle() returns true before calling this
     * method.
     *
     * @return the title of the book
     */
    @Nullable
    String getTitle();

    /**
     * Sets the title of the book.
     * <p>
     * Limited to 32 characters. Removes title when given null.
     *
     * @param title the title to set
     * @return true if the title was successfully set
     */
    boolean setTitle(@Nullable String title);

    /**
     * Checks for the existence of an author in the book.
     *
     * @return true if the book has an author
     */
    boolean hasAuthor();

    /**
     * Gets the author of the book.
     * <p>
     * Plugins should check that hasAuthor() returns true before calling this
     * method.
     *
     * @return the author of the book
     */
    @Nullable
    String getAuthor();

    /**
     * Sets the author of the book. Removes author when given null.
     *
     * @param author the author to set
     */
    void setAuthor(@Nullable String author);

    /**
     * Checks for the existence of generation level in the book.
     *
     * @return true if the book has a generation level
     */
    boolean hasGeneration();

    /**
     * Gets the generation of the book.
     * <p>
     * Plugins should check that hasGeneration() returns true before calling
     * this method.
     *
     * @return the generation of the book
     */
    @Nullable
    Generation getGeneration();

    /**
     * Sets the generation of the book. Removes generation when given null.
     *
     * @param generation the generation to set
     */
    void setGeneration(@Nullable Generation generation);

    /**
     * Checks for the existence of pages in the book.
     *
     * @return true if the book has pages
     */
    boolean hasPages();

    /**
     * Gets the specified page in the book. The given page must exist.
     * <p>
     * Pages are 1-indexed.
     *
     * @param page the page number to get, in range [1, getPageCount()]
     * @return the page from the book
     */
    @NotNull
    String getPage(int page);

    /**
     * Sets the specified page in the book. Pages of the book must be
     * contiguous.
     * <p>
     * The data can be up to 256 characters in length, additional characters
     * are truncated.
     * <p>
     * Pages are 1-indexed.
     *
     * @param page the page number to set, in range [1, getPageCount()]
     * @param data the data to set for that page
     */
    void setPage(int page, @NotNull String data);

    /**
     * Gets all the pages in the book.
     *
     * @return list of all the pages in the book
     */
    @NotNull
    List<String> getPages();

    /**
     * Clears the existing book pages, and sets the book to use the provided
     * pages. Maximum 100 pages with 256 characters per page.
     *
     * @param pages A list of pages to set the book to use
     */
    void setPages(@NotNull List<String> pages);

    /**
     * Clears the existing book pages, and sets the book to use the provided
     * pages. Maximum 50 pages with 256 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void setPages(@NotNull String... pages);

    /**
     * Adds new pages to the end of the book. Up to a maximum of 50 pages with
     * 256 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void addPage(@NotNull String... pages);

    /**
     * Gets the number of pages in the book.
     *
     * @return the number of pages in the book
     */
    int getPageCount();

    @Override
    @NotNull
    BookMeta clone();

    // Spigot start
    public class Spigot {

    }

    @NotNull
    Spigot spigot();
    // Spigot end
}
