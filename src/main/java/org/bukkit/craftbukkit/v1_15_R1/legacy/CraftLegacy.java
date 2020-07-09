package org.bukkit.craftbukkit.v1_15_R1.legacy;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.util.datafix.fixes.BlockStateFlatteningMap;
import net.minecraft.util.datafix.fixes.ItemIntIDToString;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.util.registry.Registry;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

import java.util.*;

/**
 * This class may seem unnecessarily slow and complicated/repetitive however it
 * is able to handle a lot more edge cases and invertible transformations (many
 * of which are not immediately obvious) than any other alternative. If you do
 * make changes to this class please make sure to contribute them back
 * https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse so
 * that all may benefit.
 *
 * @deprecated legacy use only
 */
@Deprecated
public final class CraftLegacy {

    private static final Map<Byte, Material> SPAWN_EGGS = new HashMap<>();
    private static final Set<String> whitelistedStates = new HashSet<>(Arrays.asList("explode", "check_decay", "decayable", "facing"));
    private static final Map<MaterialData, Item> materialToItem = new HashMap<>(16384);
    private static final Map<Item, MaterialData> itemToMaterial = new HashMap<>(1024);
    private static final Map<MaterialData, BlockState> materialToData = new HashMap<>(4096);
    private static final Map<BlockState, MaterialData> dataToMaterial = new HashMap<>(4096);
    private static final Map<MaterialData, Block> materialToBlock = new HashMap<>(4096);
    private static final Map<Block, MaterialData> blockToMaterial = new HashMap<>(1024);

    private CraftLegacy() {
        //
    }

    public static Material toLegacy(Material material) {
        if (material == null || material.isLegacy()) {
            return material;
        }

        return toLegacyData(material).getItemType();
    }

    public static MaterialData toLegacyData(Material material) {
        Preconditions.checkArgument(!material.isLegacy(), "toLegacy on legacy Material");
        MaterialData mappedData;

        if (material.isBlock()) {
            Block block = CraftMagicNumbers.getBlock(material);
            BlockState blockData = block.getDefaultState();

            // Try exact match first
            mappedData = dataToMaterial.get(blockData);
            // Fallback to any block
            if (mappedData == null) {
                mappedData = blockToMaterial.get(block);
                // Fallback to matching item
                if (mappedData == null) {
                    mappedData = itemToMaterial.get(block.asItem());
                }
            }
        } else {
            Item item = CraftMagicNumbers.getItem(material);
            mappedData = itemToMaterial.get(item);
        }

        return (mappedData == null) ? new MaterialData(Material.LEGACY_AIR) : mappedData;
    }

    public static BlockState fromLegacyData(Material material, byte data) {
        Preconditions.checkArgument(material.isLegacy(), "fromLegacyData on modern Material");

        MaterialData materialData = new MaterialData(material, data);

        // Try exact match first
        BlockState converted = materialToData.get(materialData);
        if (converted != null) {
            return converted;
        }

        // Fallback to any block
        Block convertedBlock = materialToBlock.get(materialData);
        if (convertedBlock != null) {
            return convertedBlock.getDefaultState();
        }

        // Return air
        return Blocks.AIR.getDefaultState();
    }

    public static Item fromLegacyData(Material material, short data) {
        Preconditions.checkArgument(material.isLegacy(), "fromLegacyData on modern Material. Did you forget to define a modern (1.13+) api-version in your plugin.yml?");

        MaterialData materialData = new MaterialData(material, (byte) data);

        // First try matching item
        Item convertedItem = materialToItem.get(materialData);
        if (convertedItem != null) {
            return convertedItem;
        }

        // Fallback to matching block
        if (material.isBlock()) {
            // Try exact match first
            BlockState converted = materialToData.get(materialData);
            if (converted != null) {
                return converted.getBlock().asItem();
            }

            // Fallback to any block
            Block convertedBlock = materialToBlock.get(materialData);
            if (convertedBlock != null) {
                return convertedBlock.asItem();
            }
        }

        // Return air
        return Items.AIR;
    }

