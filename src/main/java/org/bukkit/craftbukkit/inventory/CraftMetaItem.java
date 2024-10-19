package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.mojang.serialization.DynamicOps;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.Overridden;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.ItemMetaKey.Specific;
import org.bukkit.craftbukkit.inventory.components.CraftFoodComponent;
import org.bukkit.craftbukkit.inventory.components.CraftJukeboxComponent;
import org.bukkit.craftbukkit.inventory.components.CraftToolComponent;
import org.bukkit.craftbukkit.inventory.tags.DeprecatedCustomTagContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNBTTagConfigSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;

/**
 * Children must include the following:
 *
 * <li> Constructor(CraftMetaItem meta)
 * <li> Constructor(NBTTagCompound tag)
 * <li> Constructor(Map&lt;String, Object&gt; map)
 * <br><br>
 * <li> void applyToItem(CraftMetaItem.Applicator tag)
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
 * <li> Builder&lt;String, Object&gt; serialize(Builder&lt;String, Object&gt; builder)
 * <li> SerializableMeta.Deserializers deserializer()
 */
@DelegateDeserialization(SerializableMeta.class)
class CraftMetaItem implements ItemMeta, Damageable, Repairable, BlockDataMeta {

    static class ItemMetaKey {

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.FIELD)
        @interface Specific {
            enum To {
                BUKKIT,
                NBT,
                ;
            }
            To value();
        }

        final String BUKKIT;
        final String NBT;

        ItemMetaKey(final String both) {
            this(both, both);
        }

