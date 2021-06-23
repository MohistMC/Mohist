package io.papermc.paper.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.minecraft.util.text.ITextComponent;

final class WrapperAwareSerializer implements ComponentSerializer<Component, Component, ITextComponent> {
    @Override
    public Component deserialize(final ITextComponent input) {
        if (input instanceof AdventureComponent) {
            return ((AdventureComponent) input).wrapped;
        }
        return PaperAdventure.GSON.serializer().fromJson(ITextComponent.Serializer.toJsonTree(input), Component.class);
    }

    @Override
    public ITextComponent serialize(final Component component) {
        return ITextComponent.Serializer.fromJson(PaperAdventure.GSON.serializer().toJsonTree(component));
    }
}
