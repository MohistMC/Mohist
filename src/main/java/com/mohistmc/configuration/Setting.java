package com.mohistmc.configuration;


public abstract class Setting<T>
{
    public final String path;
    public final T def;

    public Setting(String path, T def)
    {
        this.path = path;
        this.def = def;
    }

    public abstract T getValue();
    
    public abstract void setValue(String value);
}