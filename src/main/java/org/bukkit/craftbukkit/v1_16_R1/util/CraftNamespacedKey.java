package org.bukkit.craftbukkit.v1_16_R1.util;

import net.minecraft.util.Identifier;
import org.bukkit.NamespacedKey;

public final class CraftNamespacedKey
{
    public static NamespacedKey fromStringOrNull(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        Identifier minecraft = Identifier.tryParse(string);
        return (minecraft == null) ? null : fromMinecraft(minecraft);
    }


    public static NamespacedKey fromString(String string) {
        return fromMinecraft(new Identifier(string));
    }

    public static NamespacedKey fromMinecraft(Identifier minecraft) {
        return new NamespacedKey(minecraft.getNamespace(), minecraft.getPath());
    }

    public static Identifier toMinecraft(NamespacedKey key) {
        return new Identifier(key.getNamespace(), key.getKey());
    }
}
