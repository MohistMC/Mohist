package red.mohist.forge.registry;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftCustomEntity;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import red.mohist.forge.MohistMod;
import red.mohist.util.MohistEnumHelper;

public class ForgeInjectBukkit {

    public static void init() {
        addForgeItems();
        addForgeBlocks();
        addForgeEntitys();
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
                if (material != null) {
                    MohistMod.LOGGER.info(String.format("Injected new Forge item material %s.", material.name()));
                } else {
                    MohistMod.LOGGER.info(String.format("Inject item failure %s with ID %d.", materialName, Item.getIdFromItem(item)));
                }
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
                if (material != null) {
                    MohistMod.LOGGER.info(String.format("Injected new Forge block material %s.", material.name()));
                } else {
                    MohistMod.LOGGER.info(String.format("Inject block failure %s with ID %d.", materialName, Item.getIdFromItem(block.asItem())));
                }
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

}
