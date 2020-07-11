package net.minecraftforge.eventbus;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventListener;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringBuilderFormattable;

import java.io.PrintWriter;
import java.io.StringWriter;

public class EventBusErrorMessage implements Message, StringBuilderFormattable {
    private final Event event;
    private final int index;
    private final IEventListener[] listeners;
    private final Throwable throwable;

    public EventBusErrorMessage(final Event event, final int index, final IEventListener[] listeners, final Throwable throwable) {
        this.event = event;
        this.index = index;
        this.listeners = listeners;
        this.throwable = throwable;
    }

    @Override
    public String getFormattedMessage() {
        return "";
    }

    @Override
    public String getFormat() {
        return "";
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

    @Override
    public Throwable getThrowable() {
        return null; // Cannot return the throwable here - it causes weird classloading issues inside log4j
    }

    @Override
    public void formatTo(final StringBuilder buffer) {
        buffer.
                append("Exception caught during firing event: ").append(throwable.getMessage()).append('\n').
                append("\tIndex: ").append(index).append('\n').
                append("\tListeners:\n");
        for (int x = 0; x < listeners.length; x++)
        {
            buffer.append("\t\t").append(x).append(": ").append(listeners[x]).append('\n');
        }
        final StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        buffer.append(sw.getBuffer());
    }
}
