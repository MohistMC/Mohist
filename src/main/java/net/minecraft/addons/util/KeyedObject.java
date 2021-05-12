package net.minecraft.addons.util;

import net.minecraft.util.ResourceLocation;

public interface KeyedObject {
    ResourceLocation getResourceLocation();

    default String getResourceLocationString() {
        ResourceLocation key = getResourceLocation();
        return key != null ? key.toString() : null;
    }
}
