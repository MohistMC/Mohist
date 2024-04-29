package ca.spottedleaf.dataconverter.converters.datatypes;

public interface DataHook<T, R> {

    public R preHook(final T data, final long fromVersion, final long toVersion);

    public R postHook(final T data, final long fromVersion, final long toVersion);

}
