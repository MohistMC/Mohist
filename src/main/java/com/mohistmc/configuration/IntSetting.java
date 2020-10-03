package com.mohistmc.configuration;

public class IntSetting extends Setting<Integer>
{
    private Integer value;
    private ConfigBase config;
    
    public IntSetting(ConfigBase config, String path, Integer def, String description)
    {
        super(path, def, description);
        this.value = def;
        this.config = config;
    }

    @Override
    public Integer getValue()
    {
        return value;
    }

    @Override
    public void setValue(String value)
    {
        this.value = org.apache.commons.lang.math.NumberUtils.toInt(value, def);
        config.set(path, this.value);
    }
}

