package com.mohistmc.configuration;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class ArraySetting<T> extends Setting<String>
{
	protected ConfigBase config;
    protected String value;

	public ArraySetting(String path, String def, String description, ConfigBase config)
	{
		super(path, def, description);
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
