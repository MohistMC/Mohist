/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.property;

import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class Properties
{
    /**
     * Property indicating if the model should be rendered in the static renderer or in the TESR. AnimationTESR sets it to false.
     */
    public static final BooleanProperty StaticProperty = BooleanProperty.create("static");
}
