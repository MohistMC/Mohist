package com.mohistmc.configuration;

public class StringSetting extends Setting<String> {
    private String value;
    private ConfigBase config;

    public StringSetting(ConfigBase config, String path, String def,
            String description) {
        super(path, def, description);
        this.value = def;
        this.config = config;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        config.set(path, this.value = value);
    }
}
