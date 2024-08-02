package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.HelperItemNameV102;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class V102 {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final int VERSION = MCVersions.V15W32A + 2;

    public static void register() {
        // V102 -> V15W32A + 2
        // V102 schema only modifies ITEM_STACK to have only a string ID, but our ITEM_NAME is generic (int or String) so we don't
        // actually need to update the walker

        MCTypeRegistry.ITEM_NAME.addConverter(new DataConverter<>(VERSION) {
            @Override
            public Object convert(final Object data, final long sourceVersion, final long toVersion) {
                if (!(data instanceof Number)) {
                    return null;
                }
                final int id = ((Number)data).intValue();
                final String remap = HelperItemNameV102.getNameFromId(id);
                if (remap == null) {
                    LOGGER.warn("Unknown legacy integer id (V102) " + id);
                }
                return remap == null ? HelperItemNameV102.getNameFromId(0) : remap;
            }
        });

        MCTypeRegistry.ITEM_STACK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (!data.hasKey("id", ObjectType.NUMBER)) {
                    return null;
                }

                final int id = data.getInt("id");

                String remap = HelperItemNameV102.getNameFromId(id);
                if (remap == null) {
                    LOGGER.warn("Unknown legacy integer id (V102) " + id);
                    remap = HelperItemNameV102.getNameFromId(0);
                }

                data.setString("id", remap);

                return null;
            }
        });
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:potion", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final short damage = data.getShort("Damage");
                if (damage != 0) {
                    data.setShort("Damage", (short)0);
                }
                MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    tag = Types.NBT.createEmptyMap();
                    data.setMap("tag", tag);
                }

                if (!tag.hasKey("Potion", ObjectType.STRING)) {
                    final String converted = HelperItemNameV102.getPotionNameFromId(damage);
                    tag.setString("Potion", converted == null ? "minecraft:water" : converted);
                    if ((damage & 16384) == 16384) {
                        data.setString("id", "minecraft:splash_potion");
                    }
                }

                return null;
            }
        });
    }

    private V102() {}

}
