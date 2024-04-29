package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V111 {

    protected static final int VERSION = MCVersions.V15W33B;

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("Painting", new EntityRotationFix("Painting"));
        MCTypeRegistry.ENTITY.addConverterForId("ItemFrame", new EntityRotationFix("ItemFrame"));
    }

    private V111() {}

    protected static final class EntityRotationFix extends DataConverter<MapType<String>, MapType<String>> {

        private static final int[][] DIRECTIONS = new int[][] {
                {0, 0, 1},
                {-1, 0, 0},
                {0, 0, -1},
                {1, 0, 0}
        };

        protected final String id;

        public EntityRotationFix(final String id) {
            super(VERSION);
            this.id = id;
        }

        @Override
        public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
            if (data.getNumber("Facing") != null) {
                return null;
            }

            final Number direction = data.getNumber("Direction");
            final int facing;
            if (direction != null) {
                data.remove("Direction");
                facing = direction.intValue() % DIRECTIONS.length;
                final int[] offsets = DIRECTIONS[facing];
                data.setInt("TileX", data.getInt("TileX") + offsets[0]);
                data.setInt("TileY", data.getInt("TileY") + offsets[1]);
                data.setInt("TileZ", data.getInt("TileZ") + offsets[2]);
                if ("ItemFrame".equals(data.getString("id"))) {
                    final Number rotation = data.getNumber("ItemRotation");
                    if (rotation != null) {
                        data.setByte("ItemRotation", (byte)(rotation.byteValue() * 2));
                    }
                }
            } else {
                facing = data.getByte("Dir") % DIRECTIONS.length;
                data.remove("Dir");
            }

            data.setByte("Facing", (byte)facing);

            return null;
        }
    }

}
