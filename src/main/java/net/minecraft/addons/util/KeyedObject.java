package net.minecraft.util;

public interface KeyedObject {
    ResourceLocation getResourceLocation();

    default String getResourceLocationString() {
        ResourceLocation key = getResourceLocation();
        return key != null ? key.toString() : null;
    }
}