    public static byte toLegacyData(BlockState blockData) {
        return toLegacy(blockData).getData();
    }

    public static Material toLegacyMaterial(BlockState blockData) {
        return toLegacy(blockData).getItemType();
    }

    public static MaterialData toLegacy(BlockState blockData) {
        MaterialData mappedData;

        // Try exact match first
        mappedData = dataToMaterial.get(blockData);
        // Fallback to any block
        if (mappedData == null) {
            mappedData = blockToMaterial.get(blockData.getBlock());
        }

        return (mappedData == null) ? new MaterialData(Material.LEGACY_AIR) : mappedData;
    }

    public static Material fromLegacy(Material material) {
        if (material == null || !material.isLegacy()) {
            return material;
        }

        return fromLegacy(new MaterialData(material));
    }

    public static Material fromLegacy(MaterialData materialData) {
        return fromLegacy(materialData, false);
    }

    public static Material fromLegacy(MaterialData materialData, boolean itemPriority) {
        Material material = materialData.getItemType();
        if (material == null || !material.isLegacy()) {
            return material;
        }

        Material mappedData = null;

        // Try item first
        if (itemPriority) {
            Item item = materialToItem.get(materialData);
            if (item != null) {
                mappedData = CraftMagicNumbers.getMaterial(item);
            }
        }

        if (mappedData == null && material.isBlock()) {
            // Try exact match first
            BlockState iblock = materialToData.get(materialData);
            if (iblock != null) {
                mappedData = CraftMagicNumbers.getMaterial(iblock.getBlock());
            }

            // Fallback to any block
            if (mappedData == null) {
                Block block = materialToBlock.get(materialData);
                if (block != null) {
                    mappedData = CraftMagicNumbers.getMaterial(block);
                }
            }
        }

        // Fallback to matching item
        if (!itemPriority && mappedData == null) {
            Item item = materialToItem.get(materialData);
            if (item != null) {
                mappedData = CraftMagicNumbers.getMaterial(item);
            }
        }

        return (mappedData == null) ? Material.AIR : mappedData;
    }

    public static Material[] values() {
        Material[] values = Material.values();
        return Arrays.copyOfRange(values, Material.LEGACY_AIR.ordinal(), values.length);
    }

    public static Material valueOf(String name) {
        return (name.startsWith(Material.LEGACY_PREFIX)) ? Material.valueOf(name) : Material.valueOf(Material.LEGACY_PREFIX + name);
    }

    public static Material getMaterial(String name) {
        return (name.startsWith(Material.LEGACY_PREFIX)) ? Material.getMaterial(name) : Material.getMaterial(Material.LEGACY_PREFIX + name);
    }

    public static Material matchMaterial(String name) {
        return (name.startsWith(Material.LEGACY_PREFIX)) ? Material.matchMaterial(name) : Material.matchMaterial(Material.LEGACY_PREFIX + name);
    }

    public static int ordinal(Material material) {
        Preconditions.checkArgument(material.isLegacy(), "ordinal on modern Material");

        return material.ordinal() - Material.LEGACY_AIR.ordinal();
    }

    public static String name(Material material) {
        return material.name().substring(Material.LEGACY_PREFIX.length());
    }

    public static String toString(Material material) {
        return name(material);
    }

    public static void init() {
        //
    }

    static {
        System.err.println("Initializing Legacy Material Support. Unless you have legacy plugins and/or data this is a bug!");
        if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isDebuggingEnabled()) {
            new Exception().printStackTrace();
        }

        SPAWN_EGGS.put((byte) 0, Material.PIG_SPAWN_EGG); // Will be fixed by updateMaterial if possible

