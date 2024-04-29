package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class V1494 {

    protected static final int VERSION = MCVersions.V18W20C + 1;

    private static final Int2ObjectOpenHashMap<String> ENCH_ID_TO_NAME = new Int2ObjectOpenHashMap<>();
    static {
        ENCH_ID_TO_NAME.put(0, "minecraft:protection");
        ENCH_ID_TO_NAME.put(1, "minecraft:fire_protection");
        ENCH_ID_TO_NAME.put(2, "minecraft:feather_falling");
        ENCH_ID_TO_NAME.put(3, "minecraft:blast_protection");
        ENCH_ID_TO_NAME.put(4, "minecraft:projectile_protection");
        ENCH_ID_TO_NAME.put(5, "minecraft:respiration");
        ENCH_ID_TO_NAME.put(6, "minecraft:aqua_affinity");
        ENCH_ID_TO_NAME.put(7, "minecraft:thorns");
        ENCH_ID_TO_NAME.put(8, "minecraft:depth_strider");
        ENCH_ID_TO_NAME.put(9, "minecraft:frost_walker");
        ENCH_ID_TO_NAME.put(10, "minecraft:binding_curse");
        ENCH_ID_TO_NAME.put(16, "minecraft:sharpness");
        ENCH_ID_TO_NAME.put(17, "minecraft:smite");
        ENCH_ID_TO_NAME.put(18, "minecraft:bane_of_arthropods");
        ENCH_ID_TO_NAME.put(19, "minecraft:knockback");
        ENCH_ID_TO_NAME.put(20, "minecraft:fire_aspect");
        ENCH_ID_TO_NAME.put(21, "minecraft:looting");
        ENCH_ID_TO_NAME.put(22, "minecraft:sweeping");
        ENCH_ID_TO_NAME.put(32, "minecraft:efficiency");
        ENCH_ID_TO_NAME.put(33, "minecraft:silk_touch");
        ENCH_ID_TO_NAME.put(34, "minecraft:unbreaking");
        ENCH_ID_TO_NAME.put(35, "minecraft:fortune");
        ENCH_ID_TO_NAME.put(48, "minecraft:power");
        ENCH_ID_TO_NAME.put(49, "minecraft:punch");
        ENCH_ID_TO_NAME.put(50, "minecraft:flame");
        ENCH_ID_TO_NAME.put(51, "minecraft:infinity");
        ENCH_ID_TO_NAME.put(61, "minecraft:luck_of_the_sea");
        ENCH_ID_TO_NAME.put(62, "minecraft:lure");
        ENCH_ID_TO_NAME.put(65, "minecraft:loyalty");
        ENCH_ID_TO_NAME.put(66, "minecraft:impaling");
        ENCH_ID_TO_NAME.put(67, "minecraft:riptide");
        ENCH_ID_TO_NAME.put(68, "minecraft:channeling");
        ENCH_ID_TO_NAME.put(70, "minecraft:mending");
        ENCH_ID_TO_NAME.put(71, "minecraft:vanishing_curse");
    }

    public static void register() {
        MCTypeRegistry.ITEM_STACK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    return null;
                }

                final ListType enchants = tag.getList("ench", ObjectType.MAP);
                if (enchants != null) {
                    tag.remove("ench");
                    tag.setList("Enchantments", enchants);

                    for (int i = 0, len = enchants.size(); i < len; ++i) {
                        final MapType<String> enchant = enchants.getMap(i);
                        enchant.setString("id", ENCH_ID_TO_NAME.getOrDefault(enchant.getInt("id"), "null"));
                    }
                }

                final ListType storedEnchants = tag.getList("StoredEnchantments", ObjectType.MAP);
                if (storedEnchants != null) {
                    for (int i = 0, len = storedEnchants.size(); i < len; ++i) {
                        final MapType<String> enchant = storedEnchants.getMap(i);
                        enchant.setString("id", ENCH_ID_TO_NAME.getOrDefault(enchant.getInt("id"), "null"));
                    }
                }


                return null;
            }
        });
    }

    private V1494() {}

}
