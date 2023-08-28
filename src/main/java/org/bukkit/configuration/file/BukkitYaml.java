package org.bukkit.configuration.file;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import com.mohistmc.org.yaml.snakeyaml.DumperOptions;
import com.mohistmc.org.yaml.snakeyaml.LoaderOptions;
import com.mohistmc.org.yaml.snakeyaml.Yaml;
import com.mohistmc.org.yaml.snakeyaml.comments.CommentEventsCollector;
import com.mohistmc.org.yaml.snakeyaml.comments.CommentType;
import com.mohistmc.org.yaml.snakeyaml.constructor.BaseConstructor;
import com.mohistmc.org.yaml.snakeyaml.emitter.Emitter;
import com.mohistmc.org.yaml.snakeyaml.error.YAMLException;
import com.mohistmc.org.yaml.snakeyaml.events.Event;
import com.mohistmc.org.yaml.snakeyaml.nodes.Node;
import com.mohistmc.org.yaml.snakeyaml.representer.Representer;
import com.mohistmc.org.yaml.snakeyaml.serializer.Serializer;

final class BukkitYaml extends Yaml {

    private static final Field events;
    private static final Field blockCommentsCollector;
    private static final Field inlineCommentsCollector;

    private static Field getEmitterField(String name) {
        Field field = null;
        try {
            field = Emitter.class.getDeclaredField(name);
            field.setAccessible(true);
        } catch (ReflectiveOperationException ex) {
            // Ignore as a fail-safe fallback
        }
        return field;
    }

    static {
        events = getEmitterField("events");
        blockCommentsCollector = getEmitterField("blockCommentsCollector");
        inlineCommentsCollector = getEmitterField("inlineCommentsCollector");
    }

    public BukkitYaml(@NotNull BaseConstructor constructor, @NotNull Representer representer, @NotNull DumperOptions dumperOptions, @NotNull LoaderOptions loadingConfig) {
        super(constructor, representer, dumperOptions, loadingConfig);
    }

    @Override
    public void serialize(@NotNull Node node, @NotNull Writer output) {
        Emitter emitter = new Emitter(output, dumperOptions);
        if (events != null && blockCommentsCollector != null && inlineCommentsCollector != null) {
            Queue<Event> newEvents = new ArrayDeque<>(100);

            try {
                events.set(emitter, newEvents);
                blockCommentsCollector.set(emitter, new CommentEventsCollector(newEvents, CommentType.BLANK_LINE, CommentType.BLOCK));
                inlineCommentsCollector.set(emitter, new CommentEventsCollector(newEvents, CommentType.IN_LINE));
            } catch (ReflectiveOperationException ex) {
                // Don't ignore this as we could be in an inconsistent state
                throw new RuntimeException("Could not update Yaml event queue", ex);
            }
        }

        Serializer serializer = new Serializer(emitter, resolver, dumperOptions, null);
        try {
            serializer.open();
            serializer.serialize(node);
            serializer.close();
        } catch (IOException ex) {
            throw new YAMLException(ex);
        }
    }
}
