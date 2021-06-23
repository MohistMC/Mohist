package io.papermc.paper.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.List;

public final class AdventureComponent implements ITextComponent {
    final Component wrapped;
    private @MonotonicNonNull ITextComponent converted;

    public AdventureComponent(final Component wrapped) {
        this.wrapped = wrapped;
    }

    public ITextComponent deepConverted() {
        ITextComponent converted = this.converted;
        if (converted == null) {
            converted = PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(this.wrapped);
            this.converted = converted;
        }
        return converted;
    }

    public @Nullable ITextComponent deepConvertedIfPresent() {
        return this.converted;
    }

    @Override
    public Style getStyle() {
        return this.deepConverted().getStyle();
    }

    @Override
    public String getContents() {
        if (this.wrapped instanceof TextComponent) {
            return ((TextComponent) this.wrapped).content();
        } else {
            return this.deepConverted().getContents();
        }
    }

    @Override
    public String getString() {
        return PaperAdventure.PLAIN.serialize(this.wrapped);
    }

    @Override
    public List<ITextComponent> getSiblings() {
        return this.deepConverted().getSiblings();
    }

    @Override
    public IFormattableTextComponent  plainCopy() {
        return this.deepConverted().plainCopy();
    }

    @Override
    public IFormattableTextComponent copy() {
        return this.deepConverted().copy();
    }

    @Override
    public IReorderingProcessor getVisualOrderText() {
        return this.deepConverted().getVisualOrderText();
    }

    public static class Serializer implements JsonSerializer<AdventureComponent> {
        @Override
        public JsonElement serialize(final AdventureComponent src, final Type type, final JsonSerializationContext context) {
            return PaperAdventure.GSON.serializer().toJsonTree(src.wrapped, Component.class);
        }
    }
}
