package red.mohist.extra.world;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.world.Heightmap;
public interface ExtraWorldChunk {

    public Map<Heightmap.Type, Heightmap> getHeightMaps();

    public TypeFilterableList<Entity>[] getEntitySections();
}
