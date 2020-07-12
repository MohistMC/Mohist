package me.jellysquid.mods.lithium.common.config.parser.types;

import com.moandjiezana.toml.Toml;
import me.jellysquid.mods.lithium.common.config.parser.binding.OptionBinding;

public interface OptionSerializer {
    void read(Toml toml, OptionBinding binding) throws IllegalAccessException;
}