        SPAWN_EGGS.put((byte) EntityType.BAT.getTypeId(), Material.BAT_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.BLAZE.getTypeId(), Material.BLAZE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.CAVE_SPIDER.getTypeId(), Material.CAVE_SPIDER_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.CHICKEN.getTypeId(), Material.CHICKEN_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.COD.getTypeId(), Material.COD_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.COW.getTypeId(), Material.COW_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.CREEPER.getTypeId(), Material.CREEPER_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.DOLPHIN.getTypeId(), Material.DOLPHIN_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.DONKEY.getTypeId(), Material.DONKEY_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.ELDER_GUARDIAN.getTypeId(), Material.ELDER_GUARDIAN_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.ENDERMAN.getTypeId(), Material.ENDERMAN_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.ENDERMITE.getTypeId(), Material.ENDERMITE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.EVOKER.getTypeId(), Material.EVOKER_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.GHAST.getTypeId(), Material.GHAST_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.GUARDIAN.getTypeId(), Material.GUARDIAN_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.HORSE.getTypeId(), Material.HORSE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.HUSK.getTypeId(), Material.HUSK_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.LLAMA.getTypeId(), Material.LLAMA_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.MAGMA_CUBE.getTypeId(), Material.MAGMA_CUBE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.MUSHROOM_COW.getTypeId(), Material.MOOSHROOM_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.MULE.getTypeId(), Material.MULE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.OCELOT.getTypeId(), Material.OCELOT_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.PARROT.getTypeId(), Material.PARROT_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.PIG.getTypeId(), Material.PIG_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.PHANTOM.getTypeId(), Material.PHANTOM_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.POLAR_BEAR.getTypeId(), Material.POLAR_BEAR_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.PUFFERFISH.getTypeId(), Material.PUFFERFISH_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.RABBIT.getTypeId(), Material.RABBIT_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SALMON.getTypeId(), Material.SALMON_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SHEEP.getTypeId(), Material.SHEEP_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SHULKER.getTypeId(), Material.SHULKER_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SILVERFISH.getTypeId(), Material.SILVERFISH_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SKELETON.getTypeId(), Material.SKELETON_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SKELETON_HORSE.getTypeId(), Material.SKELETON_HORSE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SLIME.getTypeId(), Material.SLIME_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SPIDER.getTypeId(), Material.SPIDER_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.SQUID.getTypeId(), Material.SQUID_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.STRAY.getTypeId(), Material.STRAY_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.TROPICAL_FISH.getTypeId(), Material.TROPICAL_FISH_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.TURTLE.getTypeId(), Material.TURTLE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.VEX.getTypeId(), Material.VEX_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.VILLAGER.getTypeId(), Material.VILLAGER_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.VINDICATOR.getTypeId(), Material.VINDICATOR_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.WITCH.getTypeId(), Material.WITCH_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.WITHER_SKELETON.getTypeId(), Material.WITHER_SKELETON_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.WOLF.getTypeId(), Material.WOLF_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.ZOMBIE.getTypeId(), Material.ZOMBIE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.ZOMBIE_HORSE.getTypeId(), Material.ZOMBIE_HORSE_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.PIG_ZOMBIE.getTypeId(), Material.ZOMBIE_PIGMAN_SPAWN_EGG);
        SPAWN_EGGS.put((byte) EntityType.ZOMBIE_VILLAGER.getTypeId(), Material.ZOMBIE_VILLAGER_SPAWN_EGG);

        Bootstrap.register();

