package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V2531 {

    protected static final int VERSION = MCVersions.V20W17A + 2;

    private V2531() {}

    private static boolean isConnected(final String facing) {
        return !"none".equals(facing);
    }

    public static void register() {
        MCTypeRegistry.BLOCK_STATE.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (!"minecraft:redstone_wire".equals(data.getString("Name"))) {
                    return null;
                }

                final MapType<String> properties = data.getMap("Properties");

                if (properties == null) {
                    return null;
                }


                final String east = properties.getString("east", "none");
                final String west = properties.getString("west", "none");
                final String north = properties.getString("north", "none");
                final String south = properties.getString("south", "none");

                final boolean connectedX = isConnected(east) || isConnected(west);
                final boolean connectedZ = isConnected(north) || isConnected(south);

                final String newEast = !isConnected(east) && !connectedZ ? "side" : east;
                final String newWest = !isConnected(west) && !connectedZ ? "side" : west;
                final String newNorth = !isConnected(north) && !connectedX ? "side" : north;
                final String newSouth = !isConnected(south) && !connectedX ? "side" : south;

                if (properties.hasKey("east")) {
                    properties.setString("east", newEast);
                }
                if (properties.hasKey("west")) {
                    properties.setString("west", newWest);
                }
                if (properties.hasKey("north")) {
                    properties.setString("north", newNorth);
                }
                if (properties.hasKey("south")) {
                    properties.setString("south", newSouth);
                }

                return null;
            }
        });
    }
}
