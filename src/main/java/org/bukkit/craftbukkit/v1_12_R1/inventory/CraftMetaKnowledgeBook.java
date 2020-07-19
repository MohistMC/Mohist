package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaKnowledgeBook extends CraftMetaItem implements KnowledgeBookMeta {

    static final ItemMetaKey BOOK_RECIPES = new ItemMetaKey("Recipes");
    static final int MAX_RECIPES = Short.MAX_VALUE;

    protected List<NamespacedKey> recipes = new ArrayList<>();

    CraftMetaKnowledgeBook(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaKnowledgeBook) {
            CraftMetaKnowledgeBook bookMeta = (CraftMetaKnowledgeBook) meta;
            this.recipes.addAll(bookMeta.recipes);
        }
    }

    CraftMetaKnowledgeBook(NBTTagCompound tag) {
        super(tag);

        if (tag.hasKey(BOOK_RECIPES.NBT)) {
            NBTTagList pages = tag.getTagList(BOOK_RECIPES.NBT, 8);

            for (int i = 0; i < pages.tagCount(); i++) {
                String recipe = pages.getStringTagAt(i);

                addRecipe(CraftNamespacedKey.fromString(recipe));
            }
        }
    }

    CraftMetaKnowledgeBook(Map<String, Object> map) {
        super(map);

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, BOOK_RECIPES.BUKKIT, true);
        if (pages != null) {
            for (Object page : pages) {
                if (page instanceof String) {
                    addRecipe(CraftNamespacedKey.fromString((String) page));
                }
            }
        }
    }

    void applyToItem(NBTTagCompound itemData) {
        super.applyToItem(itemData);

        if (hasRecipes()) {
            NBTTagList list = new NBTTagList();
            for (NamespacedKey recipe : this.recipes) {
                list.appendTag(new NBTTagString(recipe.toString()));
            }
            itemData.setTag(BOOK_RECIPES.NBT, list);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBookEmpty();
    }

    boolean isBookEmpty() {
        return !(hasRecipes());
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case KNOWLEDGE_BOOK:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasRecipes() {
        return !recipes.isEmpty();
    }

    @Override
    public void addRecipe(NamespacedKey... recipes) {
        for (NamespacedKey recipe : recipes) {
            if (recipe != null) {
                if (this.recipes.size() >= MAX_RECIPES) {
                    return;
                }

                this.recipes.add(recipe);
            }
        }
    }

    @Override
    public List<NamespacedKey> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    @Override
    public void setRecipes(List<NamespacedKey> recipes) {
        this.recipes.clear();
        for (NamespacedKey recipe : this.recipes) {
            addRecipe(recipe);
        }
    }

    @Override
    public CraftMetaKnowledgeBook clone() {
        CraftMetaKnowledgeBook meta = (CraftMetaKnowledgeBook) super.clone();
        meta.recipes = new ArrayList<>(recipes);
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasRecipes()) {
            hash = 61 * hash + 17 * this.recipes.hashCode();
        }
        return original != hash ? CraftMetaKnowledgeBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaKnowledgeBook) {
            CraftMetaKnowledgeBook that = (CraftMetaKnowledgeBook) meta;

            return (hasRecipes() ? that.hasRecipes() && this.recipes.equals(that.recipes) : !that.hasRecipes());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaKnowledgeBook || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasRecipes()) {
            List<String> recipesString = new ArrayList<>();
            for (NamespacedKey recipe : recipes) {
                recipesString.add(recipe.toString());
            }
            builder.put(BOOK_RECIPES.BUKKIT, recipesString);
        }

        return builder;
    }
}
