package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.block.entity.ComparatorBlockEntity;
import net.minecraft.block.entity.DaylightDetectorBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DyeColor;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBanner;
import org.bukkit.craftbukkit.block.CraftBarrel;
import org.bukkit.craftbukkit.block.CraftBeacon;
import org.bukkit.craftbukkit.block.CraftBeehive;
import org.bukkit.craftbukkit.block.CraftBell;
import org.bukkit.craftbukkit.block.CraftBlastFurnace;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBrewingStand;
import org.bukkit.craftbukkit.block.CraftCampfire;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.craftbukkit.block.CraftCommandBlock;
import org.bukkit.craftbukkit.block.CraftComparator;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.block.CraftDaylightDetector;
import org.bukkit.craftbukkit.block.CraftDispenser;
import org.bukkit.craftbukkit.block.CraftDropper;
import org.bukkit.craftbukkit.block.CraftEnchantingTable;
import org.bukkit.craftbukkit.block.CraftEndGateway;
import org.bukkit.craftbukkit.block.CraftEnderChest;
import org.bukkit.craftbukkit.block.CraftFurnace;
import org.bukkit.craftbukkit.block.CraftHopper;
import org.bukkit.craftbukkit.block.CraftJigsaw;
import org.bukkit.craftbukkit.block.CraftJukebox;
import org.bukkit.craftbukkit.block.CraftLectern;
import org.bukkit.craftbukkit.block.CraftShulkerBox;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.block.CraftSkull;
import org.bukkit.craftbukkit.block.CraftSmoker;
import org.bukkit.craftbukkit.block.CraftStructureBlock;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BlockStateMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBlockState extends CraftMetaItem implements BlockStateMeta {

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");

    final Material material;
    CompoundTag blockEntityTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof CraftMetaBlockState)
                || ((CraftMetaBlockState) meta).material != material) {
            blockEntityTag = null;
            return;
        }

        CraftMetaBlockState te = (CraftMetaBlockState) meta;
        this.blockEntityTag = te.blockEntityTag;
    }

    CraftMetaBlockState(CompoundTag tag, Material material) {
        super(tag);
        this.material = material;

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        } else {
            blockEntityTag = null;
        }
    }

    CraftMetaBlockState(Map<String, Object> map) {
        super(map);
        String matName = SerializableMeta.getString(map, "blockMaterial", true);
        Material m = Material.getMaterial(matName);
        if (m != null) {
            material = m;
        } else {
            material = Material.AIR;
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        if (blockEntityTag != null) {
            tag.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(final Map<String, Tag> internalTags) {
        if (blockEntityTag != null) {
            internalTags.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put("blockMaterial", material.name());
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (blockEntityTag != null) {
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
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || blockEntityTag == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && blockEntityTag == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case FURNACE:
            case CHEST:
            case TRAPPED_CHEST:
            case JUKEBOX:
            case DISPENSER:
            case DROPPER:
            case ACACIA_SIGN:
            case ACACIA_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_WALL_SIGN:
            case SPAWNER:
            case BREWING_STAND:
            case ENCHANTING_TABLE:
            case COMMAND_BLOCK:
            case REPEATING_COMMAND_BLOCK:
            case CHAIN_COMMAND_BLOCK:
            case BEACON:
            case DAYLIGHT_DETECTOR:
            case HOPPER:
            case COMPARATOR:
            case SHIELD:
            case STRUCTURE_BLOCK:
            case SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case ENDER_CHEST:
            case BARREL:
            case BELL:
            case BLAST_FURNACE:
            case CAMPFIRE:
            case JIGSAW:
            case LECTERN:
            case SMOKER:
            case BEEHIVE:
            case BEE_NEST:
                return true;
        }
        return false;
    }

    @Override
    public CraftMetaBlockState clone() {
        CraftMetaBlockState meta = (CraftMetaBlockState) super.clone();
        if (blockEntityTag != null) {
            meta.blockEntityTag = blockEntityTag.copy();
        }
        return meta;
    }

    @Override
    public boolean hasBlockState() {
        return blockEntityTag != null;
    }

    @Override
    public BlockState getBlockState() {
        if (blockEntityTag != null) {
            switch (material) {
                case SHIELD:
                    blockEntityTag.putString("id", "banner");
                    break;
                case SHULKER_BOX:
                case WHITE_SHULKER_BOX:
                case ORANGE_SHULKER_BOX:
                case MAGENTA_SHULKER_BOX:
                case LIGHT_BLUE_SHULKER_BOX:
                case YELLOW_SHULKER_BOX:
                case LIME_SHULKER_BOX:
                case PINK_SHULKER_BOX:
                case GRAY_SHULKER_BOX:
                case LIGHT_GRAY_SHULKER_BOX:
                case CYAN_SHULKER_BOX:
                case PURPLE_SHULKER_BOX:
                case BLUE_SHULKER_BOX:
                case BROWN_SHULKER_BOX:
                case GREEN_SHULKER_BOX:
                case RED_SHULKER_BOX:
                case BLACK_SHULKER_BOX:
                    blockEntityTag.putString("id", "shulker_box");
                    break;
                case BEE_NEST:
                case BEEHIVE:
                    blockEntityTag.putString("id", "beehive");
                    break;
            }
        }
        BlockEntity te = (blockEntityTag == null) ? null : BlockEntity.createFromTag(blockEntityTag);

        switch (material) {
        case ACACIA_SIGN:
        case ACACIA_WALL_SIGN:
        case BIRCH_SIGN:
        case BIRCH_WALL_SIGN:
        case DARK_OAK_SIGN:
        case DARK_OAK_WALL_SIGN:
        case JUNGLE_SIGN:
        case JUNGLE_WALL_SIGN:
        case OAK_SIGN:
        case OAK_WALL_SIGN:
        case SPRUCE_SIGN:
        case SPRUCE_WALL_SIGN:
            if (te == null) {
                te = new SignBlockEntity();
            }
            return new CraftSign(material, (SignBlockEntity) te);
        case CHEST:
        case TRAPPED_CHEST:
            if (te == null) {
                te = new ChestBlockEntity();
            }
            return new CraftChest(material, (ChestBlockEntity) te);
        case FURNACE:
            if (te == null) {
                te = new FurnaceBlockEntity();
            }
            return new CraftFurnace(material, (AbstractFurnaceBlockEntity) te);
        case DISPENSER:
            if (te == null) {
                te = new DispenserBlockEntity();
            }
            return new CraftDispenser(material, (DispenserBlockEntity) te);
        case DROPPER:
            if (te == null) {
                te = new DropperBlockEntity();
            }
            return new CraftDropper(material, (DropperBlockEntity) te);
        case END_GATEWAY:
            if (te == null) {
                te = new EndGatewayBlockEntity();
            }
            return new CraftEndGateway(material, (EndGatewayBlockEntity) te);
        case HOPPER:
            if (te == null) {
                te = new HopperBlockEntity();
            }
            return new CraftHopper(material, (HopperBlockEntity) te);
        case SPAWNER:
            if (te == null) {
                te = new MobSpawnerBlockEntity();
            }
            return new CraftCreatureSpawner(material, (MobSpawnerBlockEntity) te);
        case JUKEBOX:
            if (te == null) {
                te = new JukeboxBlockEntity();
            }
            return new CraftJukebox(material, (JukeboxBlockEntity) te);
        case BREWING_STAND:
            if (te == null) {
                te = new BrewingStandBlockEntity();
            }
            return new CraftBrewingStand(material, (BrewingStandBlockEntity) te);
        case CREEPER_HEAD:
        case CREEPER_WALL_HEAD:
        case DRAGON_HEAD:
        case DRAGON_WALL_HEAD:
        case PLAYER_HEAD:
        case PLAYER_WALL_HEAD:
        case SKELETON_SKULL:
        case SKELETON_WALL_SKULL:
        case WITHER_SKELETON_SKULL:
        case WITHER_SKELETON_WALL_SKULL:
        case ZOMBIE_HEAD:
        case ZOMBIE_WALL_HEAD:
            if (te == null) {
                te = new SkullBlockEntity();
            }
            return new CraftSkull(material, (SkullBlockEntity) te);
        case COMMAND_BLOCK:
        case REPEATING_COMMAND_BLOCK:
        case CHAIN_COMMAND_BLOCK:
            if (te == null) {
                te = new CommandBlockBlockEntity();
            }
            return new CraftCommandBlock(material, (CommandBlockBlockEntity) te);
        case BEACON:
            if (te == null) {
                te = new BeaconBlockEntity();
            }
            return new CraftBeacon(material, (BeaconBlockEntity) te);
        case SHIELD:
            if (te == null) {
                te = new BannerBlockEntity();
            }
            ((BannerBlockEntity) te).baseColor = (blockEntityTag == null) ? DyeColor.WHITE : DyeColor.byId(blockEntityTag.getInt(CraftMetaBanner.BASE.NBT));
        case BLACK_BANNER:
        case BLACK_WALL_BANNER:
        case BLUE_BANNER:
        case BLUE_WALL_BANNER:
        case BROWN_BANNER:
        case BROWN_WALL_BANNER:
        case CYAN_BANNER:
        case CYAN_WALL_BANNER:
        case GRAY_BANNER:
        case GRAY_WALL_BANNER:
        case GREEN_BANNER:
        case GREEN_WALL_BANNER:
        case LIGHT_BLUE_BANNER:
        case LIGHT_BLUE_WALL_BANNER:
        case LIGHT_GRAY_BANNER:
        case LIGHT_GRAY_WALL_BANNER:
        case LIME_BANNER:
        case LIME_WALL_BANNER:
        case MAGENTA_BANNER:
        case MAGENTA_WALL_BANNER:
        case ORANGE_BANNER:
        case ORANGE_WALL_BANNER:
        case PINK_BANNER:
        case PINK_WALL_BANNER:
        case PURPLE_BANNER:
        case PURPLE_WALL_BANNER:
        case RED_BANNER:
        case RED_WALL_BANNER:
        case WHITE_BANNER:
        case WHITE_WALL_BANNER:
        case YELLOW_BANNER:
        case YELLOW_WALL_BANNER:
            if (te == null) {
                te = new BannerBlockEntity();
            }
            return new CraftBanner(material == Material.SHIELD ? shieldToBannerHack(blockEntityTag) : material, (BannerBlockEntity) te);
        case STRUCTURE_BLOCK:
            if (te == null) {
                te = new StructureBlockBlockEntity();
            }
            return new CraftStructureBlock(material, (StructureBlockBlockEntity) te);
        case SHULKER_BOX:
        case WHITE_SHULKER_BOX:
        case ORANGE_SHULKER_BOX:
        case MAGENTA_SHULKER_BOX:
        case LIGHT_BLUE_SHULKER_BOX:
        case YELLOW_SHULKER_BOX:
        case LIME_SHULKER_BOX:
        case PINK_SHULKER_BOX:
        case GRAY_SHULKER_BOX:
        case LIGHT_GRAY_SHULKER_BOX:
        case CYAN_SHULKER_BOX:
        case PURPLE_SHULKER_BOX:
        case BLUE_SHULKER_BOX:
        case BROWN_SHULKER_BOX:
        case GREEN_SHULKER_BOX:
        case RED_SHULKER_BOX:
        case BLACK_SHULKER_BOX:
            if (te == null) {
                te = new ShulkerBoxBlockEntity();
            }
            return new CraftShulkerBox(material, (ShulkerBoxBlockEntity) te);
        case ENCHANTING_TABLE:
            if (te == null) {
                te = new EnchantingTableBlockEntity();
            }
            return new CraftEnchantingTable(material, (EnchantingTableBlockEntity) te);
        case ENDER_CHEST:
            if (te == null) {
                te = new EnderChestBlockEntity();
            }
            return new CraftEnderChest(material, (EnderChestBlockEntity) te);
        case DAYLIGHT_DETECTOR:
            if (te == null) {
                te = new DaylightDetectorBlockEntity();
            }
            return new CraftDaylightDetector(material, (DaylightDetectorBlockEntity) te);
        case COMPARATOR:
            if (te == null) {
                te = new ComparatorBlockEntity();
            }
            return new CraftComparator(material, (ComparatorBlockEntity) te);
        case BARREL:
            if (te == null) {
                te = new BarrelBlockEntity();
            }
            return new CraftBarrel(material, (BarrelBlockEntity) te);
        case BELL:
            if (te == null) {
                te = new BellBlockEntity();
            }
            return new CraftBell(material, (BellBlockEntity) te);
        case BLAST_FURNACE:
            if (te == null) {
                te = new BlastFurnaceBlockEntity();
            }
            return new CraftBlastFurnace(material, (BlastFurnaceBlockEntity) te);
        case CAMPFIRE:
            if (te == null) {
                te = new CampfireBlockEntity();
            }
            return new CraftCampfire(material, (CampfireBlockEntity) te);
        case JIGSAW:
            if (te == null) {
                te = new JigsawBlockEntity();
            }
            return new CraftJigsaw(material, (JigsawBlockEntity) te);
        case LECTERN:
            if (te == null) {
                te = new LecternBlockEntity();
            }
            return new CraftLectern(material, (LecternBlockEntity) te);
        case SMOKER:
            if (te == null) {
                te = new SmokerBlockEntity();
            }
            return new CraftSmoker(material, (SmokerBlockEntity) te);
        case BEE_NEST:
        case BEEHIVE:
            if (te == null) {
                te = new BeehiveBlockEntity();
            }
            return new CraftBeehive(material, (BeehiveBlockEntity) te);
        default:
            throw new IllegalStateException("Missing blockState for " + material);
        }
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Validate.notNull(blockState, "blockState must not be null");

        boolean valid;
        switch (material) {
        case ACACIA_SIGN:
        case ACACIA_WALL_SIGN:
        case BIRCH_SIGN:
        case BIRCH_WALL_SIGN:
        case DARK_OAK_SIGN:
        case DARK_OAK_WALL_SIGN:
        case JUNGLE_SIGN:
        case JUNGLE_WALL_SIGN:
        case OAK_SIGN:
        case OAK_WALL_SIGN:
        case SPRUCE_SIGN:
        case SPRUCE_WALL_SIGN:
            valid = blockState instanceof CraftSign;
            break;
        case CHEST:
        case TRAPPED_CHEST:
            valid = blockState instanceof CraftChest;
            break;
        case FURNACE:
            valid = blockState instanceof CraftFurnace;
            break;
        case DISPENSER:
            valid = blockState instanceof CraftDispenser;
            break;
        case DROPPER:
            valid = blockState instanceof CraftDropper;
            break;
        case END_GATEWAY:
            valid = blockState instanceof CraftEndGateway;
            break;
        case HOPPER:
            valid = blockState instanceof CraftHopper;
            break;
        case SPAWNER:
            valid = blockState instanceof CraftCreatureSpawner;
            break;
        case JUKEBOX:
            valid = blockState instanceof CraftJukebox;
            break;
        case BREWING_STAND:
            valid = blockState instanceof CraftBrewingStand;
            break;
        case CREEPER_HEAD:
        case CREEPER_WALL_HEAD:
        case DRAGON_HEAD:
        case DRAGON_WALL_HEAD:
        case PLAYER_HEAD:
        case PLAYER_WALL_HEAD:
        case SKELETON_SKULL:
        case SKELETON_WALL_SKULL:
        case WITHER_SKELETON_SKULL:
        case WITHER_SKELETON_WALL_SKULL:
        case ZOMBIE_HEAD:
        case ZOMBIE_WALL_HEAD:
            valid = blockState instanceof CraftSkull;
            break;
        case COMMAND_BLOCK:
        case REPEATING_COMMAND_BLOCK:
        case CHAIN_COMMAND_BLOCK:
            valid = blockState instanceof CraftCommandBlock;
            break;
        case BEACON:
            valid = blockState instanceof CraftBeacon;
            break;
        case SHIELD:
        case BLACK_BANNER:
        case BLACK_WALL_BANNER:
        case BLUE_BANNER:
        case BLUE_WALL_BANNER:
        case BROWN_BANNER:
        case BROWN_WALL_BANNER:
        case CYAN_BANNER:
        case CYAN_WALL_BANNER:
        case GRAY_BANNER:
        case GRAY_WALL_BANNER:
        case GREEN_BANNER:
        case GREEN_WALL_BANNER:
        case LIGHT_BLUE_BANNER:
        case LIGHT_BLUE_WALL_BANNER:
        case LIGHT_GRAY_BANNER:
        case LIGHT_GRAY_WALL_BANNER:
        case LIME_BANNER:
        case LIME_WALL_BANNER:
        case MAGENTA_BANNER:
        case MAGENTA_WALL_BANNER:
        case ORANGE_BANNER:
        case ORANGE_WALL_BANNER:
        case PINK_BANNER:
        case PINK_WALL_BANNER:
        case PURPLE_BANNER:
        case PURPLE_WALL_BANNER:
        case RED_BANNER:
        case RED_WALL_BANNER:
        case WHITE_BANNER:
        case WHITE_WALL_BANNER:
        case YELLOW_BANNER:
        case YELLOW_WALL_BANNER:
            valid = blockState instanceof CraftBanner;
            break;
        case STRUCTURE_BLOCK:
            valid = blockState instanceof CraftStructureBlock;
            break;
        case SHULKER_BOX:
        case WHITE_SHULKER_BOX:
        case ORANGE_SHULKER_BOX:
        case MAGENTA_SHULKER_BOX:
        case LIGHT_BLUE_SHULKER_BOX:
        case YELLOW_SHULKER_BOX:
        case LIME_SHULKER_BOX:
        case PINK_SHULKER_BOX:
        case GRAY_SHULKER_BOX:
        case LIGHT_GRAY_SHULKER_BOX:
        case CYAN_SHULKER_BOX:
        case PURPLE_SHULKER_BOX:
        case BLUE_SHULKER_BOX:
        case BROWN_SHULKER_BOX:
        case GREEN_SHULKER_BOX:
        case RED_SHULKER_BOX:
        case BLACK_SHULKER_BOX:
            valid = blockState instanceof CraftShulkerBox;
            break;
        case ENCHANTING_TABLE:
            valid = blockState instanceof CraftEnchantingTable;
            break;
        case ENDER_CHEST:
            valid = blockState instanceof CraftEnderChest;
            break;
        case DAYLIGHT_DETECTOR:
            valid = blockState instanceof CraftDaylightDetector;
            break;
        case COMPARATOR:
            valid = blockState instanceof CraftComparator;
            break;
        case BARREL:
            valid = blockState instanceof CraftBarrel;
            break;
        case BELL:
            valid = blockState instanceof CraftBell;
            break;
        case BLAST_FURNACE:
            valid = blockState instanceof CraftBlastFurnace;
            break;
        case CAMPFIRE:
            valid = blockState instanceof CraftCampfire;
            break;
        case JIGSAW:
            valid = blockState instanceof CraftJigsaw;
            break;
        case LECTERN:
            valid = blockState instanceof CraftLectern;
            break;
        case SMOKER:
            valid = blockState instanceof CraftSmoker;
            break;
        case BEEHIVE:
        case BEE_NEST:
            valid = blockState instanceof CraftBeehive;
            break;
        default:
            valid = false;
            break;
        }

        Validate.isTrue(valid, "Invalid blockState for " + material);

        blockEntityTag = ((CraftBlockEntityState) blockState).getSnapshotNBT();
        // Set shield base
        if (material == Material.SHIELD) {
            blockEntityTag.putInt(CraftMetaBanner.BASE.NBT, ((CraftBanner) blockState).getBaseColor().getWoolData());
        }
    }

    private static Material shieldToBannerHack(CompoundTag tag) {
        if (tag == null || !tag.contains(CraftMetaBanner.BASE.NBT, CraftMagicNumbers.NBT.TAG_INT)) {
            return Material.WHITE_BANNER;
        }

        switch (tag.getInt(CraftMetaBanner.BASE.NBT)) {
            case 0:
                return Material.WHITE_BANNER;
            case 1:
                return Material.ORANGE_BANNER;
            case 2:
                return Material.MAGENTA_BANNER;
            case 3:
                return Material.LIGHT_BLUE_BANNER;
            case 4:
                return Material.YELLOW_BANNER;
            case 5:
                return Material.LIME_BANNER;
            case 6:
                return Material.PINK_BANNER;
            case 7:
                return Material.GRAY_BANNER;
            case 8:
                return Material.LIGHT_GRAY_BANNER;
            case 9:
                return Material.CYAN_BANNER;
            case 10:
                return Material.PURPLE_BANNER;
            case 11:
                return Material.BLUE_BANNER;
            case 12:
                return Material.BROWN_BANNER;
            case 13:
                return Material.GREEN_BANNER;
            case 14:
                return Material.RED_BANNER;
            case 15:
                return Material.BLACK_BANNER;
            default:
                throw new IllegalArgumentException("Unknown banner colour");
        }
    }
}
