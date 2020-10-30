package thermos.wrapper;

import gnu.trove.map.TLongObjectMap;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.concurrent.*;

import org.bukkit.craftbukkit.util.LongHash;

public class VanillaChunkHashMap extends LongHashMap {
	private ChunkBlockHashMap chunkt_TH;
	private ConcurrentHashMap<Long,Chunk> vanilla;
    public VanillaChunkHashMap(ChunkBlockHashMap chunkt_TH) {
        this.chunkt_TH = chunkt_TH;
        this.vanilla = new ConcurrentHashMap<Long,Chunk>();
    }
    
    private boolean notRealFace = false;
    public VanillaChunkHashMap(ChunkBlockHashMap chunkt_TH, ConcurrentHashMap<Long,Chunk> vanilla) {
        this.chunkt_TH = chunkt_TH;
        this.vanilla = vanilla;
        this.notRealFace = true;
    }
    
    public VanillaChunkHashMap thisIsNotMyRealFace()
    {
    	return new VanillaChunkHashMap(chunkt_TH, vanilla);
    }
    
    private long V2B(long key) {
    	if(notRealFace)
    	{
            return key;
    	}
    	else
    	{
    		return ChunkCoordIntPair.chunkXZ2Int(LongHash.msw(key) , LongHash.lsw(key));
    	}
    	//return LongHash.toLong((int) (key & 0xFFFFFFFFL), (int) (key >>> 32));
    }
    
    public ConcurrentHashMap<Long,Chunk> rawVanilla()
    {
    	return vanilla;
    }
    
    public ChunkBlockHashMap rawThermos()
    {
    	return chunkt_TH;
    }
    
    public int size()
    {
    	return this.chunkt_TH.size();
    }
    
    @Override
    public void add(long key, Object value) {
    	if(value instanceof Chunk)
    	{
    		Chunk c = (Chunk) value;
    		chunkt_TH.put(c);
    		vanilla.put(V2B(key), c);
    	}
    }

    
    @Override
    public boolean containsItem(long key) {
        return vanilla.containsKey(V2B(key));
    }
    
    @Override
    public Object getValueByKey(long key) {
        return vanilla.get(V2B(key));
    }
    
    @Override
    public Object remove(long key) {
        Object o = vanilla.remove(V2B(key));
        if(o instanceof Chunk) // Thermos - Use our special map
        {
        	Chunk c = (Chunk)o;
        	chunkt_TH.remove(c);
        }
        return o;
    }
      
}
