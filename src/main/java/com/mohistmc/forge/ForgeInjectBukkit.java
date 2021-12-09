package com.mohistmc.forge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mohistmc.MohistMC;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.entity.MohistModsEntity;
import com.mohistmc.util.MohistEnumHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import org.bukkit.*;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_18_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_18_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.permissions.DefaultPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgeInjectBukkit {

    public static BiMap<ResourceKey<DimensionType>, World.Environment> environment =
            HashBiMap.create(ImmutableMap.<ResourceKey<DimensionType>, World.Environment>builder()
                    .put(DimensionType.OVERWORLD_LOCATION, World.Environment.NORMAL)
                    .put(DimensionType.NETHER_LOCATION, World.Environment.NETHER)
                    .put(DimensionType.END_LOCATION, World.Environment.THE_END)
                    .build());

    public static Map<Villager.Profession, ResourceLocation> profession = new HashMap<>();
    public static Map<org.bukkit.attribute.Attribute, ResourceLocation> attributemap = new HashMap<>();
    public static Map<Motive, Art> artMap = new HashMap<>();

    public static void init(){
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumBiome();
        //addEnumEnchantment();
        //addEnumPotion();
        addEnumPattern();
        addEnumEntity();
        addEnumVillagerProfession();
        //addEnumAttribute();
        //addEnumArt();
    }


    public static void addEnumMaterialInItems(){
        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                // inject item materials into Bukkit for FML
                String materialName = normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_ITEM__", "");
                Item item = entry.getValue();
                int id = Item.getId(item);
                Material material = Material.addMaterial(materialName, id, false, resourceLocation.getNamespace());
                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                if (material != null) {
                    MohistMC.LOGGER.debug("Save-ITEM: " + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumMaterialsInBlocks(){
        for (Map.Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                // inject block materials into Bukkit for FML
                String materialName = normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_BLOCK__", "");
                Block block = entry.getValue();
                int id = Item.getId(block.asItem());
                Material material = Material.addMaterial(materialName, id, true, resourceLocation.getNamespace());
                CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
                if (material != null) {
                    MohistMC.LOGGER.debug("Save-BLOCK:" + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumEnchantment() {
        // Enchantment
        for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(entry.getValue()));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumPotion() {
        // Points
        for (Map.Entry<ResourceKey<MobEffect>, MobEffect> entry : ForgeRegistries.MOB_EFFECTS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        for (Map.Entry<ResourceKey<Biome>, Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getValue().getRegistryName().getNamespace();
            if (!biomeName.equals(NamespacedKey.MINECRAFT) && !map.contains(biomeName)) {
                map.add(biomeName);
                org.bukkit.block.Biome biome = MohistEnumHelper.addEnum0(org.bukkit.block.Biome.class, biomeName, new Class[0]);
                MohistMC.LOGGER.debug("Save-BIOME:" + biome.name() + " - " + biomeName);
            }
        }
        map.clear();
    }

    public static void addEnumPattern(){
        Map<String, PatternType> PATTERN_MAP = ObfuscationReflectionHelper.getPrivateValue(PatternType.class, null, "byString");
        for (BannerPattern bannerpattern : BannerPattern.values()) {
            String p_i47246_3_ = bannerpattern.name();
            String hashname = bannerpattern.getHashname();
            if (PatternType.getByIdentifier(hashname) == null) {
                PatternType patternType = MohistEnumHelper.addEnum0(PatternType.class, p_i47246_3_, new Class[]{String.class}, hashname);
                PATTERN_MAP.put(hashname, patternType);
            }
        }
    }

    public static void addEnumEnvironment() {
        int i = World.Environment.values().length;
        net.minecraft.core.Registry<DimensionType> registry = MinecraftServer.getServer().registryHolder.dimensionTypes();
        for (Map.Entry<ResourceKey<DimensionType>, DimensionType> entry : registry.entrySet()) {
            ResourceKey<DimensionType> key = entry.getKey();
            World.Environment environment1 = environment.get(key);
            if (environment1 == null) {
                String name = normalizeName(key.location().toString());
                int id = i - 1;
                environment1 = MohistEnumHelper.addEnum(World.Environment.class, name, new Class[]{Integer.TYPE}, new Object[]{id});
                environment.put(key, environment1);
                MohistMC.LOGGER.debug("Registered forge DimensionType as environment {}", environment1);
                i++;
            }
        }
    }

    public static WorldType addEnumWorldType(String name)
    {
        WorldType worldType = MohistEnumHelper.addEnum0(WorldType.class, name, new Class [] { String.class }, name);
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void addEnumEntity() {
        Map<String, EntityType> NAME_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<ResourceKey<net.minecraft.world.entity.EntityType<?>>, net.minecraft.world.entity.EntityType<?>> entity : ForgeRegistries.ENTITIES.getEntries()) {
            ResourceLocation resourceLocation = entity.getValue().getRegistryName();
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String entityType = normalizeName(resourceLocation.toString());
                int typeId = entityType.hashCode();
                EntityType bukkitType = MohistEnumHelper.addEnum0(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, entityType.toLowerCase(), MohistModsEntity.class, typeId, false);
                NAME_MAP.put(entityType.toLowerCase(), bukkitType);
                ID_MAP.put((short) typeId, bukkitType);
                ServerAPI.entityTypeMap.put(entity.getValue(), entityType);
            }
        }
    }

    public static void registerDefaultPermission(String name, DefaultPermissionLevel level, String desc) {
        PermissionDefault permissionDefault;
        switch (level) {
            case ALL:
                permissionDefault = PermissionDefault.TRUE;
                break;
            case OP:
                permissionDefault = PermissionDefault.OP;
                break;
            case NONE:
            default:
                permissionDefault = PermissionDefault.FALSE;
                break;
        }
        Permission permission = new Permission(name, desc, permissionDefault);
        DefaultPermissions.registerPermission(permission, false);
    }

    public static void addEnumVillagerProfession() {
        for (VillagerProfession villagerProfession : ForgeRegistries.PROFESSIONS) {
            ResourceLocation resourceLocation = villagerProfession.getRegistryName();
            String name = normalizeName(resourceLocation.toString());
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                Villager.Profession vp = MohistEnumHelper.addEnum0(Villager.Profession.class, name, new Class[0]);
                profession.put(vp, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge VillagerProfession as Profession {}", vp.name());
            }
        }
    }

    public static void addEnumAttribute() {
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES) {
            ResourceLocation resourceLocation = attribute.getRegistryName();
            String name = normalizeName(resourceLocation.getPath());
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                org.bukkit.attribute.Attribute ab = MohistEnumHelper.addEnum0(org.bukkit.attribute.Attribute.class, name, new Class[]{String.class}, resourceLocation.getPath());
                attributemap.put(ab, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge Attribute as Attribute(Bukkit) {}", ab.name());
            }
        }
    }

    public static void addEnumArt() {
        int i = Art.values().length;
        HashMap<String, Art> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(Art.class, null, "BY_NAME");
        HashMap<Integer, Art> BY_ID = ObfuscationReflectionHelper.getPrivateValue(Art.class, null, "BY_ID");
        for (Motive entry : ForgeRegistries.PAINTING_TYPES) {
            int width = entry.getWidth();
            int height = entry.getHeight();
            ResourceLocation resourceLocation = entry.getRegistryName();
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String name = normalizeName(resourceLocation.toString());
                int id = i - 1;
                Art art = MohistEnumHelper.addEnum(Art.class, name, new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}, new Object[]{id, width, height});
                artMap.put(entry, art);
                BY_NAME.put(name, art);
                BY_ID.put(id, art);
                MohistMC.LOGGER.debug("Registered forge PaintingType as Art {}", art);
                i++;
            }
        }
    }

    public static String normalizeName(String name) {
        return name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
    }
}
