package ca.spottedleaf.dataconverter.types;

public interface TypeUtil {

    public ListType createEmptyList();

    public <K> MapType<K> createEmptyMap();

}
