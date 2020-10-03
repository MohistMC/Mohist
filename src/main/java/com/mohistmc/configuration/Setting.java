package com.mohistmc.configuration;


public abstract class Setting<T>
{
    public final String path;
    public final T def;
    public final String description;

    public Setting(String path, T def, String description)
    {
        this.path = path;
        this.def = def;
        this.description = description;
    }

    public abstract T getValue();
    
    public abstract void setValue(String value);
}