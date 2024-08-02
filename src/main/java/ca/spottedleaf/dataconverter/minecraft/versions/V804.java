package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V804 {

    protected static final int VERSION = MCVersions.V16W35A + 1;

    public static void register() {
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:banner", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    return null;
                }

                final MapType<String> blockEntity = tag.getMap("BlockEntityTag");
                if (blockEntity == null) {
                    return null;
                }

                if (!blockEntity.hasKey("Base", ObjectType.NUMBER)) {
                    return null;
                }

                data.setShort("Damage", (short)(blockEntity.getShort("Base") & 15));

                final MapType<String> display = tag.getMap("display");
                if (display != null) {
                    final ListType lore = display.getList("Lore", ObjectType.STRING);
                    if (lore != null) {
                        if (lore.size() == 1 && "(+NBT)".equals(lore.getString(0))) {
                            return null;
                        }
                    }
                }

                blockEntity.remove("Base");
                if (blockEntity.isEmpty()) {
                    tag.remove("BlockEntityTag");
                }

                if (tag.isEmpty()) {
                    data.remove("tag");
                }

                return null;
            }
        });
    }

    private V804() {}

}
