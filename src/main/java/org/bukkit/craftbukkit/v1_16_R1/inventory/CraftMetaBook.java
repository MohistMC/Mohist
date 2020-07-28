package org.bukkit.craftbukkit.v1_16_R1.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.text.Text;
import net.minecraft.text.Text.Serializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BookMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBook extends CraftMetaItem implements BookMeta {

    protected static final ItemMetaKey BOOK_TITLE = new ItemMetaKey("title");
    protected static final ItemMetaKey BOOK_AUTHOR = new ItemMetaKey("author");
    protected static final ItemMetaKey BOOK_PAGES = new ItemMetaKey("pages");
    protected static final ItemMetaKey RESOLVED = new ItemMetaKey("resolved");
    protected static final ItemMetaKey GENERATION = new ItemMetaKey("generation");
    protected static final int MAX_PAGES = 100;
    protected static final int MAX_PAGE_LENGTH = 320; // 256 limit + 64 characters to allow for psuedo colour codes
    protected static final int MAX_TITLE_LENGTH = 32;

    protected String title;
    protected String author;
    public List<Text> pages = new ArrayList<Text>();
    protected Integer generation;

    CraftMetaBook(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaBook) {
            CraftMetaBook bookMeta = (CraftMetaBook) meta;
            this.title = bookMeta.title;
            this.author = bookMeta.author;
            pages.addAll(bookMeta.pages);
            this.generation = bookMeta.generation;
        }
    }

    CraftMetaBook(CompoundTag tag) {
        this(tag, true);
    }

    CraftMetaBook(CompoundTag tag, boolean handlePages) {
        super(tag);

        boolean resolved = false;
        if (tag.contains(RESOLVED.NBT))
            resolved = tag.getBoolean(RESOLVED.NBT);

        if (tag.contains(GENERATION.NBT))
            generation = tag.getInt(GENERATION.NBT);

        if (tag.contains(BOOK_PAGES.NBT) && handlePages) {
            ListTag pages = tag.getList(BOOK_PAGES.NBT, CraftMagicNumbers.NBT.TAG_STRING);

            for (int i = 0; i < Math.min(pages.size(), MAX_PAGES); i++) {
                String page = pages.getString(i);
                if (resolved) {
                    try {
                        this.pages.add(Serializer.fromJson(page));
                        continue;
                    } catch (Exception ignore) {/*PreJSON book*/}
                }
            }
        }
    }

    CraftMetaBook(Map<String, Object> map) {
        super(map);

        setAuthor(SerializableMeta.getString(map, BOOK_AUTHOR.BUKKIT, true));

        setTitle(SerializableMeta.getString(map, BOOK_TITLE.BUKKIT, true));

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, BOOK_PAGES.BUKKIT, true);
        if (pages != null)
            for (Object page : pages)
                if (page instanceof String)
                    addPage((String) page);

        generation = SerializableMeta.getObject(Integer.class, map, GENERATION.BUKKIT, true);
    }

    @Override
    void applyToItem(CompoundTag itemData) {
        applyToItem(itemData, true);
    }

    void applyToItem(CompoundTag itemData, boolean handlePages) {
        super.applyToItem(itemData);

        if (hasTitle())
            itemData.putString(BOOK_TITLE.NBT, this.title);

        if (hasAuthor())
            itemData.putString(BOOK_AUTHOR.NBT, this.author);

        if (handlePages) {
            if (hasPages()) {
                ListTag list = new ListTag();
                for (Text page : pages)
                    list.add(StringTag.of(page == null ? "" : page.getString()));
                itemData.put(BOOK_PAGES.NBT, list);
            }

            itemData.remove(RESOLVED.NBT);
        }

        if (generation != null)
            itemData.putInt(GENERATION.NBT, generation);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBookEmpty();
    }

    boolean isBookEmpty() {
        return !(hasPages() || hasAuthor() || hasTitle());
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case WRITTEN_BOOK:
            case WRITABLE_BOOK:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasAuthor() {
        return this.author != null;
    }

    @Override
    public boolean hasTitle() {
        return this.title != null;
    }

    @Override
    public boolean hasPages() {
        return !pages.isEmpty();
    }

    @Override
    public boolean hasGeneration() {
        return generation != null;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean setTitle(final String title) {
        if (title == null) {
            this.title = null;
            return true;
        } else if (title.length() > MAX_TITLE_LENGTH) return false;

        this.title = title;
        return true;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public void setAuthor(final String author) {
        this.author = author;
    }

    @Override
    public Generation getGeneration() {
        return (generation == null) ? null : Generation.values()[generation];
    }

    @Override
    public void setGeneration(Generation generation) {
        this.generation = (generation == null) ? null : generation.ordinal();
    }

    @Override
    public String getPage(final int page) {
        Validate.isTrue(isValidPage(page), "Invalid page number");
        return CraftChatMessage.fromComponent(pages.get(page - 1));
    }

    @Override
    public void setPage(final int page, final String text) {
        if (!isValidPage(page))
            throw new IllegalArgumentException("Invalid page number " + page + "/" + pages.size());

        String newText = text == null ? "" : text.length() > MAX_PAGE_LENGTH ? text.substring(0, MAX_PAGE_LENGTH) : text;
        pages.set(page - 1, CraftChatMessage.fromString(newText, true)[0]);
    }

    @Override
    public void setPages(final String... pages) {
        this.pages.clear();

        addPage(pages);
    }

    @Override
    public void addPage(final String... pages) {
        for (String page : pages) {
            if (this.pages.size() >= MAX_PAGES)
                return;

            if (page == null)
                page = "";
            else if (page.length() > MAX_PAGE_LENGTH)
                page = page.substring(0, MAX_PAGE_LENGTH);

            this.pages.add(CraftChatMessage.fromString(page, true)[0]);
        }
    }

    @Override
    public int getPageCount() {
        return pages.size();
    }

    @Override
    public List<String> getPages() {
        return pages.stream().map(CraftChatMessage::fromComponent).collect(ImmutableList.toImmutableList());
    }

    @Override
    public void setPages(List<String> pages) {
        this.pages.clear();
        for (String page : pages)
            addPage(page);
    }

    private boolean isValidPage(int page) {
        return page > 0 && page <= pages.size();
    }

    @Override
    public CraftMetaBook clone() {
        CraftMetaBook meta = (CraftMetaBook) super.clone();
        meta.pages = new ArrayList<Text>(pages);
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasTitle())
            hash = 61 * hash + this.title.hashCode();
        if (hasAuthor())
            hash = 61 * hash + 13 * this.author.hashCode();
        if (hasPages())
            hash = 61 * hash + 17 * this.pages.hashCode();
        if (hasGeneration())
            hash = 61 * hash + 19 * this.generation.hashCode();

        return original != hash ? CraftMetaBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta))
            return false;

        if (meta instanceof CraftMetaBook) {
            CraftMetaBook that = (CraftMetaBook) meta;

            return (hasTitle() ? that.hasTitle() && this.title.equals(that.title) : !that.hasTitle())
                    && (hasAuthor() ? that.hasAuthor() && this.author.equals(that.author) : !that.hasAuthor())
                    && (hasPages() ? that.hasPages() && this.pages.equals(that.pages) : !that.hasPages())
                    && (hasGeneration() ? that.hasGeneration() && this.generation.equals(that.generation) : !that.hasGeneration());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBook || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasTitle())
            builder.put(BOOK_TITLE.BUKKIT, title);

        if (hasAuthor())
            builder.put(BOOK_AUTHOR.BUKKIT, author);

        if (hasPages()) {
            List<String> pagesString = new ArrayList<String>();
            for (Text comp : pages)
                pagesString.add(CraftChatMessage.fromComponent(comp));
            builder.put(BOOK_PAGES.BUKKIT, pagesString);
        }

        if (generation != null) {
            builder.put(GENERATION.BUKKIT, generation);
        }

        return builder;
    }
}
