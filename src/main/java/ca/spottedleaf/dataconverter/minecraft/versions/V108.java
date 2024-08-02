package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.UUID;

public final class V108 {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final int VERSION = MCVersions.V15W32C + 4;

    public static void register() {
        // Convert String UUID into UUIDMost and UUIDLeast
        MCTypeRegistry.ENTITY.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String uuidString = data.getString("UUID");

                if (uuidString == null) {
                    return null;
                }
                data.remove("UUID");

                final UUID uuid;
                try {
                    uuid = UUID.fromString(uuidString);
                } catch (final Exception ex) {
                    LOGGER.warn("Failed to parse UUID for legacy entity (V108): " + uuidString, ex);
                    return null;
                }

                data.setLong("UUIDMost", uuid.getMostSignificantBits());
                data.setLong("UUIDLeast", uuid.getLeastSignificantBits());

                return null;
            }
        });
    }

    private V108() {}

}