        ItemMetaKey(final String nbt, final String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }
    }

    static final class ItemMetaKeyType<T> extends ItemMetaKey {

        final DataComponentType<T> TYPE;

        ItemMetaKeyType(final DataComponentType<T> type) {
            this(type, null, null);
        }

        ItemMetaKeyType(final DataComponentType<T> type, final String both) {
            this(type, both, both);
        }

        ItemMetaKeyType(final DataComponentType<T> type, final String nbt, final String bukkit) {
            super(nbt, bukkit);
            this.TYPE = type;
        }
    }

    static final class Applicator {

        private final DataComponentPatch.Builder builder = DataComponentPatch.builder();

        <T> Applicator put(ItemMetaKeyType<T> key, T value) {
            this.builder.set(key.TYPE, value);
            return this;
        }

        <T> Applicator putIfAbsent(TypedDataComponent<?> component) {
            if (!builder.isSet(component.type())) {
                builder.set(component);
            }
            return this;
        }

        DataComponentPatch build() {
            return this.builder.build();
        }
    }

    static final ItemMetaKeyType<Component> NAME = new ItemMetaKeyType(DataComponents.CUSTOM_NAME, "display-name");
    static final ItemMetaKeyType<Component> ITEM_NAME = new ItemMetaKeyType(DataComponents.ITEM_NAME, "item-name");
    static final ItemMetaKeyType<ItemLore> LORE = new ItemMetaKeyType<>(DataComponents.LORE, "lore");
    static final ItemMetaKeyType<CustomModelData> CUSTOM_MODEL_DATA = new ItemMetaKeyType<>(DataComponents.CUSTOM_MODEL_DATA, "custom-model-data");
    static final ItemMetaKeyType<ItemEnchantments> ENCHANTMENTS = new ItemMetaKeyType<>(DataComponents.ENCHANTMENTS, "enchants");
    static final ItemMetaKeyType<Integer> REPAIR = new ItemMetaKeyType<>(DataComponents.REPAIR_COST, "repair-cost");
    static final ItemMetaKeyType<ItemAttributeModifiers> ATTRIBUTES = new ItemMetaKeyType<>(DataComponents.ATTRIBUTE_MODIFIERS, "attribute-modifiers");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_SLOT = new ItemMetaKey("Slot");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("ItemFlags");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Unit> HIDE_TOOLTIP = new ItemMetaKeyType<>(DataComponents.HIDE_TOOLTIP, "hide-tool-tip");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Unbreakable> UNBREAKABLE = new ItemMetaKeyType<>(DataComponents.UNBREAKABLE, "Unbreakable");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Boolean> ENCHANTMENT_GLINT_OVERRIDE = new ItemMetaKeyType<>(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, "enchantment-glint-override");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Unit> FIRE_RESISTANT = new ItemMetaKeyType<>(DataComponents.FIRE_RESISTANT, "fire-resistant");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Integer> MAX_STACK_SIZE = new ItemMetaKeyType<>(DataComponents.MAX_STACK_SIZE, "max-stack-size");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Rarity> RARITY = new ItemMetaKeyType<>(DataComponents.RARITY, "rarity");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<FoodProperties> FOOD = new ItemMetaKeyType<>(DataComponents.FOOD, "food");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Tool> TOOL = new ItemMetaKeyType<>(DataComponents.TOOL, "tool");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<JukeboxPlayable> JUKEBOX_PLAYABLE = new ItemMetaKeyType<>(DataComponents.JUKEBOX_PLAYABLE, "jukebox-playable");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Integer> DAMAGE = new ItemMetaKeyType<>(DataComponents.DAMAGE, "Damage");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Integer> MAX_DAMAGE = new ItemMetaKeyType<>(DataComponents.MAX_DAMAGE, "max-damage");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<BlockItemStateProperties> BLOCK_DATA = new ItemMetaKeyType<>(DataComponents.BLOCK_STATE, "BlockStateTag");
    static final ItemMetaKey BUKKIT_CUSTOM_TAG = new ItemMetaKey("PublicBukkitValues");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Unit> HIDE_ADDITIONAL_TOOLTIP = new ItemMetaKeyType(DataComponents.HIDE_ADDITIONAL_TOOLTIP);
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<CustomData> CUSTOM_DATA = new ItemMetaKeyType<>(DataComponents.CUSTOM_DATA);

    // We store the raw original JSON representation of all text data. See SPIGOT-5063, SPIGOT-5656, SPIGOT-5304
    private Component displayName;
    private Component itemName;
    private List<Component> lore; // null and empty are two different states internally
    private Integer customModelData;
    private Map<String, String> blockData;
    private Map<Enchantment, Integer> enchantments;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;
    private int repairCost;
    private int hideFlag;
    private boolean hideTooltip;
    private boolean unbreakable;
    private Boolean enchantmentGlintOverride;
    private boolean fireResistant;
    private Integer maxStackSize;
    private ItemRarity rarity;
    private CraftFoodComponent food;
    private CraftToolComponent tool;
    private CraftJukeboxComponent jukebox;
    private int damage;
    private Integer maxDamage;

    private static final Set<DataComponentType> HANDLED_TAGS = Sets.newHashSet();
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    private CompoundTag customTag;
    protected DataComponentPatch.Builder unhandledTags = DataComponentPatch.builder();
    private Set<DataComponentType<?>> removedTags = Sets.newHashSet();
    private CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(CraftMetaItem.DATA_TYPE_REGISTRY);

    private int version = CraftMagicNumbers.INSTANCE.getDataVersion(); // Internal use only

    CraftMetaItem(CraftMetaItem meta) {
        if (meta == null) {
            return;
        }

        this.displayName = meta.displayName;
        this.itemName = meta.itemName;

        if (meta.lore != null) {
            this.lore = new ArrayList<Component>(meta.lore);
        }

        this.customModelData = meta.customModelData;
        this.blockData = meta.blockData;

        if (meta.enchantments != null) {
            this.enchantments = new LinkedHashMap<Enchantment, Integer>(meta.enchantments);
        }

        if (meta.hasAttributeModifiers()) {
            this.attributeModifiers = LinkedHashMultimap.create(meta.attributeModifiers);
        }

        this.repairCost = meta.repairCost;
        this.hideFlag = meta.hideFlag;
        this.hideTooltip = meta.hideTooltip;
        this.unbreakable = meta.unbreakable;
        this.enchantmentGlintOverride = meta.enchantmentGlintOverride;
        this.fireResistant = meta.fireResistant;
        this.maxStackSize = meta.maxStackSize;
        this.rarity = meta.rarity;
        if (meta.hasFood()) {
            this.food = new CraftFoodComponent(meta.food);
        }
        if (meta.hasTool()) {
            this.tool = new CraftToolComponent(meta.tool);
        }
        if (meta.hasJukeboxPlayable()) {
            this.jukebox = new CraftJukeboxComponent(meta.jukebox);
        }
        this.damage = meta.damage;
        this.maxDamage = meta.maxDamage;
        this.unhandledTags.copy(meta.unhandledTags.build());
        this.removedTags.addAll(meta.removedTags);
        this.persistentDataContainer.putAll(meta.persistentDataContainer.getRaw());

        this.customTag = meta.customTag;

        this.version = meta.version;
    }

    CraftMetaItem(DataComponentPatch tag) {
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.NAME).ifPresent((component) -> {
            this.displayName = component;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ITEM_NAME).ifPresent((component) -> {
            this.itemName = component;
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.LORE).ifPresent((l) -> {
            List<Component> list = l.lines();
            this.lore = new ArrayList<Component>(list.size());
            for (int index = 0; index < list.size(); index++) {
                Component line = list.get(index);
                this.lore.add(line);
            }
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.CUSTOM_MODEL_DATA).ifPresent((i) -> {
            this.customModelData = i.value();
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.BLOCK_DATA).ifPresent((t) -> {
            this.blockData = t.properties();
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ENCHANTMENTS).ifPresent((en) -> {
            this.enchantments = CraftMetaItem.buildEnchantments(en);
            if (!en.showInTooltip) {
                this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ATTRIBUTES).ifPresent((en) -> {
            this.attributeModifiers = CraftMetaItem.buildModifiers(en);
            if (!en.showInTooltip()) {
                this.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.REPAIR).ifPresent((i) -> {
            this.repairCost = i;
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.HIDE_ADDITIONAL_TOOLTIP).ifPresent((h) -> {
            this.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.HIDE_TOOLTIP).ifPresent((u) -> {
            this.hideTooltip = true;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.UNBREAKABLE).ifPresent((u) -> {
            this.unbreakable = true;
            if (!u.showInTooltip()) {
                this.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE).ifPresent((override) -> {
            this.enchantmentGlintOverride = override;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.FIRE_RESISTANT).ifPresent((u) -> {
            this.fireResistant = true;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.MAX_STACK_SIZE).ifPresent((i) -> {
            this.maxStackSize = i;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.RARITY).ifPresent((enumItemRarity) -> {
            this.rarity = ItemRarity.valueOf(enumItemRarity.name());
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.FOOD).ifPresent((foodInfo) -> {
            this.food = new CraftFoodComponent(foodInfo);
        });
        getOrEmpty(tag, TOOL).ifPresent((toolInfo) -> {
            tool = new CraftToolComponent(toolInfo);
        });
        getOrEmpty(tag, JUKEBOX_PLAYABLE).ifPresent((jukeboxPlayable) -> {
            jukebox = new CraftJukeboxComponent(jukeboxPlayable);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.DAMAGE).ifPresent((i) -> {
            this.damage = i;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.MAX_DAMAGE).ifPresent((i) -> {
            this.maxDamage = i;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.CUSTOM_DATA).ifPresent((customData) -> {
            this.customTag = customData.copyTag();
            if (this.customTag.contains(CraftMetaItem.BUKKIT_CUSTOM_TAG.NBT)) {
                CompoundTag compound = this.customTag.getCompound(CraftMetaItem.BUKKIT_CUSTOM_TAG.NBT);
                Set<String> keys = compound.getAllKeys();
                for (String key : keys) {
                    this.persistentDataContainer.put(key, compound.get(key).copy());
                }
                customTag.remove(BUKKIT_CUSTOM_TAG.NBT);
            }

            if (customTag.isEmpty()) {
                customTag = null;
            }
        });

        Set<Map.Entry<DataComponentType<?>, Optional<?>>> keys = tag.entrySet();
        for (Map.Entry<DataComponentType<?>, Optional<?>> key : keys) {
            if (!CraftMetaItem.getHandledTags().contains(key.getKey())) {
                key.getValue().ifPresent((value) -> {
                    this.unhandledTags.set((DataComponentType) key.getKey(), value);
                });
            }

            if (key.getValue().isEmpty()) {
                removedTags.add(key.getKey());
            }
        }
    }

    static Map<Enchantment, Integer> buildEnchantments(ItemEnchantments tag) {
        Map<Enchantment, Integer> enchantments = new LinkedHashMap<Enchantment, Integer>(tag.size());

        tag.entrySet().forEach((entry) -> {
            Holder<net.minecraft.world.item.enchantment.Enchantment> id = entry.getKey();
            int level = entry.getIntValue();

            Enchantment enchant = CraftEnchantment.minecraftHolderToBukkit(id);
            if (enchant != null) {
                enchantments.put(enchant, level);
            }
        });

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(ItemAttributeModifiers tag) {
        Multimap<Attribute, AttributeModifier> modifiers = LinkedHashMultimap.create();
        List<ItemAttributeModifiers.Entry> mods = tag.modifiers();
        int size = mods.size();

        for (int i = 0; i < size; i++) {
            ItemAttributeModifiers.Entry entry = mods.get(i);
            net.minecraft.world.entity.ai.attributes.AttributeModifier nmsModifier = entry.modifier();
            if (nmsModifier == null) {
                continue;
            }

            AttributeModifier attribMod = CraftAttributeInstance.convert(nmsModifier);

            Attribute attribute = CraftAttribute.minecraftHolderToBukkit(entry.attribute());
            if (attribute == null) {
                continue;
            }

            if (entry.slot() != null) {
                EquipmentSlotGroup slotName = entry.slot();
                if (slotName == null) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                org.bukkit.inventory.EquipmentSlotGroup slot = null;
                try {
                    slot = CraftEquipmentSlot.getSlot(slotName);
                } catch (IllegalArgumentException ex) {
                    // SPIGOT-4551 - Slot is invalid, should really match nothing but this is undefined behaviour anyway
                }

                if (slot == null) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                attribMod = new AttributeModifier(attribMod.getKey(), attribMod.getAmount(), attribMod.getOperation(), slot);
            }
            modifiers.put(attribute, attribMod);
        }
        return modifiers;
    }

    CraftMetaItem(Map<String, Object> map) {
        this.displayName = CraftChatMessage.fromJSONOrString(SerializableMeta.getString(map, CraftMetaItem.NAME.BUKKIT, true), true, false);
        this.itemName = CraftChatMessage.fromJSONOrNull(SerializableMeta.getString(map, CraftMetaItem.ITEM_NAME.BUKKIT, true));

        Iterable<?> lore = SerializableMeta.getObject(Iterable.class, map, CraftMetaItem.LORE.BUKKIT, true);
        if (lore != null) {
            CraftMetaItem.safelyAdd(lore, this.lore = new ArrayList<Component>(), true);
        }

        Integer customModelData = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.CUSTOM_MODEL_DATA.BUKKIT, true);
        if (customModelData != null) {
            this.setCustomModelData(customModelData);
        }

        Object blockData = SerializableMeta.getObject(Object.class, map, CraftMetaItem.BLOCK_DATA.BUKKIT, true);
        if (blockData != null) {
            Map<String, String> mapBlockData = new HashMap<>();

            if (blockData instanceof Map) {
                for (Entry<?, ?> entry : ((Map<?, ?>) blockData).entrySet()) {
                    mapBlockData.put(entry.getKey().toString(), entry.getValue().toString());
                }
            } else {
                // Legacy pre 1.20.5:
                CompoundTag nbtBlockData = (CompoundTag) CraftNBTTagConfigSerializer.deserialize(blockData);
                for (String key : nbtBlockData.getAllKeys()) {
                    mapBlockData.put(key, nbtBlockData.getString(key));
                }
            }

            this.blockData = mapBlockData;
        }

        this.enchantments = CraftMetaItem.buildEnchantments(map, CraftMetaItem.ENCHANTMENTS);
        this.attributeModifiers = CraftMetaItem.buildModifiers(map, CraftMetaItem.ATTRIBUTES);

        Integer repairCost = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.REPAIR.BUKKIT, true);
        if (repairCost != null) {
            this.setRepairCost(repairCost);
        }

        Iterable<?> hideFlags = SerializableMeta.getObject(Iterable.class, map, CraftMetaItem.HIDEFLAGS.BUKKIT, true);
        if (hideFlags != null) {
            for (Object hideFlagObject : hideFlags) {
                String hideFlagString = (String) hideFlagObject;
                try {
                    ItemFlag hideFlatEnum = CraftItemFlag.stringToBukkit(hideFlagString);
                    this.addItemFlags(hideFlatEnum);
                } catch (IllegalArgumentException ex) {
                    // Ignore when we got a old String which does not map to a Enum value anymore
                }
            }
        }

        Boolean hideTooltip = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.HIDE_TOOLTIP.BUKKIT, true);
        if (hideTooltip != null) {
            this.setHideTooltip(hideTooltip);
        }

        Boolean unbreakable = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.UNBREAKABLE.BUKKIT, true);
        if (unbreakable != null) {
            this.setUnbreakable(unbreakable);
        }

        Boolean enchantmentGlintOverride = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE.BUKKIT, true);
        if (enchantmentGlintOverride != null) {
            this.setEnchantmentGlintOverride(enchantmentGlintOverride);
        }

        Boolean fireResistant = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.FIRE_RESISTANT.BUKKIT, true);
        if (fireResistant != null) {
            this.setFireResistant(fireResistant);
        }

        Integer maxStackSize = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.MAX_STACK_SIZE.BUKKIT, true);
        if (maxStackSize != null) {
            this.setMaxStackSize(maxStackSize);
        }

        String rarity = SerializableMeta.getString(map, CraftMetaItem.RARITY.BUKKIT, true);
        if (rarity != null) {
            this.setRarity(ItemRarity.valueOf(rarity));
        }

        CraftFoodComponent food = SerializableMeta.getObject(CraftFoodComponent.class, map, CraftMetaItem.FOOD.BUKKIT, true);
        if (food != null) {
            this.setFood(food);
        }

        CraftToolComponent tool = SerializableMeta.getObject(CraftToolComponent.class, map, TOOL.BUKKIT, true);
        if (tool != null) {
            setTool(tool);
        }

        CraftJukeboxComponent jukeboxPlayable = SerializableMeta.getObject(CraftJukeboxComponent.class, map, JUKEBOX_PLAYABLE.BUKKIT, true);
        if (jukeboxPlayable != null) {
            setJukeboxPlayable(jukeboxPlayable);
        }

        Integer damage = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.DAMAGE.BUKKIT, true);
        if (damage != null) {
            this.setDamage(damage);
        }

        Integer maxDamage = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.MAX_DAMAGE.BUKKIT, true);
        if (maxDamage != null) {
            this.setMaxDamage(maxDamage);
        }

        String internal = SerializableMeta.getString(map, "internal", true);
        if (internal != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(internal));
            try {
                CompoundTag internalTag = NbtIo.readCompressed(buf, NbtAccounter.unlimitedHeap());
                this.deserializeInternal(internalTag, map);
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String unhandled = SerializableMeta.getString(map, "unhandled", true);
        if (unhandled != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(unhandled));
            try {
                CompoundTag unhandledTag = NbtIo.readCompressed(buf, NbtAccounter.unlimitedHeap());
                DataComponentPatch unhandledPatch = DataComponentPatch.CODEC.parse(MinecraftServer.getDefaultRegistryAccess().createSerializationContext(NbtOps.INSTANCE), unhandledTag).result().get();
                this.unhandledTags.copy(unhandledPatch);

                for (Entry<DataComponentType<?>, Optional<?>> entry : unhandledPatch.entrySet()) {
                    // Move removed unhandled tags to dedicated removedTags
                    if (!entry.getValue().isPresent()) {
                        DataComponentType<?> key = entry.getKey();

                        this.unhandledTags.clear(key);
                        this.removedTags.add(key);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Iterable<?> removed = SerializableMeta.getObject(Iterable.class, map, "removed", true);
        if (removed != null) {
            net.minecraft.core.RegistryAccess registryAccess = CraftRegistry.getMinecraftRegistry();
            net.minecraft.core.Registry<DataComponentType<?>> componentTypeRegistry = registryAccess.registryOrThrow(Registries.DATA_COMPONENT_TYPE);

            for (Object removedObject : removed) {
                String removedString = (String) removedObject;

                DataComponentType<?> component = componentTypeRegistry.get(ResourceLocation.parse(removedString));
                if (component != null) {
                    this.removedTags.add(component);
                }
            }
        }

        Object nbtMap = SerializableMeta.getObject(Object.class, map, CraftMetaItem.BUKKIT_CUSTOM_TAG.BUKKIT, true); // We read both legacy maps and potential modern snbt strings here
        if (nbtMap != null) {
            this.persistentDataContainer.putAll((CompoundTag) CraftNBTTagConfigSerializer.deserialize(nbtMap));
        }

        String custom = SerializableMeta.getString(map, "custom", true);
        if (custom != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(custom));
            try {
                this.customTag = NbtIo.readCompressed(buf, NbtAccounter.unlimitedHeap());
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void deserializeInternal(CompoundTag tag, Object context) {
        // SPIGOT-4576: Need to migrate from internal to proper data
        if (tag.contains(CraftMetaItem.ATTRIBUTES.NBT, CraftMagicNumbers.NBT.TAG_LIST)) {
            this.attributeModifiers = CraftMetaItem.buildModifiersLegacy(tag, CraftMetaItem.ATTRIBUTES);
        }
    }

    private static Multimap<Attribute, AttributeModifier> buildModifiersLegacy(CompoundTag tag, ItemMetaKey key) {
        Multimap<Attribute, AttributeModifier> modifiers = LinkedHashMultimap.create();
        if (!tag.contains(key.NBT, CraftMagicNumbers.NBT.TAG_LIST)) {
            return modifiers;
        }
        ListTag mods = tag.getList(key.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
        int size = mods.size();

        for (int i = 0; i < size; i++) {
            CompoundTag entry = mods.getCompound(i);
            if (entry.isEmpty()) {
                // entry is not an actual NBTTagCompound. getCompound returns empty NBTTagCompound in that case
                continue;
            }
            net.minecraft.world.entity.ai.attributes.AttributeModifier nmsModifier = net.minecraft.world.entity.ai.attributes.AttributeModifier.load(entry);
            if (nmsModifier == null) {
                continue;
            }

            AttributeModifier attribMod = CraftAttributeInstance.convert(nmsModifier);

            String attributeName = entry.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT);
            if (attributeName == null || attributeName.isEmpty()) {
                continue;
            }

            Attribute attribute = CraftAttribute.stringToBukkit(attributeName);
            if (attribute == null) {
                continue;
            }

            if (entry.contains(CraftMetaItem.ATTRIBUTES_SLOT.NBT, CraftMagicNumbers.NBT.TAG_STRING)) {
                String slotName = entry.getString(CraftMetaItem.ATTRIBUTES_SLOT.NBT);
                if (slotName == null || slotName.isEmpty()) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                EquipmentSlot slot = null;
                try {
                    slot = CraftEquipmentSlot.getSlot(net.minecraft.world.entity.EquipmentSlot.byName(slotName.toLowerCase(Locale.ROOT)));
                } catch (IllegalArgumentException ex) {
                    // SPIGOT-4551 - Slot is invalid, should really match nothing but this is undefined behaviour anyway
                }

                if (slot == null) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                attribMod = new AttributeModifier(attribMod.getKey(), attribMod.getAmount(), attribMod.getOperation(), slot.getGroup());
            }
            modifiers.put(attribute, attribMod);
        }
        return modifiers;
    }

    static Map<Enchantment, Integer> buildEnchantments(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> ench = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }

        Map<Enchantment, Integer> enchantments = new LinkedHashMap<Enchantment, Integer>(ench.size());
        for (Map.Entry<?, ?> entry : ench.entrySet()) {
            Enchantment enchantment = CraftEnchantment.stringToBukkit(entry.getKey().toString());
            if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                enchantments.put(enchantment, (Integer) entry.getValue());
            }
        }

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> mods = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        Multimap<Attribute, AttributeModifier> result = LinkedHashMultimap.create();
        if (mods == null) {
            return result;
        }

        for (Object obj : mods.keySet()) {
            if (!(obj instanceof String)) {
                continue;
            }
            String attributeName = (String) obj;
            if (Strings.isNullOrEmpty(attributeName)) {
                continue;
            }
            List<?> list = SerializableMeta.getObject(List.class, mods, attributeName, true);
            if (list == null || list.isEmpty()) {
                return result;
            }

            for (Object o : list) {
                if (!(o instanceof AttributeModifier)) { // this catches null
                    continue;
                }
                AttributeModifier modifier = (AttributeModifier) o;
                Attribute attribute = CraftAttribute.stringToBukkit(attributeName);
                if (attribute == null) {
                    continue;
                }

                result.put(attribute, modifier);
            }
        }
        return result;
    }

    @Overridden
    void applyToItem(CraftMetaItem.Applicator itemTag) {
        if (this.hasDisplayName()) {
            itemTag.put(CraftMetaItem.NAME, this.displayName);
        }

        if (this.hasItemName()) {
            itemTag.put(CraftMetaItem.ITEM_NAME, this.itemName);
        }

        if (this.lore != null) {
            itemTag.put(CraftMetaItem.LORE, new ItemLore(this.lore));
        }

        if (this.hasCustomModelData()) {
            itemTag.put(CraftMetaItem.CUSTOM_MODEL_DATA, new CustomModelData(this.customModelData));
        }

        if (this.hasBlockData()) {
            itemTag.put(CraftMetaItem.BLOCK_DATA, new BlockItemStateProperties(this.blockData));
        }

        if (this.hideFlag != 0) {
            if (this.hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)) {
                itemTag.put(CraftMetaItem.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
            }
        }

        this.applyEnchantments(this.enchantments, itemTag, CraftMetaItem.ENCHANTMENTS, ItemFlag.HIDE_ENCHANTS);
        this.applyModifiers(this.attributeModifiers, itemTag);

        if (this.hasRepairCost()) {
            itemTag.put(CraftMetaItem.REPAIR, this.repairCost);
        }

        if (this.isHideTooltip()) {
            itemTag.put(CraftMetaItem.HIDE_TOOLTIP, Unit.INSTANCE);
        }

        if (this.isUnbreakable()) {
            itemTag.put(CraftMetaItem.UNBREAKABLE, new Unbreakable(!this.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)));
        }

        if (this.hasEnchantmentGlintOverride()) {
            itemTag.put(CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE, this.getEnchantmentGlintOverride());
        }

        if (this.isFireResistant()) {
            itemTag.put(CraftMetaItem.FIRE_RESISTANT, Unit.INSTANCE);
        }

        if (this.hasMaxStackSize()) {
            itemTag.put(CraftMetaItem.MAX_STACK_SIZE, this.maxStackSize);
        }

        if (this.hasRarity()) {
            itemTag.put(CraftMetaItem.RARITY, Rarity.valueOf(this.rarity.name()));
        }

        if (this.hasFood()) {
            itemTag.put(CraftMetaItem.FOOD, this.food.getHandle());
        }

        if (hasTool()) {
            itemTag.put(TOOL, tool.getHandle());
        }

        if (hasJukeboxPlayable()) {
            itemTag.put(JUKEBOX_PLAYABLE, jukebox.getHandle());
        }

        if (this.hasDamage()) {
            itemTag.put(CraftMetaItem.DAMAGE, this.damage);
        }

        if (this.hasMaxDamage()) {
            itemTag.put(CraftMetaItem.MAX_DAMAGE, this.maxDamage);
        }

        for (Map.Entry<DataComponentType<?>, Optional<?>> e : this.unhandledTags.build().entrySet()) {
            e.getValue().ifPresent((value) -> {
                itemTag.builder.set((DataComponentType) e.getKey(), value);
            });
        }

        for (DataComponentType<?> removed : removedTags) {
            if (!itemTag.builder.isSet(removed)) {
                itemTag.builder.remove(removed);
            }
        }

        if (!this.persistentDataContainer.isEmpty()) {
            CompoundTag bukkitCustomCompound = new CompoundTag();
            Map<String, Tag> rawPublicMap = this.persistentDataContainer.getRaw();

            for (Map.Entry<String, Tag> nbtBaseEntry : rawPublicMap.entrySet()) {
                bukkitCustomCompound.put(nbtBaseEntry.getKey(), nbtBaseEntry.getValue());
            }

            if (this.customTag == null) {
                this.customTag = new CompoundTag();
            }
            this.customTag.put(CraftMetaItem.BUKKIT_CUSTOM_TAG.BUKKIT, bukkitCustomCompound);
        }

        if (this.customTag != null) {
            itemTag.put(CraftMetaItem.CUSTOM_DATA, CustomData.of(this.customTag));
        }
    }

    void applyEnchantments(Map<Enchantment, Integer> enchantments, CraftMetaItem.Applicator tag, ItemMetaKeyType<ItemEnchantments> key, ItemFlag itemFlag) {
        if (enchantments == null && !hasItemFlag(itemFlag)) {
            return;
        }

        ItemEnchantments.Mutable list = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

        if (enchantments != null) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                list.set(CraftEnchantment.bukkitToMinecraftHolder(entry.getKey()), entry.getValue());
            }
        }

        list.showInTooltip = !this.hasItemFlag(itemFlag);
        tag.put(key, list.toImmutable());
    }

    void applyModifiers(Multimap<Attribute, AttributeModifier> modifiers, CraftMetaItem.Applicator tag) {
        if (modifiers == null || modifiers.isEmpty()) {
            if (hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                tag.put(ATTRIBUTES, new ItemAttributeModifiers(Collections.emptyList(), false));
            }
            return;
        }

        ItemAttributeModifiers.Builder list = ItemAttributeModifiers.builder();
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            net.minecraft.world.entity.ai.attributes.AttributeModifier nmsModifier = CraftAttributeInstance.convert(entry.getValue());

            Holder<net.minecraft.world.entity.ai.attributes.Attribute> name = CraftAttribute.bukkitToMinecraftHolder(entry.getKey());
            if (name == null) {
                continue;
            }

            EquipmentSlotGroup group = CraftEquipmentSlot.getNMSGroup(entry.getValue().getSlotGroup());
            list.add(name, nmsModifier, group);
        }
        tag.put(CraftMetaItem.ATTRIBUTES, list.build().withTooltip(!this.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)));
    }

    @Overridden
    boolean applicableTo(Material type) {
        return type != Material.AIR;
    }

    @Overridden
    boolean isEmpty() {
        return !(hasDisplayName() || hasItemName() || hasLocalizedName() || hasEnchants() || (lore != null) || hasCustomModelData() || hasBlockData() || hasRepairCost() || !unhandledTags.build().isEmpty() || !removedTags.isEmpty() || !persistentDataContainer.isEmpty() || hideFlag != 0 || isHideTooltip() || isUnbreakable() || hasEnchantmentGlintOverride() || isFireResistant() || hasMaxStackSize() || hasRarity() || hasFood() || hasTool() || hasJukeboxPlayable() || hasDamage() || hasMaxDamage() || hasAttributeModifiers() || customTag != null);
    }

    @Override
    public String getDisplayName() {
        return CraftChatMessage.fromComponent(this.displayName);
    }

    @Override
    public final void setDisplayName(String name) {
        this.displayName = CraftChatMessage.fromStringOrNull(name);
    }

    @Override
    public boolean hasDisplayName() {
        return this.displayName != null;
    }

    @Override
    public String getItemName() {
        return CraftChatMessage.fromComponent(this.itemName);
    }

    @Override
    public final void setItemName(String name) {
        this.itemName = CraftChatMessage.fromStringOrNull(name);
    }

    @Override
    public boolean hasItemName() {
        return this.itemName != null;
    }

    @Override
    public String getLocalizedName() {
        return this.getDisplayName();
    }

    @Override
    public void setLocalizedName(String name) {
    }

    @Override
    public boolean hasLocalizedName() {
        return false;
    }

    @Override
    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    @Override
    public boolean hasRepairCost() {
        return this.repairCost > 0;
    }

    @Override
    public boolean hasEnchant(Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        return this.hasEnchants() && this.enchantments.containsKey(ench);
    }

    @Override
    public int getEnchantLevel(Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        Integer level = this.hasEnchants() ? this.enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return this.hasEnchants() ? ImmutableMap.copyOf(this.enchantments) : ImmutableMap.<Enchantment, Integer>of();
    }

    @Override
    public boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        if (this.enchantments == null) {
            this.enchantments = new LinkedHashMap<Enchantment, Integer>(4);
        }

        if (ignoreRestrictions || level >= ench.getStartLevel() && level <= ench.getMaxLevel()) {
            Integer old = this.enchantments.put(ench, level);
            return old == null || old != level;
        }
        return false;
    }

    @Override
    public boolean removeEnchant(Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        boolean enchantmentRemoved = this.hasEnchants() && this.enchantments.remove(ench) != null;
        // If we no longer have any enchantments, then clear enchantment tag
        if (enchantmentRemoved && this.enchantments.isEmpty()) {
            this.enchantments = null;
        }
        return enchantmentRemoved;
    }

    @Override
    public void removeEnchantments() {
        if (this.hasEnchants()) {
            this.enchantments.clear();
        }
    }

    @Override
    public boolean hasEnchants() {
        return !(this.enchantments == null || this.enchantments.isEmpty());
    }

    @Override
    public boolean hasConflictingEnchant(Enchantment ench) {
        return CraftMetaItem.checkConflictingEnchants(this.enchantments, ench);
    }

    @Override
    public void addItemFlags(ItemFlag... hideFlags) {
        for (ItemFlag f : hideFlags) {
            this.hideFlag |= this.getBitModifier(f);
        }
    }

    @Override
    public void removeItemFlags(ItemFlag... hideFlags) {
        for (ItemFlag f : hideFlags) {
            this.hideFlag &= ~this.getBitModifier(f);
        }
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);

        for (ItemFlag f : ItemFlag.values()) {
            if (this.hasItemFlag(f)) {
                currentFlags.add(f);
            }
        }

        return currentFlags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        int bitModifier = this.getBitModifier(flag);
        return (this.hideFlag & bitModifier) == bitModifier;
    }

    private int getBitModifier(ItemFlag hideFlag) {
        return 1 << hideFlag.ordinal();
    }

    @Override
    public List<String> getLore() {
        return this.lore == null ? null : new ArrayList<String>(Lists.transform(this.lore, CraftChatMessage::fromComponent));
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore == null || lore.isEmpty()) {
            this.lore = null;
        } else {
            if (this.lore == null) {
                this.lore = new ArrayList<Component>(lore.size());
            } else {
                this.lore.clear();
            }
            CraftMetaItem.safelyAdd(lore, this.lore, false);
        }
    }

    @Override
    public boolean hasCustomModelData() {
        return this.customModelData != null;
    }

    @Override
    public int getCustomModelData() {
        Preconditions.checkState(this.hasCustomModelData(), "We don't have CustomModelData! Check hasCustomModelData first!");
        return this.customModelData;
    }

    @Override
    public void setCustomModelData(Integer data) {
        this.customModelData = data;
    }

    @Override
    public boolean hasBlockData() {
        return this.blockData != null;
    }

    @Override
    public BlockData getBlockData(Material material) {
        BlockState defaultData = CraftBlockType.bukkitToMinecraft(material).defaultBlockState();
        return CraftBlockData.fromData((this.hasBlockData()) ? new BlockItemStateProperties(this.blockData).apply(defaultData) : defaultData);
    }

    @Override
    public void setBlockData(BlockData blockData) {
        this.blockData = (blockData == null) ? null : ((CraftBlockData) blockData).toStates();
    }

    @Override
    public int getRepairCost() {
        return this.repairCost;
    }

    @Override
    public void setRepairCost(int cost) { // TODO: Does this have limits?
        this.repairCost = cost;
    }

    @Override
    public boolean isHideTooltip() {
        return this.hideTooltip;
    }

    @Override
    public void setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;
    }

    @Override
    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @Override
    public boolean hasEnchantmentGlintOverride() {
        return this.enchantmentGlintOverride != null;
    }

    @Override
    public Boolean getEnchantmentGlintOverride() {
        Preconditions.checkState(this.hasEnchantmentGlintOverride(), "We don't have enchantment_glint_override! Check hasEnchantmentGlintOverride first!");
        return this.enchantmentGlintOverride;
    }

    @Override
    public void setEnchantmentGlintOverride(Boolean override) {
        this.enchantmentGlintOverride = override;
    }

    @Override
    public boolean isFireResistant() {
        return this.fireResistant;
    }

    @Override
    public void setFireResistant(boolean fireResistant) {
        this.fireResistant = fireResistant;
    }

    @Override
    public boolean hasMaxStackSize() {
        return this.maxStackSize != null;
    }

    @Override
    public int getMaxStackSize() {
        Preconditions.checkState(this.hasMaxStackSize(), "We don't have max_stack_size! Check hasMaxStackSize first!");
        return this.maxStackSize;
    }

    @Override
    public void setMaxStackSize(Integer max) {
        Preconditions.checkArgument(max == null || max > 0, "max_stack_size must be > 0");
        Preconditions.checkArgument(max == null || max <= net.minecraft.world.item.Item.ABSOLUTE_MAX_STACK_SIZE, "max_stack_size must be <= 99");
        this.maxStackSize = max;
    }

    @Override
    public boolean hasRarity() {
        return this.rarity != null;
    }

    @Override
    public ItemRarity getRarity() {
        Preconditions.checkState(this.hasRarity(), "We don't have rarity! Check hasRarity first!");
        return this.rarity;
    }

    @Override
    public void setRarity(ItemRarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean hasFood() {
        return this.food != null;
    }

    @Override
    public FoodComponent getFood() {
        return (this.hasFood()) ? new CraftFoodComponent(this.food) : new CraftFoodComponent(new FoodProperties(0, 0, false, 0, Optional.empty(), Collections.emptyList()));
    }

    @Override
    public void setFood(FoodComponent food) {
        this.food = (food == null) ? null : new CraftFoodComponent((CraftFoodComponent) food);
    }

    @Override
    public boolean hasTool() {
        return this.tool != null;
    }

    @Override
    public ToolComponent getTool() {
        return (this.hasTool()) ? new CraftToolComponent(this.tool) : new CraftToolComponent(new Tool(Collections.emptyList(), 1.0F, 0));
    }

    @Override
    public void setTool(ToolComponent tool) {
        this.tool = (tool == null) ? null : new CraftToolComponent((CraftToolComponent) tool);
    }

    @Override
    public boolean hasJukeboxPlayable() {
        return this.jukebox != null;
    }

    @Override
    public JukeboxPlayableComponent getJukeboxPlayable() {
        return (this.hasJukeboxPlayable()) ? new CraftJukeboxComponent(this.jukebox) : new CraftJukeboxComponent(new JukeboxPlayable(new EitherHolder<>(JukeboxSongs.THIRTEEN), true));
    }

    @Override
    public void setJukeboxPlayable(JukeboxPlayableComponent jukeboxPlayable) {
        this.jukebox = (jukeboxPlayable == null) ? null : new CraftJukeboxComponent((CraftJukeboxComponent) jukeboxPlayable);
    }

    @Override
    public boolean hasAttributeModifiers() {
        return this.attributeModifiers != null && !this.attributeModifiers.isEmpty();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.hasAttributeModifiers() ? ImmutableMultimap.copyOf(this.attributeModifiers) : null;
    }

    private void checkAttributeList() {
        if (this.attributeModifiers == null) {
            this.attributeModifiers = LinkedHashMultimap.create();
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable EquipmentSlot slot) {
        this.checkAttributeList();
        SetMultimap<Attribute, AttributeModifier> result = LinkedHashMultimap.create();
        for (Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifiers.entries()) {
            if (entry.getValue().getSlot() == null || entry.getValue().getSlot() == slot) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers(@Nonnull Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        return this.attributeModifiers.containsKey(attribute) ? ImmutableList.copyOf(this.attributeModifiers.get(attribute)) : null;
    }

    @Override
    public boolean addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        Preconditions.checkNotNull(modifier, "AttributeModifier cannot be null");
        this.checkAttributeList();
        for (Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifiers.entries()) {
            Preconditions.checkArgument(!entry.getValue().getKey().equals(modifier.getKey()), "Cannot register AttributeModifier. Modifier is already applied! %s", modifier);
        }
        return this.attributeModifiers.put(attribute, modifier);
    }

    @Override
    public void setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        if (attributeModifiers == null || attributeModifiers.isEmpty()) {
            this.attributeModifiers = LinkedHashMultimap.create();
            return;
        }

        this.checkAttributeList();
        this.attributeModifiers.clear();

        Iterator<Map.Entry<Attribute, AttributeModifier>> iterator = attributeModifiers.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> next = iterator.next();

            if (next.getKey() == null || next.getValue() == null) {
                iterator.remove();
                continue;
            }
            this.attributeModifiers.put(next.getKey(), next.getValue());
        }
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        this.checkAttributeList();
        return !this.attributeModifiers.removeAll(attribute).isEmpty();
    }

    @Override
    public boolean removeAttributeModifier(@Nullable EquipmentSlot slot) {
        this.checkAttributeList();
        int removed = 0;
        Iterator<Map.Entry<Attribute, AttributeModifier>> iter = this.attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> entry = iter.next();
            // Explicitly match against null because (as of MC 1.13) AttributeModifiers without a -
            // set slot are active in any slot.
            if (entry.getValue().getSlot() == null || entry.getValue().getSlot() == slot) {
                iter.remove();
                ++removed;
            }
        }
        return removed > 0;
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        Preconditions.checkNotNull(modifier, "AttributeModifier cannot be null");
        this.checkAttributeList();
        int removed = 0;
        Iterator<Map.Entry<Attribute, AttributeModifier>> iter = this.attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> entry = iter.next();
            if (entry.getKey() == null || entry.getValue() == null) {
                iter.remove();
                ++removed;
                continue; // remove all null values while we are here
            }

            if (entry.getKey() == attribute && entry.getValue().getKey().equals(modifier.getKey())) {
                iter.remove();
                ++removed;
            }
        }
        return removed > 0;
    }

    @Override
    public String getAsString() {
        CraftMetaItem.Applicator tag = new CraftMetaItem.Applicator();
        this.applyToItem(tag);
        DataComponentPatch patch = tag.build();
        Tag nbt = DataComponentPatch.CODEC.encodeStart(MinecraftServer.getDefaultRegistryAccess().createSerializationContext(NbtOps.INSTANCE), patch).getOrThrow();
        return nbt.toString();
    }

    @Override
    public String getAsComponentString() {
        CraftMetaItem.Applicator tag = new CraftMetaItem.Applicator();
        applyToItem(tag);
        DataComponentPatch patch = tag.build();

        RegistryAccess registryAccess = CraftRegistry.getMinecraftRegistry();
        DynamicOps<Tag> ops = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        net.minecraft.core.Registry<DataComponentType<?>> componentTypeRegistry = registryAccess.registryOrThrow(Registries.DATA_COMPONENT_TYPE);

        StringJoiner componentString = new StringJoiner(",", "[", "]");
        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : patch.entrySet()) {
            DataComponentType<?> componentType = entry.getKey();
            Optional<?> componentValue = entry.getValue();
            String componentKey = componentTypeRegistry.getResourceKey(componentType).orElseThrow().location().toString();

            if (componentValue.isPresent()) {
                Tag componentValueAsNBT = (Tag) ((DataComponentType) componentType).codecOrThrow().encodeStart(ops, componentValue.get()).getOrThrow();
                String componentValueAsNBTString = new SnbtPrinterTagVisitor("", 0, new ArrayList<>()).visit(componentValueAsNBT);
                componentString.add(componentKey + "=" + componentValueAsNBTString);
            } else {
                componentString.add("!" + componentKey);
            }
        }

        return componentString.toString();
    }

    @Override
    public CustomItemTagContainer getCustomTagContainer() {
        return new DeprecatedCustomTagContainer(this.getPersistentDataContainer());
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    private static boolean compareModifiers(Multimap<Attribute, AttributeModifier> first, Multimap<Attribute, AttributeModifier> second) {
        if (first == null || second == null) {
            return false;
        }
        for (Map.Entry<Attribute, AttributeModifier> entry : first.entries()) {
            if (!second.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        for (Map.Entry<Attribute, AttributeModifier> entry : second.entries()) {
            if (!first.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasDamage() {
        return this.damage > 0;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public boolean hasMaxDamage() {
        return this.maxDamage != null;
    }

    @Override
    public int getMaxDamage() {
        Preconditions.checkState(this.hasMaxDamage(), "We don't have max_damage! Check hasMaxDamage first!");
        return this.maxDamage;
    }

    @Override
    public void setMaxDamage(Integer maxDamage) {
        this.maxDamage = maxDamage;
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
                && (this.hasItemName() ? that.hasItemName() && this.itemName.equals(that.itemName) : !that.hasItemName())
                && (this.hasEnchants() ? that.hasEnchants() && this.enchantments.equals(that.enchantments) : !that.hasEnchants())
                && (Objects.equals(this.lore, that.lore))
                && (this.hasCustomModelData() ? that.hasCustomModelData() && this.customModelData.equals(that.customModelData) : !that.hasCustomModelData())
                && (this.hasBlockData() ? that.hasBlockData() && this.blockData.equals(that.blockData) : !that.hasBlockData())
                && (this.hasRepairCost() ? that.hasRepairCost() && this.repairCost == that.repairCost : !that.hasRepairCost())
                && (this.hasAttributeModifiers() ? that.hasAttributeModifiers() && CraftMetaItem.compareModifiers(this.attributeModifiers, that.attributeModifiers) : !that.hasAttributeModifiers())
                && (this.unhandledTags.equals(that.unhandledTags))
                && (this.removedTags.equals(that.removedTags))
                && (Objects.equals(this.customTag, that.customTag))
                && (this.persistentDataContainer.equals(that.persistentDataContainer))
                && (this.hideFlag == that.hideFlag)
                && (this.isHideTooltip() == that.isHideTooltip())
                && (this.isUnbreakable() == that.isUnbreakable())
                && (this.hasEnchantmentGlintOverride() ? that.hasEnchantmentGlintOverride() && this.enchantmentGlintOverride.equals(that.enchantmentGlintOverride) : !that.hasEnchantmentGlintOverride())
                && (this.fireResistant == that.fireResistant)
                && (this.hasMaxStackSize() ? that.hasMaxStackSize() && this.maxStackSize.equals(that.maxStackSize) : !that.hasMaxStackSize())
                && (this.rarity == that.rarity)
                && (this.hasFood() ? that.hasFood() && this.food.equals(that.food) : !that.hasFood())
                && (this.hasTool() ? that.hasTool() && this.tool.equals(that.tool) : !that.hasTool())
                && (this.hasJukeboxPlayable() ? that.hasJukeboxPlayable() && this.jukebox.equals(that.jukebox) : !that.hasJukeboxPlayable())
                && (this.hasDamage() ? that.hasDamage() && this.damage == that.damage : !that.hasDamage())
                && (this.hasMaxDamage() ? that.hasMaxDamage() && this.maxDamage.equals(that.maxDamage) : !that.hasMaxDamage())
                && (this.version == that.version);
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
        return this.applyHash();
    }

    @Overridden
    int applyHash() {
        int hash = 3;
        hash = 61 * hash + (this.hasDisplayName() ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (this.hasItemName() ? this.itemName.hashCode() : 0);
        hash = 61 * hash + ((this.lore != null) ? this.lore.hashCode() : 0);
        hash = 61 * hash + (this.hasCustomModelData() ? this.customModelData.hashCode() : 0);
        hash = 61 * hash + (this.hasBlockData() ? this.blockData.hashCode() : 0);
        hash = 61 * hash + (this.hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (this.hasRepairCost() ? this.repairCost : 0);
        hash = 61 * hash + this.unhandledTags.hashCode();
        hash = 61 * hash + removedTags.hashCode();
        hash = 61 * hash + ((this.customTag != null) ? this.customTag.hashCode() : 0);
        hash = 61 * hash + (!this.persistentDataContainer.isEmpty() ? this.persistentDataContainer.hashCode() : 0);
        hash = 61 * hash + this.hideFlag;
        hash = 61 * hash + (this.isHideTooltip() ? 1231 : 1237);
        hash = 61 * hash + (this.isUnbreakable() ? 1231 : 1237);
        hash = 61 * hash + (this.hasEnchantmentGlintOverride() ? this.enchantmentGlintOverride.hashCode() : 0);
        hash = 61 * hash + (this.isFireResistant() ? 1231 : 1237);
        hash = 61 * hash + (this.hasMaxStackSize() ? this.maxStackSize.hashCode() : 0);
        hash = 61 * hash + (this.hasRarity() ? this.rarity.hashCode() : 0);
        hash = 61 * hash + (this.hasFood() ? this.food.hashCode() : 0);
        hash = 61 * hash + (hasTool() ? this.tool.hashCode() : 0);
        hash = 61 * hash + (hasJukeboxPlayable() ? this.jukebox.hashCode() : 0);
        hash = 61 * hash + (this.hasDamage() ? this.damage : 0);
        hash = 61 * hash + (this.hasMaxDamage() ? 1231 : 1237);
        hash = 61 * hash + (this.hasAttributeModifiers() ? this.attributeModifiers.hashCode() : 0);
        hash = 61 * hash + this.version;
        return hash;
    }

    @Overridden
    @Override
    public CraftMetaItem clone() {
        try {
            CraftMetaItem clone = (CraftMetaItem) super.clone();
            if (this.lore != null) {
                clone.lore = new ArrayList<Component>(this.lore);
            }
            clone.customModelData = this.customModelData;
            clone.blockData = this.blockData;
            if (this.enchantments != null) {
                clone.enchantments = new LinkedHashMap<Enchantment, Integer>(this.enchantments);
            }
            if (this.hasAttributeModifiers()) {
                clone.attributeModifiers = LinkedHashMultimap.create(this.attributeModifiers);
            }
            if (this.customTag != null) {
                clone.customTag = this.customTag.copy();
            }
            clone.removedTags = Sets.newHashSet(this.removedTags);
            clone.persistentDataContainer = new CraftPersistentDataContainer(this.persistentDataContainer.getRaw(), CraftMetaItem.DATA_TYPE_REGISTRY);
            clone.hideFlag = this.hideFlag;
            clone.hideTooltip = this.hideTooltip;
            clone.unbreakable = this.unbreakable;
            clone.enchantmentGlintOverride = this.enchantmentGlintOverride;
            clone.fireResistant = this.fireResistant;
            clone.maxStackSize = this.maxStackSize;
            clone.rarity = this.rarity;
            if (this.hasFood()) {
                clone.food = new CraftFoodComponent(this.food);
            }
            if (this.hasTool()) {
                clone.tool = new CraftToolComponent(tool);
            }
            if (this.hasJukeboxPlayable()) {
                clone.jukebox = new CraftJukeboxComponent(jukebox);
            }
            clone.damage = this.damage;
            clone.maxDamage = this.maxDamage;
            clone.version = this.version;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public final Map<String, Object> serialize() {
        ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
        map.put(SerializableMeta.TYPE_FIELD, SerializableMeta.classMap.get(this.getClass()));
        this.serialize(map);
        return map.build();
    }

    @Overridden
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        if (this.hasDisplayName()) {
            builder.put(CraftMetaItem.NAME.BUKKIT, CraftChatMessage.toJSON(this.displayName));
        }

        if (this.hasItemName()) {
            builder.put(CraftMetaItem.ITEM_NAME.BUKKIT, CraftChatMessage.toJSON(this.itemName));
        }

        if (this.hasLore()) {
            // SPIGOT-7625: Convert lore to json before serializing it
            List<String> jsonLore = new ArrayList<>();

            for (Component component : this.lore) {
                jsonLore.add(CraftChatMessage.toJSON(component));
            }

            builder.put(CraftMetaItem.LORE.BUKKIT, jsonLore);
        }

        if (this.hasCustomModelData()) {
            builder.put(CraftMetaItem.CUSTOM_MODEL_DATA.BUKKIT, this.customModelData);
        }
        if (this.hasBlockData()) {
            builder.put(CraftMetaItem.BLOCK_DATA.BUKKIT, this.blockData);
        }

        CraftMetaItem.serializeEnchantments(this.enchantments, builder, CraftMetaItem.ENCHANTMENTS);
        CraftMetaItem.serializeModifiers(this.attributeModifiers, builder, CraftMetaItem.ATTRIBUTES);

        if (this.hasRepairCost()) {
            builder.put(CraftMetaItem.REPAIR.BUKKIT, this.repairCost);
        }

        List<String> hideFlags = new ArrayList<String>();
        for (ItemFlag hideFlagEnum : this.getItemFlags()) {
            hideFlags.add(CraftItemFlag.bukkitToString(hideFlagEnum));
        }
        if (!hideFlags.isEmpty()) {
            builder.put(CraftMetaItem.HIDEFLAGS.BUKKIT, hideFlags);
        }

        if (this.isHideTooltip()) {
            builder.put(CraftMetaItem.HIDE_TOOLTIP.BUKKIT, this.hideTooltip);
        }

        if (this.isUnbreakable()) {
            builder.put(CraftMetaItem.UNBREAKABLE.BUKKIT, this.unbreakable);
        }

        if (this.hasEnchantmentGlintOverride()) {
            builder.put(CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE.BUKKIT, this.enchantmentGlintOverride);
        }

        if (this.isFireResistant()) {
            builder.put(CraftMetaItem.FIRE_RESISTANT.BUKKIT, this.fireResistant);
        }

        if (this.hasMaxStackSize()) {
            builder.put(CraftMetaItem.MAX_STACK_SIZE.BUKKIT, this.maxStackSize);
        }

        if (this.hasRarity()) {
            builder.put(CraftMetaItem.RARITY.BUKKIT, this.rarity.name());
        }

        if (this.hasFood()) {
            builder.put(CraftMetaItem.FOOD.BUKKIT, this.food);
        }

        if (hasTool()) {
            builder.put(TOOL.BUKKIT, tool);
        }

        if (hasJukeboxPlayable()) {
            builder.put(JUKEBOX_PLAYABLE.BUKKIT, jukebox);
        }

        if (this.hasDamage()) {
            builder.put(CraftMetaItem.DAMAGE.BUKKIT, this.damage);
        }

        if (this.hasMaxDamage()) {
            builder.put(CraftMetaItem.MAX_DAMAGE.BUKKIT, this.maxDamage);
        }

        final Map<String, Tag> internalTags = new HashMap<String, Tag>();
        this.serializeInternal(internalTags);
        if (!internalTags.isEmpty()) {
            CompoundTag internal = new CompoundTag();
            for (Map.Entry<String, Tag> e : internalTags.entrySet()) {
                internal.put(e.getKey(), e.getValue());
            }
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                NbtIo.writeCompressed(internal, buf);
                builder.put("internal", Base64.getEncoder().encodeToString(buf.toByteArray()));
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!this.unhandledTags.isEmpty()) {
            Tag unhandled = DataComponentPatch.CODEC.encodeStart(MinecraftServer.getDefaultRegistryAccess().createSerializationContext(NbtOps.INSTANCE), this.unhandledTags.build()).getOrThrow(IllegalStateException::new);
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                NbtIo.writeCompressed((CompoundTag) unhandled, buf);
                builder.put("unhandled", Base64.getEncoder().encodeToString(buf.toByteArray()));
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!this.removedTags.isEmpty()) {
            RegistryAccess registryAccess = CraftRegistry.getMinecraftRegistry();
            Registry<DataComponentType<?>> componentTypeRegistry = registryAccess.registryOrThrow(Registries.DATA_COMPONENT_TYPE);

            List<String> removedTags = new ArrayList<>();
            for (DataComponentType<?> removed : this.removedTags) {
                String componentKey = componentTypeRegistry.getResourceKey(removed).orElseThrow().location().toString();

                removedTags.add(componentKey);
            }

            builder.put("removed", removedTags);
        }

        if (!this.persistentDataContainer.isEmpty()) { // Store custom tags, wrapped in their compound
            builder.put(CraftMetaItem.BUKKIT_CUSTOM_TAG.BUKKIT, this.persistentDataContainer.serialize());
        }

        if (this.customTag != null && !this.customTag.isEmpty()) {
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                NbtIo.writeCompressed(this.customTag, buf);
                builder.put("custom", Base64.getEncoder().encodeToString(buf.toByteArray()));
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return builder;
    }

    void serializeInternal(final Map<String, Tag> unhandledTags) {
    }

    static void serializeEnchantments(Map<Enchantment, Integer> enchantments, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }

        ImmutableMap.Builder<String, Integer> enchants = ImmutableMap.builder();
        for (Map.Entry<? extends Enchantment, Integer> enchant : enchantments.entrySet()) {
            enchants.put(CraftEnchantment.bukkitToString(enchant.getKey()), enchant.getValue());
        }

        builder.put(key.BUKKIT, enchants.build());
    }

    static void serializeModifiers(Multimap<Attribute, AttributeModifier> modifiers, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (modifiers == null || modifiers.isEmpty()) {
            return;
        }

        Map<String, List<Object>> mods = new LinkedHashMap<>();
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey() == null) {
                continue;
            }
            Collection<AttributeModifier> modCollection = modifiers.get(entry.getKey());
            if (modCollection == null || modCollection.isEmpty()) {
                continue;
            }
            mods.put(CraftAttribute.bukkitToString(entry.getKey()), new ArrayList<>(modCollection));
        }
        builder.put(key.BUKKIT, mods);
    }

    static void safelyAdd(Iterable<?> addFrom, Collection<Component> addTo, boolean possiblyJsonInput) {
        if (addFrom == null) {
            return;
        }

        for (Object object : addFrom) {
            if (!(object instanceof String)) {
                if (object != null) {
                    // SPIGOT-7399: Null check via if is important,
                    // otherwise object.getClass().getName() could throw an error for a valid argument -> when it is null which is valid,
                    // when using Preconditions
                    throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                }

                addTo.add(Component.empty());
            } else {
                String entry = object.toString();
                Component component = (possiblyJsonInput) ? CraftChatMessage.fromJSONOrString(entry) : CraftChatMessage.fromStringOrNull(entry);

                if (component != null) {
                    addTo.add(component);
                } else {
                    addTo.add(Component.empty());
                }
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

    @Override
    public final String toString() {
        return SerializableMeta.classMap.get(this.getClass()) + "_META:" + this.serialize(); // TODO: cry
    }

    public int getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public static Set<DataComponentType> getHandledTags() {
        synchronized (CraftMetaItem.HANDLED_TAGS) {
            if (CraftMetaItem.HANDLED_TAGS.isEmpty()) {
                CraftMetaItem.HANDLED_TAGS.addAll(Arrays.asList(
                        CraftMetaItem.NAME.TYPE,
                        CraftMetaItem.ITEM_NAME.TYPE,
                        CraftMetaItem.LORE.TYPE,
                        CraftMetaItem.CUSTOM_MODEL_DATA.TYPE,
                        CraftMetaItem.BLOCK_DATA.TYPE,
                        CraftMetaItem.REPAIR.TYPE,
                        CraftMetaItem.ENCHANTMENTS.TYPE,
                        CraftMetaItem.HIDE_ADDITIONAL_TOOLTIP.TYPE,
                        CraftMetaItem.HIDE_TOOLTIP.TYPE,
                        CraftMetaItem.UNBREAKABLE.TYPE,
                        CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE.TYPE,
                        CraftMetaItem.FIRE_RESISTANT.TYPE,
                        CraftMetaItem.MAX_STACK_SIZE.TYPE,
                        CraftMetaItem.RARITY.TYPE,
                        CraftMetaItem.FOOD.TYPE,
                        TOOL.TYPE,
                        JUKEBOX_PLAYABLE.TYPE,
                        CraftMetaItem.DAMAGE.TYPE,
                        CraftMetaItem.MAX_DAMAGE.TYPE,
                        CraftMetaItem.CUSTOM_DATA.TYPE,
                        CraftMetaItem.ATTRIBUTES.TYPE,
                        CraftMetaArmor.TRIM.TYPE,
                        CraftMetaArmorStand.ENTITY_TAG.TYPE,
                        CraftMetaBanner.PATTERNS.TYPE,
                        CraftMetaEntityTag.ENTITY_TAG.TYPE,
                        CraftMetaLeatherArmor.COLOR.TYPE,
                        CraftMetaMap.MAP_POST_PROCESSING.TYPE,
                        CraftMetaMap.MAP_COLOR.TYPE,
                        CraftMetaMap.MAP_ID.TYPE,
                        CraftMetaPotion.POTION_CONTENTS.TYPE,
                        CraftMetaSkull.SKULL_PROFILE.TYPE,
                        CraftMetaSkull.NOTE_BLOCK_SOUND.TYPE,
                        CraftMetaSpawnEgg.ENTITY_TAG.TYPE,
                        CraftMetaBlockState.BLOCK_ENTITY_TAG.TYPE,
                        CraftMetaBook.BOOK_CONTENT.TYPE,
                        CraftMetaBookSigned.BOOK_CONTENT.TYPE,
                        CraftMetaFirework.FIREWORKS.TYPE,
                        CraftMetaEnchantedBook.STORED_ENCHANTMENTS.TYPE,
                        CraftMetaCharge.EXPLOSION.TYPE,
                        CraftMetaKnowledgeBook.BOOK_RECIPES.TYPE,
                        CraftMetaTropicalFishBucket.ENTITY_TAG.TYPE,
                        CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG.TYPE,
                        CraftMetaAxolotlBucket.ENTITY_TAG.TYPE,
                        CraftMetaAxolotlBucket.BUCKET_ENTITY_TAG.TYPE,
                        CraftMetaCrossbow.CHARGED_PROJECTILES.TYPE,
                        CraftMetaSuspiciousStew.EFFECTS.TYPE,
                        CraftMetaCompass.LODESTONE_TARGET.TYPE,
                        CraftMetaBundle.ITEMS.TYPE,
                        CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.TYPE,
                        CraftMetaOminousBottle.OMINOUS_BOTTLE_AMPLIFIER.TYPE
                ));
            }
            return CraftMetaItem.HANDLED_TAGS;
        }
    }

    protected static <T> Optional<? extends T> getOrEmpty(DataComponentPatch tag, ItemMetaKeyType<T> type) {
        Optional<? extends T> result = tag.get(type.TYPE);

        return (result != null) ? result : Optional.empty();
    }
}
