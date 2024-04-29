package ca.spottedleaf.dataconverter.types.nbt;

import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.TypeUtil;

public final class NBTTypeUtil implements TypeUtil {

    @Override
    public ListType createEmptyList() {
        return new NBTListType();
    }

    @Override
    public MapType<String> createEmptyMap() {
        return new NBTMapType();
    }
}
