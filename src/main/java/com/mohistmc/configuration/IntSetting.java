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

public class IntSetting extends Setting<Integer>
{
    private Integer value;
    private ConfigBase config;
    
    public IntSetting(ConfigBase config, String path, Integer def)
    {
        super(path, def);
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

