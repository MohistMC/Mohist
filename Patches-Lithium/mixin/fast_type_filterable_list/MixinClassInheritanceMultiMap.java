package me.jellysquid.mods.lithium.mixin.fast_type_filterable_list;

import net.minecraft.util.ClassInheritanceMultiMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

/**
 * Patches {@link ClassInheritanceMultiMap} to improve performance when entities are being queried in the world.
 */
@Mixin(ClassInheritanceMultiMap.class)
public class MixinClassInheritanceMultiMap<T> {
    @Shadow
    @Final
    private Class<T> baseClass;

    @Shadow
    @Final
    private Map<Class<?>, List<T>> map;

    @Shadow
    @Final
    private List<T> values;

    /**
     * @reason Only perform the slow Class#isAssignableFrom(Class) if a list doesn't exist for the type, otherwise
     * we can assume it's already valid. The slow-path code is moved to a separate method to help the JVM inline this.
     * @author JellySquid
     */
    @SuppressWarnings("unchecked")
    @Overwrite
    public <S> Collection<S> getByClass(Class<S> type) {
        Collection<T> collection = this.map.get(type);

        if (collection == null) {
            collection = this.createAllOfType(type);
        }

        return (Collection<S>) Collections.unmodifiableCollection(collection);
    }

    private <S> Collection<T> createAllOfType(Class<S> type) {
        if (!this.baseClass.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Don't know how to search for " + type);
        }

        List<T> list = new ArrayList<>();

        for (T allElement : this.values) {
            if (type.isInstance(allElement)) {
                list.add(allElement);
            }
        }

        this.map.put(type, list);

        return list;
    }
}
