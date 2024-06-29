package org.bukkit.craftbukkit.v1_20_R1.util;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.paper.attribute.UnmodifiableAttributeMap;
import com.mohistmc.paper.inventory.ItemRarity;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;
import org.bukkit.Bukkit;
import org.bukkit.FeatureFlag;
import org.bukkit.Fluid;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.UnsafeValues;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R1.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_20_R1.CraftFeatureFlag;
import org.bukkit.craftbukkit.v1_20_R1.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.v1_20_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.legacy.CraftLegacy;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("deprecation")
public final class CraftMagicNumbers implements UnsafeValues {
    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    private CraftMagicNumbers() {}

    public static BlockState getBlock(MaterialData material) {
        return getBlock(material.getItemType(), material.getData());
    }

    public static BlockState getBlock(Material material, byte data) {
        return CraftLegacy.fromLegacyData(CraftLegacy.toLegacy(material), data);
    }

    public static MaterialData getMaterial(BlockState data) {
        return CraftLegacy.toLegacy(getMaterial(data.getBlock())).getNewData(toLegacyData(data));
    }

    public static Item getItem(Material material, short data) {
        if (material.isLegacy()) {
            return CraftLegacy.fromLegacyData(CraftLegacy.toLegacy(material), data);
        }

        return getItem(material);
    }

    public static MaterialData getMaterialData(Item item) {
        return CraftLegacy.toLegacyData(getMaterial(item));
    }

    // ========================================================================
    public static final Map<Block, Material> BLOCK_MATERIAL = new HashMap<>();
    public static final Map<Item, Material> ITEM_MATERIAL = new HashMap<>();
    public static final BiMap<net.minecraft.world.level.material.Fluid, Fluid> FLUIDTYPE_FLUID = HashBiMap.create();
    public static final Map<Material, Item> MATERIAL_ITEM = new HashMap<>();
    public static final Map<Material, Block> MATERIAL_BLOCK = new HashMap<>();

    static {
        for (Block block : BuiltInRegistries.BLOCK) {
            BLOCK_MATERIAL.put(block, Material.getMaterial(BuiltInRegistries.BLOCK.getKey(block).getPath().toUpperCase(Locale.ROOT)));
        }

        for (Item item : BuiltInRegistries.ITEM) {
            ITEM_MATERIAL.put(item, Material.getMaterial(BuiltInRegistries.ITEM.getKey(item).getPath().toUpperCase(Locale.ROOT)));
        }

        for (net.minecraft.world.level.material.Fluid fluidType : BuiltInRegistries.FLUID) {
            if (BuiltInRegistries.FLUID.getKey(fluidType).getNamespace().equals(NamespacedKey.MINECRAFT)) {
                Fluid fluid = Registry.FLUID.get(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.FLUID.getKey(fluidType)));
                if (fluid != null) {
                    FLUIDTYPE_FLUID.put(fluidType, fluid);
                }
            }
        }

