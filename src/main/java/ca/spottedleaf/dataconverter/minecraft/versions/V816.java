package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.Locale;

public final class V816 {

    protected static final int VERSION = MCVersions.V16W43A;

    public static void register() {
        MCTypeRegistry.OPTIONS.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String lang = data.getString("lang");
                if (lang != null) {
                    data.setString("lang", lang.toLowerCase(Locale.ROOT));
                }
                return null;
            }
        });
    }

    private V816() {}

}