        for (Material material : Material.values()) {
            if (!material.isLegacy()) {
                continue;
            }

            // Handle blocks
            if (material.isBlock()) {
                for (byte data = 0; data < 16; data++) {
                    MaterialData matData = new MaterialData(material, data);
                    Dynamic blockTag = BlockStateFlatteningMap.getFixedNBTForID(material.getId() << 4 | data);
                    blockTag = DataFixesManager.getDataFixer().update(TypeReferences.BLOCK_STATE, blockTag, 100, CraftMagicNumbers.INSTANCE.getDataVersion());
                    // TODO: better skull conversion, chests
                    if (blockTag.get("Name").asString("").contains("%%FILTER_ME%%")) {
                        continue;
                    }

                    String name = blockTag.get("Name").asString("");
                    Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(name));
                    if (block == null) {
                        continue;
                    }
                    BlockState blockData = block.getDefaultState();
                    StateContainer states = block.getStateContainer();

                    Optional<CompoundNBT> propMap = blockTag.getElement("Properties");
                    if (propMap.isPresent()) {
                        CompoundNBT properties = propMap.get();
                        for (String dataKey : properties.keySet()) {
                            IProperty state = states.getProperty(dataKey);

                            if (state == null) {
                                if (whitelistedStates.contains(dataKey)) {
                                    continue;
                                }
                                throw new IllegalStateException("No state for " + dataKey);
                            }

                            Preconditions.checkState(!properties.getString(dataKey).isEmpty(), "Empty data string");
                            Optional opt = state.parseValue(properties.getString(dataKey));
                            if (!opt.isPresent()) {
                                throw new IllegalStateException("No state value " + properties.getString(dataKey) + " for " + dataKey);
                            }
                            blockData = blockData.with(state, (Comparable) opt.get());
                        }
                    }

                    if (block == Blocks.AIR) {
                        continue;
                    }

                    materialToData.put(matData, blockData);
                    if (!dataToMaterial.containsKey(blockData)) {
                        dataToMaterial.put(blockData, matData);
                    }

                    materialToBlock.put(matData, block);
                    if (!blockToMaterial.containsKey(block)) {
                        blockToMaterial.put(block, matData);
                    }
                }
            }

            // Handle items (and second fallback for blocks)
            int maxData = material.getMaxDurability() == 0 ? 16 : 1;
            // Manually do oldold spawn eggs
            if (material == Material.LEGACY_MONSTER_EGG) {
                maxData = 121; // Vilager + 1
            }

            for (byte data = 0; data < maxData; data++) {
                // Manually skip invalid oldold spawn
                if (material == Material.LEGACY_MONSTER_EGG /*&& data != 0 && EntityType.fromId(data) == null*/) { // Mojang broke 18w19b
                    continue;
                }
                // Skip non item stacks for now (18w19b)
                if (ItemIntIDToString.getItem(material.getId()) == null) {
                    continue;
                }

                MaterialData matData = new MaterialData(material, data);

                CompoundNBT stack = new CompoundNBT();
                stack.putInt("id", material.getId());
                stack.putShort("Damage", data);

                Dynamic<INBT> converted = DataFixesManager.getDataFixer().update(TypeReferences.ITEM_STACK, new Dynamic<INBT>(NBTDynamicOps.INSTANCE, stack), -1, CraftMagicNumbers.INSTANCE.getDataVersion());

                String newId = converted.get("id").asString("");
                // Recover spawn eggs with invalid data
                if (newId.equals("minecraft:spawn_egg")) {
                    newId = "minecraft:pig_spawn_egg";
                }

                // Preconditions.checkState(newId.contains("minecraft:"), "Unknown new material for " + matData);
                Item newMaterial = Registry.ITEM.getOrDefault(new ResourceLocation(newId));

                if (newMaterial == Items.AIR) {
                    continue;
                }

                materialToItem.put(matData, newMaterial);
                if (!itemToMaterial.containsKey(newMaterial)) {
                    itemToMaterial.put(newMaterial, matData);
                }
            }

            for (Map.Entry<Byte, Material> entry : SPAWN_EGGS.entrySet()) {
                MaterialData matData = new MaterialData(Material.LEGACY_MONSTER_EGG, entry.getKey());
                Item newMaterial = CraftMagicNumbers.getItem(entry.getValue());

                materialToItem.put(matData, newMaterial);
                itemToMaterial.put(newMaterial, matData);
            }
        }
    }

    public static void main(String[] args) {
        System.err.println("");
    }
}
