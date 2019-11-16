/*
 * Copyright (C) 2016 Robotia.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package thermos.wrapper;

import java.util.*;
import net.minecraft.world.ChunkPosition;
/**
 *
 * @author Robotia
 */
public class ThermiteMap implements Map
{
    private HashMap<Integer,Object[][]> map = new HashMap<Integer,Object[][]>();
    private HashMap<Object,Object> original = new HashMap<Object,Object>();
    private int size = 0;
    
    public ThermiteMap()
    {
        for(int i = 0; i <= 256; i++)
        {
            map.put(i, new Object[16][16]);
        }
    }
    @Override
    public int size()
    {
        return original.size();
    }

    @Override
    public boolean isEmpty()
    {
        return original.isEmpty();
    }
    
    public static boolean isValid(Object o) { return o instanceof ChunkPosition; }
    public static int[] getCoords(Object o) { ChunkPosition cp = (ChunkPosition)o; return new int[]{cp.chunkPosX, cp.chunkPosY, cp.chunkPosZ}; }
    @Override
    public boolean containsKey(Object key)
    {
        if(!isValid(key)) return false;
        int[] coords = getCoords(key);
        return map.get(coords[1])[coords[0]][coords[2]] != null;
    }

    @Override
    public boolean containsValue(Object value)
    {
        return original.containsValue(value);
    }

    @Override
    public Object get(Object key)
    {
        if(!isValid(key)) return null;
        int[] coords = getCoords(key);
        return map.get(coords[1])[coords[0]][coords[2]];
    }

    @Override
    public Object put(Object key, Object value)
    {
        if(!isValid(key))return null;
        int[] coords = getCoords(key);

        Object instance = original.put(key,value);
        map.get(coords[1])[coords[0]][coords[2]] = value; size++;
        return instance;
    }

    @Override
    public Object remove(Object key)
    {
        if(!isValid(key))return null;
        int[] coords = getCoords(key);
      
        Object instance = original.remove(key);
        map.get(coords[1])[coords[0]][coords[2]] = null; size--;
        return instance;
    }

    @Override
    public void putAll(Map m)
    {
        this.original.putAll(m);
    }

    @Override
    public void clear()
    {
        original.clear();
        map.clear();
    }

    @Override
    public Set keySet()
    {
        return this.original.keySet();
    }

    @Override
    public Collection values()
    {
        return original.values();
    }

    @Override
    public Set entrySet()
    {
        return this.original.entrySet();
    }

}

