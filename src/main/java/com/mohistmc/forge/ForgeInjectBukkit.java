package com.mohistmc.forge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mohistmc.MohistMC;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.entity.CraftCustomEntity;
import com.mohistmc.util.MohistEnumHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_16_R3.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.permissions.DefaultPermissions;

import java.util.Map;

public class ForgeInjectBukkit {

    public static BiMap<RegistryKey<DimensionType>, World.Environment> environment =
            HashBiMap.create(ImmutableMap.<RegistryKey<DimensionType>, World.Environment>builder()
                    .put(DimensionType.OVERWORLD, World.Environment.NORMAL)
                    .put(DimensionType.THE_NETHER, World.Environment.NETHER)
                    .put(DimensionType.THE_END, World.Environment.THE_END)
                    .build());

    public static Map<Villager.Profession, ResourceLocation> profession = new HashMap<>();

    public static void init(){
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumBiome();
        addEnumEnchantment();
        addEnumPotion();
        addEnumPattern();
        addEnumEntity();
        addEnumVillagerProfession();
    }


    public static void addEnumMaterialInItems(){
        for (Map.Entry<RegistryKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if(!resourceLocation.getNamespace().equals("minecraft")) {
                // inject item materials into Bukkit for FML
                String materialName = Material.normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_ITEM__", "");
                Item item = entry.getValue();
                int id = Item.getIdFromItem(item);
                Material material = Material.addMaterial(materialName, id, false);
                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                if (material != null) {
                    MohistMC.LOGGER.debug("Save-ITEM: " + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumMaterialsInBlocks(){
        for (Map.Entry<RegistryKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if(!resourceLocation.getNamespace().equals("minecraft")) {
                // inject block materials into Bukkit for FML
                String materialName = Material.normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_BLOCK__", "");
                Block block = entry.getValue();
                int id = Item.getIdFromItem(block.asItem());
                Material material = Material.addMaterial(materialName, id, true);
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
        for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(entry.getValue()));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumPotion() {
        // Points
        for (Map.Entry<RegistryKey<Effect>, Effect> entry : ForgeRegistries.POTIONS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        for (Map.Entry<RegistryKey<Biome>, Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getValue().getRegistryName().getNamespace();
            if (!biomeName.equals("minecraft") && !map.contains(biomeName)) {
                map.add(biomeName);
                MohistEnumHelper.addEnum0(org.bukkit.block.Biome.class, biomeName, new Class[0]);
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
        Registry<DimensionType> registry = MinecraftServer.getServer().func_244267_aX().getRegistry(Registry.DIMENSION_TYPE_KEY);
        for (Map.Entry<RegistryKey<DimensionType>, DimensionType> entry : registry.getEntries()) {
            RegistryKey<DimensionType> key = entry.getKey();
            World.Environment environment1 = environment.get(key);
            if (environment1 == null) {
                String name = Material.normalizeName(key.getLocation().toString());
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
        Map<String, EntityType> NAME_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<RegistryKey<net.minecraft.entity.EntityType<?>>, net.minecraft.entity.EntityType<?>> entity : ForgeRegistries.ENTITIES.getEntries()) {
            String name = entity.getValue().getRegistryName().getNamespace();
            String entityType = name.toUpperCase();
            int typeId = name.hashCode();
            EntityType bukkitType = MohistEnumHelper.addEnum0(EntityType.class, entityType, new Class[] { String.class, Class.class, Integer.TYPE, Boolean.TYPE }, name, CraftCustomEntity.class, typeId, false);

            NAME_MAP.put(name.toLowerCase(), bukkitType);
            ID_MAP.put((short)typeId, bukkitType);
            ServerAPI.entityTypeMap.put(entity.getValue(), entityType);
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
            if(!resourceLocation.getNamespace().equals("minecraft")) {
                Villager.Profession vp = MohistEnumHelper.addEnum0(Villager.Profession.class, name, new Class[0]);
                profession.put(vp, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge VillagerProfession as Profession {}", vp.name());
            }
        }
    }

    public static String normalizeName(String name) {
        return name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
    }
}
