package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;

public final class CraftItemFactory implements ItemFactory {
    static final Color DEFAULT_LEATHER_COLOR = Color.fromRGB(0xA06540);
    static final Collection<String> KNOWN_NBT_ATTRIBUTE_NAMES;
    private static final CraftItemFactory instance;

    static {
        instance = new CraftItemFactory();
        ConfigurationSerialization.registerClass(CraftMetaItem.SerializableMeta.class);
        KNOWN_NBT_ATTRIBUTE_NAMES = ImmutableSet.<String>builder()
                .add("generic.armor")
                .add("generic.armorToughness")
                .add("generic.attackDamage")
                .add("generic.followRange")
                .add("generic.knockbackResistance")
                .add("generic.maxHealth")
                .add("generic.movementSpeed")
                .add("generic.flyingSpeed")
                .add("generic.attackSpeed")
                .add("generic.luck")
                .add("horse.jumpStrength")
                .add("zombie.spawnReinforcements")
                .add("generic.reachDistance")
                .build();
    }

    private CraftItemFactory() {
    }

    public static CraftItemFactory instance() {
        return instance;
    }

    public boolean isApplicable(ItemMeta meta, ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        }
        return isApplicable(meta, itemstack.getType());
    }

    public boolean isApplicable(ItemMeta meta, Material type) {
        if (type == null || meta == null) {
            return false;
        }
        if (!(meta instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Meta of " + meta.getClass().toString() + " not created by " + CraftItemFactory.class.getName());
        }

        return ((CraftMetaItem) meta).applicableTo(type);
    }

    public ItemMeta getItemMeta(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return getItemMeta(material, null);
    }

    private ItemMeta getItemMeta(Material material, CraftMetaItem meta) {
        switch (material) {
            case AIR:
                return null;
            case WRITTEN_BOOK:
                return meta instanceof CraftMetaBookSigned ? meta : new CraftMetaBookSigned(meta);
            case BOOK_AND_QUILL:
                return meta != null && meta.getClass().equals(CraftMetaBook.class) ? meta : new CraftMetaBook(meta);
            case SKULL_ITEM:
                return meta instanceof CraftMetaSkull ? meta : new CraftMetaSkull(meta);
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return meta instanceof CraftMetaLeatherArmor ? meta : new CraftMetaLeatherArmor(meta);
            case POTION:
            case SPLASH_POTION:
            case LINGERING_POTION:
            case TIPPED_ARROW:
                return meta instanceof CraftMetaPotion ? meta : new CraftMetaPotion(meta);
            case MAP:
                return meta instanceof CraftMetaMap ? meta : new CraftMetaMap(meta);
            case FIREWORK:
                return meta instanceof CraftMetaFirework ? meta : new CraftMetaFirework(meta);
            case FIREWORK_CHARGE:
                return meta instanceof CraftMetaCharge ? meta : new CraftMetaCharge(meta);
            case ENCHANTED_BOOK:
                return meta instanceof CraftMetaEnchantedBook ? meta : new CraftMetaEnchantedBook(meta);
            case BANNER:
                return meta instanceof CraftMetaBanner ? meta : new CraftMetaBanner(meta);
            case MONSTER_EGG:
                return meta instanceof CraftMetaSpawnEgg ? meta : new CraftMetaSpawnEgg(meta);
            case KNOWLEDGE_BOOK:
                return meta instanceof CraftMetaKnowledgeBook ? meta : new CraftMetaKnowledgeBook(meta);
            case FURNACE:
            case CHEST:
            case TRAPPED_CHEST:
            case JUKEBOX:
            case DISPENSER:
            case DROPPER:
            case SIGN:
            case MOB_SPAWNER:
            case NOTE_BLOCK:
            case BREWING_STAND_ITEM:
            case ENCHANTMENT_TABLE:
            case COMMAND:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN:
            case BEACON:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case HOPPER:
            case REDSTONE_COMPARATOR:
            case FLOWER_POT_ITEM:
            case SHIELD:
            case STRUCTURE_BLOCK:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case SILVER_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case ENDER_CHEST:
                return new CraftMetaBlockState(meta, material);
            default:
                return new CraftMetaItem(meta);
        }
    }

    public boolean equals(ItemMeta meta1, ItemMeta meta2) {
        if (meta1 == meta2) {
            return true;
        }
        if (meta1 != null && !(meta1 instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("First meta of " + meta1.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
        }
        if (meta2 != null && !(meta2 instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Second meta " + meta2.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
        }
        if (meta1 == null) {
            return ((CraftMetaItem) meta2).isEmpty();
        }
        if (meta2 == null) {
            return ((CraftMetaItem) meta1).isEmpty();
        }

        return equals((CraftMetaItem) meta1, (CraftMetaItem) meta2);
    }

    boolean equals(CraftMetaItem meta1, CraftMetaItem meta2) {
        /*
         * This couldn't be done inside of the objects themselves, else force recursion.
         * This is a fairly clean way of implementing it, by dividing the methods into purposes and letting each method perform its own function.
         *
         * The common and uncommon were split, as both could have variables not applicable to the other, like a skull and book.
         * Each object needs its chance to say "hey wait a minute, we're not equal," but without the redundancy of using the 1.equals(2) && 2.equals(1) checking the 'commons' twice.
         *
         * Doing it this way fills all conditions of the .equals() method.
         */
        return meta1.equalsCommon(meta2) && meta1.notUncommon(meta2) && meta2.notUncommon(meta1);
    }

    public ItemMeta asMetaFor(ItemMeta meta, ItemStack stack) {
        Validate.notNull(stack, "Stack cannot be null");
        return asMetaFor(meta, stack.getType());
    }

    public ItemMeta asMetaFor(ItemMeta meta, Material material) {
        Validate.notNull(material, "Material cannot be null");
        if (!(meta instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Meta of " + (meta != null ? meta.getClass().toString() : "null") + " not created by " + CraftItemFactory.class.getName());
        }
        return getItemMeta(material, (CraftMetaItem) meta);
    }

    public Color getDefaultLeatherColor() {
        return DEFAULT_LEATHER_COLOR;
    }
}
