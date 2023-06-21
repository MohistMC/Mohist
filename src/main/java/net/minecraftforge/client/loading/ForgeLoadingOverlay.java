/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.loading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * This is an implementation of the LoadingOverlay that calls back into the early window rendering, as part of the
 * game loading cycle. We completely replace the {@link #render(GuiGraphics, int, int, float)} call from the parent
 * with one of our own, that allows us to blend our early loading screen into the main window, in the same manner as
 * the Mojang screen. It also allows us to see and tick appropriately as the later stages of the loading system run.
 *
 * It is somewhat a copy of the superclass render method.
 */
public class ForgeLoadingOverlay extends LoadingOverlay {

    public ForgeLoadingOverlay(Minecraft p_96172_, ReloadInstance p_96173_, Consumer<Optional<Throwable>> p_96174_, boolean p_96175_) {
        super(p_96172_, p_96173_, p_96174_, p_96175_);
    }
}
