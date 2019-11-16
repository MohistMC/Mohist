package thermos.wrapper;

import gnu.trove.map.TLongObjectMap;
import net.minecraft.util.LongHashMap;

public class LongHashMapTrove<T> extends LongHashMap {
    private final TLongObjectMap<T> mMap;

    public LongHashMapTrove(TLongObjectMap<T> map) {
        mMap = map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(long key, Object value) {
        mMap.put(key, (T) value);
    }

    @Override
    public int getNumHashElements() {
        return mMap.size();
    }

    @Override
    public Object getValueByKey(long key) {
        return mMap.get(key);
    }

    @Override
    public boolean containsItem(long key) {
        return mMap.containsKey(key);
    }

    @Override
    public Object remove(long key) {
        return mMap.remove(key);
    }
    

}
