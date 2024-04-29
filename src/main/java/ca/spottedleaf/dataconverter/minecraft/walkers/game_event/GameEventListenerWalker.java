package ca.spottedleaf.dataconverter.minecraft.walkers.game_event;

import ca.spottedleaf.dataconverter.converters.datatypes.DataWalker;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.MapType;

public final class GameEventListenerWalker implements DataWalker<String> {

    @Override
    public MapType<String> walk(final MapType<String> data, final long fromVersion, final long toVersion) {
        final MapType<String> listener = data.getMap("listener");
        if (listener == null) {
            return null;
        }

        final MapType<String> event = listener.getMap("event");
        if (event == null) {
            return null;
        }

        WalkerUtils.convert(MCTypeRegistry.GAME_EVENT_NAME, event, "game_event", fromVersion, toVersion);

        return null;
    }
}
