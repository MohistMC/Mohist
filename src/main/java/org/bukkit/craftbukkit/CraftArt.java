package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.bukkit.Art;

public class CraftArt {
    private static final BiMap<PaintingType, Art> artwork;

    static {
        ImmutableBiMap.Builder<PaintingType, Art> artworkBuilder = ImmutableBiMap.builder();
        for (ResourceLocation key : Registry.MOTIVE.keySet()) {
            artworkBuilder.put(Registry.MOTIVE.getOrDefault(key), Art.getByName(key.getPath()));
        }

        artwork = artworkBuilder.build();
    }

    public static Art NotchToBukkit(PaintingType art) {
        Art bukkit = artwork.get(art);
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }

    public static PaintingType BukkitToNotch(Art art) {
        PaintingType nms = artwork.inverse().get(art);
        Preconditions.checkArgument(nms != null);
        return nms;
    }
}