        for (Material material : Material.values()) {
            if (material.isLegacy()) {
                continue;
            }

            ResourceLocation key = key(material);
            BuiltInRegistries.ITEM.getOptional(key).ifPresent((item) -> {
                MATERIAL_ITEM.put(material, item);
            });
            BuiltInRegistries.BLOCK.getOptional(key).ifPresent((block) -> {
                MATERIAL_BLOCK.put(material, block);
            });
        }
    }

    public static Material getMaterial(Block block) {
        return BLOCK_MATERIAL.get(block);
    }

    public static Material getMaterial(Item item) {
        return ITEM_MATERIAL.getOrDefault(item, Material.AIR);
    }

    public static Fluid getFluid(net.minecraft.world.level.material.Fluid fluid) {
        return FLUIDTYPE_FLUID.get(fluid);
    }

    public static Item getItem(Material material) {
        if (material != null && material.isLegacy()) {
            material = CraftLegacy.fromLegacy(material);
        }

        return MATERIAL_ITEM.get(material);
    }

    public static Block getBlock(Material material) {
        if (material != null && material.isLegacy()) {
            material = CraftLegacy.fromLegacy(material);
        }

        return MATERIAL_BLOCK.get(material);
    }

    public static net.minecraft.world.level.material.Fluid getFluid(Fluid fluid) {
        return FLUIDTYPE_FLUID.inverse().get(fluid);
    }

    public static ResourceLocation key(Material mat) {
        return CraftNamespacedKey.toMinecraft(mat.getKey());
    }
    // ========================================================================

    public static byte toLegacyData(BlockState data) {
        return CraftLegacy.toLegacyData(data);
    }

    @Override
    public Material toLegacy(Material material) {
        return CraftLegacy.toLegacy(material);
    }

    @Override
    public Material fromLegacy(Material material) {
        return CraftLegacy.fromLegacy(material);
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        return CraftLegacy.fromLegacy(material);
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        return CraftLegacy.fromLegacy(material, itemPriority);
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        return CraftBlockData.fromData(getBlock(material, data));
    }

    @Override
    public Material getMaterial(String material, int version) {
        Preconditions.checkArgument(material != null, "material == null");
        Preconditions.checkArgument(version <= this.getDataVersion(), "Newer version! Server downgrades are not supported!");

        // Fastpath up to date materials
        if (version == this.getDataVersion()) {
            return Material.getMaterial(material);
        }

        Dynamic<Tag> name = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf("minecraft:" + material.toLowerCase(Locale.ROOT)));
        Dynamic<Tag> converted = DataFixers.getDataFixer().update(References.ITEM_NAME, name, version, this.getDataVersion());

        if (name.equals(converted)) {
            converted = DataFixers.getDataFixer().update(References.BLOCK_NAME, name, version, this.getDataVersion());
        }

        return Material.matchMaterial(converted.asString(""));
    }

    /**
     * This string should be changed if the NMS mappings do.
     *
     * It has no meaning and should only be used as an equality check. Plugins
     * which are sensitive to the NMS mappings may read it and refuse to load if
     * it cannot be found or is different to the expected value.
     *
     * Remember: NMS is not supported API and may break at any time for any
     * reason irrespective of this. There is often supported API to do the same
     * thing as many common NMS usages. If not, you are encouraged to open a
     * feature and/or pull request for consideration, or use a well abstracted
     * third-party API such as ProtocolLib.
     *
     * @return string
     */
    public String getMappingsVersion() {
        return "bcf3dcb22ad42792794079f9443df2c0";
    }

    @Override
    public int getDataVersion() {
        return SharedConstants.getCurrentVersion().getDataVersion().getVersion();
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try {
            nmsStack.setTag((CompoundTag) TagParser.parseTag(arguments));
        } catch (CommandSyntaxException ex) {
            Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, null, ex);
        }

        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));

        return stack;
    }

    private static File getBukkitDataPackFolder() {
        return new File(MinecraftServer.getServer().getWorldPath(LevelResource.DATAPACK_DIR).toFile(), "bukkit");
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        Preconditions.checkArgument(Bukkit.getAdvancement(key) == null, "Advancement %s already exists", key);
        ResourceLocation minecraftkey = CraftNamespacedKey.toMinecraft(key);

        JsonElement jsonelement = ServerAdvancementManager.GSON.fromJson(advancement, JsonElement.class);
        JsonObject jsonobject = GsonHelper.convertToJsonObject(jsonelement, "advancement");
        net.minecraft.advancements.Advancement.Builder nms = net.minecraft.advancements.Advancement.Builder.fromJson(jsonobject, new DeserializationContext(minecraftkey, MinecraftServer.getServer().getLootData()));
        if (nms != null) {
            MinecraftServer.getServer().getAdvancements().advancements.add(Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
            Advancement bukkit = Bukkit.getAdvancement(key);

            if (bukkit != null) {
                File file = new File(getBukkitDataPackFolder(), "data" + File.separator + key.getNamespace() + File.separator + "advancements" + File.separator + key.getKey() + ".json");
                file.getParentFile().mkdirs();

                try {
                    Files.write(advancement, file, Charsets.UTF_8);
                } catch (IOException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "Error saving advancement " + key, ex);
                }

                MinecraftServer.getServer().getPlayerList().reloadResources();

                return bukkit;
            }
        }

        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        File file = new File(getBukkitDataPackFolder(), "data" + File.separator + key.getNamespace() + File.separator + "advancements" + File.separator + key.getKey() + ".json");
        return file.delete();
    }

    private static final List<String> SUPPORTED_API = Arrays.asList("1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.19", "1.20");

    @Override
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {
        String minimumVersion = MinecraftServer.getServer().server.minimumAPI;
        int minimumIndex = SUPPORTED_API.indexOf(minimumVersion);

        if (pdf.getAPIVersion() != null) {
            int pluginIndex = SUPPORTED_API.indexOf(pdf.getAPIVersion());

            if (pluginIndex == -1) {
                throw new InvalidPluginException("Unsupported API version " + pdf.getAPIVersion());
            }

            if (pluginIndex < minimumIndex) {
                throw new InvalidPluginException("Plugin API version " + pdf.getAPIVersion() + " is lower than the minimum allowed version. Please update or replace it.");
            }
        } else {
            if (minimumIndex == -1) {
                CraftLegacy.init();
                Bukkit.getLogger().log(Level.WARNING, "Legacy plugin " + pdf.getFullName() + " does not specify an api-version.");
            } else {
                throw new InvalidPluginException("Plugin API version " + pdf.getAPIVersion() + " is lower than the minimum allowed version. Please update or replace it.");
            }
        }
    }

    public static boolean isLegacy(PluginDescriptionFile pdf) {
        return /*pdf.getAPIVersion() == null*/ false;
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        try {
            clazz = Commodore.convert(clazz, !isLegacy(pdf));
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Fatal error trying to convert " + pdf.getFullName() + ":" + path, ex);
        }

        return clazz;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material material, EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> defaultAttributes = ImmutableMultimap.builder();

        Multimap<net.minecraft.world.entity.ai.attributes.Attribute, net.minecraft.world.entity.ai.attributes.AttributeModifier> nmsDefaultAttributes = getItem(material).getDefaultAttributeModifiers(CraftEquipmentSlot.getNMS(slot));
        for (Map.Entry<net.minecraft.world.entity.ai.attributes.Attribute, net.minecraft.world.entity.ai.attributes.AttributeModifier> mapEntry : nmsDefaultAttributes.entries()) {
            Attribute attribute = CraftAttributeMap.fromMinecraft(BuiltInRegistries.ATTRIBUTE.getKey(mapEntry.getKey()).toString());
            defaultAttributes.put(attribute, CraftAttributeInstance.convert(mapEntry.getValue(), slot));
        }

        return defaultAttributes.build();
    }

    @Override
    public CreativeCategory getCreativeCategory(Material material) {
        return CreativeCategory.BUILDING_BLOCKS; // TODO: Figure out what to do with this
    }

    @Override
    public String getBlockTranslationKey(Material material) {
        Block block = getBlock(material);
        return (block != null) ? block.getDescriptionId() : null;
    }

    @Override
    public String getItemTranslationKey(Material material) {
        Item item = getItem(material);
        return (item != null) ? item.getDescriptionId() : null;
    }

    @Override
    public String getTranslationKey(EntityType entityType) {
        Preconditions.checkArgument(entityType.getName() != null, "Invalid name of EntityType %s for translation key", entityType);
        String mods = ServerAPI.entityTypeMap0.containsKey(entityType) ? ServerAPI.entityTypeMap0.get(entityType).getDescriptionId() : entityType.getKey().asString();
        return net.minecraft.world.entity.EntityType.byString(entityType.getName()).map(net.minecraft.world.entity.EntityType::getDescriptionId).orElse(mods);
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsItemStack.getItem().getDescriptionId(nmsItemStack);
    }

    @Override
    public FeatureFlag getFeatureFlag(NamespacedKey namespacedKey) {
        Preconditions.checkArgument(namespacedKey != null, "NamespaceKey cannot be null");
        return CraftFeatureFlag.getFromNMS(namespacedKey);
    }

    @Override
    public boolean isSupportedApiVersion(String apiVersion) {
        return true;
    }

    // Paper start
    @Override
    public byte[] serializeItem(ItemStack item) {
        Preconditions.checkNotNull(item, "null cannot be serialized");
        Preconditions.checkArgument(item.getType() != Material.AIR, "air cannot be serialized");

        return serializeNbtToBytes((item instanceof CraftItemStack ? ((CraftItemStack) item).handle : CraftItemStack.asNMSCopy(item)).save(new CompoundTag()));
    }

    @Override
    public ItemStack deserializeItem(byte[] data) {
        Preconditions.checkNotNull(data, "null cannot be deserialized");
        Preconditions.checkArgument(data.length > 0, "cannot deserialize nothing");

        CompoundTag compound = deserializeNbtFromBytes(data);
        int dataVersion = compound.getInt("DataVersion");
        return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of(ca.spottedleaf.dataconverter.minecraft.MCDataConverter.convertTag(ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry.ITEM_STACK, compound, dataVersion, getDataVersion())));
    }

    @Override
    public byte[] serializeEntity(org.bukkit.entity.Entity entity) {
        Preconditions.checkNotNull(entity, "null cannot be serialized");
        Preconditions.checkArgument(entity instanceof org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity, "only CraftEntities can be serialized");

        CompoundTag compound = new CompoundTag();
        ((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity) entity).getHandle().serializeEntity(compound);
        return serializeNbtToBytes(compound);
    }

    @Override
    public org.bukkit.entity.Entity deserializeEntity(byte[] data, org.bukkit.World world, boolean preserveUUID) {
        Preconditions.checkNotNull(data, "null cannot be deserialized");
        Preconditions.checkArgument(data.length > 0, "cannot deserialize nothing");

        CompoundTag compound = deserializeNbtFromBytes(data);
        int dataVersion = compound.getInt("DataVersion");
        compound = ca.spottedleaf.dataconverter.minecraft.MCDataConverter.convertTag(ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry.ENTITY, compound, dataVersion, getDataVersion());
        if (!preserveUUID) compound.remove("UUID"); // Generate a new UUID so we don't have to worry about deserializing the same entity twice
        return net.minecraft.world.entity.EntityType.create(compound, ((org.bukkit.craftbukkit.v1_20_R1.CraftWorld) world).getHandle())
                .orElseThrow(() -> new IllegalArgumentException("An ID was not found for the data. Did you downgrade?")).getBukkitEntity();
    }

    private byte[] serializeNbtToBytes(CompoundTag compound) {
        compound.putInt("DataVersion", getDataVersion());
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        try {
            net.minecraft.nbt.NbtIo.writeCompressed(
                    compound,
                    outputStream
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return outputStream.toByteArray();
    }

    private CompoundTag deserializeNbtFromBytes(byte[] data) {
        CompoundTag compound;
        try {
            compound = net.minecraft.nbt.NbtIo.readCompressed(
                    new java.io.ByteArrayInputStream(data)
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        int dataVersion = compound.getInt("DataVersion");
        Preconditions.checkArgument(dataVersion <= getDataVersion(), "Newer version! Server downgrades are not supported!");
        return compound;
    }

    @Override
    public int nextEntityId() {
        return net.minecraft.world.entity.Entity.nextEntityId();
    }

    @Override
    public String getMainLevelName() {
        return ((net.minecraft.server.dedicated.DedicatedServer) net.minecraft.server.MinecraftServer.getServer()).getProperties().levelName;
    }

    @Override
    public ItemRarity getItemRarity(org.bukkit.Material material) {
        Item item = getItem(material);
        if (item == null) {
            throw new IllegalArgumentException(material + " is not an item, and rarity does not apply to blocks");
        }
        return ItemRarity.values()[item.rarity.ordinal()];
    }

    @Override
    public ItemRarity getItemStackRarity(org.bukkit.inventory.ItemStack itemStack) {
        return ItemRarity.values()[getItem(itemStack.getType()).getRarity(CraftItemStack.asNMSCopy(itemStack)).ordinal()];
    }

    @Override
    public boolean isValidRepairItemStack(org.bukkit.inventory.ItemStack itemToBeRepaired, org.bukkit.inventory.ItemStack repairMaterial) {
        if (!itemToBeRepaired.getType().isItem() || !repairMaterial.getType().isItem()) {
            return false;
        }
        return CraftMagicNumbers.getItem(itemToBeRepaired.getType()).isValidRepairItem(CraftItemStack.asNMSCopy(itemToBeRepaired), CraftItemStack.asNMSCopy(repairMaterial));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getItemAttributes(Material material, EquipmentSlot equipmentSlot) {
        Item item = CraftMagicNumbers.getItem(material);
        if (item == null) {
            throw new IllegalArgumentException(material + " is not an item and therefore does not have attributes");
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeMapBuilder = ImmutableMultimap.builder();
        item.getDefaultAttributeModifiers(CraftEquipmentSlot.getNMS(equipmentSlot)).forEach((attributeBase, attributeModifier) -> {
            attributeMapBuilder.put(CraftAttributeMap.fromMinecraft(net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE.getKey(attributeBase).toString()), CraftAttributeInstance.convert(attributeModifier, equipmentSlot));
        });
        return attributeMapBuilder.build();
    }

    @Override
    public int getProtocolVersion() {
        return net.minecraft.SharedConstants.getCurrentVersion().getProtocolVersion();
    }

    @Override
    public boolean hasDefaultEntityAttributes(NamespacedKey bukkitEntityKey) {
        return net.minecraft.world.entity.ai.attributes.DefaultAttributes.hasSupplier(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(CraftNamespacedKey.toMinecraft(bukkitEntityKey)));
    }

    @Override
    public org.bukkit.attribute.Attributable getDefaultEntityAttributes(NamespacedKey bukkitEntityKey) {
        Preconditions.checkArgument(hasDefaultEntityAttributes(bukkitEntityKey), bukkitEntityKey + " doesn't have default attributes");
        var supplier = net.minecraft.world.entity.ai.attributes.DefaultAttributes.getSupplier((net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.LivingEntity>) net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(CraftNamespacedKey.toMinecraft(bukkitEntityKey)));
        return new UnmodifiableAttributeMap(supplier);
    }

    @Override
    public boolean isCollidable(Material material) {
        Preconditions.checkArgument(material.isBlock(), material + " is not a block");
        return getBlock(material).hasCollision;
    }

    @Override
    public org.bukkit.NamespacedKey getBiomeKey(org.bukkit.RegionAccessor accessor, int x, int y, int z) {
        org.bukkit.craftbukkit.v1_20_R1.CraftRegionAccessor cra = (org.bukkit.craftbukkit.v1_20_R1.CraftRegionAccessor) accessor;
        return org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey.fromMinecraft(cra.getHandle().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BIOME).getKey(cra.getHandle().getBiome(new net.minecraft.core.BlockPos(x, y, z)).value()));
    }

    @Override
    public void setBiomeKey(org.bukkit.RegionAccessor accessor, int x, int y, int z, org.bukkit.NamespacedKey biomeKey) {
        org.bukkit.craftbukkit.v1_20_R1.CraftRegionAccessor cra = (org.bukkit.craftbukkit.v1_20_R1.CraftRegionAccessor) accessor;
        net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biomeBase = cra.getHandle().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BIOME).getHolderOrThrow(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BIOME, org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey.toMinecraft(biomeKey)));
        cra.setBiome(x, y, z, biomeBase);
    }

    @Override
    public String getStatisticCriteriaKey(org.bukkit.Statistic statistic) {
        if (statistic.getType() != org.bukkit.Statistic.Type.UNTYPED) return "minecraft.custom:minecraft." + statistic.getKey().getKey();
        return org.bukkit.craftbukkit.v1_20_R1.CraftStatistic.getNMSStatistic(statistic).getName();
    }
    // Paper end

    /**
     * This helper class represents the different NBT Tags.
     * <p>
     * These should match Tag#getTypeId
     */
    public static class NBT {

        public static final int TAG_END = 0;
        public static final int TAG_BYTE = 1;
        public static final int TAG_SHORT = 2;
        public static final int TAG_INT = 3;
        public static final int TAG_LONG = 4;
        public static final int TAG_FLOAT = 5;
        public static final int TAG_DOUBLE = 6;
        public static final int TAG_BYTE_ARRAY = 7;
        public static final int TAG_STRING = 8;
        public static final int TAG_LIST = 9;
        public static final int TAG_COMPOUND = 10;
        public static final int TAG_INT_ARRAY = 11;
        public static final int TAG_ANY_NUMBER = 99;
    }
}
