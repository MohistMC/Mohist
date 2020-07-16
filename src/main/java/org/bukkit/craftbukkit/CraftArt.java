package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.bukkit.Art;

public class CraftArt {
    private static final BiMap<PaintingMotive, Art> artwork;

    static {
        ImmutableBiMap.Builder<PaintingMotive, Art> artworkBuilder = ImmutableBiMap.builder();
        for (Identifier key : Registry.PAINTING_MOTIVE.getIds()) {
            artworkBuilder.put(Registry.PAINTING_MOTIVE.get(key), Art.getByName(key.getPath()));
        }

        artwork = artworkBuilder.build();
    }

    public static Art NotchToBukkit(PaintingMotive art) {
        Art bukkit = artwork.get(art);
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }

    public static PaintingMotive BukkitToNotch(Art art) {
        PaintingMotive nms = artwork.inverse().get(art);
        Preconditions.checkArgument(nms != null);
        return nms;
    }
}
