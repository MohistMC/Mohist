package ca.spottedleaf.dataconverter.converters;

import java.util.Comparator;

public abstract class DataConverter<T, R> {

    public static final Comparator<DataConverter<?, ?>> LOWEST_VERSION_COMPARATOR = (x, y) -> {
        return Long.compare(x.getEncodedVersion(), y.getEncodedVersion());
    };

    protected final int toVersion;
    protected final int versionStep;

    public DataConverter(final int toVersion) {
        this.toVersion = toVersion;
        this.versionStep = 0;
    }

    public DataConverter(final int toVersion, final int versionStep) {
        this.toVersion = toVersion;
        this.versionStep = versionStep;
    }

    public final int getToVersion() {
        return this.toVersion;
    }

    public final int getVersionStep() {
        return this.versionStep;
    }

    public final long getEncodedVersion() {
        return encodeVersions(this.toVersion, this.versionStep);
    }

    public abstract R convert(final T data, final long sourceVersion, final long toVersion);

    // step must be in the lower bits, so that encodeVersions(version, step) < encodeVersions(version, step + 1)
    public static long encodeVersions(final int version, final int step) {
        return ((long)version << 32) | (step & 0xFFFFFFFFL);
    }

    public static int getVersion(final long encoded) {
        return (int)(encoded >>> 32);
    }

    public static int getStep(final long encoded) {
        return (int)encoded;
    }

    public static String encodedToString(final long encoded) {
        return getVersion(encoded) + "." + getStep(encoded);
    }
}
