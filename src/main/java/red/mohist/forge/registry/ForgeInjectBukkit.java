package red.mohist.forge.registry;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import red.mohist.api.Unsafe;
import red.mohist.entity.CraftCustomEntity;
import red.mohist.forge.MohistMod;
import red.mohist.util.MohistEnumHelper;

public class ForgeInjectBukkit {

    private static final Map<String, EntityType> ENTITY_NAME_MAP = getStatic(EntityType.class, "NAME_MAP");
    private static final List<Class<?>> ENTITY_CTOR = ImmutableList.of(String.class, Class.class, int.class);

    public static void init() {
        addForgeItems();
        addForgeBlocks();
        addForgeEntitys();
        addForgePotion();
    }

    private static void addForgeItems() {
        for (Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation key = entry.getKey();
            Item item = entry.getValue();
            if (!key.getNamespace().equals("minecraft")) {
                String materialName = key.toString().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
                Material material = Material
                        .addMaterial(MohistEnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE, Integer.TYPE}, new Object[]{Item.getIdFromItem(item), item.getMaxStackSize()}));
                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
            }
        }
    }

    private static void addForgeBlocks() {
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation key = entry.getKey();
            Block block = entry.getValue();
            if (!key.getNamespace().equals("minecraft")) {
                String materialName = key.toString().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
                Material material = Material.addMaterial(MohistEnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE}, new Object[]{Item.getIdFromItem(block.asItem())}));
                CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
            }
        }
    }

    private static void addForgeEntitys() {
        Map<String, EntityType> NAME_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Entry<ResourceLocation, net.minecraft.entity.EntityType<?>> entity : ForgeRegistries.ENTITIES.getEntries()) {
            String entityname = entity.getKey().getNamespace();
            String entityType = entityname.replace("-", "_").toUpperCase();

            EntityType bukkitType = MohistEnumHelper
                    .addEnum(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE},
                            new Object[]{entityname, CraftCustomEntity.class, entity.getKey().getNamespace().hashCode(), false});

            NAME_MAP.put(entityname.toLowerCase(), bukkitType);
            ID_MAP.put((short) entity.getKey().getNamespace().hashCode(), bukkitType);
        }
    }

    public static void addForgePotion() {
        // Points
        for (Map.Entry<ResourceLocation, Effect> entry : ForgeRegistries.POTIONS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    private static <T> T getStatic(Class<?> cl, String name) {
        try {
            Unsafe.ensureClassInitialized(cl);
            Field field = cl.getDeclaredField(name);
            Object materialByNameBase = Unsafe.staticFieldBase(field);
            long materialByNameOffset = Unsafe.staticFieldOffset(field);
            return (T) Unsafe.getObject(materialByNameBase, materialByNameOffset);
        } catch (Exception e) {
            return null;
        }
    }
}
