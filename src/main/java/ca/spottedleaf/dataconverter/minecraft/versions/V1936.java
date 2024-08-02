package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V1936 {

    protected static final int VERSION = MCVersions.V19W09A + 1;

    private V1936() {}

    public static void register() {
        MCTypeRegistry.OPTIONS.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String chatOpacity = data.getString("chatOpacity");
                if (chatOpacity != null) {
                    // Vanilla uses createDouble here, but options is always string -> string. I presume they made
                    // a mistake with this converter.
                    data.setString("textBackgroundOpacity", Double.toString(calculateBackground(chatOpacity)));
                }
                return null;
            }
        });
    }

    private static double calculateBackground(final String opacity) {
        try {
            final double d = 0.9D * Double.parseDouble(opacity) + 0.1D;
            return d / 2.0D;
        } catch (final NumberFormatException ex) {
            return 0.5D;
        }
    }
}
