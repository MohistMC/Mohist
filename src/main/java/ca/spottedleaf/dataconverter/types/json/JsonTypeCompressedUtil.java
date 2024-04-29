package ca.spottedleaf.dataconverter.types.json;

import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.TypeUtil;

public final class JsonTypeCompressedUtil implements TypeUtil {

    @Override
    public ListType createEmptyList() {
        return new JsonListType(true);
    }

    @Override
    public MapType<String> createEmptyMap() {
        return new JsonMapType(true);
    }
}
