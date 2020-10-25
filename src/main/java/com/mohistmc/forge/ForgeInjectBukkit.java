package com.mohistmc.forge;

import com.mohistmc.MohistMC;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.util.i18n.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionEffectType;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.permissions.DefaultPermissions;
import com.mohistmc.entity.CraftCustomEntity;

public class ForgeInjectBukkit {

    public static void init(){
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumEnchantment();
        addEnumPotion();
        addEnumBiome();
        addEnumPattern();
        addEnumEntity();
    }

    public static void addEnumMaterialInItems(){
        for (Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation key = entry.getKey();
            if(!key.getResourceDomain().equals("minecraft")) {
                // inject item materials into Bukkit for FML
                String[] res = key.toString().split(":");
                String modid = Material.normalizeName(res[0]);
                String materialName = Material.normalizeName(key.toString());
                Item item = entry.getValue();
                int id = Item.getIdFromItem(item);
                Material material = Material.addMaterial(materialName, id, false);
                if (material != null) {
                    // <bukkit_name, modid>
                    ItemAPI.MODNAME_MAP.put(material.name(), modid);
                    // <bukkit_id, modid>
                    ItemAPI.MODID_MAP.put(id, modid);
                    MohistMC.LOGGER.debug("Save: " + Message.getFormatString("injected.item", new Object[]{material.name(), String.valueOf(material.getId()), String.valueOf(ItemAPI.getBukkit(material).getDurability())}));
                }
            }
        }
    }

    public static void addEnumMaterialsInBlocks(){
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation key = entry.getKey();
            if(!key.getResourceDomain().equals("minecraft")) {
                // inject block materials into Bukkit for FML
                String[] res = key.toString().split(":");
                String modid = Material.normalizeName(res[0]);
                String materialName = Material.normalizeName(key.toString());
                Block block = entry.getValue();
                int id = Block.getIdFromBlock(block);
                Material material = Material.addMaterial(materialName, id, true);
                if (material != null) {
                    // <bukkit_name, modid>
                    ItemAPI.MODNAME_MAP.put(material.name(), modid);
                    // <bukkit_id, modid>
                    ItemAPI.MODID_MAP.put(id, modid);
                    MohistMC.LOGGER.debug("Save: " + Message.getFormatString("injected.block", new Object[]{material.name(), String.valueOf(material.getBlockID())}));
                }
            }
        }

        for (Material material : Material.values()) {
            int id = material.getId();
            if (id < 256) {
                material.blockById[id] = material;
            }
        }
    }

    public static void addEnumEnchantment() {
        // Enchantment
        for (Map.Entry<ResourceLocation, Enchantment> entry : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(entry.getValue()));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumPotion() {
        // Points
        for (Map.Entry<ResourceLocation, Potion> entry : ForgeRegistries.POTIONS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        for (Map.Entry<ResourceLocation, net.minecraft.world.biome.Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getKey().getResourcePath().toUpperCase(java.util.Locale.ENGLISH);
            if (!entry.getKey().getResourceDomain().equals("minecraft") && !map.contains(biomeName)) {
                map.add(biomeName);
                EnumHelper.addEnum(Biome.class, biomeName, new Class[0]);
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
                PatternType patternType = EnumHelper.addEnum(PatternType.class, p_i47246_3_, new Class[]{String.class}, hashname);
                PATTERN_MAP.put(hashname, patternType);
            }
        }
    }

    public static World.Environment addEnumEnvironment(int id, String name) {
        return (World.Environment)EnumHelper.addEnum(World.Environment.class, name, new Class[]{Integer.TYPE}, new Object[]{id});
    }

    public static WorldType addEnumWorldType(String name)
    {
        WorldType worldType = EnumHelper.addEnum(WorldType.class, name, new Class [] { String.class }, name);
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void addEnumEntity() {
        Map<String, EntityType> NAME_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<String, Class<? extends Entity>> entity : EntityRegistry.entityClassMap.entrySet()) {
            String name = entity.getKey();
            String entityType = name.toUpperCase();
            int typeId = GameData.getEntityRegistry().getID(EntityRegistry.getEntry(entity.getValue()));
            EntityType bukkitType = EnumHelper.addEnum(EntityType.class, entityType, new Class[] { String.class, Class.class, Integer.TYPE, Boolean.TYPE }, name, CraftCustomEntity.class, typeId, false);

            NAME_MAP.put(name.toLowerCase(), bukkitType);
            ID_MAP.put((short)typeId, bukkitType);
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
}
