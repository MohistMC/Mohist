package io.papermc.paper.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

final class WrapperAwareSerializer implements ComponentSerializer<Component, Component, net.minecraft.network.chat.Component> {
    @Override
    public Component deserialize(final net.minecraft.network.chat.Component input) {
        if (input instanceof AdventureComponent) {
            return ((AdventureComponent) input).adventure;
        }
        return GsonComponentSerializer.gson().serializer().fromJson(net.minecraft.network.chat.Component.Serializer.toJsonTree(input), Component.class);
    }

    @Override
    public net.minecraft.network.chat.Component serialize(final Component component) {
        return net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(component));
    }
}
