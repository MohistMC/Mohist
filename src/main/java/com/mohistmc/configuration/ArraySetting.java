/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.configuration;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class ArraySetting<T> extends Setting<String>
{
	protected ConfigBase config;
    protected String value;

	public ArraySetting(String path, String def, ConfigBase config)
	{
		super(path, def);
        this.value = def;
		this.config = config;
		this.initArr(def);
	}

	protected HashSet<T> value_set;
	protected ArrayList<T> value_array;

    @Override
    public final String getValue()
    {
        return this.value;
    }

    @Override
	public final void setValue(String value)
    {
    	this.config.set(path, this.value = value);
    	this.value_set.clear();
    	this.value_array.clear();
    	this.initArr(value);
    }


	public boolean contains(T t)
	{
		return this.value_set.contains(t);
	}

	public T get(int i)
	{
		if(i < 0 || i > this.value_array.size() - 1) {
			return null;
		}

		return this.value_array.get(i);

	}

	public abstract void initArr(String array);

}
