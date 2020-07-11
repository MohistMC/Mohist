package org.bukkit.inventory.meta;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;

import java.util.List;

/**
 * Represents a book ({@link Material#BOOK_AND_QUILL} or {@link
 * Material#WRITTEN_BOOK}) that can have a title, an author, and pages.
 */
public interface BookMeta extends ItemMeta {

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
    String getTitle();

    /**
     * Sets the title of the book.
     * <p>
     * Limited to 16 characters. Removes title when given null.
     *
     * @param title the title to set
     * @return true if the title was successfully set
     */
    boolean setTitle(String title);

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
    String getAuthor();

    /**
     * Sets the author of the book. Removes author when given null.
     *
     * @param author the author to set
     */
    void setAuthor(String author);

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
    Generation getGeneration();

    /**
     * Sets the generation of the book. Removes generation when given null.
     *
     * @param generation the generation to set
     */
    void setGeneration(Generation generation);

    /**
     * Checks for the existence of pages in the book.
     *
     * @return true if the book has pages
     */
    boolean hasPages();

    /**
     * Gets the specified page in the book. The given page must exist.
     *
     * @param page the page number to get
     * @return the page from the book
     */
    String getPage(int page);

    /**
     * Sets the specified page in the book. Pages of the book must be
     * contiguous.
     * <p>
     * The data can be up to 256 characters in length, additional characters
     * are truncated.
     *
     * @param page the page number to set
     * @param data the data to set for that page
     */
    void setPage(int page, String data);

    /**
     * Gets all the pages in the book.
     *
     * @return list of all the pages in the book
     */
    List<String> getPages();

    /**
     * Clears the existing book pages, and sets the book to use the provided
     * pages. Maximum 50 pages with 256 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void setPages(String... pages);

    /**
     * Clears the existing book pages, and sets the book to use the provided
     * pages. Maximum 50 pages with 256 characters per page.
     *
     * @param pages A list of pages to set the book to use
     */
    void setPages(List<String> pages);

    /**
     * Adds new pages to the end of the book. Up to a maximum of 50 pages with
     * 256 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void addPage(String... pages);

    /**
     * Gets the number of pages in the book.
     *
     * @return the number of pages in the book
     */
    int getPageCount();

    BookMeta clone();

    @Override
    Spigot spigot();

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
        TATTERED
    }

    // Spigot start
    class Spigot extends ItemMeta.Spigot {
        /**
         * Gets the specified page in the book. The given page must exist.
         *
         * @param page the page number to get
         * @return the page from the book
         */
        public BaseComponent[] getPage(int page) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sets the specified page in the book. Pages of the book must be
         * contiguous.
         * <p>
         * The data can be up to 256 characters in length, additional characters
         * are truncated.
         *
         * @param page the page number to set
         * @param data the data to set for that page
         */
        public void setPage(int page, BaseComponent... data) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Gets all the pages in the book.
         *
         * @return list of all the pages in the book
         */
        public List<BaseComponent[]> getPages() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Clears the existing book pages, and sets the book to use the provided
         * pages. Maximum 50 pages with 256 characters per page.
         *
         * @param pages A list of component arrays, each being a page
         */
        public void setPages(BaseComponent[]... pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Clears the existing book pages, and sets the book to use the provided
         * pages. Maximum 50 pages with 256 characters per page.
         *
         * @param pages A list of pages to set the book to use
         */
        public void setPages(List<BaseComponent[]> pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Adds new pages to the end of the book. Up to a maximum of 50 pages
         * with 256 characters per page.
         *
         * @param pages A list of component arrays, each being a page
         */
        public void addPage(BaseComponent[]... pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    // Spigot end
}
