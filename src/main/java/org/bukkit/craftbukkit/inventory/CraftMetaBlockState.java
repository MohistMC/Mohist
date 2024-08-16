package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BlockStateMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBlockState extends CraftMetaItem implements BlockStateMeta {

    private static final Set<Material> SHULKER_BOX_MATERIALS = Sets.newHashSet(
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX
    );

    private static final Set<Material> BLOCK_STATE_MATERIALS = Sets.newHashSet(
            Material.FURNACE,
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.JUKEBOX,
            Material.DISPENSER,
            Material.DROPPER,
            Material.ACACIA_HANGING_SIGN,
            Material.ACACIA_SIGN,
            Material.ACACIA_WALL_HANGING_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.BAMBOO_HANGING_SIGN,
            Material.BAMBOO_SIGN,
            Material.BAMBOO_WALL_HANGING_SIGN,
            Material.BAMBOO_WALL_SIGN,
            Material.BIRCH_HANGING_SIGN,
            Material.BIRCH_SIGN,
            Material.BIRCH_WALL_HANGING_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.CHERRY_HANGING_SIGN,
            Material.CHERRY_SIGN,
            Material.CHERRY_WALL_HANGING_SIGN,
            Material.CHERRY_WALL_SIGN,
            Material.CRIMSON_HANGING_SIGN,
            Material.CRIMSON_SIGN,
            Material.CRIMSON_WALL_HANGING_SIGN,
            Material.CRIMSON_WALL_SIGN,
            Material.DARK_OAK_HANGING_SIGN,
            Material.DARK_OAK_SIGN,
            Material.DARK_OAK_WALL_HANGING_SIGN,
            Material.DARK_OAK_WALL_SIGN,
            Material.JUNGLE_HANGING_SIGN,
            Material.JUNGLE_SIGN,
            Material.JUNGLE_WALL_HANGING_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.MANGROVE_HANGING_SIGN,
            Material.MANGROVE_SIGN,
            Material.MANGROVE_WALL_HANGING_SIGN,
            Material.MANGROVE_WALL_SIGN,
            Material.OAK_HANGING_SIGN,
            Material.OAK_SIGN,
            Material.OAK_WALL_HANGING_SIGN,
            Material.OAK_WALL_SIGN,
            Material.SPRUCE_HANGING_SIGN,
            Material.SPRUCE_SIGN,
            Material.SPRUCE_WALL_HANGING_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.WARPED_HANGING_SIGN,
            Material.WARPED_SIGN,
            Material.WARPED_WALL_HANGING_SIGN,
            Material.WARPED_WALL_SIGN,
            Material.SPAWNER,
            Material.BREWING_STAND,
            Material.ENCHANTING_TABLE,
            Material.COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.BEACON,
            Material.DAYLIGHT_DETECTOR,
            Material.HOPPER,
            Material.COMPARATOR,
            Material.SHIELD,
            Material.STRUCTURE_BLOCK,
            Material.ENDER_CHEST,
            Material.BARREL,
            Material.BELL,
            Material.BLAST_FURNACE,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.JIGSAW,
            Material.LECTERN,
            Material.SMOKER,
            Material.BEEHIVE,
            Material.BEE_NEST,
            Material.SCULK_CATALYST,
            Material.SCULK_SHRIEKER,
            Material.CALIBRATED_SCULK_SENSOR,
            Material.SCULK_SENSOR,
            Material.CHISELED_BOOKSHELF,
            Material.DECORATED_POT,
            Material.SUSPICIOUS_SAND,
            Material.SUSPICIOUS_GRAVEL,
            Material.TRIAL_SPAWNER,
            Material.CRAFTER,
            Material.VAULT
    );

    static {
        // Add shulker boxes to the list of block state materials too
        BLOCK_STATE_MATERIALS.addAll(SHULKER_BOX_MATERIALS);
    }

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<CustomData> BLOCK_ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.BLOCK_ENTITY_DATA, "BlockEntityTag");

    final Material material;
    private CraftBlockEntityState<?> blockEntityTag;
    private CompoundTag internalTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof CraftMetaBlockState)
                || ((CraftMetaBlockState) meta).material != material) {
            this.blockEntityTag = null;
            return;
        }

        CraftMetaBlockState te = (CraftMetaBlockState) meta;
        this.blockEntityTag = te.blockEntityTag;
    }

    CraftMetaBlockState(DataComponentPatch tag, Material material) {
        super(tag);
        this.material = material;

        getOrEmpty(tag, CraftMetaBlockState.BLOCK_ENTITY_TAG).ifPresent((nbt) -> {
            this.blockEntityTag = CraftMetaBlockState.getBlockState(material, nbt.copyTag());
        });

        if (!tag.isEmpty()) {
            CraftBlockEntityState<?> blockEntityTag = this.blockEntityTag;
            if (blockEntityTag == null) {
                blockEntityTag = CraftMetaBlockState.getBlockState(material, null);
            }

            // Convert to map
            PatchedDataComponentMap map = new PatchedDataComponentMap(DataComponentMap.EMPTY);
            map.applyPatch(tag);
            // Apply
            Set<DataComponentType<?>> applied = blockEntityTag.applyComponents(map, tag);
            // Mark applied components as handled
            for (DataComponentType<?> seen : applied) {
                this.unhandledTags.clear(seen);
            }
            // Only set blockEntityTag if something was applied
            if (!applied.isEmpty()) {
                this.blockEntityTag = blockEntityTag;
            }
        }
    }

    CraftMetaBlockState(Map<String, Object> map) {
        super(map);
        String matName = SerializableMeta.getString(map, "blockMaterial", true);
        Material m = Material.getMaterial(matName);
        if (m != null) {
            this.material = m;
        } else {
            this.material = Material.AIR;
        }
        if (internalTag != null) {
            blockEntityTag = getBlockState(material, internalTag);
            internalTag = null;
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.blockEntityTag != null) {
            tag.put(CraftMetaBlockState.BLOCK_ENTITY_TAG, CustomData.of(this.blockEntityTag.getSnapshotNBTWithoutComponents()));

            for (TypedDataComponent<?> component : this.blockEntityTag.collectComponents()) {
                tag.putIfAbsent(component);
            }
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            this.internalTag = tag.getCompound(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(final Map<String, Tag> internalTags) {
        if (this.blockEntityTag != null) {
            internalTags.put(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, this.blockEntityTag.getSnapshotNBT());
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put("blockMaterial", this.material.name());
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        return original != hash ? CraftMetaBlockState.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBlockState) {
            CraftMetaBlockState that = (CraftMetaBlockState) meta;

            return Objects.equal(this.blockEntityTag, that.blockEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || this.blockEntityTag == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.blockEntityTag == null;
    }

    @Override
    boolean applicableTo(Material type) {
        return CraftMetaBlockState.BLOCK_STATE_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaBlockState clone() {
        CraftMetaBlockState meta = (CraftMetaBlockState) super.clone();
        if (this.blockEntityTag != null) {
            meta.blockEntityTag = this.blockEntityTag.copy();
        }
        return meta;
    }

    @Override
    public boolean hasBlockState() {
        return this.blockEntityTag != null;
    }

    @Override
    public BlockState getBlockState() {
        return (this.blockEntityTag != null) ? this.blockEntityTag.copy() : CraftMetaBlockState.getBlockState(this.material, null);
    }

    private static CraftBlockEntityState<?> getBlockState(Material material, CompoundTag blockEntityTag) {
        BlockPos pos = BlockPos.ZERO;
        Material stateMaterial = (material != Material.SHIELD) ? material : CraftMetaBlockState.shieldToBannerHack(); // Only actually used for jigsaws
        if (blockEntityTag != null) {
            if (material == Material.SHIELD) {
                blockEntityTag.putString("id", "minecraft:banner");
            } else if (material == Material.BEE_NEST || material == Material.BEEHIVE) {
                blockEntityTag.putString("id", "minecraft:beehive");
            } else if (CraftMetaBlockState.SHULKER_BOX_MATERIALS.contains(material)) {
                blockEntityTag.putString("id", "minecraft:shulker_box");
            }

            pos = BlockEntity.getPosFromTag(blockEntityTag);
        }

        // This is expected to always return a CraftBlockEntityState for the passed material:
        return (CraftBlockEntityState<?>) CraftBlockStates.getBlockState(pos, stateMaterial, blockEntityTag);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");

        Material stateMaterial = (this.material != Material.SHIELD) ? this.material : CraftMetaBlockState.shieldToBannerHack();
        Class<?> blockStateType = CraftBlockStates.getBlockStateType(stateMaterial);
        Preconditions.checkArgument(blockStateType == blockState.getClass() && blockState instanceof CraftBlockEntityState, "Invalid blockState for " + this.material);

        this.blockEntityTag = (CraftBlockEntityState<?>) blockState;
    }

    private static Material shieldToBannerHack() {
        return Material.WHITE_BANNER;
    }
}
