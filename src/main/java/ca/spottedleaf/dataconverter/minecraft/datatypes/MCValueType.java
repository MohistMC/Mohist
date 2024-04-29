package ca.spottedleaf.dataconverter.minecraft.datatypes;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.converters.datatypes.DataHook;
import ca.spottedleaf.dataconverter.converters.datatypes.DataType;
import ca.spottedleaf.dataconverter.minecraft.MCVersionRegistry;
import ca.spottedleaf.dataconverter.util.Long2ObjectArraySortedMap;
import java.util.ArrayList;
import java.util.List;

public class MCValueType extends DataType<Object, Object> {

    public final String name;

    protected final ArrayList<DataConverter<Object, Object>> converters = new ArrayList<>();
    protected final Long2ObjectArraySortedMap<List<DataHook<Object, Object>>> structureHooks = new Long2ObjectArraySortedMap<>();

    public MCValueType(final String name) {
        this.name = name;
    }

    public void addStructureHook(final int minVersion, final DataHook<Object, Object> hook) {
        this.addStructureHook(minVersion, 0, hook);
    }

    public void addStructureHook(final int minVersion, final int versionStep, final DataHook<Object, Object> hook) {
        this.structureHooks.computeIfAbsent(DataConverter.encodeVersions(minVersion, versionStep), (final long keyInMap) -> {
            return new ArrayList<>();
        }).add(hook);
    }

    public void addConverter(final DataConverter<Object, Object> converter) {
        MCVersionRegistry.checkVersion(converter.getEncodedVersion());
        this.converters.add(converter);
        this.converters.sort(DataConverter.LOWEST_VERSION_COMPARATOR);
    }

    @Override
    public Object convert(final Object data, final long fromVersion, final long toVersion) {
        Object ret = null;
        final List<DataConverter<Object, Object>> converters = this.converters;

        for (int i = 0, len = converters.size(); i < len; ++i) {
            final DataConverter<Object, Object> converter = converters.get(i);
            final long converterVersion = converter.getEncodedVersion();

            if (converterVersion <= fromVersion) {
                continue;
            }

            if (converterVersion > toVersion) {
                break;
            }

            List<DataHook<Object, Object>> hooks = this.structureHooks.getFloor(converterVersion);

            if (hooks != null) {
                for (int k = 0, klen = hooks.size(); k < klen; ++k) {
                    final Object replace = hooks.get(k).preHook(ret == null ? data : ret, fromVersion, toVersion);
                    if (replace != null) {
                        ret = replace;
                    }
                }
            }

            final Object converted = converter.convert(ret == null ? data : ret, fromVersion, toVersion);
            if (converted != null) {
                ret = converted;
            }

            // possibly new data format, update hooks
            hooks = this.structureHooks.getFloor(toVersion);

            if (hooks != null) {
                for (int k = 0, klen = hooks.size(); k < klen; ++k) {
                    final Object replace = hooks.get(k).postHook(ret == null ? data : ret, fromVersion, toVersion);
                    if (replace != null) {
                        ret = replace;
                    }
                }
            }
        }

        return ret;
    }
}
