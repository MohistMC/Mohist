package com.mohistmc.paper.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

public final class AdventureComponent implements net.minecraft.network.chat.Component {
    final Component adventure;

    private net.minecraft.network.chat.Component vanilla;

    public AdventureComponent(final Component adventure) {
        this.adventure = adventure;
    }

    public net.minecraft.network.chat.Component deepConverted() {
        net.minecraft.network.chat.Component vanilla = this.vanilla;
        if (vanilla == null) {
            vanilla = PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(this.adventure);
            this.vanilla = vanilla;
        }
        return vanilla;
    }

    @Nullable
    public net.minecraft.network.chat.Component deepConvertedIfPresent() {
        return this.vanilla;
    }

    @Override
    public Style getStyle() {
        return this.deepConverted().getStyle();
    }

    @Override
    public ComponentContents getContents() {
        if (this.adventure instanceof TextComponent) {
            return new LiteralContents(((TextComponent) this.adventure).content());
        } else {
            return this.deepConverted().getContents();
        }
    }

    @Override
    public String getString() {
        return PlainTextComponentSerializer.plainText().serialize(this.adventure);
    }

    @Override
    public List<net.minecraft.network.chat.Component> getSiblings() {
        return this.deepConverted().getSiblings();
    }

    @Override
    public MutableComponent plainCopy() {
        return this.deepConverted().plainCopy();
    }

    @Override
    public MutableComponent copy() {
        return this.deepConverted().copy();
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        return this.deepConverted().getVisualOrderText();
    }

    public Component adventure$component() {
        return this.adventure;
    }

    public static class Serializer implements JsonSerializer<AdventureComponent> {
        @Override
        public JsonElement serialize(final AdventureComponent src, final Type type, final JsonSerializationContext context) {
            return GsonComponentSerializer.gson().serializer().toJsonTree(src.adventure, Component.class);
        }
    }
}
