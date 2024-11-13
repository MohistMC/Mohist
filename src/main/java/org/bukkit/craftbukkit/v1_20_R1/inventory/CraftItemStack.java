package org.bukkit.craftbukkit.v1_20_R1.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_20_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftLegacy;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Map;
import java.util.Objects;

import static org.bukkit.craftbukkit.v1_20_R1.inventory.CraftMetaItem.ENCHANTMENTS;
import static org.bukkit.craftbukkit.v1_20_R1.inventory.CraftMetaItem.ENCHANTMENTS_ID;
import static org.bukkit.craftbukkit.v1_20_R1.inventory.CraftMetaItem.ENCHANTMENTS_LVL;

@DelegateDeserialization(ItemStack.class)
public final class CraftItemStack extends ItemStack {

    // Paper start - override isEmpty to use vanilla's impl
    @Override
    public boolean isEmpty() {
        return handle == null || handle.isEmpty();
    }
    // Paper end

    public static net.minecraft.world.item.ItemStack asNMSCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return stack.handle == null ? net.minecraft.world.item.ItemStack.EMPTY : stack.handle.copy();
        }
        if (original == null || original.isEmpty()) { // Paper - use isEmpty
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        Item item = CraftMagicNumbers.getItem(original.getType(), original.getDurability());

        if (item == null) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(item, original.getAmount());
        if (original.hasItemMeta()) {
            setItemMeta(stack, original.getItemMeta());
        }
        return stack;
    }

    public static net.minecraft.world.item.ItemStack copyNMSStack(net.minecraft.world.item.ItemStack original, int amount) {
        net.minecraft.world.item.ItemStack stack = original.copy();
        stack.setCount(amount);
        return stack;
    }

    /**
     * Copies the NMS stack to return as a strictly-Bukkit stack
     */
    public static ItemStack asBukkitCopy(net.minecraft.world.item.ItemStack original) {
        if (original.isEmpty()) {
            return new ItemStack(Material.AIR);
        }
        ItemStack stack = new ItemStack(CraftMagicNumbers.getMaterial(original.getItem()), original.getCount());
        if (hasItemMeta(original)) {
            stack.setItemMeta(getItemMeta(original));
        }
        return stack;
    }

    public static CraftItemStack asCraftMirror(net.minecraft.world.item.ItemStack original) {
        return new CraftItemStack((original == null || original.isEmpty()) ? null : original);
    }

    public static CraftItemStack asCraftCopy(ItemStack original) {
        if (original instanceof CraftItemStack stack) {
            return new CraftItemStack(stack.handle == null ? null : stack.handle.copy());
        }
        return new CraftItemStack(original);
    }

    public static CraftItemStack asNewCraftStack(Item item) {
        return asNewCraftStack(item, 1);
    }

    public static CraftItemStack asNewCraftStack(Item item, int amount) {
        return new CraftItemStack(CraftMagicNumbers.getMaterial(item), amount, (short) 0, null);
    }

    public net.minecraft.world.item.ItemStack handle;

    /**
     * Mirror
     */
    private CraftItemStack(net.minecraft.world.item.ItemStack item) {
        this.handle = item;
    }

    private CraftItemStack(ItemStack item) {
        this(item.getType(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
    }

    private CraftItemStack(Material type, int amount, short durability, ItemMeta itemMeta) {
        setType(type);
        setAmount(amount);
        setDurability(durability);
        setItemMeta(itemMeta);
    }

    @Override
    public MaterialData getData() {
        return handle != null ? CraftMagicNumbers.getMaterialData(handle.getItem()) : super.getData();
    }

    @Override
    public Material getType() {
        return handle != null ? CraftMagicNumbers.getMaterial(handle.getItem()) : Material.AIR;
    }

    @Override
    public void setType(Material type) {
        if (getType() == type) {
            return;
        } else if (type == Material.AIR) {
            handle = null;
        } else if (CraftMagicNumbers.getItem(type) == null) { // :(
            handle = null;
        } else if (handle == null) {
            handle = new net.minecraft.world.item.ItemStack(CraftMagicNumbers.getItem(type), 1);
        } else {
            handle.setItem(CraftMagicNumbers.getItem(type));
            if (hasItemMeta()) {
                // This will create the appropriate item meta, which will contain all the data we intend to keep
                setItemMeta(handle, getItemMeta(handle));
            }
        }
        setData(null);
    }

    @Override
    public int getAmount() {
        return handle != null ? handle.getCount() : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (handle == null) {
            return;
        }

        handle.setCount(amount);
        if (amount == 0) {
            handle = null;
        }
    }

    @Override
    public void setDurability(final short durability) {
        // Ignore damage if item is null
        if (handle != null) {
            handle.setDamageValue(durability);
        }
    }

    @Override
    public short getDurability() {
        if (handle != null) {
            return (short) handle.getDamageValue();
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return (handle == null) ? Material.AIR.getMaxStackSize() : handle.getItem().getMaxStackSize();
    }

    // Paper start
    @Override
    public int getMaxItemUseDuration() {
        return handle == null ? 0 : handle.getUseDuration();
    }
    // Paper end

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");

        if (!makeTag(handle)) {
            return;
        }
        ListTag list = getEnchantmentList(handle);
        if (list == null) {
            list = new ListTag();
            handle.getTag().put(ENCHANTMENTS.NBT, list);
        }
        int size = list.size();

        for (int i = 0; i < size; i++) {
            CompoundTag tag = (CompoundTag) list.get(i);
            String id = tag.getString(ENCHANTMENTS_ID.NBT);
            if (ench.getKey().equals(NamespacedKey.fromString(id))) {
                tag.putShort(ENCHANTMENTS_LVL.NBT, (short) level);
                return;
            }
        }
        CompoundTag tag = new CompoundTag();
        tag.putString(ENCHANTMENTS_ID.NBT, ench.getKey().toString());
        tag.putShort(ENCHANTMENTS_LVL.NBT, (short) level);
        list.add(tag);
    }

    static boolean makeTag(net.minecraft.world.item.ItemStack item) {
        if (item == null) {
            return false;
        }

        if (item.getTag() == null) {
            item.setTag(new CompoundTag());
        }

        return true;
    }

    @Override
    public boolean containsEnchantment(Enchantment ench) {
        return getEnchantmentLevel(ench) > 0;
    }

    @Override
    public int getEnchantmentLevel(Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        if (handle == null) {
            return 0;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(CraftEnchantment.getRaw(ench), handle);
    }

    @Override
    public int removeEnchantment(Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");

        ListTag list = getEnchantmentList(handle), listCopy;
        if (list == null) {
            return 0;
        }
        int index = Integer.MIN_VALUE;
        int level = Integer.MIN_VALUE;
        int size = list.size();

        for (int i = 0; i < size; i++) {
            CompoundTag enchantment = (CompoundTag) list.get(i);
            String id = enchantment.getString(ENCHANTMENTS_ID.NBT);
            if (ench.getKey().equals(NamespacedKey.fromString(id))) {
                index = i;
                level = 0xffff & enchantment.getShort(ENCHANTMENTS_LVL.NBT);
                break;
            }
        }

        if (index == Integer.MIN_VALUE) {
            return 0;
        }
        if (size == 1) {
            handle.getTag().remove(ENCHANTMENTS.NBT);
            if (handle.getTag().isEmpty()) {
                handle.setTag(null);
            }
            return level;
        }

        // This is workaround for not having an index removal
        listCopy = new ListTag();
        for (int i = 0; i < size; i++) {
            if (i != index) {
                listCopy.add(list.get(i));
            }
        }
        handle.getTag().put(ENCHANTMENTS.NBT, listCopy);

        return level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return getEnchantments(handle);
    }

    static Map<Enchantment, Integer> getEnchantments(net.minecraft.world.item.ItemStack item) {
        ListTag list = (item != null && item.isEnchanted()) ? item.getEnchantmentTags() : null;

        if (list == null || list.size() == 0) {
            return ImmutableMap.of();
        }

        ImmutableMap.Builder<Enchantment, Integer> result = ImmutableMap.builder();

        for (int i = 0; i < list.size(); i++) {
            String id = ((CompoundTag) list.get(i)).getString(ENCHANTMENTS_ID.NBT);
            int level = 0xffff & ((CompoundTag) list.get(i)).getShort(ENCHANTMENTS_LVL.NBT);

            Enchantment enchant = Enchantment.getByKey(CraftNamespacedKey.fromStringOrNull(id));
            if (enchant != null) {
                result.put(enchant, level);
            }
        }

        return result.build();
    }

    static ListTag getEnchantmentList(net.minecraft.world.item.ItemStack item) {
        return (item != null && item.isEnchanted()) ? item.getEnchantmentTags() : null;
    }

    @Override
    public CraftItemStack clone() {
        CraftItemStack itemStack = (CraftItemStack) super.clone();
        if (this.handle != null) {
            itemStack.handle = this.handle.copy();
        }
        return itemStack;
    }

    @Override
    public ItemMeta getItemMeta() {
        return getItemMeta(handle);
    }

    public static ItemMeta getItemMeta(net.minecraft.world.item.ItemStack item) {

        if (item == null || item == net.minecraft.world.item.ItemStack.EMPTY)
            return null;

        if (item.getTag() == null) {
            ItemMeta meta = CraftItemFactory.instance().getItemMeta(getType(item));
            ((CraftMetaItem) meta).setForgeCaps(item.getForgeCaps());
            return meta;
        }
        CraftMetaItem meta = switch (getType(item)) {
            case WRITTEN_BOOK -> new CraftMetaBookSigned(item.getTag());
            case WRITABLE_BOOK -> new CraftMetaBook(item.getTag());
            case CREEPER_HEAD, CREEPER_WALL_HEAD, DRAGON_HEAD, DRAGON_WALL_HEAD, PIGLIN_HEAD, PIGLIN_WALL_HEAD, PLAYER_HEAD, PLAYER_WALL_HEAD, SKELETON_SKULL, SKELETON_WALL_SKULL, WITHER_SKELETON_SKULL, WITHER_SKELETON_WALL_SKULL, ZOMBIE_HEAD, ZOMBIE_WALL_HEAD ->
                    new CraftMetaSkull(item.getTag());
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS, DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS, GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS, IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS, TURTLE_HELMET ->
                    new CraftMetaArmor(item.getTag());
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS ->
                    new CraftMetaColorableArmor(item.getTag());
            case LEATHER_HORSE_ARMOR -> new CraftMetaLeatherArmor(item.getTag());
            case POTION, SPLASH_POTION, LINGERING_POTION, TIPPED_ARROW -> new CraftMetaPotion(item.getTag());
            case FILLED_MAP -> new CraftMetaMap(item.getTag());
            case FIREWORK_ROCKET -> new CraftMetaFirework(item.getTag());
            case FIREWORK_STAR -> new CraftMetaCharge(item.getTag());
            case ENCHANTED_BOOK -> new CraftMetaEnchantedBook(item.getTag());
            case BLACK_BANNER, BLACK_WALL_BANNER, BLUE_BANNER, BLUE_WALL_BANNER, BROWN_BANNER, BROWN_WALL_BANNER, CYAN_BANNER, CYAN_WALL_BANNER, GRAY_BANNER, GRAY_WALL_BANNER, GREEN_BANNER, GREEN_WALL_BANNER, LIGHT_BLUE_BANNER, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_BANNER, LIME_WALL_BANNER, MAGENTA_BANNER, MAGENTA_WALL_BANNER, ORANGE_BANNER, ORANGE_WALL_BANNER, PINK_BANNER, PINK_WALL_BANNER, PURPLE_BANNER, PURPLE_WALL_BANNER, RED_BANNER, RED_WALL_BANNER, WHITE_BANNER, WHITE_WALL_BANNER, YELLOW_BANNER, YELLOW_WALL_BANNER ->
                    new CraftMetaBanner(item.getTag());
            case ALLAY_SPAWN_EGG, AXOLOTL_SPAWN_EGG, BAT_SPAWN_EGG, BEE_SPAWN_EGG, BLAZE_SPAWN_EGG, CAT_SPAWN_EGG, CAMEL_SPAWN_EGG, CAVE_SPIDER_SPAWN_EGG, CHICKEN_SPAWN_EGG, COD_SPAWN_EGG, COW_SPAWN_EGG, CREEPER_SPAWN_EGG, DOLPHIN_SPAWN_EGG, DONKEY_SPAWN_EGG, DROWNED_SPAWN_EGG, ELDER_GUARDIAN_SPAWN_EGG, ENDER_DRAGON_SPAWN_EGG, ENDERMAN_SPAWN_EGG, ENDERMITE_SPAWN_EGG, EVOKER_SPAWN_EGG, FOX_SPAWN_EGG, FROG_SPAWN_EGG, GHAST_SPAWN_EGG, GLOW_SQUID_SPAWN_EGG, GOAT_SPAWN_EGG, GUARDIAN_SPAWN_EGG, HOGLIN_SPAWN_EGG, HORSE_SPAWN_EGG, HUSK_SPAWN_EGG, IRON_GOLEM_SPAWN_EGG, LLAMA_SPAWN_EGG, MAGMA_CUBE_SPAWN_EGG, MOOSHROOM_SPAWN_EGG, MULE_SPAWN_EGG, OCELOT_SPAWN_EGG, PANDA_SPAWN_EGG, PARROT_SPAWN_EGG, PHANTOM_SPAWN_EGG, PIGLIN_BRUTE_SPAWN_EGG, PIGLIN_SPAWN_EGG, PIG_SPAWN_EGG, PILLAGER_SPAWN_EGG, POLAR_BEAR_SPAWN_EGG, PUFFERFISH_SPAWN_EGG, RABBIT_SPAWN_EGG, RAVAGER_SPAWN_EGG, SALMON_SPAWN_EGG, SHEEP_SPAWN_EGG, SHULKER_SPAWN_EGG, SILVERFISH_SPAWN_EGG, SKELETON_HORSE_SPAWN_EGG, SKELETON_SPAWN_EGG, SLIME_SPAWN_EGG, SNIFFER_SPAWN_EGG, SNOW_GOLEM_SPAWN_EGG, SPIDER_SPAWN_EGG, SQUID_SPAWN_EGG, STRAY_SPAWN_EGG, STRIDER_SPAWN_EGG, TADPOLE_SPAWN_EGG, TRADER_LLAMA_SPAWN_EGG, TROPICAL_FISH_SPAWN_EGG, TURTLE_SPAWN_EGG, VEX_SPAWN_EGG, VILLAGER_SPAWN_EGG, VINDICATOR_SPAWN_EGG, WANDERING_TRADER_SPAWN_EGG, WARDEN_SPAWN_EGG, WITCH_SPAWN_EGG, WITHER_SKELETON_SPAWN_EGG, WITHER_SPAWN_EGG, WOLF_SPAWN_EGG, ZOGLIN_SPAWN_EGG, ZOMBIE_HORSE_SPAWN_EGG, ZOMBIE_SPAWN_EGG, ZOMBIE_VILLAGER_SPAWN_EGG, ZOMBIFIED_PIGLIN_SPAWN_EGG ->
                    new CraftMetaSpawnEgg(item.getTag());
            case ARMOR_STAND -> new CraftMetaArmorStand(item.getTag());
            case KNOWLEDGE_BOOK -> new CraftMetaKnowledgeBook(item.getTag());
            case FURNACE, CHEST, TRAPPED_CHEST, JUKEBOX, DISPENSER, DROPPER, ACACIA_HANGING_SIGN, ACACIA_SIGN, ACACIA_WALL_HANGING_SIGN, ACACIA_WALL_SIGN, BAMBOO_HANGING_SIGN, BAMBOO_SIGN, BAMBOO_WALL_HANGING_SIGN, BAMBOO_WALL_SIGN, BIRCH_HANGING_SIGN, BIRCH_SIGN, BIRCH_WALL_HANGING_SIGN, BIRCH_WALL_SIGN, CHERRY_HANGING_SIGN, CHERRY_SIGN, CHERRY_WALL_HANGING_SIGN, CHERRY_WALL_SIGN, CRIMSON_HANGING_SIGN, CRIMSON_SIGN, CRIMSON_WALL_HANGING_SIGN, CRIMSON_WALL_SIGN, DARK_OAK_HANGING_SIGN, DARK_OAK_SIGN, DARK_OAK_WALL_HANGING_SIGN, DARK_OAK_WALL_SIGN, JUNGLE_HANGING_SIGN, JUNGLE_SIGN, JUNGLE_WALL_HANGING_SIGN, JUNGLE_WALL_SIGN, MANGROVE_HANGING_SIGN, MANGROVE_SIGN, MANGROVE_WALL_HANGING_SIGN, MANGROVE_WALL_SIGN, OAK_HANGING_SIGN, OAK_SIGN, OAK_WALL_HANGING_SIGN, OAK_WALL_SIGN, SPRUCE_HANGING_SIGN, SPRUCE_SIGN, SPRUCE_WALL_HANGING_SIGN, SPRUCE_WALL_SIGN, WARPED_HANGING_SIGN, WARPED_SIGN, WARPED_WALL_HANGING_SIGN, WARPED_WALL_SIGN, SPAWNER, BREWING_STAND, ENCHANTING_TABLE, COMMAND_BLOCK, REPEATING_COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, BEACON, DAYLIGHT_DETECTOR, HOPPER, COMPARATOR, SHIELD, STRUCTURE_BLOCK, SHULKER_BOX, WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX, GRAY_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX, ENDER_CHEST, BARREL, BELL, BLAST_FURNACE, CAMPFIRE, SOUL_CAMPFIRE, JIGSAW, LECTERN, SMOKER, BEEHIVE, BEE_NEST, SCULK_CATALYST, SCULK_SHRIEKER, SCULK_SENSOR, CALIBRATED_SCULK_SENSOR, CHISELED_BOOKSHELF, DECORATED_POT, SUSPICIOUS_SAND, SUSPICIOUS_GRAVEL ->
                    new CraftMetaBlockState(item.getTag(), CraftMagicNumbers.getMaterial(item.getItem()));
            case TROPICAL_FISH_BUCKET -> new CraftMetaTropicalFishBucket(item.getTag());
            case AXOLOTL_BUCKET -> new CraftMetaAxolotlBucket(item.getTag());
            case CROSSBOW -> new CraftMetaCrossbow(item.getTag());
            case SUSPICIOUS_STEW -> new CraftMetaSuspiciousStew(item.getTag());
            case COD_BUCKET, PUFFERFISH_BUCKET, SALMON_BUCKET, ITEM_FRAME, GLOW_ITEM_FRAME, PAINTING ->
                    new CraftMetaEntityTag(item.getTag());
            case COMPASS -> new CraftMetaCompass(item.getTag());
            case BUNDLE -> new CraftMetaBundle(item.getTag());
            case GOAT_HORN -> new CraftMetaMusicInstrument(item.getTag());
            default -> new CraftMetaItem(item.getTag());
        };
        CompoundTag tag = item.getTag();
        if (tag != null)
            meta.offerUnhandledTags(tag);
        meta.setForgeCaps(item.getForgeCaps());
        return meta;
    }

    static Material getType(net.minecraft.world.item.ItemStack item) {
        return item == null ? Material.AIR : CraftMagicNumbers.getMaterial(item.getItem());
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        return setItemMeta(handle, itemMeta);
    }

    public static boolean setItemMeta(net.minecraft.world.item.ItemStack item, ItemMeta itemMeta) {
        if (item == null) {
            return false;
        }
        if (CraftItemFactory.instance().equals(itemMeta, null)) {
            item.setTag(null);
            return true;
        }
        if (!CraftItemFactory.instance().isApplicable(itemMeta, getType(item))) {
            return false;
        }

        itemMeta = CraftItemFactory.instance().asMetaFor(itemMeta, getType(item));
        if (itemMeta == null) return true;

        Item oldItem = item.getItem();
        Item newItem = CraftMagicNumbers.getItem(CraftItemFactory.instance().updateMaterial(itemMeta, CraftMagicNumbers.getMaterial(oldItem)));
        if (oldItem != newItem) {
            item.setItem(newItem);
        }

        CompoundTag tag = new CompoundTag();
        item.setTag(tag);

        ((CraftMetaItem) itemMeta).applyToItem(tag);
        item.convertStack(((CraftMetaItem) itemMeta).getVersion());
        CompoundTag forgeCaps = ((CraftMetaItem) itemMeta).getForgeCaps();
        if (forgeCaps != null)
            item.setForgeCaps(forgeCaps.copy());
        // SpigotCraft#463 this is required now by the Vanilla client, so mimic ItemStack constructor in ensuring it
        if (item.getItem() != null && item.getItem().canBeDepleted()) {
            item.setDamageValue(item.getDamageValue());
        }

        return true;
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        if (!(stack instanceof CraftItemStack that)) {
            return stack.getClass() == ItemStack.class && stack.isSimilar(this);
        }

        if (handle == that.handle) {
            return true;
        }
        if (handle == null || that.handle == null) {
            return false;
        }
        Material comparisonType = CraftLegacy.fromLegacy(that.getType()); // This may be called from legacy item stacks, try to get the right material
        if (!(comparisonType == getType() && getDurability() == that.getDurability())) {
            return false;
        }
        return hasItemMeta() ? (that.hasItemMeta() && Objects.equals(handle.getTag(), that.handle.getTag()) && Objects.equals(handle.getForgeCaps(), that.handle.getForgeCaps())) : !that.hasItemMeta();
    }

    @Override
    public boolean hasItemMeta() {
        return hasItemMeta(handle) && !CraftItemFactory.instance().equals(getItemMeta(), null);
    }

    static boolean hasItemMeta(net.minecraft.world.item.ItemStack item) {
        if (item != null) {
            CompoundTag forgeCaps = item.getForgeCaps();
            if (forgeCaps != null && !forgeCaps.isEmpty()) return true;
        }
        return !(item == null || item.getTag() == null || item.getTag().isEmpty());
    }
}
