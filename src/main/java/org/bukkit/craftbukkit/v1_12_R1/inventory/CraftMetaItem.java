package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.nbt.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_12_R1.Overridden;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem.ItemMetaKey.Specific;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Children must include the following:
 *
 * <li> Constructor(CraftMetaItem meta)
 * <li> Constructor(NBTTagCompound tag)
 * <li> Constructor(Map<String, Object> map)
 * <br><br>
 * <li> void applyToItem(NBTTagCompound tag)
 * <li> boolean applicableTo(Material type)
 * <br><br>
 * <li> boolean equalsCommon(CraftMetaItem meta)
 * <li> boolean notUncommon(CraftMetaItem meta)
 * <br><br>
 * <li> boolean isEmpty()
 * <li> boolean is{Type}Empty()
 * <br><br>
 * <li> int applyHash()
 * <li> public Class clone()
 * <br><br>
 * <li> Builder<String, Object> serialize(Builder<String, Object> builder)
 * <li> SerializableMeta.Deserializers deserializer()
 */
@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaItem implements ItemMeta, Repairable {

    static final ItemMetaKey NAME = new ItemMetaKey("Name", "display-name");
    static final ItemMetaKey LOCNAME = new ItemMetaKey("LocName", "loc-name");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey DISPLAY = new ItemMetaKey("display");
    static final ItemMetaKey LORE = new ItemMetaKey("Lore", "lore");
    static final ItemMetaKey ENCHANTMENTS = new ItemMetaKey("ench", "enchants");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_ID = new ItemMetaKey("id");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
    static final ItemMetaKey REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES = new ItemMetaKey("AttributeModifiers");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_NAME = new ItemMetaKey("Name");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_VALUE = new ItemMetaKey("Amount");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_TYPE = new ItemMetaKey("Operation");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_HIGH = new ItemMetaKey("UUIDMost");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_LOW = new ItemMetaKey("UUIDLeast");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("HideFlags", "ItemFlags");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey UNBREAKABLE = new ItemMetaKey("Unbreakable");
    private static final Set<String> HANDLED_TAGS = Sets.newHashSet();
    private final Map<String, NBTBase> unhandledTags = new HashMap<>();
    private String displayName;
    private String locName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private int repairCost;
    private int hideFlag;
    private boolean unbreakable;
    // Spigot start
    private final Spigot spigot = new Spigot() {
        @Override
        public boolean isUnbreakable() {
            return CraftMetaItem.this.unbreakable;
        }

        @Override
        public void setUnbreakable(boolean setUnbreakable) {
            CraftMetaItem.this.setUnbreakable(setUnbreakable);
        }
    };
    private NBTTagCompound internalTag;

    CraftMetaItem(CraftMetaItem meta) {
        if (meta == null) {
            return;
        }

        this.displayName = meta.displayName;
        this.locName = meta.locName;

        if (meta.hasLore()) {
            this.lore = new ArrayList<>(meta.lore);
        }

        if (meta.enchantments != null) {
            this.enchantments = new HashMap<>(meta.enchantments);
        }

        this.repairCost = meta.repairCost;
        this.hideFlag = meta.hideFlag;
        this.unbreakable = meta.unbreakable;
        this.unhandledTags.putAll(meta.unhandledTags);

        this.internalTag = meta.internalTag;
        if (this.internalTag != null) {
            deserializeInternal(internalTag);
        }
    }

    CraftMetaItem(NBTTagCompound tag) {
        if (tag.hasKey(DISPLAY.NBT)) {
            NBTTagCompound display = tag.getCompoundTag(DISPLAY.NBT);

            if (display.hasKey(NAME.NBT)) {
                displayName = display.getString(NAME.NBT);
            }

            if (display.hasKey(LOCNAME.NBT)) {
                locName = display.getString(LOCNAME.NBT);
            }

            if (display.hasKey(LORE.NBT)) {
                NBTTagList list = display.getTagList(LORE.NBT, CraftMagicNumbers.NBT.TAG_STRING);
                lore = new ArrayList<>(list.tagCount());

                for (int index = 0; index < list.tagCount(); index++) {
                    String line = list.getStringTagAt(index);
                    lore.add(line);
                }
            }
        }

        this.enchantments = buildEnchantments(tag, ENCHANTMENTS);

        if (tag.hasKey(REPAIR.NBT)) {
            repairCost = tag.getInteger(REPAIR.NBT);
        }

        if (tag.hasKey(HIDEFLAGS.NBT)) {
            hideFlag = tag.getInteger(HIDEFLAGS.NBT);
        }
        if (tag.hasKey(UNBREAKABLE.NBT)) {
            unbreakable = tag.getBoolean(UNBREAKABLE.NBT);
        }
        if (tag.hasKey(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT)) {
            unhandledTags.put(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, tag.getTag(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT));
        }

        if (tag.getTag(ATTRIBUTES.NBT) instanceof NBTTagList) {
            NBTTagList save = null;
            NBTTagList nbttaglist = tag.getTagList(ATTRIBUTES.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
            // Spigot start
            gnu.trove.map.hash.TObjectDoubleHashMap<String> attributeTracker = new gnu.trove.map.hash.TObjectDoubleHashMap<>();
            gnu.trove.map.hash.TObjectDoubleHashMap<String> attributeTrackerX = new gnu.trove.map.hash.TObjectDoubleHashMap<>();
            Map<String, IAttribute> attributesByName = new HashMap<>();
            attributeTracker.put("generic.maxHealth", 20.0);
            attributesByName.put("generic.maxHealth", SharedMonsterAttributes.MAX_HEALTH);
            attributeTracker.put("generic.followRange", 32.0);
            attributesByName.put("generic.followRange", SharedMonsterAttributes.FOLLOW_RANGE);
            attributeTracker.put("generic.knockbackResistance", 0.0);
            attributesByName.put("generic.knockbackResistance", SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
            attributeTracker.put("generic.movementSpeed", 0.7);
            attributesByName.put("generic.movementSpeed", SharedMonsterAttributes.MOVEMENT_SPEED);
            attributeTracker.put("generic.attackDamage", 1.0);
            attributesByName.put("generic.attackDamage", SharedMonsterAttributes.ATTACK_DAMAGE);
            NBTTagList oldList = nbttaglist;
            nbttaglist = new NBTTagList();

            List<NBTTagCompound> op0 = new ArrayList<>();
            List<NBTTagCompound> op1 = new ArrayList<>();
            List<NBTTagCompound> op2 = new ArrayList<>();
            for (int i = 0; i < oldList.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = oldList.getCompoundTagAt(i);
                if (nbttagcompound == null) continue;

                if (!nbttagcompound.hasKey(ATTRIBUTES_UUID_HIGH.NBT, 99)) {
                    continue;
                }
                if (!nbttagcompound.hasKey(ATTRIBUTES_UUID_LOW.NBT, 99)) {
                    continue;
                }
                if (!(nbttagcompound.getTag(ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString) || !CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(ATTRIBUTES_IDENTIFIER.NBT))) {
                    continue;
                }
                if (!(nbttagcompound.getTag(ATTRIBUTES_NAME.NBT) instanceof NBTTagString) || nbttagcompound.getString(ATTRIBUTES_NAME.NBT).isEmpty()) {
                    continue;
                }
                if (!nbttagcompound.hasKey(ATTRIBUTES_VALUE.NBT, 99)) {
                    continue;
                }
                if (!nbttagcompound.hasKey(ATTRIBUTES_TYPE.NBT, 99) || nbttagcompound.getInteger(ATTRIBUTES_TYPE.NBT) < 0 || nbttagcompound.getInteger(ATTRIBUTES_TYPE.NBT) > 2) {
                    continue;
                }

                switch (nbttagcompound.getInteger(ATTRIBUTES_TYPE.NBT)) {
                    case 0:
                        op0.add(nbttagcompound);
                        break;
                    case 1:
                        op1.add(nbttagcompound);
                        break;
                    case 2:
                        op2.add(nbttagcompound);
                        break;
                }
            }
            for (NBTTagCompound nbtTagCompound : op0) {
                String name = nbtTagCompound.getString(ATTRIBUTES_IDENTIFIER.NBT);
                if (attributeTracker.containsKey(name)) {
                    double val = attributeTracker.get(name);
                    val += nbtTagCompound.getDouble(ATTRIBUTES_VALUE.NBT);
                    if (val != attributesByName.get(name).clampValue(val)) {
                        continue;
                    }
                    attributeTracker.put(name, val);
                }
                nbttaglist.appendTag(nbtTagCompound);
            }
            for (String name : attributeTracker.keySet()) {
                attributeTrackerX.put(name, attributeTracker.get(name));
            }
            for (NBTTagCompound nbtTagCompound : op1) {
                String name = nbtTagCompound.getString(ATTRIBUTES_IDENTIFIER.NBT);
                if (attributeTracker.containsKey(name)) {
                    double val = attributeTracker.get(name);
                    double valX = attributeTrackerX.get(name);
                    val += valX * nbtTagCompound.getDouble(ATTRIBUTES_VALUE.NBT);
                    if (val != attributesByName.get(name).clampValue(val)) {
                        continue;
                    }
                    attributeTracker.put(name, val);
                }
                nbttaglist.appendTag(nbtTagCompound);
            }
            for (NBTTagCompound nbtTagCompound : op2) {
                String name = nbtTagCompound.getString(ATTRIBUTES_IDENTIFIER.NBT);
                if (attributeTracker.containsKey(name)) {
                    double val = attributeTracker.get(name);
                    val += val * nbtTagCompound.getDouble(ATTRIBUTES_VALUE.NBT);
                    if (val != attributesByName.get(name).clampValue(val)) {
                        continue;
                    }
                    attributeTracker.put(name, val);
                }
                nbttaglist.appendTag(nbtTagCompound);
            }

            // Spigot end
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                if (!(nbttaglist.get(i) instanceof NBTTagCompound)) {
                    continue;
                }
                NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.get(i);

                if (!nbttagcompound.hasKey(ATTRIBUTES_UUID_HIGH.NBT, CraftMagicNumbers.NBT.TAG_ANY_NUMBER)) {
                    continue;
                }
                if (!nbttagcompound.hasKey(ATTRIBUTES_UUID_LOW.NBT, CraftMagicNumbers.NBT.TAG_ANY_NUMBER)) {
                    continue;
                }
                if (!(nbttagcompound.getTag(ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString) || !CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(ATTRIBUTES_IDENTIFIER.NBT))) {
                    continue;
                }
                if (!(nbttagcompound.getTag(ATTRIBUTES_NAME.NBT) instanceof NBTTagString) || nbttagcompound.getString(ATTRIBUTES_NAME.NBT).isEmpty()) {
                    continue;
                }
                if (!nbttagcompound.hasKey(ATTRIBUTES_VALUE.NBT, CraftMagicNumbers.NBT.TAG_ANY_NUMBER)) {
                    continue;
                }
                if (!nbttagcompound.hasKey(ATTRIBUTES_TYPE.NBT, CraftMagicNumbers.NBT.TAG_ANY_NUMBER) || nbttagcompound.getInteger(ATTRIBUTES_TYPE.NBT) < 0 || nbttagcompound.getInteger(ATTRIBUTES_TYPE.NBT) > 2) {
                    continue;
                }

                if (save == null) {
                    save = new NBTTagList();
                }

                NBTTagCompound entry = new NBTTagCompound();
                entry.setTag(ATTRIBUTES_UUID_HIGH.NBT, nbttagcompound.getTag(ATTRIBUTES_UUID_HIGH.NBT));
                entry.setTag(ATTRIBUTES_UUID_LOW.NBT, nbttagcompound.getTag(ATTRIBUTES_UUID_LOW.NBT));
                entry.setTag(ATTRIBUTES_IDENTIFIER.NBT, nbttagcompound.getTag(ATTRIBUTES_IDENTIFIER.NBT));
                entry.setTag(ATTRIBUTES_NAME.NBT, nbttagcompound.getTag(ATTRIBUTES_NAME.NBT));
                entry.setTag(ATTRIBUTES_VALUE.NBT, nbttagcompound.getTag(ATTRIBUTES_VALUE.NBT));
                entry.setTag(ATTRIBUTES_TYPE.NBT, nbttagcompound.getTag(ATTRIBUTES_TYPE.NBT));
                save.appendTag(entry);
            }

            unhandledTags.put(ATTRIBUTES.NBT, save);
        }

        Set<String> keys = tag.getKeySet();
        for (String key : keys) {
            if (!getHandledTags().contains(key)) {
                unhandledTags.put(key, tag.getTag(key));
            }
        }
    }

    CraftMetaItem(Map<String, Object> map) {
        setDisplayName(SerializableMeta.getString(map, NAME.BUKKIT, true));
        setLocalizedName(SerializableMeta.getString(map, LOCNAME.BUKKIT, true));

        Iterable<?> lore = SerializableMeta.getObject(Iterable.class, map, LORE.BUKKIT, true);
        if (lore != null) {
            safelyAdd(lore, this.lore = new ArrayList<>(), Integer.MAX_VALUE);
        }

        enchantments = buildEnchantments(map, ENCHANTMENTS);

        Integer repairCost = SerializableMeta.getObject(Integer.class, map, REPAIR.BUKKIT, true);
        if (repairCost != null) {
            setRepairCost(repairCost);
        }

        Iterable<?> hideFlags = SerializableMeta.getObject(Iterable.class, map, HIDEFLAGS.BUKKIT, true);
        if (hideFlags != null) {
            for (Object hideFlagObject : hideFlags) {
                String hideFlagString = (String) hideFlagObject;
                try {
                    ItemFlag hideFlatEnum = ItemFlag.valueOf(hideFlagString);
                    addItemFlags(hideFlatEnum);
                } catch (IllegalArgumentException ex) {
                    // Ignore when we got a old String which does not map to a Enum value anymore
                }
            }
        }

        Boolean unbreakable = SerializableMeta.getObject(Boolean.class, map, UNBREAKABLE.BUKKIT, true);
        if (unbreakable != null) {
            setUnbreakable(unbreakable);
        }

        String internal = SerializableMeta.getString(map, "internal", true);
        if (internal != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64(internal));
            try {
                internalTag = CompressedStreamTools.readCompressed(buf);
                deserializeInternal(internalTag);
                Set<String> keys = internalTag.getKeySet();
                for (String key : keys) {
                    if (!getHandledTags().contains(key)) {
                        unhandledTags.put(key, internalTag.getTag(key));
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static Map<Enchantment, Integer> buildEnchantments(NBTTagCompound tag, ItemMetaKey key) {
        if (!tag.hasKey(key.NBT)) {
            return null;
        }

        NBTTagList ench = tag.getTagList(key.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
        Map<Enchantment, Integer> enchantments = new HashMap<>(ench.tagCount());

        for (int i = 0; i < ench.tagCount(); i++) {
            int id = 0xffff & ((NBTTagCompound) ench.get(i)).getShort(ENCHANTMENTS_ID.NBT);
            int level = 0xffff & ((NBTTagCompound) ench.get(i)).getShort(ENCHANTMENTS_LVL.NBT);

            Enchantment enchant = Enchantment.getById(id);
            if (enchant != null) {
                enchantments.put(enchant, level);
            }
        }

        return enchantments;
    }

    static Map<Enchantment, Integer> buildEnchantments(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> ench = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }

        Map<Enchantment, Integer> enchantments = new HashMap<>(ench.size());
        for (Map.Entry<?, ?> entry : ench.entrySet()) {
            // Doctor older enchants
            String enchantKey = entry.getKey().toString();
            if (enchantKey.equals("SWEEPING")) {
                enchantKey = "SWEEPING_EDGE";
            }

            Enchantment enchantment = Enchantment.getByName(enchantKey);
            if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                enchantments.put(enchantment, (Integer) entry.getValue());
            }
        }

        return enchantments;
    }

    static NBTTagList createStringList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        NBTTagList tagList = new NBTTagList();
        for (String value : list) {
            tagList.appendTag(new NBTTagString(value));
        }

        return tagList;
    }

    static void applyEnchantments(Map<Enchantment, Integer> enchantments, NBTTagCompound tag, ItemMetaKey key) {
        if (enchantments == null /*|| enchantments.size() == 0*/) { // Spigot - remove size check
            return;
        }

        NBTTagList list = new NBTTagList();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            NBTTagCompound subtag = new NBTTagCompound();

            subtag.setShort(ENCHANTMENTS_ID.NBT, (short) entry.getKey().getId());
            subtag.setShort(ENCHANTMENTS_LVL.NBT, entry.getValue().shortValue());

            list.appendTag(subtag);
        }

        tag.setTag(key.NBT, list);
    }

    static void serializeEnchantments(Map<Enchantment, Integer> enchantments, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }

        ImmutableMap.Builder<String, Integer> enchants = ImmutableMap.builder();
        for (Map.Entry<? extends Enchantment, Integer> enchant : enchantments.entrySet()) {
            enchants.put(enchant.getKey().getName(), enchant.getValue());
        }

        builder.put(key.BUKKIT, enchants.build());
    }

    static void safelyAdd(Iterable<?> addFrom, Collection<String> addTo, int maxItemLength) {
        if (addFrom == null) {
            return;
        }

        for (Object object : addFrom) {
            if (!(object instanceof String)) {
                if (object != null) {
                    throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                }

                addTo.add("");
            } else {
                String page = object.toString();

                if (page.length() > maxItemLength) {
                    page = page.substring(0, maxItemLength);
                }

                addTo.add(page);
            }
        }
    }

    static boolean checkConflictingEnchants(Map<Enchantment, Integer> enchantments, Enchantment ench) {
        if (enchantments == null || enchantments.isEmpty()) {
            return false;
        }

        for (Enchantment enchant : enchantments.keySet()) {
            if (enchant.conflictsWith(ench)) {
                return true;
            }
        }

        return false;
    }

    public static Set<String> getHandledTags() {
        synchronized (HANDLED_TAGS) {
            if (HANDLED_TAGS.isEmpty()) {
                HANDLED_TAGS.addAll(Arrays.asList(
                        DISPLAY.NBT,
                        REPAIR.NBT,
                        ENCHANTMENTS.NBT,
                        HIDEFLAGS.NBT,
                        UNBREAKABLE.NBT,
                        CraftMetaMap.MAP_SCALING.NBT,
                        CraftMetaPotion.POTION_EFFECTS.NBT,
                        CraftMetaPotion.DEFAULT_POTION.NBT,
                        CraftMetaSkull.SKULL_OWNER.NBT,
                        CraftMetaSkull.SKULL_PROFILE.NBT,
                        CraftMetaSpawnEgg.ENTITY_TAG.NBT,
                        CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT,
                        CraftMetaBook.BOOK_TITLE.NBT,
                        CraftMetaBook.BOOK_AUTHOR.NBT,
                        CraftMetaBook.BOOK_PAGES.NBT,
                        CraftMetaBook.RESOLVED.NBT,
                        CraftMetaBook.GENERATION.NBT,
                        CraftMetaFirework.FIREWORKS.NBT,
                        CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT,
                        CraftMetaCharge.EXPLOSION.NBT,
                        CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT,
                        CraftMetaKnowledgeBook.BOOK_RECIPES.NBT
                ));
            }
            return HANDLED_TAGS;
        }
    }

    void deserializeInternal(NBTTagCompound tag) {
    }

    @Overridden
    void applyToItem(NBTTagCompound itemTag) {
        if (hasDisplayName()) {
            setDisplayTag(itemTag, NAME.NBT, new NBTTagString(displayName));
        }
        if (hasLocalizedName()) {
            setDisplayTag(itemTag, LOCNAME.NBT, new NBTTagString(locName));
        }

        if (hasLore()) {
            setDisplayTag(itemTag, LORE.NBT, createStringList(lore));
        }

        if (hideFlag != 0) {
            itemTag.setInteger(HIDEFLAGS.NBT, hideFlag);
        }

        applyEnchantments(enchantments, itemTag, ENCHANTMENTS);

        if (hasRepairCost()) {
            itemTag.setInteger(REPAIR.NBT, repairCost);
        }

        if (isUnbreakable()) {
            itemTag.setBoolean(UNBREAKABLE.NBT, unbreakable);
        }

        for (Map.Entry<String, NBTBase> e : unhandledTags.entrySet()) {
            itemTag.setTag(e.getKey(), e.getValue());
        }
    }

    void setDisplayTag(NBTTagCompound tag, String key, NBTBase value) {
        final NBTTagCompound display = tag.getCompoundTag(DISPLAY.NBT);

        if (!tag.hasKey(DISPLAY.NBT)) {
            tag.setTag(DISPLAY.NBT, display);
        }

        display.setTag(key, value);
    }

    @Overridden
    boolean applicableTo(Material type) {
        return type != Material.AIR;
    }

    @Overridden
    boolean isEmpty() {
        return !(hasDisplayName() || hasLocalizedName() || hasEnchants() || hasLore() || hasRepairCost() || !unhandledTags.isEmpty() || hideFlag != 0 || isUnbreakable());
    }

    public String getDisplayName() {
        return displayName;
    }

    public final void setDisplayName(String name) {
        this.displayName = name;
    }

    public boolean hasDisplayName() {
        return !Strings.isNullOrEmpty(displayName);
    }

    @Override
    public String getLocalizedName() {
        return locName;
    }

    @Override
    public void setLocalizedName(String name) {
        this.locName = name;
    }

    @Override
    public boolean hasLocalizedName() {
        return !Strings.isNullOrEmpty(locName);
    }

    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    public boolean hasRepairCost() {
        return repairCost > 0;
    }

    public boolean hasEnchant(Enchantment ench) {
        Validate.notNull(ench, "Enchantment cannot be null");
        return hasEnchants() && enchantments.containsKey(ench);
    }

    public int getEnchantLevel(Enchantment ench) {
        Validate.notNull(ench, "Enchantment cannot be null");
        Integer level = hasEnchants() ? enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return hasEnchants() ? ImmutableMap.copyOf(enchantments) : ImmutableMap.of();
    }

    public boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
        Validate.notNull(ench, "Enchantment cannot be null");
        if (enchantments == null) {
            enchantments = new HashMap<>(4);
        }

        if (ignoreRestrictions || level >= ench.getStartLevel() && level <= ench.getMaxLevel()) {
            Integer old = enchantments.put(ench, level);
            return old == null || old != level;
        }
        return false;
    }

    public boolean removeEnchant(Enchantment ench) {
        Validate.notNull(ench, "Enchantment cannot be null");
        // Spigot start
        boolean b = hasEnchants() && enchantments.remove(ench) != null;
        if (enchantments != null && enchantments.isEmpty()) {
            this.enchantments = null;
        }
        return b;
        // Spigot end
    }

    public boolean hasEnchants() {
        return !(enchantments == null || enchantments.isEmpty());
    }

    public boolean hasConflictingEnchant(Enchantment ench) {
        return checkConflictingEnchants(enchantments, ench);
    }

    @Override
    public void addItemFlags(ItemFlag... hideFlags) {
        for (ItemFlag f : hideFlags) {
            this.hideFlag |= getBitModifier(f);
        }
    }

    @Override
    public void removeItemFlags(ItemFlag... hideFlags) {
        for (ItemFlag f : hideFlags) {
            this.hideFlag &= ~getBitModifier(f);
        }
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);

        for (ItemFlag f : ItemFlag.values()) {
            if (hasItemFlag(f)) {
                currentFlags.add(f);
            }
        }

        return currentFlags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        int bitModifier = getBitModifier(flag);
        return (this.hideFlag & bitModifier) == bitModifier;
    }

    private byte getBitModifier(ItemFlag hideFlag) {
        return (byte) (1 << hideFlag.ordinal());
    }

    public List<String> getLore() {
        return this.lore == null ? null : new ArrayList<>(this.lore);
    }

    public void setLore(List<String> lore) { // too tired to think if .clone is better
        if (lore == null) {
            this.lore = null;
        } else {
            if (this.lore == null) {
                safelyAdd(lore, this.lore = new ArrayList<>(lore.size()), Integer.MAX_VALUE);
            } else {
                this.lore.clear();
                safelyAdd(lore, this.lore, Integer.MAX_VALUE);
            }
        }
    }

    public int getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(int cost) { // TODO: Does this have limits?
        repairCost = cost;
    }

    @Override
    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @Override
    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof CraftMetaItem)) {
            return false;
        }
        return CraftItemFactory.instance().equals(this, (ItemMeta) object);
    }

    /**
     * This method is almost as weird as notUncommon.
     * Only return false if your common internals are unequal.
     * Checking your own internals is redundant if you are not common, as notUncommon is meant for checking those 'not common' variables.
     */
    @Overridden
    boolean equalsCommon(CraftMetaItem that) {
        return ((this.hasDisplayName() ? that.hasDisplayName() && this.displayName.equals(that.displayName) : !that.hasDisplayName()))
                && (this.hasLocalizedName() ? that.hasLocalizedName() && this.locName.equals(that.locName) : !that.hasLocalizedName())
                && (this.hasEnchants() ? that.hasEnchants() && this.enchantments.equals(that.enchantments) : !that.hasEnchants())
                && (this.hasLore() ? that.hasLore() && this.lore.equals(that.lore) : !that.hasLore())
                && (this.hasRepairCost() ? that.hasRepairCost() && this.repairCost == that.repairCost : !that.hasRepairCost())
                && (this.unhandledTags.equals(that.unhandledTags))
                && (this.hideFlag == that.hideFlag)
                && (this.isUnbreakable() == that.isUnbreakable());
    }

    /**
     * This method is a bit weird...
     * Return true if you are a common class OR your uncommon parts are empty.
     * Empty uncommon parts implies the NBT data would be equivalent if both were applied to an item
     */
    @Overridden
    boolean notUncommon(CraftMetaItem meta) {
        return true;
    }

    @Override
    public final int hashCode() {
        return applyHash();
    }

    @Overridden
    int applyHash() {
        int hash = 3;
        hash = 61 * hash + (hasDisplayName() ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (hasLocalizedName() ? this.locName.hashCode() : 0);
        hash = 61 * hash + (hasLore() ? this.lore.hashCode() : 0);
        hash = 61 * hash + (hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (hasRepairCost() ? this.repairCost : 0);
        hash = 61 * hash + unhandledTags.hashCode();
        hash = 61 * hash + hideFlag;
        hash = 61 * hash + (isUnbreakable() ? 1231 : 1237);
        return hash;
    }

    @Overridden
    @Override
    public CraftMetaItem clone() {
        try {
            CraftMetaItem clone = (CraftMetaItem) super.clone();
            if (this.lore != null) {
                clone.lore = new ArrayList<>(this.lore);
            }
            if (this.enchantments != null) {
                clone.enchantments = new HashMap<>(this.enchantments);
            }
            clone.hideFlag = this.hideFlag;
            clone.unbreakable = this.unbreakable;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public final Map<String, Object> serialize() {
        ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
        map.put(SerializableMeta.TYPE_FIELD, SerializableMeta.classMap.get(getClass()));
        serialize(map);
        return map.build();
    }

    @Overridden
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        if (hasDisplayName()) {
            builder.put(NAME.BUKKIT, displayName);
        }
        if (hasLocalizedName()) {
            builder.put(LOCNAME.BUKKIT, locName);
        }

        if (hasLore()) {
            builder.put(LORE.BUKKIT, ImmutableList.copyOf(lore));
        }

        serializeEnchantments(enchantments, builder, ENCHANTMENTS);

        if (hasRepairCost()) {
            builder.put(REPAIR.BUKKIT, repairCost);
        }

        List<String> hideFlags = new ArrayList<>();
        for (ItemFlag hideFlagEnum : getItemFlags()) {
            hideFlags.add(hideFlagEnum.name());
        }
        if (!hideFlags.isEmpty()) {
            builder.put(HIDEFLAGS.BUKKIT, hideFlags);
        }

        if (isUnbreakable()) {
            builder.put(UNBREAKABLE.BUKKIT, unbreakable);
        }

        final Map<String, NBTBase> internalTags = new HashMap<>(unhandledTags);
        serializeInternal(internalTags);
        if (!internalTags.isEmpty()) {
            NBTTagCompound internal = new NBTTagCompound();
            for (Map.Entry<String, NBTBase> e : internalTags.entrySet()) {
                internal.setTag(e.getKey(), e.getValue());
            }
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                CompressedStreamTools.writeCompressed(internal, buf);
                builder.put("internal", Base64.encodeBase64String(buf.toByteArray()));
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return builder;
    }

    void serializeInternal(final Map<String, NBTBase> unhandledTags) {
    }

    @Override
    public final String toString() {
        return SerializableMeta.classMap.get(getClass()) + "_META:" + serialize(); // TODO: cry
    }

    @Override
    public Spigot spigot() {
        return spigot;
    }

    static class ItemMetaKey {

        final String BUKKIT;
        final String NBT;

        ItemMetaKey(final String both) {
            this(both, both);
        }

        ItemMetaKey(final String nbt, final String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.FIELD)
        @interface Specific {
            To value();

            enum To {
                BUKKIT,
                NBT,
                ;
            }
        }
    }

    @SerializableAs("ItemMeta")
    public static class SerializableMeta implements ConfigurationSerializable {
        static final String TYPE_FIELD = "meta-type";

        static final ImmutableMap<Class<? extends CraftMetaItem>, String> classMap;
        static final ImmutableMap<String, Constructor<? extends CraftMetaItem>> constructorMap;

        static {
            classMap = ImmutableMap.<Class<? extends CraftMetaItem>, String>builder()
                    .put(CraftMetaBanner.class, "BANNER")
                    .put(CraftMetaBlockState.class, "TILE_ENTITY")
                    .put(CraftMetaBook.class, "BOOK")
                    .put(CraftMetaBookSigned.class, "BOOK_SIGNED")
                    .put(CraftMetaSkull.class, "SKULL")
                    .put(CraftMetaLeatherArmor.class, "LEATHER_ARMOR")
                    .put(CraftMetaMap.class, "MAP")
                    .put(CraftMetaPotion.class, "POTION")
                    .put(CraftMetaSpawnEgg.class, "SPAWN_EGG")
                    .put(CraftMetaEnchantedBook.class, "ENCHANTED")
                    .put(CraftMetaFirework.class, "FIREWORK")
                    .put(CraftMetaCharge.class, "FIREWORK_EFFECT")
                    .put(CraftMetaKnowledgeBook.class, "KNOWLEDGE_BOOK")
                    .put(CraftMetaItem.class, "UNSPECIFIC")
                    .build();

            final ImmutableMap.Builder<String, Constructor<? extends CraftMetaItem>> classConstructorBuilder = ImmutableMap.builder();
            for (Map.Entry<Class<? extends CraftMetaItem>, String> mapping : classMap.entrySet()) {
                try {
                    classConstructorBuilder.put(mapping.getValue(), mapping.getKey().getDeclaredConstructor(Map.class));
                } catch (NoSuchMethodException e) {
                    throw new AssertionError(e);
                }
            }
            constructorMap = classConstructorBuilder.build();
        }

        private SerializableMeta() {
        }

        public static ItemMeta deserialize(Map<String, Object> map) throws Throwable {
            Validate.notNull(map, "Cannot deserialize null map");

            String type = getString(map, TYPE_FIELD, false);
            Constructor<? extends CraftMetaItem> constructor = constructorMap.get(type);

            if (constructor == null) {
                throw new IllegalArgumentException(type + " is not a valid " + TYPE_FIELD);
            }

            try {
                return constructor.newInstance(map);
            } catch (final InstantiationException | IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (final InvocationTargetException e) {
                throw e.getCause();
            }
        }

        static String getString(Map<?, ?> map, Object field, boolean nullable) {
            return getObject(String.class, map, field, nullable);
        }

        static boolean getBoolean(Map<?, ?> map, Object field) {
            Boolean value = getObject(Boolean.class, map, field, true);
            return value != null && value;
        }

        static <T> T getObject(Class<T> clazz, Map<?, ?> map, Object field, boolean nullable) {
            final Object object = map.get(field);

            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            }
            if (object == null) {
                if (!nullable) {
                    throw new NoSuchElementException(map + " does not contain " + field);
                }
                return null;
            }
            throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
        }

        public Map<String, Object> serialize() {
            throw new AssertionError();
        }
    }
    // Spigot end
}
