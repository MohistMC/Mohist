package red.mohist.configuration;

import org.apache.commons.lang.BooleanUtils;

public class BoolSetting extends Setting<Boolean> {
    private final ConfigBase config;
    private Boolean value;

    public BoolSetting(ConfigBase config, String path, Boolean def, String description) {
        super(path, def, description);
        this.value = def;
        this.config = config;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = BooleanUtils.toBooleanObject(value);
        this.value = this.value == null ? def : this.value;
        config.set(path, this.value);
    }
}
